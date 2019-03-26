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
  private String downloadButton;
  private String captionButton;
  private String cancelButton;
  private SimpleRepoProps repoProps;

  public class SimpleRepoProps {
    private String storePath;
    private int flushDelay;
    private int cleanDelay;

    public String getStorePath() {
      return storePath;
    }

    public void setStorePath(String storePath) {
      this.storePath = storePath;
    }

    public int getFlushDelay() {
      return flushDelay;
    }

    public void setFlushDelay(int flushDelay) {
      this.flushDelay = flushDelay;
    }

    public int getCleanDelay() {
      return cleanDelay;
    }

    public void setCleanDelay(int cleanDelay) {
      this.cleanDelay = cleanDelay;
    }
  }

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

  public String getDownloadButton() {
    return downloadButton;
  }

  public void setDownloadButton(String downloadButton) {
    this.downloadButton = downloadButton;
  }

  public String getCaptionButton() {
    return captionButton;
  }

  public void setCaptionButton(String captionButton) {
    this.captionButton = captionButton;
  }

  public String getCancelButton() {
    return cancelButton;
  }

  public void setCancelButton(String cancelButton) {
    this.cancelButton = cancelButton;
  }

  public SimpleRepoProps getRepoProps() {
    return repoProps;
  }

  public void setRepoProps(SimpleRepoProps repoProps) {
    this.repoProps = repoProps;
  }
}
