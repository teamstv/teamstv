package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.model.PhotoModel;
import com.emc.teamstv.telegrambot.services.BotRepo;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class BotRepoImpl implements BotRepo<PhotoModel, Long> {

  private final Map<Long, Set<PhotoModel>> map = new ConcurrentHashMap<>();

  @PostConstruct
  private void init() {

  }

  @Override
  public void save(PhotoModel model, Long aLong) {
    Set<PhotoModel> models = map.getOrDefault(aLong, new HashSet<>());
    models.add(model);
    map.put(aLong,models);
  }

  @Override
  public boolean checkByID(Long aLong, PhotoModel model) {
    Set<PhotoModel> models = map.get(aLong);
    if (models == null) {
      return false;
    }
    return models.stream().anyMatch(m -> m.equals(model));
  }

  @Override
  public void deleteByID(Long aLong) {
    map.remove(aLong);
  }

  @Override
  public void deleteAll() {
    map.clear();
  }

  @Override
  public void deleteByValue(Long aLong, PhotoModel model) {
    Set<PhotoModel> models = map.get(aLong);
    if (models == null) {
      return;
    }
    models.remove(model);
  }
}
