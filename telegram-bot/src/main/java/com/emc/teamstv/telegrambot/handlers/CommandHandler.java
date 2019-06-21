package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.model.BotCommandsEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Command handler
 *
 * @author talipa
 */

@Service
public class CommandHandler extends Handler {

  public CommandHandler(TransferService<String, Photo> transferService) {
    super(transferService);
  }

  @Override
  public void onUpdateReceived() {
    String command = update.getMessage().getText();
    if (isKnownCommand(command)) {
      executeCommand(command);
    } else {
      BotApiMethod msg = prepareResponse(BotReplies.UNKNOWN_COMMAND);
      sendText(msg);
    }
  }

  @Override
  Optional<? extends BotApiObject> getContent() {
    return Optional.empty();
  }

  @Override
  Optional<Photo> operateOnContent(BotApiObject content) {
    return Optional.empty();
  }

  @Override
  void createKeyboard(Photo model, BotApiMethod msg) {

  }

  @Override
  Response getResponse() {
    return null;
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
