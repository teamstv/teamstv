package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.THANKS_FOR_PHOTO;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.keyboard.Keyboard;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Class does: 1. Handling photo messages. 2. Creates Inline keyboard.
 *
 * @author talipa
 */

@Service
public class PhotoMessageHandler implements Handler {

  private final BotProperties properties;
  private final Keyboard keyboard;

  public PhotoMessageHandler(BotProperties properties,
      Keyboard keyboard) {
    this.properties = properties;
    this.keyboard = keyboard;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    if (update.hasMessage() && update.getMessage().hasPhoto()) {
      String user = update.getMessage().getFrom().getUserName();
      Optional<PhotoSize> photo = update.getMessage()
          .getPhoto()
          .stream()
          .max(Comparator.comparing(PhotoSize::getFileSize));
      log.info("PhotoSize object from user " + user + " received");
      SendMessage msg = prepareResponse(update, THANKS_FOR_PHOTO.getResponse());
      msg.setReplyMarkup(keyboard.keyboard());
      sendText(msg, sender, update);
    }
  }

  private void download(Optional<PhotoSize> photo, DefaultAbsSender sender) {
    photo.ifPresent(p -> {
          try {
            String path;
            if (p.hasFilePath()) {
              path = p.getFilePath();
            } else {
              GetFile getFile = new GetFile();
              getFile.setFileId(p.getFileId());
              File file = sender.execute(getFile);
              path = file.getFilePath();
            }
            java.io.File file = sender.downloadFile(path);
            try (InputStream ios = new FileInputStream(file);) {
              java.io.File fileI = new java.io.File(
                  properties.getPath() + p.getFileId() + ".jpg");
              Files.copy(ios, fileI.toPath(), StandardCopyOption.REPLACE_EXISTING);
              log.info("File " + fileI.getPath() + " loaded");
            } catch (IOException e) {
              log.error("Error while moving downloaded file = " + p.getFileId(), e);
            }
          } catch (TelegramApiException e) {
            log.error("Error while downloading file = " + p.getFileId(), e);
          }
        }
    );
  }


}
