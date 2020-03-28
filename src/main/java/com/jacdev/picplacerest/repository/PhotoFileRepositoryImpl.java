package com.jacdev.picplacerest.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.jacdev.picplacerest.entity.Photo;
import com.jacdev.picplacerest.utils.FilepathResolver;
import com.jacdev.picplacerest.utils.PhotoSize;
import com.jacdev.picplacerest.utils.PhotoUtils;

@Component
public class PhotoFileRepositoryImpl implements PhotoFileRepository {

	@Autowired private FilepathResolver filepathResolver;
	@Autowired private PhotoUtils photoUtils;

	final String ABSOLUTE_PATH_PREFIX = "file://";
	
	public PhotoFileRepositoryImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Photo save(Photo photo, byte[] bytes) {
		String userId = photo.getUserId();
		long photoId = photo.getId();
		boolean isSaved =  savePhoto(bytes,userId, photoId, PhotoSize.LARGE) && 
				savePhoto(bytes,userId, photoId, PhotoSize.MEDIUM) && 
				savePhoto(bytes,userId, photoId, PhotoSize.THUMBNAIL);

		if(isSaved) {
			return photo;
		}
		
		return null;
	}
	
	

	private boolean savePhoto(byte[] bytes, String username, long photoId, PhotoSize photoSize) {
		
		byte[] bytesToWrite = photoUtils.createByteArray(bytes, photoSize);
		String path = filepathResolver.getPhotoPath(username, photoId, photoSize);
		
		return writeToFile(bytesToWrite, path);
	}
	
	
	
	@Override
	public boolean delete(String userId, long photoId) {
		
		boolean largeDeleted = deletePhoto(userId, photoId, PhotoSize.LARGE);
		boolean mediumDeleted = deletePhoto(userId, photoId, PhotoSize.MEDIUM);
		boolean thumbnailDeleted = deletePhoto(userId, photoId, PhotoSize.THUMBNAIL);
		return largeDeleted && mediumDeleted && thumbnailDeleted;
	}
	
	
	private boolean deletePhoto(String userId, long photoId, PhotoSize photoSize) {
		String path = filepathResolver.getPhotoPath(userId,  photoId,  photoSize);
		File file = new File(path);
		return file.delete();
		
	}
	
	@Override
	public void attachPhotoData(Photo photo, PhotoSize photoSize) {
		String path = filepathResolver.getPhotoPath(photo.getUserId(), photo.getId(), photoSize);
		String photoStr = getPhotoData(path);
		
		switch(photoSize) {
		case THUMBNAIL:
			photo.setThumbnailData(photoStr);
			break;
		case MEDIUM:
			photo.setMediumData(photoStr);
			break;
		case LARGE:
			photo.setLargeData(photoStr);
		}
	}
	
	@Override
	public String getImageData(String userId, long photoId, PhotoSize photoSize) {
		
		String path = filepathResolver.getPhotoPath(userId,  photoId,  photoSize);
		return getPhotoData(path);
	}
	
	
		
	
	@Override
	public void attachThumbnailData(Page<Photo> photos) {
		for(Photo photo : photos) {
			String path = filepathResolver.getPhotoPath(photo.getUserId(), photo.getId(), PhotoSize.THUMBNAIL);
			photo.setThumbnailData(getPhotoData(path));
		}
	}
	
	
	
	
	private boolean writeToFile(byte[] bytes, String filename) {
		
		boolean success = false;
		File file = new File(filename);
		if(!file.exists()) {
			try {
				//file.mkdirs();
				file.createNewFile();
				System.out.println("Tried to create file: " + file.getAbsolutePath() + " exists: " + file.exists());
			}catch(IOException e) {
				System.out.println("Unable to create the file for saving image: " +  filename);
				e.printStackTrace(); 
				return false;
			}
		} 
		try (OutputStream outputStream = new FileOutputStream(filename);
				 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){  
	
			 byteArrayOutputStream.write(bytes);
			 byteArrayOutputStream.writeTo(outputStream);
			 success = true;
		 } catch (IOException e) {  
		   e.printStackTrace();  
		 }
		return success;
	}
	
	
	private String getPhotoData(String path) {
		
		File f = new File(path);
		byte[] content = new byte[(int) f.length()];

		try (InputStream in = new FileInputStream(f)){
			System.out.println("Reading file: "+  path);
		    for (int off = 0, read;
		        (read = in.read(content, off, content.length - off)) > 0;
		        off += read);	    
		    
		    return DatatypeConverter.printBase64Binary(content);
		    
		} catch (IOException e) {
		    // Some error occured
		}	
		return "";		
	}
	
	@Override
	public String getPath(Photo photo, PhotoSize photoSize) {
		return filepathResolver.getPhotoPath(photo.getUserId(), photo.getId(), photoSize);
	}
	

	@Override
	public void attachPhotoBytes(Photo photo, PhotoSize photoSize) {
		String path = filepathResolver.getPhotoPath(photo.getUserId(), photo.getId(), photoSize);
		System.out.println("PhotoFileReposImpl attachPhotoBytes() path: "+  path);
		byte[] bytes = getPhotoBytes(path);
		
		photo.setMediumBytes(bytes);
	}
	
	
	public byte[] getPhotoBytes(String path) {
		
		byte[] bytes = new byte[0];
		File file = new File(path);
		try (FileInputStream fis = new FileInputStream(file)){
			bytes = fis.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	
	}
}
