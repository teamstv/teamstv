package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.model.Photo;
import com.emc.teamstv.telegrambot.services.BotRepo;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of BotRepo service
 *
 * @author talipa
 */

@Service
public class BotRepoImpl implements BotRepo<Photo, Long> {

  private final Map<Long, Set<Photo>> map = new ConcurrentHashMap<>();

  @PostConstruct
  private void init() {

  }

  @Override
  public void save(Photo model, Long aLong) {
    Set<Photo> models = map.getOrDefault(aLong, new HashSet<>());
    models.add(model);
    map.put(aLong, models);
  }

  @Override
  public boolean checkByID(Long aLong, Photo model) {
    Set<Photo> models = map.get(aLong);
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
  public void deleteByValue(Long aLong, Photo model) {
    Set<Photo> models = map.get(aLong);
    models.remove(model);
  }
}
