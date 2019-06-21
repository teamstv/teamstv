package com.emc.teamstv.telegrambot.handlers.messages;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public abstract class Response {

  private final String msg;

  protected Response(String msg) {
    this.msg = msg;
  }

  public final BotApiMethod getResponse() {
      long msgId = getMessageId();
      long chatId = getChatId();
      BotApiMethod response = setChatId(chatId);
      setMessageId(response, msgId);
      return setText(response, msg);
    }

    abstract long getMessageId();
    abstract long getChatId();
    abstract BotApiMethod setChatId(long id);
    abstract BotApiMethod setMessageId(BotApiMethod response, long id);
    abstract BotApiMethod setText(BotApiMethod response, String msg);
}
