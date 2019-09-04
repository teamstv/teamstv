package com.teamstv.telegrambot.providers;

import com.teamstv.telegrambot.BotProperties;
import com.teamstv.telegrambot.model.Photo;
import com.teamstv.telegrambot.services.IdGenerator;
import com.teamstv.telegrambot.services.TransferService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Simple implementation of transfer service based on ConcurrentHashMap
 *
 * @author talipa
 */

@Service
public class PhotoTransferServiceImpl implements TransferService<Integer, Photo> {

  private final int capacity;
  private final Map<Integer, Photo> photoSizeMap;
  private final Map<String, Queue<Integer>> userMap;
  private final Queue<Photo> photoQueue;
  private final BotProperties properties;
  private final IdGenerator<Integer> idGenerator;

  @PostConstruct
  public void init() throws IOException {
    Path path = Paths.get(properties.getPath());
    try (Stream<Path> files = Files.walk(path, 1)) {
      files.map(Path::getFileName)
          .map(Path::toString)
          .filter(s -> s.endsWith(".jpg"))
          .map(s -> s.replace(".jpg", ""))
          .forEach(s ->{
            PhotoSize photoSize = new PhotoSizeDecorator(s);
            photoSizeMap.put(idGenerator.getUniq(), Photo.getPhotoModel(photoSize, s));
          }

          );
    }
  }

  private class PhotoSizeDecorator extends PhotoSize{
    private final String fileId;

    private PhotoSizeDecorator(String fileId) {
      this.fileId = fileId;
    }

    @Override
    public String getFileId() {
      return fileId;
    }
  }

  public PhotoTransferServiceImpl(BotProperties properties,
      IdGenerator<Integer> idGenerator) {
    capacity = properties.getTransferServiceCapacity();
    this.idGenerator = idGenerator;
    photoSizeMap = new ConcurrentHashMap<>(2 * capacity);
    userMap = new ConcurrentHashMap<>(2 * capacity);
    photoQueue = new ArrayBlockingQueue<>(capacity);
    this.properties = properties;
  }

  @Override
  public Optional<Photo> get(Integer id) {
    return Optional.ofNullable(photoSizeMap.get(id));
  }

  @Override
  public void set(Integer id, Photo photo) {
    photo.setTransferId(id);
    addModelToQueue(photo);
    photoSizeMap.put(id, photo);
  }

  @Override
  public void delete(Integer id) {
    Photo model = photoSizeMap.remove(id);
    photoQueue.remove(model);
  }

  @Override
  public Optional<Photo> get(String user) {
    Queue<Integer> actions = userMap.get(user);
    if (actions != null && !actions.isEmpty()) {
      int id = actions.peek();
      return Optional.of(photoSizeMap.get(id));
    }
    return Optional.empty();
  }

  @Override
  public void set(String user, Integer id) {
    Queue<Integer> actions = userMap.get(user);
    if (userMap.get(user) == null) {
      actions = new ArrayBlockingQueue<>(capacity);
      userMap.put(user, actions);
    }
    actions.offer(id);
  }

  @Override
  public void delete(String user) {
    Queue<Integer> actions = userMap.get(user);
    if (actions != null && !actions.isEmpty()) {
      actions.poll();
      return;
    }
    userMap.remove(user);
  }

  @Override
  public Collection<Photo> getAll() {
    return photoSizeMap.values();
  }

  @Override
  public Optional<Photo> getByFileID(String fileID) {
    return photoSizeMap.values().parallelStream().filter(
        p -> p.getFileId().equals(fileID)
    ).findFirst();
  }

  private void addModelToQueue(Photo photo) {
    Photo model = null;
    if (photoQueue.size() == capacity) {
      model = photoQueue.poll();
    }
    if (model != null) {
      photoSizeMap.remove(model.getTransferId());
      try {
        model.delete();
      } catch (IOException e) {
        Logger log = LoggerFactory.getLogger(TransferService.class);
        log.error("Error while deleting files for model = {} ", model.getFileId(), e);
      }
    }
    photoQueue.offer(photo);
  }

}
