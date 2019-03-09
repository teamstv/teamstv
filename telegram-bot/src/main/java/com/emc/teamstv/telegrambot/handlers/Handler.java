package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.TEXT_NOT_SUPPORTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Handler interface with abstract method onUpdateReceived.
 * Used to handle different types of messages
 * Default sendText method could be commonly used in implementations
 * @author talipa
 */
public interface Handler {

  Logger log = LoggerFactory.getLogger(Handler.class);

  void onUpdateReceived(Update update, DefaultAbsSender sender);

  default void sendText(Update update, DefaultAbsSender sender, String msgToSend) {
    String user = update.getMessage().getFrom().getUserName();
    String msg = update.getMessage().getText();
    long chatId = update.getMessage().getChatId();
    log.debug("Message from user " + user + "received. Message: " + msg);
    SendMessage reply = new SendMessage();
    reply.setChatId(chatId);
    reply.setText(user + msgToSend);
    try {
      sender.execute(reply);
      log.info("Text message to user " + user + " sent.");
    } catch (TelegramApiException e) {
      log.error("Error  + while sending message to user = " + user, e);
    }
  }

}
