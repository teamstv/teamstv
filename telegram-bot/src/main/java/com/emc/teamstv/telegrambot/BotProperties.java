package com.emc.teamstv.telegrambot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class which maps external props to POJO
 *
 * @author talipa
 */

@Component
@ConfigurationProperties(prefix = "bot.properties")
public class BotProperties {

  private String token;
  private String botName;
  private String path;
  private String downloadButName;
  private String addCaption;
  private String cancelButton;
  private Long cleanUpTimeOut;

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

  public String getDownloadButName() {
    return downloadButName;
  }

  public void setDownloadButName(String downloadButName) {
    this.downloadButName = downloadButName;
  }

  public String getAddCaption() {
    return addCaption;
  }

  public void setAddCaption(String addCaption) {
    this.addCaption = addCaption;
  }

  public String getCancelButton() {
    return cancelButton;
  }

  public void setCancelButton(String cancelButton) {
    this.cancelButton = cancelButton;
  }

  public Long getCleanUpTimeOut() {
    return cleanUpTimeOut;
  }

  public void setCleanUpTimeOut(Long cleanUpTimeOut) {
    this.cleanUpTimeOut = cleanUpTimeOut;
  }
}
