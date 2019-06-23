package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.NULL_USER;
import static com.emc.teamstv.telegrambot.BotReplies.SEND_CAPTION;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Class for handing callback when caption button pressed
 *
 * @author talipa
 */

@Service
public class CaptionCallbackHandler extends CallbackHandler {

  public CaptionCallbackHandler(
      TransferService<String, Photo> transferService) {
    super(transferService);
  }

  @Override
  public void onUpdateReceived() {
    getPhotoModel(ButtonNameEnum.ADD_CAPTION).ifPresent(
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
          BotApiMethod msg = prepareCallbackReply(response);
          sendText(msg);
        }
    );
  }

  @Override
  Optional<? extends BotApiObject> getContent() {
    return Optional.empty();
  }

  @Override
  Optional<Photo> operateOnContent(BotApiObject content) {
    return Optional.empty();
  }

  @Override
  void createKeyboard(Photo model, BotApiMethod msg) {

  }

  @Override
  Response getResponse() {
    return null;
  }
}
