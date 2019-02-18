package com.emc.team.tv.bot;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import java.lang.reflect.Field;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;


public class RegisterTest {

  @Spy
  private Register register = new Register("", "");

  @BeforeEach
  public void init() throws IllegalAccessException {
    TelegramBotRegistry registry = mock(TelegramBotRegistry.class);
    TelegramBot bot = mock(TelegramBot.class);
    doReturn(Collections.singleton(bot))
        .when(registry)
        .getBots();
    Field[] fields = register.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getName().equals("registry")) {
        field.setAccessible(true);
        field.set(register, registry);
      }
    }
  }

  @Test
  public void getBot() throws InterruptedException {
    TelegramBot bot = register.getBot();
    assertNotNull(bot);
  }
}
