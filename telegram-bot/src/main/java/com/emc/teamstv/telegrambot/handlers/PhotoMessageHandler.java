package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.THANKS_FOR_PHOTO;

import com.emc.teamstv.telegrambot.BotReplies;
import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.IdGenerator;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Class does: 1. Handling photo messages. 2. Creates Inline keyboard.
 *
 * @author talipa
 */

@Service
public class PhotoMessageHandler extends Handler {

  private final Keyboard keyboard;
  private final IdGenerator<String> generator;

  public PhotoMessageHandler(
      Keyboard keyboard,
      TransferService<String, Photo> transferService,
      IdGenerator<String> generator) {
    super(transferService);
    this.keyboard = keyboard;
    this.generator = generator;
  }

  @Override
  public void onUpdateReceived() {
    String user = update.getMessage().getFrom().getUserName();
    String caption = update.getMessage().getCaption();
    Optional<PhotoSize> photo = update.getMessage()
        .getPhoto()
        .stream()
        .max(Comparator.comparing(PhotoSize::getFileSize));
    photo.ifPresent(
        p -> {
          log.info("PhotoSize object from user {} received", user);
          SendMessage msg = (SendMessage) prepareResponse(THANKS_FOR_PHOTO);
          Photo model = Photo.getPhotoModel(p, p.getFileId());
          model.setCaption(caption);
          model.setLoaded(false);
          String id = generator.getUniq();
          keyboard.keyboard(model, id).ifPresent(msg::setReplyMarkup);
          transferService.set(String.valueOf(id), model);
          sendText(msg);
        }
    );
  }

  @Override
  void getContent() {

  }

  @Override
  Response operateOnContent() {
    return null;
  }

  @Override
  void createKeyboard() {

  }
}
