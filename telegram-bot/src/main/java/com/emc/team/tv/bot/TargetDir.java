package com.emc.team.tv.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetDir {

  private final String location;
  private final Properties props;

  private final static Logger log = LoggerFactory.getLogger(TargetDir.class);


  private TargetDir(String location, Properties props) {
    this.location = location;
    this.props = props;
  }

  public List<String> getPath() {
    List<String> path = new ArrayList<>();
    if (location.equals(props.getProperty("both").toLowerCase())) {
      path.add(props.getProperty("path") + props.getProperty("once").toLowerCase() + "/");
      path.add(props.getProperty("path") + props.getProperty("carousel").toLowerCase() + "/");
    } else {
      path.add(props.getProperty("path") + location + "/");
    }
    createDir(path);
    return path;
  }

  public static TargetDir getTargetDir(String location, Properties props) {
    return new TargetDir(location, props);
  }

  void createDir(List<String> paths) {
    for(String path: paths) {
      try {
        Files.createDirectories(Paths.get(path));
        log.info("CallbackQuery result analyzed");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
