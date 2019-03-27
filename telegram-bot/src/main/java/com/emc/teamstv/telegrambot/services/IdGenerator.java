package com.emc.teamstv.telegrambot.services;

/**
 * Service provides uniq ID values
 * @param <ID> type of id value
 * @author talipa
 */

public interface IdGenerator<ID> {

  ID getUniq();
}
