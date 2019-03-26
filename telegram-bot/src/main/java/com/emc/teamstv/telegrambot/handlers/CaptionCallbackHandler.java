package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.NULL_USER;
import static com.emc.teamstv.telegrambot.BotReplies.SEND_CAPTION;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class for handing callback when caption button pressed
 *
 * @author talipa
 */

@Service
public class CaptionCallbackHandler implements Handler {

  private final TransferService<String, Photo> transferService;

  public CaptionCallbackHandler(
      TransferService<String, Photo> transferService) {
    this.transferService = transferService;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    getPhotoModel(update, transferService, ButtonNameEnum.ADD_CAPTION).ifPresent(
        model -> {
          String user = getUser(update);
          BotReplies response;
          model.setTransferId(getTransferID(update, ButtonNameEnum.ADD_CAPTION));
          if (user == null) {
            response = NULL_USER;
          } else {
            transferService.set(user, model);
            response = SEND_CAPTION;
          }
          EditMessageText msg = prepareCallbackReply(update, response);
          sendText(msg, sender, update);
        }
    );
  }
}
