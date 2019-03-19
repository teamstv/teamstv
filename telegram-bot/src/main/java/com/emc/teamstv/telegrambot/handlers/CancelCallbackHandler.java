package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.CLEAN_UP;

import com.emc.teamstv.telegrambot.model.ButtonNameEnum;
import com.emc.teamstv.telegrambot.model.PhotoModel;
import com.emc.teamstv.telegrambot.services.BotRepo;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class for handling callbacks from inline keyboard. This one will cancel any progression on data
 * loading
 * @author talipa
 */
@Service
public class CancelCallbackHandler implements Handler {

  private final TransferService<String, PhotoModel> transferService;
  private final BotRepo<PhotoModel, Long> botRepo;

  public CancelCallbackHandler(
      TransferService<String, PhotoModel> transferService,
      BotRepo<PhotoModel, Long> botRepo) {
    this.transferService = transferService;
    this.botRepo = botRepo;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    getPhotoModel(update, transferService, ButtonNameEnum.CANCEL).ifPresent(
        model -> {
          String transferId = getTransferID(update, ButtonNameEnum.CANCEL);
          transferService.delete(transferId);
          log.info("Transfer service cleaned for transferId = " + transferId);
          long userId = update.getCallbackQuery().getFrom().getId();
          if (botRepo.checkByID(userId, model)) {
            botRepo.deleteByValue(userId, model);
          }
          log.info("Photo deleted from repo: " + model);
          if (model.isLoaded()) {
            try {
              Files.deleteIfExists(Paths.get(model.getLocalPath()));
              log.info("File " + model.getLocalPath() + " deleted");
            } catch (IOException e) {
              log.error("Error while deleting file = " + model.getFileId(), e);
            }
          }
        }
    );
    EditMessageText msg = prepareCallbackReply(update, CLEAN_UP);
    sendText(msg, sender, update);
  }
}
