package com.emc.teamstv.telegrambot.handlers.messages;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class TextResponse extends Response {

  public TextResponse(String msg) {
    super(msg);
  }

  @Override
  long getMessageId() {
    return 0;
  }

  @Override
  long getChatId() {
    return 0;
  }

  @Override
  BotApiMethod setChatId(long id) {
    return null;
  }

  @Override
  BotApiMethod setMessageId(BotApiMethod response, long id) {
    return null;
  }

  @Override
  BotApiMethod setText(BotApiMethod response, String msg) {
    return null;
  }
}
