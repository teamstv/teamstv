package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.SEND_CAPTION;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.handlers.messages.CallbackResponse;
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
  Optional<Photo> operateOnContent(BotApiObject content) {
    getPhotoModel(ButtonNameEnum.ADD_CAPTION).ifPresent(
        model -> {
          String user = getUser();
          model.setTransferId(getTransferID(ButtonNameEnum.ADD_CAPTION));
          transferService.set(user, model);
        }
    );
    return Optional.empty();
  }

  @Override
  Response getResponse() {
    return new CallbackResponse(SEND_CAPTION.getResponse(), update);
  }
}
