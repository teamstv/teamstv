package com.emc.teamstv.telegrambot.providers;

import com.emc.teamstv.telegrambot.services.IdGenerator;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of IdGenerator service based on AtomicLong
 * @author talipa
 */

@Service
public class IdGeneratorImpl implements IdGenerator<String> {

  private static final AtomicLong generator = new AtomicLong(0);

  @Override
  public String getUniq() {
    return String.valueOf(generator.getAndIncrement());
  }
}
