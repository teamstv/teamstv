package com.emc.teamstv.telegrambot.handlers;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handler factory class returns Handler interface instance based on type of update Private methods
 * isText, isPhoto, etc used for simplify code
 *
 * @author talipa
 */

@Service
public class HandlerFactory {

  private final PhotoMessageHandler photoMessageHandler;
  private final TextMessageHandler textMessageHandler;

  public HandlerFactory(
      PhotoMessageHandler photoMessageHandler,
      TextMessageHandler textMessageHandler) {
    this.photoMessageHandler = photoMessageHandler;
    this.textMessageHandler = textMessageHandler;
  }

  public Optional<Handler> getHandler(Update update) {
    if (isPhoto(update)) {
      return Optional.of(photoMessageHandler);
    }
    if (isText(update)) {
      return Optional.of(textMessageHandler);
    }
    return Optional.empty();
  }

  private boolean isPhoto(Update update) {
    return update.hasMessage() && update.getMessage().hasPhoto();
  }

  private boolean isText(Update update) {
    return update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText()
        .startsWith("/");
  }
}
