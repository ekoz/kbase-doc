/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.IOUtils;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午5:22:56
 * @version 1.0
 */
public class ImageProcessor extends AbstractProcessor {

	public ImageProcessor(File file, File imageFile) {
		super(file, imageFile);
	}

	@Override
	public void process() throws WatermarkException {
		try {
			Image srcImage = ImageIO.read(file);
			BufferedImage bufferImg = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImage.getScaledInstance(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB), 0, 0, null);
			ImageIcon imageIcon = new ImageIcon(IOUtils.toByteArray(new FileInputStream(imageFile)));
			Image img = imageIcon.getImage();
			float alpha = 0.3f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			//水印图片设计在右下角
			g.drawImage(img, srcImage.getWidth(null)-img.getWidth(null)-10, srcImage.getHeight(null)-img.getHeight(null)-10, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g.dispose();
			ImageIO.write(bufferImg, FilenameUtils.getExtension(file.getName()), file);
		} catch (FileNotFoundException e) {
			throw new WatermarkException("FileNotFoundException", e);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		}
	}

	
}
