package com.emc.teamstv.telegrambot.handlers;

import static com.emc.teamstv.telegrambot.BotReplies.THANKS_FOR_PHOTO;

import com.emc.teamstv.telegrambot.handlers.messages.Response;
import com.emc.teamstv.telegrambot.handlers.messages.TextResponse;
import com.emc.teamstv.telegrambot.model.Keyboard;
import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.IdGenerator;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
  Optional<? extends BotApiObject> getContent() {
    return update.getMessage()
        .getPhoto()
        .stream()
        .max(Comparator.comparing(PhotoSize::getFileSize));
  }

  @Override
  Optional<Photo> operateOnContent(BotApiObject content) {
    Photo model = null;
    if (content instanceof PhotoSize) {
      PhotoSize photo = (PhotoSize) content;
      model = Photo.getPhotoModel(photo, photo.getFileId());
      model.setLoaded(false);
      log.info("PhotoSize object from user {} received", getUser());
    }
    return Optional.ofNullable(model);
  }

  @Override
  void createKeyboard(Photo model, BotApiMethod msg) {
    String id = generator.getUniq();
    if (msg instanceof SendMessage) {
      SendMessage txtMsg = (SendMessage) msg;
      keyboard.keyboard(model, id).ifPresent(txtMsg::setReplyMarkup);
      transferService.set(String.valueOf(id), model);
    }
  }

  @Override
  Response getResponse() {
    return new TextResponse(THANKS_FOR_PHOTO.getResponse(), update);
  }
}
