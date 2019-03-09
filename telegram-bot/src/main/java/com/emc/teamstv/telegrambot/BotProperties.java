package com.emc.teamstv.telegrambot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class which maps external props to POJO
 * @author talipa
 */

@Component
@ConfigurationProperties(prefix = "bot.properties")
public class BotProperties {

  private String token;
  private String botName;
  private String path;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBotName() {
    return botName;
  }

  public void setBotName(String botName) {
    this.botName = botName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
