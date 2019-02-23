package com.emc.team.tv.bot;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.chat.Chat;
import com.jtelegram.api.events.Event;
import com.jtelegram.api.events.EventHandler;
import com.jtelegram.api.events.EventRegistry;
import com.jtelegram.api.events.inline.keyboard.CallbackQueryEvent;
import com.jtelegram.api.events.message.PhotoMessageEvent;
import com.jtelegram.api.inline.keyboard.InlineKeyboardMarkup;
import com.jtelegram.api.inline.keyboard.InlineKeyboardRow;
import com.jtelegram.api.requests.GetFile;
import com.jtelegram.api.requests.message.framework.ReplyMarkup;
import com.jtelegram.api.requests.message.send.SendText;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PhotoMessageHandler extends MessageHandler {

  private final Register register;
  private final Properties props;
  private final Map<Long, List<String>> mediaMap = new ConcurrentHashMap<>();

  protected PhotoMessageHandler(TelegramBot bot, Register register, Properties props) {
    super(bot);
    this.register = register;
    this.props = props;
  }

  @Override
  public void handleEvent() {
    bot.getEventRegistry().registerEvent(PhotoMessageEvent.class, (event) -> {
      List<String> fileIds;
      long mediaGroupId = event.getMessage().getMediaGroupId();
      if (mediaGroupId > 0) {
        if (mediaMap.containsKey(mediaGroupId)) {
          fileIds =  mediaMap.get(mediaGroupId);
          fileIds.add(getFileId(event));
        } else {
          fileIds = new ArrayList<>();
          fileIds.add(getFileId(event));
          mediaMap.put(mediaGroupId, fileIds);
          Executors.newScheduledThreadPool(1).schedule(reply(event, fileIds), 1, TimeUnit.SECONDS);
        }
      } else {
        fileIds = new ArrayList<>();
        fileIds.add(getFileId(event));
        reply(event, fileIds).run();
      }
    });
  }

  Runnable reply(PhotoMessageEvent event, List<String> fileIds) {
    return () -> {
      bot.perform(
          eventHandler(event, ", спасибо за ваше фото")
              .replyMarkup(markup(props.getProperty("once"), props.getProperty("carousel"), props
                  .getProperty("both")))
              .build());

      String[] idsArr = new String[fileIds.size()];
      fileIds.toArray(idsArr);
      handleCallback(idsArr);
    };
  }

  String getFileId(PhotoMessageEvent event) {
    return event.getMessage()
        .getPhoto()
        .get(3)
        .getFileId();
  }

  ReplyMarkup markup(String... values) {
    log.debug("Inline keyboard created");
    Keyboard keyboard = Keyboard.getKeyboard(values);
    Set<InlineKeyboardRow> keyboards = keyboard.getKeyboardRows();
    return InlineKeyboardMarkup.builder()
        .inlineKeyboard(keyboards)
        .build();
  }

  void handleCallback(String[] fileIds) {
    bot.getEventRegistry()
        .registerEvent(CallbackQueryEvent.class, (query) -> {
          String location = query.getQuery().getData();
          TargetDir dir = TargetDir.getTargetDir(location, props);
          log.debug(query.toString());
          downloadPhoto(dir.getPath(), fileIds);
          cleanUp(query, fileIds);
        });
  }

  void downloadPhoto(List<String> dirs, String[] fileIds) {
    for (String fileId : fileIds) {
      bot.perform(GetFile.builder()
          .fileId(fileId)
          .callback(file -> {
            for (String dir : dirs) {
              try {
                File fileI = new File(dir + fileId + ".jpg");
                log.info("Path " + fileI);
                InputStream ios = register.downloadFile(file.getFilePath());
                Files.copy(ios, fileI.toPath(), StandardCopyOption.REPLACE_EXISTING);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
          })
          .build());
    }

  }

  //Вторая часть метода костылище
  @SuppressWarnings("unchecked")
  void cleanUp(CallbackQueryEvent query, String[] fileIds) {
    Chat chat = query.getQuery().getMessage().getChat();
    for (String fileId : fileIds) {
      bot.perform(SendText.builder()
          .text("Фото: " + fileId + ".jpg загружено")
          .chatId(chat.getChatId())
          .build()
      );
    }
    /* Собственно сам костыль
     * Через рефлексию будем дропать коллбэк из мапа
     * А то он будет его теребить постоянно и засрет нам всю картину
     * Вообще эта либа забагованна в усмерть
     */
    try {
      EventRegistry registry = bot.getEventRegistry();
      Field field = registry.getClass().getDeclaredField("handlers");
      field.setAccessible(true);
      Map<Class<? extends Event>, List<EventHandler<? extends Event>>> handlers = (Map) field.get
          (registry);
      handlers.remove(CallbackQueryEvent.class);
      field.set(registry, handlers);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
