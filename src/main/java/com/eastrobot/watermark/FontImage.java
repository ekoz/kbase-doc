/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * copy from https://github.com/puyulin/learn_java
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月18日 上午10:11:03
 * @version 1.0
 */
public class FontImage {

	/**
	 * 根据指定的文本创建图片
	 * @author eko.zhan at 2018年9月18日 上午10:14:44
	 * @param text
	 * @throws WatermarkException 
	 */
	public static File createImage(String text) throws WatermarkException {
		Font font = new Font("宋体", Font.PLAIN, 100);
		int[] arr = getWidthAndHeight(text, font);
		int width = arr[0];
		int height = arr[1];
		// 创建图片 
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);//创建图片画布
//		Graphics gs = image.getGraphics();
//		Graphics2D g = (Graphics2D)gs;
//		g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
		Graphics2D g = image.createGraphics();
		// 增加下面代码使得背景透明
		image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = image.createGraphics();
		// 背景透明代码结束
//		g.fillRect(0, 0, width, height);//画出矩形区域，以便于在矩形区域内写入文字
		g.setColor(new Color(242, 242, 242));// 再换成黑色，以便于写入文字
		g.setFont(font);// 设置画笔字体
		g.translate(10, 10);
//		g.rotate(0.1*Math.PI);//旋转
		g.rotate(0.16);
		g.drawString(text, 0, font.getSize());// 画出一行字符串
		g.dispose();
		
		String property = System.getProperty("java.io.tmpdir");
		File imageFile = new File(property + "/kbs-watermark-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".png");
		try {
			// 输出png图片
			ImageIO.write(image, "png", imageFile);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		}
		return imageFile;
	}

	private static int[] getWidthAndHeight(String text, Font font) {
		Rectangle2D r = font.getStringBounds(text, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
		int unitHeight = (int) Math.floor(r.getHeight());//
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		int width = (int) Math.round(r.getWidth()) + 20;
		// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		int height = unitHeight + 20;
//		System.out.println("width:" + width + ", height:" + height);
		return new int[]{width, height*2};
	}  
}