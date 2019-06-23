package com.emc.teamstv.telegrambot.handlers;

import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Optional;

public abstract class CallbackHandler extends Handler {

  protected CallbackHandler(
      TransferService<String, Photo> transferService) {
    super(transferService);
  }

  final String getTransferID(ButtonNameEnum nameEnum) {
    return update.getCallbackQuery()
        .getData()
        .replace(nameEnum.getData(), "");
  }

  final Optional<Photo> getPhotoModel(ButtonNameEnum nameEnum) {
    return transferService.get(getTransferID(nameEnum));
  }

}
