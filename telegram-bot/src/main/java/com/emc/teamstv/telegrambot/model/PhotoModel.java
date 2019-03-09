package com.emc.teamstv.telegrambot.model;

import java.util.Objects;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Class for encapsulating PhotoSize object
 *
 * @author talipa
 */
public class PhotoModel {

  private final PhotoSize photoSize;
  private final String fileId;
  private String localPath;
  private boolean hasCaption;
  private boolean isLoaded;

  private PhotoModel(PhotoSize photoSize, String fileId) {
    this.photoSize = photoSize;
    this.fileId = fileId;
  }

  public static PhotoModel getPhotoModel(PhotoSize photoSize, String fileId) {
    return new PhotoModel(photoSize, fileId);
  }

  public void hasCaption(boolean hasCaption) {
    this.hasCaption = hasCaption;
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

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }

  public boolean hasCaption() {
    return hasCaption;
  }

  public boolean isLoaded() {
    return isLoaded;
  }

  public void setLoaded(boolean loaded) {
    isLoaded = loaded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PhotoModel)) {
      return false;
    }
    PhotoModel that = (PhotoModel) o;
    return fileId.equals(that.fileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId);
  }

  @Override
  public String toString() {
    return "PhotoModel{" +
        "fileId='" + fileId + '\'' +
        ", localPath='" + localPath + '\'' +
        ", hasCaption=" + hasCaption +
        ", isLoaded=" + isLoaded +
        '}';
  }
}
