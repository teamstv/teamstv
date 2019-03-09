package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.model.PhotoModel;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of transfer service based on ConcurrentHashMap
 *
 * @author talipa
 */

@Service
public class PhotoTransferService implements TransferService<String, PhotoModel> {

  private final BotProperties properties;

  public PhotoTransferService(BotProperties properties) {
    this.properties = properties;
  }

  @PostConstruct
  private void init() {
    Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
        cleanUp(), 60, properties.getCleanUpTimeOut(), TimeUnit.SECONDS
    );
  }

  private final Map<String, PhotoModel> photoSizeMap = new ConcurrentHashMap<>();
  private final Map<LocalDateTime, String> cleanMap = new ConcurrentHashMap<>();

  @Override
  public Optional<PhotoModel> get(String s) {
    return Optional.ofNullable(photoSizeMap.get(s));
  }

  @Override
  public void put(String s, PhotoModel model) {
    photoSizeMap.put(s, model);
    cleanMap.put(LocalDateTime.now(), s);
  }

  @Override
  public void delete(String s) {
    photoSizeMap.remove(s);
  }

  private Runnable cleanUp() {
    return ()-> {
      Set<LocalDateTime> dates = cleanMap.keySet()
          .stream()
          .filter(
              l -> LocalDateTime.now().getSecond() - l.getSecond() > properties.getCleanUpTimeOut())
          .collect(Collectors.toSet());
      dates.forEach(l -> {
        photoSizeMap.remove(cleanMap.get(l));
        cleanMap.remove(l);
      });
    };
  }
}
