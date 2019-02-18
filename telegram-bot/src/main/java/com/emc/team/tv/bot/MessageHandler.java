package com.emc.team.tv.bot;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.chat.Chat;
import com.jtelegram.api.events.message.MessageEvent;
import com.jtelegram.api.message.Message;
import com.jtelegram.api.requests.message.send.SendText;
import com.jtelegram.api.requests.message.send.SendText.SendTextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageHandler {

  protected static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
  protected final TelegramBot bot;

  protected MessageHandler(TelegramBot bot) {
    this.bot = bot;
  }

  public SendTextBuilder eventHandler(MessageEvent event, String text) {
    Message message = event.getMessage();
    log.debug(message.toString());
    Chat chat = message.getChat();
    log.info("Message from user: " + chat.getUsername() + " received");
    return SendText.builder()
        .chatId(chat.getChatId())
        .replyToMessageID(message.getMessageId())
        .text(chat.getUsername() + text);
  }

  public abstract void handleEvent();
}
