package com.emc.teamstv.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * Class for encapsulating PhotoSize object
 *
 * @author talipa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

  private final PhotoSize photoSize;
  private final String fileId;
  @JsonProperty("localPath")
  private String localPath;
  @JsonProperty("caption")
  private String caption;
  @JsonProperty("isLoaded")
  private boolean isLoaded;
  @JsonProperty("transferId")
  private String transferId = "";

  @JsonCreator
  private Photo(@JsonProperty("photoSize") PhotoSize photoSize,
      @JsonProperty("fileId") String fileId) {
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

  public String getLocalPath() {
    return localPath;
  }

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
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

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getTransferId() {
    return transferId;
  }

  public void setTransferId(String transferId) {
    this.transferId = transferId;
  }

  public String getCaption() {
    return caption;
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
        ", localPath='" + localPath + '\'' +
        ", caption='" + caption + '\'' +
        ", isLoaded=" + isLoaded +
        '}';
  }
}
