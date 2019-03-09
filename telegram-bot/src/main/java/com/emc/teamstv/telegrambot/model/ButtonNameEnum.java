package com.emc.teamstv.telegrambot.model;

public enum  ButtonNameEnum {

  DOWNLOAD("_download"),
  ADD_CAPTION("_caption"),
  CANCEL("_cancel");

  private final String data;

  ButtonNameEnum(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }
}
