package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.model.BotCommandsEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.BotRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Command handler
 *
 * @author talipa
 */

@Service
public class CommandHandler implements Handler {

  private final BotRepo<Photo, Long> repo;

  public CommandHandler(
      BotRepo<Photo, Long> repo) {
    this.repo = repo;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    String command = update.getMessage().getText();
    if (isKnownCommand(command)) {
      executeCommand(command);
    } else {
      SendMessage msg = prepareResponse(update, BotReplies.UNKNOWN_COMMAND);
      sendText(msg, sender, update);
    }
  }

  private boolean isKnownCommand(String command) {
    for (BotCommandsEnum cmd : BotCommandsEnum.values()) {
      if (cmd.getName().equals(command)) {
        return true;
      }
    }
    return false;
  }

  private void executeCommand(String command) {
    switch (command) {

    }
  }

}
