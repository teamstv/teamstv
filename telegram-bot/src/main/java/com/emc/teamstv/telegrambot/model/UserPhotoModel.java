package com.emc.teamstv.telegrambot.model;

import java.util.Objects;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Class for encapsulating PhotoSize object
 * @author talipa
 */
public class UserPhotoModel {

  private final PhotoSize photoSize;
  private final String fileId;
  private String localPath;
  private boolean hasCaption;
  private boolean isLoaded;

  private UserPhotoModel(PhotoSize photoSize, String fileId) {
    this.photoSize = photoSize;
    this.fileId = fileId;
  }

  public static UserPhotoModel getPhotoModel(PhotoSize photoSize, String fileId) {
    return new UserPhotoModel(photoSize, fileId);
  }

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }

  public void hasCaption(boolean hasCaption) {
    this.hasCaption = hasCaption;
  }

  public void setLoaded(boolean loaded) {
    isLoaded = loaded;
  }

  public PhotoSize getPhotoSize() {
    return photoSize;
  }

  public String getFileId() {
    return fileId;
  }

  public String getLocalPath() {
    return localPath;
  }

  public boolean hasCaption() {
    return hasCaption;
  }

  public boolean isLoaded() {
    return isLoaded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserPhotoModel)) {
      return false;
    }
    UserPhotoModel that = (UserPhotoModel) o;
    return fileId.equals(that.fileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId);
  }
}
