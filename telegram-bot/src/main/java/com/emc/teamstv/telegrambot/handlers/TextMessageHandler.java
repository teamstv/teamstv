package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.TEXT_NOT_SUPPORTED;
import static com.emc.teamstv.telegrambot.BotReplies.THANKS_FOR_CAPTION;

import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class for handling text messages(not commands)
 *
 * @author talipa
 */

@Service
public class TextMessageHandler implements Handler {

  private final TransferService<String, Photo> transferService;
  private final Keyboard keyboard;

  public TextMessageHandler(
      TransferService<String, Photo> transferService,
      Keyboard keyboard) {
    this.transferService = transferService;
    this.keyboard = keyboard;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    if (waitForCaptionMsg(update, sender)) {
      return;
    }
    SendMessage msg = prepareResponse(update, TEXT_NOT_SUPPORTED);
    sendText(msg, sender, update);
  }

  private boolean waitForCaptionMsg(Update update, DefaultAbsSender sender) {
    Optional<Photo> optModel = transferService.get(getUser(update));
    optModel.ifPresent(
        model -> {
          String caption = update.getMessage().getText();
          log.info("Caption: " + caption + ". For photo: " + model.getFileId() + " provided.");
          model.setCaption(caption);
          SendMessage msg = prepareResponse(update, THANKS_FOR_CAPTION);
          keyboard.keyboard(model, model.getTransferId())
              .ifPresent(msg::setReplyMarkup);
          sendText(msg, sender, update);
          transferService.delete(getUser(update));
          model.setTransferId("");
        }
    );
    return optModel.isPresent();
  }

}
