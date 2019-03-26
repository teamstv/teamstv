package com.emc.teamstv.telegrambot.services;

import com.emc.teamstv.telegrambot.model.Photo;
import java.util.List;

public interface BotRepo<T, ID> {

  void save(T t, ID id);

  boolean checkByID(ID id, T t);

  void deleteByID(ID id);

  void deleteAll();

  void deleteByValue(ID id, T t);

  Iterable<Photo> list(ID id);

  Iterable<Photo> listAll();

}
