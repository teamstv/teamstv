package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.model.Photo;
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
public class PhotoTransferServiceImpl implements TransferService<String, Photo> {

  private final Map<String, Photo> photoSizeMap = new ConcurrentHashMap<>();

  @Override
  public Optional<Photo> get(String s) {
    return Optional.ofNullable(photoSizeMap.get(s));
  }

  @Override
  public void set(String s, Photo model) {
    photoSizeMap.put(s, model);
  }

  @Override
  public void delete(String s) {
    photoSizeMap.remove(s);
  }
}
