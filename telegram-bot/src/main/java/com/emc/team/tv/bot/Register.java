package com.emc.team.tv.bot;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.update.PollingUpdateProvider;
import com.jtelegram.api.update.UpdateType;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Register {

  private final String token;
  private final String downloadFileURL;
  private boolean isRegistered;
  private TelegramBotRegistry registry;

  private final static Logger log = LoggerFactory.getLogger(Register.class);

  public Register(String token, String downloadFileURL) {
    this.token = token;
    this.downloadFileURL = downloadFileURL;
  }

  private void setRegistry() {
    registry = TelegramBotRegistry.builder()
        .updateProvider(new PollingUpdateProvider())
        .updateProvider(PollingUpdateProvider.builder()
            .timeout(10)
            .allowedUpdate(UpdateType.CALLBACK_QUERY)
            .allowedUpdate(UpdateType.MESSAGE)
            .build()
        )
        .build();
    log.info("Registry build successfully");
  }

  private void register() {
    if (registry == null) {
      setRegistry();
    }
    isRegistered = true;
    registry.registerBot(token, (bot, error) -> {
      if (error != null) {
        throw new RuntimeException("Login failed");
      }
    });
  }

  public TelegramBot getBot() throws InterruptedException {
    if (!isRegistered) {
      register();
    }
    while (registry.getBots().isEmpty()) {
      Thread.sleep(10);
    }
    return registry.getBots().iterator().next();
  }

  public InputStream downloadFile(String filePath) throws IOException {
    if (registry == null) {
      setRegistry();
    }
    log.info("Starting file loading");
    ResponseBody body = registry.getClient().newCall(
        new Request.Builder()
            .url(downloadFileURL + token + "/" + filePath)
            .get()
            .build()
    ).execute().body();
    log.info("File form path: " + filePath + ". Loaded.");
    return body == null ? null : body.byteStream();
  }

}
