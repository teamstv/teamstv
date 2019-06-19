package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.CLEAN_UP;

import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Class for handling callbacks from inline keyboard. This one will cancel any progression on data
 * loading
 *
 * @author talipa
 */
@Service
public class CancelCallbackHandler extends Handler {

  private final TransferService<String, Photo> transferService;

  public CancelCallbackHandler(
      TransferService<String, Photo> transferService) {
    this.transferService = transferService;
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
              Files.deleteIfExists(Paths.get(model.getLocalPath()));
              log.info("File {} deleted", model.getLocalPath());
            } catch (IOException e) {
              log.error("Error while deleting file = " + model.getFileId(), e);
            }
          }
        }
    );
    EditMessageText msg = prepareCallbackReply(CLEAN_UP);
    sendText(msg);
  }
}
