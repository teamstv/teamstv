package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.LOAD_COMPLETED;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.PhotoModel;
import com.emc.teamstv.telegrambot.services.BotRepo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Class for handling callbacks from inline keyboard. This one will load data into our server
 *
 * @author talipa
 */
@Service
public class DownloadPhotoHandler implements Handler {

  private final TransferService<String, PhotoModel> transferService;
  private final Keyboard keyboard;
  private final BotProperties properties;
  private final BotRepo<PhotoModel, Long> botRepo;

  public DownloadPhotoHandler(
      TransferService<String, PhotoModel> transferService,
      Keyboard keyboard,
      BotProperties properties,
      BotRepo<PhotoModel, Long> botRepo) {
    this.transferService = transferService;
    this.keyboard = keyboard;
    this.properties = properties;
    this.botRepo = botRepo;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    getPhotoModel(update, transferService, ButtonNameEnum.DOWNLOAD).ifPresent(
        model -> {
          String fileId = model.getPhotoSize().getFileId();
          try {
            log.info("Found entry for fileId = " + fileId);
            saveFile(model, sender);
          } catch (TelegramApiException e) {
            log.error("Error while downloading file = " + fileId, e);
          }
          long userId = update.getCallbackQuery().getFrom().getId();
          botRepo.save(model, userId);
          EditMessageText msg = prepareCallbackReply(update, LOAD_COMPLETED);
          keyboard.keyboard(model, getTransferID(update, ButtonNameEnum.DOWNLOAD))
              .ifPresent(msg::setReplyMarkup);
          sendText(msg, sender, update);
        }
    );

  }

  private void saveFile(PhotoModel model, DefaultAbsSender sender) throws TelegramApiException {
    PhotoSize p = model.getPhotoSize();
    Path file = sender.downloadFile(getPath(p, sender)).toPath();
    try (InputStream ios = Files.newInputStream(file);) {
      String localPath = properties.getPath() + java.io.File.separator + p.getFileId() + ".jpg";
      Path fileI = Paths.get(localPath);
      Files.copy(ios, fileI, StandardCopyOption.REPLACE_EXISTING);
      log.info("File " + fileI + " loaded");
      model.setLoaded(true);
      model.setLocalPath(localPath);
    } catch (IOException e) {
      log.error("Error while moving downloaded file = " + p.getFileId(), e);
    }
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
