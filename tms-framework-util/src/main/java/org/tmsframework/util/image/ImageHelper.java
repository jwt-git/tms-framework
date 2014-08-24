package org.tmsframework.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageHelper {
	
	/**
	 * 添加水印图片
	 * 		默认不透明，水印边距为10像素,位置为9，右下角
	 * @param targetImg		被添加水印的图片
	 * @param watermarkImg		水印上去的图片	
	 * @throws IOException
	 */
	public static void pWatermarkImage(String targetImg, String watermarkImg) throws IOException {
		pWatermarkImage(targetImg,watermarkImg,9,1f,10);
	} 
	
	/**
	 * 添加水印图片
	 * 		默认不透明，水印边距为10像素
	 * @param targetImg		被添加水印的图片
	 * @param watermarkImg		水印上去的图片	
	 * @param position		九宫格位置参数,[1-9]
	 * @throws IOException
	 */
	public static void pWatermarkImage(String targetImg, String watermarkImg,int position) throws IOException {
		pWatermarkImage(targetImg,watermarkImg,position,1f,10);
	} 
	
	/**
	 * 添加水印图片
	 * 		默认水印边距为10像素
	 * @param targetImg		被添加水印的图片
	 * @param watermarkImg		水印上去的图片	
	 * @param position		九宫格位置参数,[1-9]
	 * @param alpha		水印透明度，0-1范围取值
	 * @throws IOException
	 */
	public static void pWatermarkImage(String targetImg, String watermarkImg,int position, float alpha) throws IOException {
		pWatermarkImage(targetImg,watermarkImg,position,alpha,10);
	} 
	
	/**
	 * 添加水印图片
	 * @param targetImg		被添加水印的图片
	 * @param watermarkImg		水印上去的图片	
	 * @param position		九宫格位置参数,[1-9]
	 * @param alpha		水印透明度，0-1范围取值
	 * @param margin	水印边距
	 * @throws IOException
	 */
	public static void pWatermarkImage(String targetImg, String watermarkImg,int position, float alpha, int margin) throws IOException {
		if (targetImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = targetImg
				.substring(targetImg.lastIndexOf(".") + 1);
		File tarImgFile = new File(targetImg);
		Image src = ImageIO.read(tarImgFile);
		File wmFile = new File(watermarkImg);
		Image wmSrc = ImageIO.read(wmFile);
		int[] xy = getXY(position,src, wmSrc,margin);
		BufferedImage image = watermarkImage(src,wmSrc,xy[0],xy[1],alpha,distImgType);
		ImageIO.write(image, distImgType, new File(targetImg));
	}


	private static int[] getXY(int position, Image src, int wmWidth, int wmHeight, int margin){
		int[] xy = new int[2];
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if(position==1){
			xy[0] = margin;
			xy[1] = margin;
		}else if(position==2){
			xy[0] = (width-wmWidth)/2;
			xy[1] = margin;
		}else if(position==3){
			xy[0] = width-wmWidth-margin;
			xy[1] = margin;		
		}else if(position==4){
			xy[0] = margin;
			xy[1] = (height-wmHeight)/2;
		}else if(position==5){
			xy[0] = (width-wmWidth)/2;
			xy[1] =  (height-wmHeight)/2;
		}else if(position==6){
			xy[0] = width-wmWidth-margin;
			xy[1] = (height - wmHeight)/2; 
		}else if(position==7){
			xy[0] =margin;
			xy[1] = height - wmHeight - margin;
		}else if(position==8){
			xy[0] =  (width-wmWidth)/2;
			xy[1] = height - wmHeight - margin;
		}else{
			xy[0] = width-wmWidth-margin;
			xy[1] = height - wmHeight - margin;
		}
		return xy;
	}
	
	private static int[] getXY(int position, Image src, Image wmSrc, int margin) {
		int wmWidth = wmSrc.getWidth(null);
		int wmHeight = wmSrc.getHeight(null);
		return getXY(position,src,wmWidth,wmHeight,margin);
	}


	private static BufferedImage watermarkImage(Image targetImg, Image watermarkImg,
			int x, int y, float alpha, String distImgType) throws IOException {
		int width = targetImg.getWidth(null);
		int height = targetImg.getHeight(null);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
//		如果是png图片使用透明背景
		Graphics2D g = image.createGraphics();
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		g.drawImage(targetImg, 0, 0, width, height, null);
		//读取水印文件
		int wmWideth = watermarkImg.getWidth(null);
		int wmHeight = watermarkImg.getHeight(null);
		//添加水印
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawImage(watermarkImg, x, y, wmWideth, wmHeight, null);
		g.dispose();
		return image;
	}
	
	
	private static int[] getTextXY(int position, Image src, String text, int fontsize, int margin){
		int wmWidth;
//		判断是否是纯英文水印
		if(text.getBytes().length==text.length()){
			wmWidth = text.length()*fontsize / 2 +20;//英文水印长度除以2再加20像素的修正
		}else{
			wmWidth = text.length()*fontsize;
		}
		int wmHeight = fontsize;
		return getXY(position,src,wmWidth,wmHeight,margin);
	}
	
	/**
	 * 图片添加图片水印
	 * 		不支持Gif动态图片
	 * @param targetImg				被添加水印的图片
	 * @param watermarkImg		水印上去的图片	
	 * @param x		以图片左上角为原点的X轴偏移量
	 * @param y		以图片左上角为原点的Y轴偏移量
	 * @param alpha		水印图片透明度，取值范围0-1
	 * @throws IOException 
	 */
	public static void watermarkImage(String targetImg, String watermarkImg,
			int x, int y, float alpha) throws IOException {
		if (targetImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = targetImg
				.substring(targetImg.lastIndexOf(".") + 1);
		//读取目标文件
		File tarImgFile = new File(targetImg);
		Image src = ImageIO.read(tarImgFile);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
//		如果是png图片使用透明背景
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		
		g.drawImage(src, 0, 0, width, height, null);
		File wmFile = new File(watermarkImg);
		//读取水印文件
		Image wmSrc = ImageIO.read(wmFile);
		int wmWideth = wmSrc.getWidth(null);
		int wmHeight = wmSrc.getHeight(null);
		//			添加水印
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawImage(wmSrc, x, y, wmWideth, wmHeight, null);
		g.dispose();
		//			重新写回文件
		ImageIO.write(image, distImgType, new File(targetImg));
	}

	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片
	 * @param watermarkText  水印文字
	 * @param targetImg  目标图片
	 * @param fontName  字体名
	 * @param fontStyle  字体样式
	 * @param color  字体颜色
	 * @param fontSize  字体大小
	 * @param x   以图片左上角为原点的X轴偏移量
	 * @param y   以图片左上角为原点的Y轴偏移量
	 * @param alpha		水印文字透明度，取值范围0-1
	 * @throws IOException 
	 */
	public static void watermarkText(String watermarkText, String targetImg,
			String fontName, int fontStyle, Color color, int fontSize, int x,
			int y, float alpha) throws IOException {
		if (targetImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = targetImg
				.substring(targetImg.lastIndexOf(".") + 1);
		File tarImgFile = new File(targetImg);
		Image tarImg = ImageIO.read(tarImgFile);
		int width = tarImg.getWidth(null);
		int height = tarImg.getHeight(null);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
//		如果是png图片使用透明背景
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		
		g.drawImage(tarImg, 0, 0, width, height, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawString(watermarkText, x, y + fontSize);
		g.dispose();
		//			重新写回文件
		ImageIO.write(image, distImgType, new File(targetImg));
	}

	public static void watermarkText(String watermarkText, String targetImg,int position,int margin,
			String fontName, int fontStyle, Color color, int fontSize, float alpha,String distImgType) throws IOException {
		File tarImgFile = new File(targetImg);
		Image tarImg = ImageIO.read(tarImgFile);
		int width = tarImg.getWidth(null);
		int height = tarImg.getHeight(null);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		int[] xy = getTextXY(position, tarImg, watermarkText, fontSize, margin);
		g.drawImage(tarImg, 0, 0, width, height, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawString(watermarkText, xy[0], xy[1] + fontSize);
		g.dispose();
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片
	 * @param watermarkText  水印文字
	 * @param targetImg  目标图片
	 * @param color  字体颜色
	 * @param fontSize  字体大小
	 * @param x   以图片左上角为原点的X轴偏移量
	 * @param y   以图片左上角为原点的Y轴偏移量
	 * @param alpha		水印文字透明度，取值范围0-1
	 */
	public static void watermarkText(String watermarkText, String targetImg,
			Color color, int fontSize, int x, int y, float alpha)
			throws IOException {
		watermarkText(watermarkText, targetImg, "宋体", Font.BOLD, color,
				fontSize, x, y, alpha);
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片
	 * @param watermarkText		水印文字
	 * @param targetImg		目标图片
	 * @param position 	九宫格位置参数,[1-9]
	 * @param margin 		边距
	 * @param fontStyle 	样式
	 * @param fontName 	字体
	 * @param color		颜色
	 * @param fontSize		字体大小
	 * @param alpha		透明度，取值范围0-1
	 * @throws IOException 
	 */
	public static void pWatermarkText(String watermarkText, String targetImg,int position,int margin, Color color, int fontSize, float alpha, int fontStyle, String fontName) throws IOException{
		if (targetImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = targetImg
				.substring(targetImg.lastIndexOf(".") + 1);
		File tarImgFile = new File(targetImg);
		Image tarImg = ImageIO.read(tarImgFile);
		int width = tarImg.getWidth(null);
		int height = tarImg.getHeight(null);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
//		如果是png图片使用透明背景
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		int[] xy = getTextXY(position, tarImg, watermarkText, fontSize, margin);
		g.drawImage(tarImg, 0, 0, width, height, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawString(watermarkText, xy[0], xy[1] + fontSize);
		g.dispose();
		//			重新写回文件
		ImageIO.write(image, distImgType, new File(targetImg));
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片,
	 * 		默认设置位置为7（左下角），颜色：黑色，字体：宋体、粗体，字体大小：20px，边距：10px，透明度：1
	 * @param watermarkText		水印文字
	 * @param targetImg			目标图片
	 * @throws IOException
	 */
	public static void pWatermarkText(String watermarkText, String targetImg) throws IOException{
		pWatermarkText(watermarkText,targetImg,7,10,Color.BLACK,20,1f,Font.BOLD,"宋体");
	}

	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片,默认设置颜色：黑色，字体：宋体、粗体，字体大小：20px，边距：10px，透明度：1
	 * @param watermarkText		水印文字
	 * @param targetImg			目标图片
	 * @param position			九宫格位置参数,[1-9]
	 * @throws IOException
	 */
	public static void pWatermarkText(String watermarkText, String targetImg,int position) throws IOException{
		pWatermarkText(watermarkText,targetImg,position,10,Color.BLACK,20,1f,Font.BOLD,"宋体");
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片,默认设置字体：宋体、粗体，字体大小：20px，边距：10px，透明度：1
	 * @param watermarkText		水印文字
	 * @param targetImg			目标图片
	 * @param position			九宫格位置参数,[1-9]
	 * @param color		颜色
	 * @throws IOException
	 */
	public static void pWatermarkText(String watermarkText, String targetImg,int position,Color color) throws IOException{
		pWatermarkText(watermarkText,targetImg,position,10,color,20,1f,Font.BOLD,"宋体");
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片,默认设置字体：宋体、粗体，边距：10px，透明度：1
	 * @param watermarkText		水印文字
	 * @param targetImg			目标图片
	 * @param position			九宫格位置参数,[1-9]
	 * @param color		颜色
	 * @param fontSize		字体大小
	 * @throws IOException
	 */
	public static void pWatermarkText(String watermarkText, String targetImg,int position,Color color, int fontSize) throws IOException{
		pWatermarkText(watermarkText,targetImg,position,10,color,fontSize,1f,Font.BOLD,"宋体");
	}
	
	/**
	 * 图片添加文字水印
	 * 		不支持Gif动态图片
	 * @param watermarkText		水印文字
	 * @param targetImg			目标图片
	 * @param position			九宫格位置参数,[1-9]
	 * @param color		颜色
	 * @param fontSize		字体大小
	 * @param alpha		透明度，取值范围0-1
	 * @throws IOException 
	 */
	public static void pWatermarkText(String watermarkText, String targetImg,int position,Color color, int fontSize, float alpha) throws IOException{
		pWatermarkText(watermarkText,targetImg,position,10,color,fontSize,alpha,Font.BOLD,"宋体");
	}
	
	/** 
	 * 光滑缩放图片
	 * 		不支持动态Gif图片(Gif图片格式受到版权保护)
	 * 		生成的图片质量比较好 但速度慢
	 * @param srcImg  		原图片文件路径 
	 * @param distImg  	生成的图片文件路径 
	 * @param distWidth  生成图片的宽度 
	 * @param distHeight  生成图片的高度 
	 * @throws IOException 
	 */
	public static void zoomImgSmooth(String srcImg, String distImg,
			int distWidth, int distHeight) throws IOException {
		if (distImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = distImg.substring(distImg.lastIndexOf(".") + 1);
		File srcfile = new File(srcImg);
		Image src = ImageIO.read(srcfile);
		BufferedImage image = new BufferedImage((int) distWidth,
				(int) distHeight, BufferedImage.TYPE_INT_RGB);
		if("png".equalsIgnoreCase(distImgType)){
			Graphics2D g = image.createGraphics();
			image = g.getDeviceConfiguration().createCompatibleImage(distWidth, distHeight, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
			g.drawImage(src.getScaledInstance(distWidth, distHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		}else{
			image.getGraphics().drawImage(src.getScaledInstance(distWidth, distHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		}
		ImageIO.write(image, distImgType, new File(distImg));
	}
	
	/**
	 * 按比例光滑缩放图片
	 * @param srcImg  		原图片文件路径 
	 * @param distImg  	生成的图片文件路径 
	 * @param proportion	缩放比例
	 * @throws IOException
	 */
	public static void proportionalZoomImgSmooth(String srcImg, String distImg, float proportion) throws IOException{
		if (distImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = distImg.substring(distImg.lastIndexOf(".") + 1);
		File srcfile = new File(srcImg);
		Image src = ImageIO.read(srcfile);
		int distWidth = Math.round(src.getWidth(null) * proportion);
		int distHeight = Math.round(src.getHeight(null) * proportion);
		zoomImgSmooth(src, distImg,distWidth, distHeight, distImgType);
	}
	
	private static void zoomImgSmooth(Image srcImg, String distImg,
			int distWidth, int distHeight, String distImgType) throws IOException {
		BufferedImage image = new BufferedImage((int) distWidth,
				(int) distHeight, BufferedImage.TYPE_INT_RGB);
		if("png".equalsIgnoreCase(distImgType)){
			Graphics2D g = image.createGraphics();
			image = g.getDeviceConfiguration().createCompatibleImage(distWidth, distHeight, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
			g.drawImage(srcImg.getScaledInstance(distWidth, distHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		}else{
			image.getGraphics().drawImage(srcImg.getScaledInstance(distWidth, distHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		}
		ImageIO.write(image, distImgType, new File(distImg));
	}
	
	/** 
	 * 缩放图片
	 * 		不支持动态Gif图片(Gif图片格式受到版权保护)
	 * @param srcImg  		原图片文件路径 
	 * @param distImg  	生成的图片文件路径 
	 * @param distWidth  生成图片的宽度 
	 * @param distHeight  生成图片的高度 
	 * @throws IOException 
	 */
	public static void zoomImg(String srcImg, String distImg,
			int distWidth, int distHeight) throws IOException {
		if (distImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = distImg.substring(distImg.lastIndexOf(".") + 1);
		File srcfile = new File(srcImg);
		Image src = ImageIO.read(srcfile);
		BufferedImage image = new BufferedImage((int) distWidth,
				(int) distHeight, BufferedImage.TYPE_INT_RGB);
		if("png".equalsIgnoreCase(distImgType)){
			Graphics2D g = image.createGraphics();
			image = g.getDeviceConfiguration().createCompatibleImage(distWidth, distHeight, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
			g.drawImage(src.getScaledInstance(distWidth, distHeight,
							Image.SCALE_DEFAULT), 0, 0, null);
		}else{
			image.getGraphics().drawImage(src.getScaledInstance(distWidth, distHeight,
							Image.SCALE_DEFAULT), 0, 0, null);
		}
		ImageIO.write(image, distImgType, new File(distImg));
	}

	
	/**
	 * 按比例缩放图片
	 * @param srcImg  		原图片文件路径 
	 * @param distImg  	生成的图片文件路径 
	 * @param proportion	缩放比例
	 * @throws IOException
	 */
	public static void proportionalZoomImg(String srcImg, String distImg, float proportion) throws IOException{
		if (distImg.indexOf(".") == -1) {
			return;
		}
		String distImgType = distImg.substring(distImg.lastIndexOf(".") + 1);
		File srcfile = new File(srcImg);
		Image src = ImageIO.read(srcfile);
		int distWidth = Math.round(src.getWidth(null) * proportion);
		int distHeight = Math.round(src.getHeight(null) * proportion);
		zoomImg(src, distImg,distWidth, distHeight, distImgType);
	}
	
	
	private static void zoomImg(Image srcImg, String distImg,
			int distWidth, int distHeight, String distImgType) throws IOException {
		BufferedImage image = new BufferedImage((int) distWidth,
				(int) distHeight, BufferedImage.TYPE_INT_RGB);
		if("png".equalsIgnoreCase(distImgType)){
			Graphics2D g = image.createGraphics();
			image = g.getDeviceConfiguration().createCompatibleImage(distWidth, distHeight, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
			g.drawImage(srcImg.getScaledInstance(distWidth, distHeight,
							Image.SCALE_DEFAULT), 0, 0, null);
		}else{
			image.getGraphics().drawImage(srcImg.getScaledInstance(distWidth, distHeight,
							Image.SCALE_DEFAULT), 0, 0, null);
		}
		ImageIO.write(image, distImgType, new File(distImg));
	}
	
	/**
	 * 缩放png图片
	 * @param srcImg		原图片文件
	 * @param distImg		生成图片文件
	 * @param distWidth	生成图片文件宽度
	 * @param distHeight	生成图片文件高度
	 * @throws IOException
	 */
	public static void zoomPngImg(String srcImg, String distImg,
			double distWidth, double distHeight) throws IOException {
		if (distImg.indexOf(".png") == -1) {
			return;
		}
		File resource = new File(srcImg);
		BufferedImage sourceImage = ImageIO.read(resource);
		int width = sourceImage.getWidth(null);
		int height = sourceImage.getHeight(null);
		double widthRatio = distWidth / width;
		double heightRatio = distHeight / height;
		BufferedImage dstImage = null;
		AffineTransform transform = AffineTransform.getScaleInstance(
				widthRatio, heightRatio);
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);
		dstImage = op.filter(sourceImage, null);
		ImageIO.write(dstImage, "png", new File(distImg));
	}
	
	 /**
     * 裁剪图片
     *
     * @param srcImgPath  原文件全路径
     * @param  targetImgPath 裁剪后的文件保存全路径
     * @param  startX 开始裁剪的x坐标
     * @param  startY 开始裁剪的y坐标
     * @param  targetWidth 裁剪的宽度
     * @param  targetHeight 裁剪的高度
     * @throws Exception 
     */
	public static void cutOutImg(String srcImgPath, String targetImgPath, int startX,
                                              int startY, int targetWidth, int targetHeight)
                                                                                            throws Exception {
    	String fileType = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);
        // 取得图片读入器
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileType);
        ImageReader reader = (ImageReader) readers.next();
        // 取得图片读入流
        InputStream source = null;
        ImageInputStream iis = null;
        try {
            source = new FileInputStream(srcImgPath);
            iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            // 设置图片参数 Rectangle(左上顶点x坐标, y坐标, 矩形宽度, 矩形高度)
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(startX, startY, targetWidth, targetHeight);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, targetImgPath.substring(targetImgPath.lastIndexOf(".")+1), new File(targetImgPath));
        } finally {
            try {
                if (source != null) {
                    source.close();
                }
            } catch (Exception e) {
                throw new Exception("ImgUtils:cutOutImg()source  关闭错误");
            }
            try {
                if (iis != null) {
                    iis.close();
                }
            } catch (Exception e) {
                throw new Exception("ImgUtils:cutOutImg()iis 关闭错误");
            }
        }
    }

    /**
     * 裁剪图片，从左上角(0,0)开始
     *
     * @param srcImgPath  原文件全路径
     * @param  targetImgPath 裁剪后的文件保存全路径
     * @param  endX 裁剪后的宽度
     * @param  endY 裁剪后的高度
     * @throws Exception 
     */
    public static void cutOutImg(String srcImgPath, String targetImgPath, int endX,
                                              int endY) throws Exception {
        cutOutImg(srcImgPath, targetImgPath, 0, 0, endX, endY);
    }

    /**
     * 不拉伸缩放图片
     *		放大或者缩小时，空白区域用白色背景填充
     * @param srcImgPath  原文件全路径
     * @param targetImgPath 压缩后的文件保存全路径
     * @param targetWidth 压缩后的宽度
     * @param targetHeight 压缩后的高度
     * @throws IOException 
     */
    public static void noneExtrudeZoomImg(String srcImgPath, String targetImgPath,
                                              int targetWidth, int targetHeight) throws IOException  {
        File srcfile = new File(srcImgPath);
        Image src = ImageIO.read(srcfile);
        int width = src.getWidth(null);
        int height = src.getHeight(null);

        BufferedImage tag = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics gra = tag.getGraphics();
        // 设置背景色
        gra.setColor(Color.white);
        gra.fillRect(0, 0, targetWidth, targetHeight);
        if (width <= targetWidth && height <= targetHeight) {
            gra.drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                (targetWidth - width) / 2, (targetHeight - height) / 2, null);
        } else {
            float wh = (float) width / (float) height;
            if (wh > 1) {
                float tmp_heigth = (float) targetWidth / wh;
                float y = ((float) targetHeight - tmp_heigth) / 2;
                gra.drawImage(src.getScaledInstance(targetWidth, (int) tmp_heigth,
                    Image.SCALE_SMOOTH), 0, (int) y, null);
            } else {
                float tmp_width = (float) targetHeight * wh;
                float x = ((float) targetWidth - tmp_width) / 2;
                gra.drawImage(src.getScaledInstance((int) tmp_width, targetHeight,
                    Image.SCALE_SMOOTH), (int) x, 0, null);
            }
        }
        ImageIO.write(tag, targetImgPath.substring(targetImgPath.lastIndexOf(".")+1), new File(targetImgPath));
    }
 
}
