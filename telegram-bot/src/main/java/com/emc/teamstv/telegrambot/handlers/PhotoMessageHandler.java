package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.THANKS_FOR_PHOTO;

import com.emc.teamstv.telegrambot.BotProperties;
import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.UserPhotoModel;
import com.emc.teamstv.telegrambot.services.IdGenerator;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Class does: 1. Handling photo messages. 2. Creates Inline keyboard.
 *
 * @author talipa
 */

@Service
public class PhotoMessageHandler implements Handler {

  private final Keyboard keyboard;
  private final TransferService<String, UserPhotoModel> transferService;
  private final IdGenerator<String> generator;

  public PhotoMessageHandler(Keyboard keyboard,
      TransferService<String, UserPhotoModel> transferService,
      IdGenerator<String> generator) {
    this.keyboard = keyboard;
    this.transferService = transferService;
    this.generator = generator;
  }

  @Override
  public void onUpdateReceived(Update update, DefaultAbsSender sender) {
    if (update.hasMessage() && update.getMessage().hasPhoto()) {
      String user = update.getMessage().getFrom().getUserName();
      boolean hasCaption = update.getMessage().getCaption() == null;
      Optional<PhotoSize> photo = update.getMessage()
          .getPhoto()
          .stream()
          .max(Comparator.comparing(PhotoSize::getFileSize));
      photo.ifPresent(
          p-> {
            log.info("PhotoSize object from user " + user + " received");
            SendMessage msg = prepareResponse(update, THANKS_FOR_PHOTO.getResponse());
            UserPhotoModel model = UserPhotoModel.getPhotoModel(p, p.getFileId());
            model.hasCaption(hasCaption);
            model.setLoaded(false);
            String id = generator.getUniq();
            keyboard.keyboard(model, id).ifPresent(msg::setReplyMarkup);
            transferService.put(String.valueOf(id), model);
            sendText(msg, sender, update);
          }
      );
    }
  }
}
