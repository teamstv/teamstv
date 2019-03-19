package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.model.PhotoModel;
import com.emc.teamstv.telegrambot.services.TransferService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of transfer service based on ConcurrentHashMap
 *
 * @author talipa
 */

@Service
public class PhotoTransferService implements TransferService<String, PhotoModel> {

  private final Map<String, PhotoModel> photoSizeMap = new ConcurrentHashMap<>();

  @Override
  public Optional<PhotoModel> get(String s) {
    return Optional.ofNullable(photoSizeMap.get(s));
  }

  @Override
  public void put(String s, PhotoModel model) {
    photoSizeMap.put(s, model);
  }

  @Override
  public void delete(String s) {
    photoSizeMap.remove(s);
  }
}
