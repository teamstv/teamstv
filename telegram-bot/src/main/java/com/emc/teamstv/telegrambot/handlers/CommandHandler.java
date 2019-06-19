package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.model.BotCommandsEnum;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Command handler
 *
 * @author talipa
 */

@Service
public class CommandHandler extends Handler {

  @Override
  public void onUpdateReceived() {
    String command = update.getMessage().getText();
    if (isKnownCommand(command)) {
      executeCommand(command);
    } else {
      SendMessage msg = prepareResponse(BotReplies.UNKNOWN_COMMAND);
      sendText(msg);
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
