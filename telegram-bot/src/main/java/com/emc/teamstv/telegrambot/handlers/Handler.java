package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
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
   * @param reply value from BotReplies enum
   * @return SendMessage object
   */
  default SendMessage prepareResponse(Update update, BotReplies reply) {
    String msg = update.getMessage().getText();
    long chatId = update.getMessage().getChatId();
    int msgId = update.getMessage().getMessageId();
    log.debug("Message from user " + getUser(update) + "received. Message: " + msg);
    return new SendMessage()
        .setReplyToMessageId(msgId)
        .setChatId(chatId)
        .setText(getUser(update) + reply.getResponse());
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

  default EditMessageText prepareCallbackReply(Update update, BotReplies reply) {
    long message_id = update.getCallbackQuery().getMessage().getMessageId();
    long chat_id = update.getCallbackQuery().getMessage().getChatId();
    return new EditMessageText()
        .setChatId(chat_id)
        .setMessageId((int) message_id)
        .setText(reply.getResponse());
  }

  default String getTransferID(Update update, ButtonNameEnum nameEnum) {
    return update.getCallbackQuery().getData().replace(nameEnum.getData(), "");
  }

  default Optional<Photo> getPhotoModel(Update update,
      TransferService<String, Photo> transferService, ButtonNameEnum nameEnum) {
    String data = update.getCallbackQuery().getData();
    log.info("Callback for data " + data + " received");
    return transferService.get(getTransferID(update, nameEnum));
  }

}
