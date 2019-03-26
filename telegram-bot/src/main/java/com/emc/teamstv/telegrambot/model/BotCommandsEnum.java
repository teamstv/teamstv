package com.emc.teamstv.telegrambot.model;

/**
 * Class contains list of available bot commands
 *
 * @author talipa
 */

public enum BotCommandsEnum {
  DELETE("/delete"),
  DELETE_ALL("/deleteall"),
  LIST("/list"),
  LIST_ALL("/listall"),
  HELP("/help");

  private final String name;

  BotCommandsEnum(String name) {
    this.name = name;
  }


  public String getName() {
    return name;
  }
}
