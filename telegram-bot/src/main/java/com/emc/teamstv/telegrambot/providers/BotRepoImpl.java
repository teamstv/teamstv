package com.emc.teamstv.telegrambot.providers;

import static java.nio.file.StandardOpenOption.READ;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.BotRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of BotRepo service
 *
 * @author talipa
 */

@Service
public class BotRepoImpl implements BotRepo<Photo, Long> {

  private final Path dir;
  private final BotProperties properties;
  private final Map<Long, Set<Photo>> map = new ConcurrentHashMap<>();

  public BotRepoImpl(BotProperties properties) {
    this.properties = properties;
    dir = Paths.get(properties.getRepoProps().getStorePath());
  }

  @PostConstruct
  public void init() throws IOException {
    Files.createDirectories(dir);
    populateMapFromStorage();
    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    int flushDelay = properties.getRepoProps().getFlushDelay();
    service.scheduleWithFixedDelay(flushToStorage(), flushDelay, flushDelay, TimeUnit.SECONDS);
    int cleanDelay = properties.getRepoProps().getCleanDelay();
    service.scheduleWithFixedDelay(cleanStorage(), cleanDelay, cleanDelay, TimeUnit.SECONDS);
  }

  @Override
  public void save(Photo model, Long aLong) {
    Set<Photo> models = map.getOrDefault(aLong, new HashSet<>());
    models.add(model);
    map.put(aLong, models);
  }

  @Override
  public boolean checkByID(Long aLong, Photo model) {
    Set<Photo> models = map.get(aLong);
    return models.stream().anyMatch(m -> m.equals(model));
  }

  @Override
  public void deleteByID(Long aLong) {
    map.remove(aLong);
  }

  @Override
  public void deleteAll() {
    map.clear();
  }

  @Override
  public void deleteByValue(Long aLong, Photo model) {
    Set<Photo> models = map.get(aLong);
    models.remove(model);
  }

  @Override
  public Iterable<Photo> list(Long aLong) {
    return map.get(aLong);
  }

  @Override
  public Iterable<Photo> listAll() {
    List<Photo> photos = new ArrayList<>(map.size());
    map.values().forEach(photos::addAll);
    return photos;
  }

  private Runnable flushToStorage() {
    return () -> {
      ObjectMapper mapper = new ObjectMapper();
      try (OutputStream ous = Files.newOutputStream(getSerializeFilePath())) {
        mapper.writeValue(ous, map);
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  private Path getSerializeFilePath() throws IOException {
    String suffix = "bot-repo_" + System.currentTimeMillis() + ".json";
    return Files.createFile(Paths.get(dir.toString(),  suffix));
  }

  private Runnable cleanStorage() {
    return () -> {
      try {
        getLatestFile().ifPresent(
            path -> {
              try {
                deleteOldFiles(path);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  private Optional<Path> getLatestFile() throws IOException {
    try (Stream<Path> files = Files.walk(dir)) {
      return files.filter(Files::isRegularFile).max(
          Comparator.comparing(Path::toString)
      );
    }
  }

  private void deleteOldFiles(Path latest) throws IOException {
    try (Stream<Path> oldFiles = Files.walk(dir).filter(p -> !p.equals(latest) && p.toFile().isFile())) {
      oldFiles.forEach(path -> {
        try {
          Files.deleteIfExists(path);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
  }

  private void populateMapFromStorage() throws IOException {
    getLatestFile().ifPresent(
        p -> {
          ObjectMapper mapper = new ObjectMapper();
          TypeReference<ConcurrentHashMap<Long, Set<Photo>>> ref
              = new TypeReference<ConcurrentHashMap<Long, Set<Photo>>>() {
          };
          try (InputStream is = Files.newInputStream(p, READ)) {
            Map<Long, Set<Photo>> tmp = mapper.readValue(is, ref);
            map.putAll(tmp);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
    );
  }
}
