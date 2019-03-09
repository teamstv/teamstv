package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.TEXT_NOT_SUPPORTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(TextMessageHandler.class);

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      SendMessage msg = prepareResponse(update, TEXT_NOT_SUPPORTED.getResponse());
      sendText(msg, sender,update);
    }
  }
}
