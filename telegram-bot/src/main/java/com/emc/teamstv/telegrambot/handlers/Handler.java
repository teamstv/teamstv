package com.emc.teamstv.telegrambot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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
    return new SendMessage()
        .setReplyToMessageId(msgId)
        .setChatId(chatId)
        .setText(getUser(update) + msgToSend);
  }

  @SuppressWarnings("unchecked")
  default void sendText(BotApiMethod msg, DefaultAbsSender sender, Update update) {
    try {
      sender.execute(msg);
      log.info("Text message to user " + getUser(update) + " sent.");
    } catch (TelegramApiException e) {
      log.error("Error while sending message to user = " + getUser(update), e);
    }
  }

  default String getUser(Update update) {
    if (update.hasMessage()) {
      return update.getMessage().getFrom().getUserName();
    }
    return update.getCallbackQuery().getFrom().getUserName();
  }

  default EditMessageText prepareCallbackReply(Update update, String msgToSend) {
    long message_id = update.getCallbackQuery().getMessage().getMessageId();
    long chat_id = update.getCallbackQuery().getMessage().getChatId();
    return new EditMessageText()
        .setChatId(chat_id)
        .setMessageId((int)message_id)
        .setText(msgToSend);
  }

}
