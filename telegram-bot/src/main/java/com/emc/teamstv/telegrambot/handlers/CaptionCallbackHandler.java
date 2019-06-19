package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.NULL_USER;
import static com.emc.teamstv.telegrambot.BotReplies.SEND_CAPTION;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Class for handing callback when caption button pressed
 *
 * @author talipa
 */

@Service
public class CaptionCallbackHandler extends Handler {

  private final TransferService<String, Photo> transferService;

  public CaptionCallbackHandler(
      TransferService<String, Photo> transferService) {
    this.transferService = transferService;
  }

  @Override
  public void onUpdateReceived() {
    getPhotoModel(transferService, ButtonNameEnum.ADD_CAPTION).ifPresent(
        model -> {
          String user = getUser();
          BotReplies response;
          model.setTransferId(getTransferID(ButtonNameEnum.ADD_CAPTION));
          if (user == null) {
            response = NULL_USER;
          } else {
            transferService.set(user, model);
            response = SEND_CAPTION;
          }
          EditMessageText msg = prepareCallbackReply(response);
          sendText(msg);
        }
    );
  }
}
