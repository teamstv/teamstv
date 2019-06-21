package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.CLEAN_UP;

import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Class for handling callbacks from inline keyboard. This one will cancel any progression on data
 * loading
 *
 * @author talipa
 */
@Service
public class CancelCallbackHandler extends Handler {

  public CancelCallbackHandler(
      TransferService<String, Photo> transferService) {
    super(transferService);
  }

  @Override
  public void onUpdateReceived() {
    getPhotoModel(transferService, ButtonNameEnum.CANCEL).ifPresent(
        model -> {
          String transferId = getTransferID(ButtonNameEnum.CANCEL);
          transferService.delete(transferId);
          log.info("Transfer service cleaned for transferId = {}", transferId);
          if (model.isLoaded()) {
            try {
              Files.deleteIfExists(Paths.get(model.getPhotoLocalPath()));
              log.info("File {} deleted", model.getPhotoLocalPath());
            } catch (IOException e) {
              log.error("Error while deleting file = " + model.getFileId(), e);
            }
          }
          if (model.hasCaption()) {
            try {
              Files.deleteIfExists(Paths.get(model.getCaptionLocalPath()));
              log.info("File {} deleted", model.getCaptionLocalPath());
            } catch (IOException e) {
              log.error("Error while deleting file = " + model.getFileId(), e);
            }
          }
        }
    );
    BotApiMethod msg = prepareCallbackReply(CLEAN_UP);
    sendText(msg);
  }

  @Override
  void getContent() {

  }

  @Override
  Response operateOnContent() {
    return null;
  }

  @Override
  void createKeyboard() {

  }
}
