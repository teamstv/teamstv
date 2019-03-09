package com.emc.teamstv.telegrambot.keyboard;

import com.emc.teamstv.telegrambot.BotProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class Keyboard {

  private final BotProperties properties;

  public Keyboard(BotProperties properties) {
    this.properties = properties;
  }

  public InlineKeyboardMarkup keyboard() {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<InlineKeyboardButton> rowInline = Arrays.asList(
        getButton(properties.getDownloadButName(),"download"),
        getButton(properties.getAddCaption(), "addCaption"));
    markupInline.setKeyboard(Collections.singletonList(rowInline));
    return markupInline;
  }

  private InlineKeyboardButton getButton(String text, String data) {
    return new InlineKeyboardButton()
        .setText(text)
        .setCallbackData(data);
  }
}
