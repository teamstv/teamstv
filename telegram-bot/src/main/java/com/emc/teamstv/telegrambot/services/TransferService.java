package com.emc.teamstv.telegrambot.services;

import java.util.Optional;

/**
 * Service used to transfer data between handlers
 *
 * @param <ID> id value, used to get necessary transfer object
 * @param <E> transfer object
 * @author talipa
 */
public interface TransferService<ID, E> {

  Optional<E> get(ID id);

  void set(ID id, E e);

  void delete(ID id);

}
