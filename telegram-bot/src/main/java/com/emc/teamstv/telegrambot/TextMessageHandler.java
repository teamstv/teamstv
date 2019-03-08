package com.emc.teamstv.telegrambot;

import static com.emc.teamstv.telegrambot.BotReplies.TEXT_NOT_SUPPORTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TextMessageHandler extends TeamsTVBot {

  private static final Logger log = LoggerFactory.getLogger(TextMessageHandler.class);

  public TextMessageHandler(BotProperties botProperties) {
    super(botProperties);
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String user = update.getMessage().getFrom().getUserName();
      log.debug("Message from user " + user + "received. Message: " +update.getMessage().getText());
      SendMessage reply = new SendMessage();
      reply.setChatId(update.getMessage().getChatId());
      reply.setText(user + TEXT_NOT_SUPPORTED.getResponse());
      try {
        execute(reply);
        log.info("Text message to user " + user + " sent.");
      } catch (TelegramApiException e) {
        log.error("Error  + while sending message to user = " + user, e);
      }
    }
  }
}
