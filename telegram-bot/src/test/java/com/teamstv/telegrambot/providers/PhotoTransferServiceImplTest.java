package com.teamstv.telegrambot.providers;

import static org.junit.Assert.*;

import com.teamstv.telegrambot.BotProperties;
import com.teamstv.telegrambot.model.Photo;
import com.teamstv.telegrambot.services.IdGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhotoTransferServiceImplTest {

  private String filePath = "test.jpg";
  private String dirPath = "events";

  @Before
  public void init() throws IOException {
    Files.createDirectory(Paths.get(dirPath));
    Files.createFile(Paths.get(dirPath, filePath));
  }

  @Test
  public void initTest() throws IOException {
    BotProperties properties = new BotProperties();
    properties.setTransferServiceCapacity(10);
    properties.setPath(dirPath);
    IdGenerator<Integer> idGenerator = new IdGeneratorImpl();
    PhotoTransferServiceImpl transferService = new PhotoTransferServiceImpl(properties, idGenerator);
    transferService.init();
    Optional<Photo> optionalPhoto = transferService.get(1);
    if(optionalPhoto.isPresent()) {
      Photo photo = optionalPhoto.get();
      assertEquals("test", photo.getFileId());
    } else {
      throw new AssertionError();
    }
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(Paths.get(dirPath, filePath));
    Files.deleteIfExists(Paths.get(dirPath));
  }
}