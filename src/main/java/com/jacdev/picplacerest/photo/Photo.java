package com.jacdev.picplacerest.photo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="images")
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long albumId;
	private String userId;
	private String description;
	private String title;
	private boolean isPublic;
	
	//TODO :  the corresponding db columns and probably these too
	//			since we only want to provide a path to the image resource
	@Transient private String thumbnailData; //base64 bytes
	@Transient private String mediumData; //base64 bytes
	@Transient private String largeData; //base64 bytes
	
	@Transient private byte[] mediumBytes;
	
	public Photo() {
	}
	
	public byte[] getMediumBytes() {
		return this.mediumBytes;
	}
	
	public void setMediumBytes(byte[] bytes) {
		this.mediumBytes = bytes.clone(); //TODO: might want to just copy the reference for speed.
	}
	
	public long getId() {
		return this.id;
	}
	
	public boolean isInitialised() {
		return this.userId != null;
	}
	
	@Basic
	public String getUserId() {
		return this.userId;
	}
	@Basic
	public String getDescription() {
		return this.description;
	}

	@Basic
	public String getTitle() {
		return this.title;
	}
	@Basic
	public long getAlbumId() {
		return this.albumId;
	}
	
	@Basic
	public boolean getIsPublic() {
		return this.isPublic;
	}

	
	public String getThumbnailData() {
		return this.thumbnailData;
	}	
	public String getMediumData() {
		return this.mediumData;
	}	
	public String getLargeData() {
		return this.largeData;
	}	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public void setId(long id) {
		this.id = id;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	
	public void setThumbnailData(String data) {
		this.thumbnailData = data;
	}

	public void setMediumData(String data) {
		this.mediumData = data;
	}
	public void setLargeData(String data) {
		this.largeData = data;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}

