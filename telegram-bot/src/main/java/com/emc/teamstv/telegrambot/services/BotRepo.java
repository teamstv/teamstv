package com.emc.teamstv.telegrambot.services;

import java.util.Optional;

public interface BotRepo<T, ID> {

  void save(T t, ID id);

  boolean checkByID(ID id, T t);

  void deleteByID(ID id);

  void deleteAll();

  void deleteByValue(ID id, T t);
}
