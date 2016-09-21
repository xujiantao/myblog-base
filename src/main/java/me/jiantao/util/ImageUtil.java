package me.jiantao.util;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ImageUtil {
	
	private ImageUtil(){};

	/**
	 * 缩放图片返回流
	 * @param is 要缩放的图片的流
	 * @param resizeTimes 缩放的比例
	 * @return
	 */
	public static InputStream zoomImageReturnInputStream(InputStream is,
			float resizeTimes) {
		BufferedImage buffImg = zoomImage(is, resizeTimes);
		return bufferedImageToInputStream(buffImg);
	}
	
	public static InputStream zoomImageReturnInputStream(InputStream is,
			int toWidth, int toHeight) {
		BufferedImage buffImg = zoomImage(is, toWidth, toHeight);
		return bufferedImageToInputStream(buffImg);
	}

	/**
	 * 
	 * @param srcBfImg 被剪裁的图片流
	 * @param x 左上角剪裁点X坐标
	 * @param y 左上角剪裁点Y坐标
	 * @param width 剪裁出的图片的宽度
	 * @param height 剪裁出的图片的高度
	 * @return 剪裁出的图片流
	 */
	public static InputStream cutImage(InputStream is, int x, int y, int width, int height) {
		BufferedImage srcBfImg = inputStreamToBufferedImage(is);
		BufferedImage buffImg = cutBufferedImage(srcBfImg, x, y, width, height);
		return bufferedImageToInputStream(buffImg);
	}
	

	/**
	 * 
	 * @param icon 水印流
	 * @param img 图片流
	 * @param opacity 透明度
	 * @param x 水印左上角x坐标
	 * @param y 水印左上角y坐标
	 * @return 添加水印之后的图片流
	 */
	public static InputStream markImageByIconReturnInputStream(InputStream icon, InputStream img , float opacity, int x, int y) {
		BufferedImage buffImg = markImageByIcon(icon, img, opacity, x, y);
		return bufferedImageToInputStream(buffImg);
	}
	
	
	
	/**********************************上面是对外暴露的接口，下面的都是private的方法*************************************/

	/**
	 * @param is 要缩放的原始图像流
	 * @param resizeTimes 倍数,比如0.5就是缩小一半,0.98等等float类型
	 * @return 缩放后的图像流
	 */
	private static BufferedImage zoomImage(InputStream is, float resizeTimes) {
		BufferedImage result = null;
		try {
			BufferedImage im = ImageIO.read(is);
			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();
			/* 缩放后的图片的宽度和高度 */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);
			/* 新生成结果图片 */
			result = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_RGB);
			result.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		} catch (Exception e) {
			throw new RuntimeException("缩放图片异常", e);
		}
		return result;
	}

	/**
	 * 等比缩放png图片，保持背景透明度
	 * 
	 * @param is 图片流
	 * @param resizeTimes 缩放比例
	 * @return
	 */
	public static InputStream resizePNG(InputStream is, float resizeTimes) {

		try {
			BufferedImage bi2 = ImageIO.read(is);
			int width = bi2.getWidth();
			int height = bi2.getHeight();
			int newWidth = (int) (width * resizeTimes);
			int newHeight = (int) (height * resizeTimes);
			BufferedImage to = new BufferedImage(newWidth, newHeight,

			BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = to.createGraphics();

			to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,
					newHeight,

					Transparency.TRANSLUCENT);

			g2d.dispose();

			g2d = to.createGraphics();

			Image from = bi2.getScaledInstance(newWidth, newHeight,
					BufferedImage.SCALE_AREA_AVERAGING);
			g2d.drawImage(from, 0, 0, null);
			g2d.dispose();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.createImageOutputStream(out);
			ImageIO.write(to, "png", out);
			return new ByteArrayInputStream(out.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException("缩放png失败", e);
		}
	}

	private static InputStream bufferedImageToInputStream(BufferedImage buffImg) {

		Objects.requireNonNull(buffImg);

		ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
		try {
			ImageIO.createImageOutputStream(byteOs);
			ImageIO.write(buffImg, "jpeg", byteOs);
		} catch (IOException e) {
			throw new RuntimeException("转换失败", e);
		}
		return new ByteArrayInputStream(byteOs.toByteArray());
	}

	private static BufferedImage inputStreamToBufferedImage(InputStream is) {
		Objects.requireNonNull(is);
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	private static BufferedImage zoomImage(InputStream is, int toWidth,
			int toHeight) {
		BufferedImage result = null;
		try {
			BufferedImage im = ImageIO.read(is);
			/* 新生成结果图片 */
			result = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_RGB);
			result.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		} catch (Exception e) {
			throw new RuntimeException("创建缩略图发生异常", e);
		}
		return result;
	}

	/**
	 * 
	 * @param srcBfImg 被剪裁的BufferedImage
	 * @param x 左上角剪裁点X坐标
	 * @param y 左上角剪裁点Y坐标
	 * @param width 剪裁出的图片的宽度
	 * @param height 剪裁出的图片的高度
	 * @return 剪裁得到的BufferedImage
	 */
	private static BufferedImage cutBufferedImage(BufferedImage srcBfImg,
			int x, int y, int width, int height) {
		BufferedImage cutedImage = null;
		CropImageFilter cropFilter = new CropImageFilter(x, y, width, height);
		Image img = Toolkit.getDefaultToolkit().createImage(
				new FilteredImageSource(srcBfImg.getSource(), cropFilter));
		cutedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = cutedImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return cutedImage;
	}

	/**
	 * 
	 * @param icon 水印流
	 * @param img 图片流
	 * @param opacity 透明度
	 * @param x 水印左上角x坐标
	 * @param y 水印左上角y坐标
	 * @return 添加水印之后的图片流
	 */
	private static BufferedImage markImageByIcon(InputStream icon,
			InputStream img, float opacity, int x, int y) {
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(img);
			int srcWidth = srcImg.getWidth(null);
			int srcHeight = srcImg.getHeight(null);
			BufferedImage buffImg = new BufferedImage(srcWidth, srcHeight,
					BufferedImage.TYPE_INT_RGB);
			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcWidth, srcHeight,
					Image.SCALE_SMOOTH), 0, 0, null);

			// 得到Image对象。
			Image iconImg = ImageIO.read(icon);
			// int iconWidth = iconImg.getWidth(null);
			// int iconHeight = iconImg.getHeight(null);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					opacity));

			// 表示水印图片的位置
			g.drawImage(iconImg, x, y, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g.dispose();
			return buffImg;
		} catch (Exception e) {
			throw new RuntimeException("添加水印发生异常", e);
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				throw new RuntimeException("输出流关闭异常", e);
			}
		}
	}
}
