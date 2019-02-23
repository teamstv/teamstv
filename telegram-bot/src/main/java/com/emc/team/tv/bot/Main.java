package com.emc.team.tv.bot;

import com.jtelegram.api.TelegramBot;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

  public static void main(String[] args) throws Exception {
    Properties props = getProps();
    Register register = new Register(props.getProperty("token"), props.getProperty("apiURL"));
    TelegramBot bot = register.getBot();
    new TextMessageHandler(bot).handleEvent();
    new PhotoMessageHandler(bot, register, props).handleEvent();
  }

  public static Properties getProps() throws IOException {
    String propsName = "bot.properties";
    Properties properties = new Properties();
    InputStream ios = Main.class.getClassLoader().getResourceAsStream(propsName);
    properties.load(ios);
    return properties;
  }

}
