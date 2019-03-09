package com.emc.teamstv.telegrambot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Handler interface with abstract method onUpdateReceived. Used to handle different types of
 * messages Default sendText method could be commonly used in implementations
 *
 * @author talipa
 */
public interface Handler {

  Logger log = LoggerFactory.getLogger(Handler.class);

  void onUpdateReceived(Update update, DefaultAbsSender sender);

  /**
   * @param update Update event from telegram API
   * @param msgToSend String value from BotReplies should be used
   * @return SendMessage object
   */
  default SendMessage prepareResponse(Update update, String msgToSend) {
    String msg = update.getMessage().getText();
    long chatId = update.getMessage().getChatId();
    int msgId = update.getMessage().getMessageId();
    log.debug("Message from user " + getUser(update) + "received. Message: " + msg);
    SendMessage reply = new SendMessage();
    reply.setReplyToMessageId(msgId);
    reply.setChatId(chatId);
    reply.setText(getUser(update) + msgToSend);
    return reply;
  }

  default void sendText(SendMessage msg, DefaultAbsSender sender, Update update) {
    try {
      sender.execute(msg);
      log.info("Text message to user " + getUser(update) + " sent.");
    } catch (TelegramApiException e) {
      log.error("Error  + while sending message to user = " + getUser(update), e);
    }
  }

  default String getUser(Update update) {
    return update.getMessage().getFrom().getUserName();
  }

}
