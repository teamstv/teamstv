package com.emc.teamstv.telegrambot;

public enum  BotReplies {
  TEXT_NOT_SUPPORTED(", наш бот на данный момент умеет работать только с фотоконтентом");

  private final String response;

  BotReplies(String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }
}
