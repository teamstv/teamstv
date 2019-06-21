package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
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
public abstract class Handler {

  final TransferService<String, Photo> transferService;
  Update update;
  DefaultAbsSender sender;
  Logger log = LoggerFactory.getLogger(Handler.class);

  protected Handler(
      TransferService<String, Photo> transferService) {
    this.transferService = transferService;
  }

  public void setUpdate(Update update) {
    this.update = update;
  }

  public void setSender(DefaultAbsSender sender) {
    this.sender = sender;
  }

  final BotApiMethod prepareCallbackReply(BotReplies reply) {
    long messageId = update.getCallbackQuery().getMessage().getMessageId();
    long chatId = update.getCallbackQuery().getMessage().getChatId();
    return new EditMessageText()
        .setChatId(chatId)
        .setMessageId((int) messageId)
        .setText(reply.getResponse());
  }

  final BotApiMethod prepareResponse(BotReplies reply) {
    long chatId = update.getMessage().getChatId();
    int msgId = update.getMessage().getMessageId();
    return new SendMessage()
        .setReplyToMessageId(msgId)
        .setChatId(chatId)
        .setText(getUser() + reply.getResponse());
  }

  public void onUpdateReceived() {
    Optional<? extends BotApiObject> content = getContent();
    content.ifPresent(
        c -> {
          Optional<Photo> optionalPhoto = operateOnContent(c);
          Response reply = getResponse();
          BotApiMethod msg = createResponse(reply);
          optionalPhoto.ifPresent(
              p -> createKeyboard(p, msg)
          );
          sendText(msg);
        }
    );

  }

  abstract Optional<? extends BotApiObject> getContent();

  abstract Optional<Photo> operateOnContent(BotApiObject content);

  abstract void createKeyboard(Photo model, BotApiMethod msg);

  abstract Response getResponse();

  final BotApiMethod createResponse(Response reply) {
    return reply.getResponse();
  }


  @SuppressWarnings("unchecked")
  final void sendText(BotApiMethod msg) {
    try {
      sender.execute(msg);
      log.info("Text message to user {} sent.", getUser());
    } catch (TelegramApiException e) {
      log.error("Error while sending message to user = " + getUser(), e);
    }
  }

  String getUser() {
    if (update.hasMessage()) {
      return update.getMessage().getFrom().getUserName();
    }
    return update.getCallbackQuery().getFrom().getUserName();
  }


  String getTransferID(ButtonNameEnum nameEnum) {
    return update.getCallbackQuery().getData().replace(nameEnum.getData(), "");
  }

  Optional<Photo> getPhotoModel(TransferService<String, Photo> transferService,
      ButtonNameEnum nameEnum) {
    String data = update.getCallbackQuery().getData();
    log.info("Callback for data {} received", data);
    return transferService.get(getTransferID(nameEnum));
  }

}
