package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class CallbackHandler extends Handler {

  protected CallbackHandler(
      TransferService<String, Photo> transferService) {
    super(transferService);
  }

  @Override
  final Optional<? extends BotApiObject> getContent() {
    return Optional.ofNullable(update.getCallbackQuery());
  }

  final String getTransferID(ButtonNameEnum nameEnum) {
    return getContent().map(
       c -> ((CallbackQuery)c).getData().replace(nameEnum.getData(), "")
    ).orElse("");
  }

  final Optional<Photo> getPhotoModel(ButtonNameEnum nameEnum) {
    return transferService.get(getTransferID(nameEnum));
  }

}
