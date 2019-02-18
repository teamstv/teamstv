package com.emc.team.tv.bot;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.events.message.TextMessageEvent;

public class TextMessageHandler extends MessageHandler {

  protected TextMessageHandler(TelegramBot bot) {
    super(bot);
  }

  @Override
  public void handleEvent() {
    bot.getEventRegistry().registerEvent(TextMessageEvent.class, (event) -> {
          bot.perform(
              eventHandler(event,
                  ", наш бот на данный момент умеет работать только с фотоконтентом")
                  .build());
        }
    );
  }
}
