package com.emc.teamstv.telegrambot.handlers.messages;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Response {

  final Update update;
  private final String msg;

  protected Response(String msg, Update update) {
    this.msg = msg;
    this.update = update;
  }

  public final BotApiMethod getResponse() {
    int msgId = getMessageId();
    long chatId = getChatId();
    return setChatId(chatId)
        .setMessageId(msgId)
        .setText(msg);
  }

  abstract int getMessageId();

  abstract Response setMessageId(int id);

  abstract long getChatId();

  abstract Response setChatId(long id);

  abstract BotApiMethod setText(String msg);
}
