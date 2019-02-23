package com.emc.team.tv.bot;

import com.jtelegram.api.inline.keyboard.InlineKeyboardButton;
import com.jtelegram.api.inline.keyboard.InlineKeyboardRow;
import com.jtelegram.api.inline.keyboard.InlineKeyboardRow.InlineKeyboardRowBuilder;
import java.util.HashSet;
import java.util.Set;

public class Keyboard {

  private final int size;
  private final Button[] buttons;
  private final Set<InlineKeyboardRow> rows;

  private Keyboard(int size, Button[] buttons,
      Set<InlineKeyboardRow> rows) {
    this.size = size;
    this.buttons = buttons;
    this.rows = rows;
  }

  public static Keyboard getKeyboard (String... values) {
    Button[] buttons = new Button[values.length];
    for (int i = 0; i < values.length; i++) {
      buttons[i] = new Button(values[i], values[i]);
    }
    Set<InlineKeyboardRow> rows = new HashSet<>();
    return new Keyboard(values.length, buttons, rows);
  }

  public Set<InlineKeyboardRow> getKeyboardRows() {
    InlineKeyboardRowBuilder builder = InlineKeyboardRow.builder();
    for (int i = 0; i < size; i++) {
      getRow(builder, buttons[i].getLabel(), buttons[i].getData());
    }
    rows.add(builder.build());
    return rows;
  }

  InlineKeyboardRowBuilder getRow(InlineKeyboardRowBuilder builder, String label, String data) {
    return builder
        .button(InlineKeyboardButton.builder()
            .label(label)
            .callbackData(data)
            .build());
  }

}
