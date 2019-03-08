package com.emc.teamstv.telegrambot;

import static com.emc.teamstv.telegrambot.BotReplies.TEXT_NOT_SUPPORTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Class represents abstract handler for telegram API
 * @author talipa
 */

public abstract class TeamsTVBot extends TelegramLongPollingBot {

  private final BotProperties botProperties;

  public TeamsTVBot(BotProperties botProperties) {
    this.botProperties = botProperties;
  }

  public String getBotUsername() {
    return botProperties.getBotName();
  }

  public String getBotToken() {
    return botProperties.getToken();
  }

  public BotProperties getBotProperties() {
    return botProperties;
  }
}
