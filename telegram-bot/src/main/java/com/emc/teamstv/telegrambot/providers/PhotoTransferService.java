package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.model.UserPhotoModel;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Simple implementation of transfer service based on ConcurrentHashMap
 * @author talipa
 */

@Service
public class PhotoTransferService implements TransferService<String, UserPhotoModel> {

  private final Map<String, UserPhotoModel> photoSizeMap = new ConcurrentHashMap<>();

  @Override
  public Optional<UserPhotoModel> get(String s) {
    return Optional.ofNullable(photoSizeMap.get(s));
  }

  @Override
  public void put(String s, UserPhotoModel model) {
    photoSizeMap.put(s, model);
  }
}
