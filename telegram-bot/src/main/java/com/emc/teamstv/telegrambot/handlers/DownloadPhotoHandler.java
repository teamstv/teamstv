package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.LOAD_COMPLETED;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.UserPhotoModel;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Class for handling callbacks from inline keyboard.
 * This one will load data into our server
 * @author talipa
 */
@Service
public class DownloadPhotoHandler implements Handler{

  private final TransferService<String, UserPhotoModel> transferService;
  private final Keyboard keyboard;
  private final BotProperties properties;

  public DownloadPhotoHandler(
      TransferService<String, UserPhotoModel> transferService,
      Keyboard keyboard, BotProperties properties) {
    this.transferService = transferService;
    this.keyboard = keyboard;
    this.properties = properties;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    String data = update.getCallbackQuery().getData();
    log.info("Callback for data " + data + " received");
    String id = data.replace(ButtonNameEnum.DOWNLOAD.getData(), "");
    Optional<UserPhotoModel> photo = transferService.get(id);
    photo.ifPresent(
        model -> {
          PhotoSize p = model.getPhotoSize();
          try {
            log.info("Found entry for fileId = " + p.getFileId() );
            java.io.File file = sender.downloadFile(getPath(p, sender));
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
          model.setLoaded(true);
          EditMessageText msg = prepareCallbackReply(update, LOAD_COMPLETED.getResponse());
          keyboard.keyboard(model, id).ifPresent(msg::setReplyMarkup);
          sendText(msg, sender, update);
        }
    );

  }

  private String getPath(PhotoSize p, DefaultAbsSender sender) throws TelegramApiException {
    String path;
    if (p.hasFilePath()) {
      path = p.getFilePath();
    } else {
      GetFile getFile = new GetFile();
      getFile.setFileId(p.getFileId());
      File file = sender.execute(getFile);
      path = file.getFilePath();
    }
    return path;
  }
}
