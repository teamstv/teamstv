package com.emc.teamstv.telegrambot.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Class for encapsulating PhotoSize object
 *
 * @author talipa
 */
public class Photo {

  private final PhotoSize photoSize;
  private final String fileId;
  private String photoLocalPath;
  private String captionLocalPath;
  private String caption;
  private boolean isLoaded;
  private int transferId;

  private Photo(PhotoSize photoSize, String fileId) {
    this.photoSize = photoSize;
    this.fileId = fileId;
  }

  public static Photo getPhotoModel(PhotoSize photoSize, String fileId) {
    return new Photo(photoSize, fileId);
  }

  public PhotoSize getPhotoSize() {
    return photoSize;
  }

  public String getFileId() {
    return fileId;
  }

  public void setPhotoLocalPath(String photoLocalPath) {
    this.photoLocalPath = photoLocalPath;
  }

  public boolean hasCaption() {
    return caption != null;
  }

  public boolean isLoaded() {
    return isLoaded;
  }

  public void setLoaded(boolean loaded) {
    isLoaded = loaded;
  }

  public int getTransferId() {
    return transferId;
  }

  public void setTransferId(int transferId) {
    this.transferId = transferId;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public void setCaptionLocalPath(String captionLocalPath) {
    this.captionLocalPath = captionLocalPath;
  }

  public void delete() throws IOException {
    if (isLoaded) {
      Files.deleteIfExists(Paths.get(photoLocalPath));
    }
    if (hasCaption()) {
      Files.deleteIfExists(Paths.get(captionLocalPath));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Photo)) {
      return false;
    }
    Photo that = (Photo) o;
    return fileId.equals(that.fileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId);
  }

  @Override
  public String toString() {
    return "Photo{" +
        "fileId='" + fileId + '\'' +
        ", photoLocalPath='" + photoLocalPath + '\'' +
        ", caption='" + caption + '\'' +
        ", isLoaded=" + isLoaded +
        '}';
  }
}
