package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
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
  private final DownloadCallbackHandler downloadCallbackHandler;
  private final CancelCallbackHandler cancelCallbackHandler;
  private final CaptionCallbackHandler captionCallbackHandler;

  public HandlerFactory(
      PhotoMessageHandler photoMessageHandler,
      TextMessageHandler textMessageHandler,
      DownloadCallbackHandler downloadCallbackHandler,
      CancelCallbackHandler cancelCallbackHandler,
      CaptionCallbackHandler captionCallbackHandler) {
    this.photoMessageHandler = photoMessageHandler;
    this.textMessageHandler = textMessageHandler;
    this.downloadCallbackHandler = downloadCallbackHandler;
    this.cancelCallbackHandler = cancelCallbackHandler;
    this.captionCallbackHandler = captionCallbackHandler;
  }

  public Optional<Handler> getHandler(Update update) {
    if (isPhoto(update)) {
      return Optional.of(photoMessageHandler);
    }
    if (isText(update)) {
      return Optional.of(textMessageHandler);
    }
    if (checkCallbackType(update, ButtonNameEnum.DOWNLOAD)) {
      return Optional.of(downloadCallbackHandler);
    }
    if (checkCallbackType(update, ButtonNameEnum.CANCEL)) {
      return Optional.of(cancelCallbackHandler);
    }
    if (checkCallbackType(update, ButtonNameEnum.ADD_CAPTION)) {
      return Optional.of(captionCallbackHandler);
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

  private boolean checkCallbackType(Update update, ButtonNameEnum nameEnum) {
    return update.hasCallbackQuery() && update.getCallbackQuery().getData()
        .endsWith(nameEnum.getData());
  }
}
