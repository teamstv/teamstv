package com.emc.teamstv.telegrambot.providers;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.BotProperties.SimpleRepoProps;
import com.emc.teamstv.telegrambot.model.Photo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

public class BotRepoImplTest {

  private BotRepoImpl repo;
  private String dir = "C:\\Users\\talipa\\tmp";

  public void setUp(int cleanDelay, int flushDelay) throws IOException {
    repo = new BotRepoImpl(getProps(cleanDelay, flushDelay));
    repo.init();
    fillRepo();
  }

  @After
  public void tearDown() throws Exception {
    try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
      paths.filter(Files::isRegularFile).forEach(
          path -> {
            try {
              Files.deleteIfExists(path);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
      );
    }
  }

  @Test
  public void shouldPersistRepoOnHardDrive() throws IOException {
    setUp(10, 3);
    await().atMost(5, TimeUnit.SECONDS).untilAsserted(
        () -> assertTrue(findAnyFile())
    );
  }

  @Test
  public void shouldBeNotMoreThanOneFile() throws Exception {
    setUp(1, 4);
    await().atMost(6, TimeUnit.SECONDS).untilAsserted(
        () -> {
          try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            long result = paths.filter(Files::isRegularFile).count();
            assertEquals(1, result);
          }
        }
    );
  }

  @Test
  public void shouldRestoreDataFromDisk() throws Exception {
    setUp(100, 4);
    with().pollDelay(6, TimeUnit.SECONDS).and().pollInterval(1, TimeUnit.SECONDS).await()
        .until(this::findAnyFile);
    repo = new BotRepoImpl(getProps(100, 100));
    repo.init();
    await().atMost(6, TimeUnit.SECONDS).until(
        () -> repoSize(repo) > 0
    );
  }

  private BotProperties getProps(int cleanDelay, int flushDelay) {
    BotProperties properties = new BotProperties();
    SimpleRepoProps repoProps = properties.new SimpleRepoProps();
    repoProps.setCleanDelay(cleanDelay);
    repoProps.setFlushDelay(flushDelay);
    repoProps.setStorePath(dir);
    properties.setRepoProps(repoProps);
    return properties;
  }

  private void fillRepo() {
    PhotoSize size1 = new PhotoSize();
    PhotoSize size2 = new PhotoSize();
    PhotoSize size3 = new PhotoSize();
    Photo photo1 = Photo.getPhotoModel(size1, "test1");
    Photo photo2 = Photo.getPhotoModel(size2, "test2");
    Photo photo3 = Photo.getPhotoModel(size3, "test3");
    repo.save(photo1, 1L);
    repo.save(photo2, 2L);
    repo.save(photo3, 3L);
  }

  private boolean findAnyFile() throws IOException {
    try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
      return paths.anyMatch(Files::isRegularFile);
    }
  }

  private int repoSize(BotRepoImpl repository) {
    Iterator<Photo> iterator = repository.listAll().iterator();
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    return count;
  }
}