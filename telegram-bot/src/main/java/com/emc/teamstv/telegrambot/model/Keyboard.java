package com.emc.teamstv.telegrambot.model;

import static com.emc.teamstv.telegrambot.model.ButtonNameEnum.*;

import com.emc.teamstv.telegrambot.BotProperties;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class Keyboard {

  private final BotProperties properties;

  public Keyboard(BotProperties properties) {
    this.properties = properties;
  }

  public Optional<InlineKeyboardMarkup> keyboard(UserPhotoModel model, String id) {
    if (!model.isLoaded() && !model.hasCaption()) {
      return keyboard(getAllButtons(id));
    }
    if (!model.isLoaded() && model.hasCaption()) {
      return keyboard(getDownloadButtons(id));
    }
    if (!model.hasCaption()) {
      return keyboard(getCaptionButtons(id));
    }
    return Optional.empty();
  }

  private Optional<InlineKeyboardMarkup> keyboard(List<InlineKeyboardButton> buttons) {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    markupInline.setKeyboard(Collections.singletonList(buttons));
    return Optional.of(markupInline);
  }

  private List<InlineKeyboardButton> getAllButtons(String id) {
    return Arrays.asList(
        getButton(properties.getDownloadButName(), id + DOWNLOAD.getData()),
        getButton(properties.getAddCaption(), id + ADD_CAPTION.getData()),
        getButton(properties.getCancelButton(), id + CANCEL.getData())
    );
  }

  private List<InlineKeyboardButton> getDownloadButtons(String id) {
    return Arrays.asList(
        getButton(properties.getDownloadButName(), id + DOWNLOAD.getData()),
        getButton(properties.getCancelButton(), id + CANCEL.getData())
    );
  }

  private List<InlineKeyboardButton> getCaptionButtons(String id) {
    return Arrays.asList(
        getButton(properties.getAddCaption(), id + ADD_CAPTION.getData()),
        getButton(properties.getCancelButton(), id + CANCEL.getData())
    );
  }

  private InlineKeyboardButton getButton(String text, String data) {
    return new InlineKeyboardButton()
        .setText(text)
        .setCallbackData(data);
  }
}
