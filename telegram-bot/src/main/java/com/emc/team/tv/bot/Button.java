package com.emc.team.tv.bot;

public class Button {

  private final String label;
  private final String data;

  public Button(String label, String data) {
    this.label = label;
    this.data = data;
  }

  public String getLabel() {
    return label;
  }

  public String getData() {
    return data.toLowerCase();
  }
}
