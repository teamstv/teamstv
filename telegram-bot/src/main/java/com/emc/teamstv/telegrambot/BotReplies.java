package com.emc.teamstv.telegrambot;

public enum BotReplies {
  TEXT_NOT_SUPPORTED(", наш бот на данный момент умеет работать только с фотоконтентом."),
  THANKS_FOR_PHOTO(" , спасибо за фото, выберите дальнейшее действие."),
  LOAD_COMPLETED("Фото успешно загружено на сервер");

  private final String response;

  BotReplies(String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }
}
