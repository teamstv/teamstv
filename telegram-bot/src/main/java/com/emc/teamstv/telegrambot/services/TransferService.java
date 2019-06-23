package com.emc.teamstv.telegrambot.services;

import java.util.Optional;

/**
 * Service used to transfer data between handlers
 *
 * @param <T> id value, used to get necessary transfer object
 * @param <E> transfer object
 * @author talipa
 */
public interface TransferService<T extends Number, U, E> {

  Optional<E> get(T id);

  void set(T id, E e);

  void delete(T id);

  Optional<E> get(U user);

  void set(U user, T id);

  void delete(U user);

}
