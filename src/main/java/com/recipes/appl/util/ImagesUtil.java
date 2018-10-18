package com.recipes.appl.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Kastalski Sergey
 */
public abstract class ImagesUtil {
	
	private final static String BASE64_DELIMITER = ",";
	
	public static String compressImage(final String jsBase64Image, final int maxWidth, final int maxHeight) throws IOException {
		BufferedImage image = convertToBufferedImageFromJSBase64(jsBase64Image);
		image = resizeImage(image, maxWidth, maxHeight);
		
		final String imageMetadata = getMetaDataFromJSBase64(jsBase64Image);
		return convertToJSBase64(image, imageMetadata);
	}
	
	private static String getMetaDataFromJSBase64(final String jsBase64) {
		return StringUtils.substringBefore(jsBase64, BASE64_DELIMITER);
	}
	
	private static String getBase64FromJSBase64(final String jsBase64) {
		return StringUtils.substringAfter(jsBase64, BASE64_DELIMITER);
	}
	
	private static BufferedImage convertToBufferedImageFromJSBase64(final String jsBase64) throws IOException {
		final String base64 = getBase64FromJSBase64(jsBase64);
		return convertToBufferedImage(base64);
	}
	
	private static BufferedImage convertToBufferedImage(final String base64) throws IOException {
		final byte[] imageBytes = Base64.getDecoder().decode(base64);
		final ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		final BufferedImage image = ImageIO.read(bis);
		bis.close();
		
		return image;
	}
	
	private static String convertToJSBase64(final BufferedImage image, final String imageMetadata) throws IOException {
		return imageMetadata + BASE64_DELIMITER + convertToBase64(image, imageMetadata);
	}
	
	private static String convertToBase64(final BufferedImage image, final String imageMetadata) throws IOException {
		final String imageFormat = StringUtils.substringAfter(StringUtils.substringBefore(imageMetadata, ";"), "/");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, imageFormat, baos);
		baos.flush();
		final byte[] imageBytes = baos.toByteArray();
		baos.close();
		
		return Base64.getEncoder().encodeToString(imageBytes);
	}
	
	private static BufferedImage resizeImage(final BufferedImage image, final int maxWidth, final int maxHeight) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		if (width <= maxWidth && height <= maxHeight) {
			return image;
		}
		
		final float widthCoef = (float) width / (float) maxWidth;
		final float heightCoef = (float) height / (float) maxHeight;
		final float coef = Math.max(widthCoef, heightCoef);
		
		final int finalWidth = Math.round((float) width / coef - 0.5f);
		final int finalHeight = Math.round((float) height / coef - 0.5f);
		
		
		final Image scaledImage = image.getScaledInstance(finalWidth, finalHeight, Image.SCALE_REPLICATE);
		final BufferedImage resultImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
		
		final Graphics2D g2d = resultImage.createGraphics();
		g2d.drawImage(scaledImage, 0, 0, null);
		g2d.dispose();
		
		return resultImage;
	}
}
