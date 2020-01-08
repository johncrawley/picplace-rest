package com.jacdev.picplacerest.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoUtils {


	@Value("${thumbnail.height}")  int THUMBNAIL_HEIGHT=100;
	@Value("${thumbnail.width}")   int THUMBNAIL_WIDTH=100;
	@Value("${medium.width}")   int MEDIUM_WIDTH=100;
	@Value("${medium.height}")  int MEDIUM_HEIGHT=100;
	@Value("${large.width}")   	int LARGE_WIDTH=100;
	@Value("${large.height}")   int LARGE_HEIGHT=100;

	
	private BufferedImage convertToImage(byte[] bytes) {
		BufferedImage bim = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			bim = ImageIO.read(inputStream);
		}catch(IOException e) {
			System.out.println("Error converting image bytes to bufferedImage");
			return null;
		}
		return bim;
	}
	
	private BufferedImage createThumbnail(BufferedImage original, int newWidth, int newHeight) {
		
		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g = thumbnail.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(original ,0,0,null);//newWidth, newHeight, null);
		g.drawImage(original, 0, 0, newWidth, newHeight,null);
		//g.setColor(Color.BLUE);
		//g.drawRect(40, 40, 80, 80);
	
		g.dispose();
		
		return thumbnail;
	}
	
	private static byte[] convertToBytes(BufferedImage bim) {
		//int outputStreamSize = bim.getWidth() * bim.getHeight() * 8 + 5000;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "jpg", baos);
		
		}catch(IOException e) {
			System.out.println("Error try to convert thumbnail image back to byte array");
		}
			return baos.toByteArray();
		
	}

	public byte[] createByteArray(byte[] originalBytes, PhotoSize photoSize) {
		
		if(photoSize == PhotoSize.LARGE) {
			return originalBytes;
		}
		BufferedImage bim = convertToImage(originalBytes);
		PhotoDimensions dims = getDimensions(photoSize);
		BufferedImage thumbnailImage = createThumbnail(bim, dims.getWidth() , dims.getHeight());
		
		return convertToBytes(thumbnailImage);
	}
	
	public byte[] createThumbnailByteArray(byte[] originalBytes) {
		
		BufferedImage bim = convertToImage(originalBytes);
		BufferedImage thumbnailImage = createThumbnail(bim, THUMBNAIL_WIDTH , THUMBNAIL_HEIGHT);
		
		return convertToBytes(thumbnailImage);
	}
	
	
	private <E> List<E> toList(Iterable<E> i){
		
		List<E> list = new ArrayList<>();
		i.forEach(list::add);
		return list;
	}
	
	
	private PhotoDimensions getDimensions(PhotoSize photoSize) {
		
		switch(photoSize) {
		
		case THUMBNAIL:
			return new PhotoDimensions(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
		case MEDIUM:
			return new PhotoDimensions(MEDIUM_WIDTH, MEDIUM_HEIGHT);
		case LARGE:
			return new PhotoDimensions(LARGE_WIDTH, LARGE_HEIGHT);
		}
		return new PhotoDimensions(100,100);
	}
	
}
