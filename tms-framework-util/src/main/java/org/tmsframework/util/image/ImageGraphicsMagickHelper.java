/**
 * 
 */
package org.tmsframework.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.GraphicsMagickCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

/**
 * 
 * <b>图片处理类</b><br/>
 * 	<p>&nbsp;&nbsp;&nbsp;&nbsp;此图片处理类是基于graphicsMagick软件进行工作的，如果服务器上没有安装
 * graphicsMagick软件，请到http://www.graphicsmagick.org/download.html下载。
 * 注意：Linux版本下载下来的是源码包，需要手动编译并安装。<br/>
 * 各类常用图片的库文件下载地址：http://www.imagemagick.org/download/delegates/<br/><br/></p>
 * <p>
 * graphicsMagick软件安装方法：<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;1、安装rpm包（其实是解包），rpm -Uvh GraphicsMagick-1.3.12-1.src.rpm<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;2、找到解包后的GraphicsMagick-1.3.12.tar.bz2文件<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;3、再解包：tar -jxvf GraphicsMagick-1.3.12.tar.bz2<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;4、接续解包：tar -xvf GraphicsMagick-1.3.12.tar<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;5、进入到GraphicsMagick-1.3.12目录：cd GraphicsMagick-1.3.12/<br/>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;6、更新配置：./configure<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;7、编译：make<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;8、安装：make install<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;9、（可选）卸载：make uninstall<br/><br/>
 *</p>
 *<p>
 *依赖包：<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;im4java-1.1.0.jar&nbsp;&nbsp;&nbsp;&nbsp;下载地址：http://sourceforge.net/projects/im4java/files/ <br/><br/>
 * </p><p>
 *使用方法：<br/>
 *		&nbsp;&nbsp;如果是Windows下使用<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;第一种方法：需要在环境变量path中配置GraphicsMagick安装路径。<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;第二种方法：在使用本类前调用setMagickPath(安装路径)方法<br/>
 *		&nbsp;&nbsp;Linux没有关系，但是在安装完成后应该执行命令：gm 测试是否正常</p>
 * 
 * @author sun.zhang
 * @date Sep 20, 2010 1:51:04 PM
 *	@version 1.0
 */
public class ImageGraphicsMagickHelper {
	
//	需要更新文字水印图片文件标识
	private static boolean UPDATE_TEXT_WATERMARK_IMAGE = false;
//	是否已经更新了文字水印图片文件标识
	private static boolean IS_UPDATED_TEXT_WATERMARK_IMAGE = false;
	
	private static String _font = null;
	private static String[] fontList = {"zysong.ttf","gkai00mp.ttf","gbsn00lp.ttf","simsun.ttc","MSYH.TTF"};
	private static String[] fontPathList = {"/usr/share/fonts/","/usr/share/fonts/zh_CN/","/usr/share/fonts/zh_CN/TrueType/"};
	private static String magickPath = null;
	
	/**
	 * 调整单个图片到指定大小
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param width		新图片宽度百分比[0-100]
	 * @param height	新图片高度百分比[0-100]
	 * @throws IOException		
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void resizeProportion(String srcPath,String distPath,float width,float height) throws IOException, InterruptedException, IM4JavaException{
		File f = new File(srcPath);
		Image img = ImageIO.read(f);
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		resize(srcPath, distPath, Math.round(imgWidth*(width/100)), Math.round(imgHeight*(height/100)));
	}
	
	/**
	 * 调整单个图片到指定大小
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param width		新图片宽度
	 * @param height	新图片高度
	 * @throws IOException		
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public synchronized static void resize(String srcPath,String distPath,int width,int height) throws IOException, InterruptedException, IM4JavaException{
		GraphicsMagickCmd  cmd = new GraphicsMagickCmd("convert");
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width,height);
		op.addImage(distPath);
		cmd.run(op);
	}
	
	/**
	 * 调整多个图片到指定大小，在相应目录生成新图片
	 * @param width		宽度
	 * @param height	高度
	 * @param srcPaths		多个图片路径
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public synchronized static void resizeImages(int width,int height,String... srcPaths) throws IOException, InterruptedException, IM4JavaException {
	  for (String srcImage:srcPaths) {
	    int lastDot = srcImage.lastIndexOf('.');
	    String dstImage = srcImage.substring(1,lastDot-1)+"_new"+srcImage.substring(srcImage.lastIndexOf("."));
	    resize(srcImage,dstImage,width,height);
	  }
	}
	
	/**
	 * 从左上角开始剪切指定高度宽度的图片
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param width		宽度
	 * @param height	高度
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void cutOutImg(String srcPath, String distPath, int width, int height) throws IOException, InterruptedException, IM4JavaException{
		cutOutImg(srcPath, distPath, width, height, 0, 0);
	}
	
	/**
	 * 从任意点剪切指定高度宽度的图片
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param width		宽度
	 * @param height	高度
	 * @param x		X坐标
	 * @param y		Y坐标
	 * @throws IM4JavaException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public synchronized static void cutOutImg(String srcPath, String distPath, int width, int height, int x, int y) throws IOException, InterruptedException, IM4JavaException{
		GraphicsMagickCmd  cmd = new GraphicsMagickCmd("convert");
		IMOperation op = new IMOperation();
		op.crop(width, height, x, y);
		op.addImage(srcPath);
		op.addImage(distPath);
		cmd.run(op);
	}
	
	/**
	 * 按九宫格位置添加水印
	 * 		默认边距为10像素，透明度为100，位置为9号位（右下角）
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param watermarkImg		水印图片路径
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void posWatermarkImg(String srcPath,String distPath,String watermarkImg) throws IOException, InterruptedException, IM4JavaException{
		int[] watermarkImgSide = getImageSide(watermarkImg);
		int[] srcImgSide = getImageSide(srcPath);
		int[] xy = getXY(srcImgSide, watermarkImgSide, 9, 10, false);
		watermarkImg(srcPath,distPath,watermarkImg,watermarkImgSide[0],watermarkImgSide[1],xy[0],xy[1],100);
	}
	
	/**
	 * 按九宫格位置添加水印
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param watermarkImg		水印图片路径
	 * @param position		九宫格位置[1-9]
	 * @param margin 		边距
	 * @param alpha		透明度
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void posWatermarkImg(String srcPath,String distPath,String watermarkImg, int position, int margin, int alpha) throws IOException, InterruptedException, IM4JavaException{
		int[] watermarkImgSide = getImageSide(watermarkImg);
		int[] srcImgSide = getImageSide(srcPath);
		int[] xy = getXY(srcImgSide, watermarkImgSide, position, margin, false);
		watermarkImg(srcPath,distPath,watermarkImg,watermarkImgSide[0],watermarkImgSide[1],xy[0],xy[1],alpha);
	}
	
	/**
	 * 添加图片水印
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param watermarkImg		水印图片路径
	 * @param x		水印开始X坐标
	 * @param y		水印开始Y坐标
	 * @param alpha		透明度[0-100]
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void watermarkImg(String srcPath,String distPath,String watermarkImg, int x, int y, int alpha) throws IOException, InterruptedException, IM4JavaException{
		int[] watermarkImgSide = getImageSide(watermarkImg);
		watermarkImg(srcPath,distPath,watermarkImg,watermarkImgSide[0],watermarkImgSide[1],x,y,alpha);
	}
	
	/**
	 * 添加图片水印
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param watermarkImg		水印图片路径
	 * @param width		水印宽度（可以于水印图片大小不同）
	 * @param height	水印高度（可以于水印图片大小不同）
	 * @param x		水印开始X坐标
	 * @param y		水印开始Y坐标
	 * @param alpha		透明度[0-100]
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public synchronized static void watermarkImg(String srcPath,String distPath,String watermarkImg, int width, int height, int x, int y, int alpha) throws IOException, InterruptedException, IM4JavaException{
		GraphicsMagickCmd  cmd = new GraphicsMagickCmd("composite");
		IMOperation op = new IMOperation();
		op.dissolve(alpha);
		op.geometry(width, height, x, y);
		op.addImage(watermarkImg);  
		op.addImage(srcPath);  
		op.addImage(distPath);  
		cmd.run(op);
	}
	
	/**
	 * 按照九宫格位置添加文字水印(Windows Only)
	 * 		默认设置：位置右下角，边距10像素，不透明，颜色为黑色
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param textFile		水印文字UTF-8文本文件路径
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	@Deprecated
	public static void posWatermarkText(String srcPath,String distPath,String textFile) throws IOException, InterruptedException, IM4JavaException{
		posWatermarkText(srcPath, distPath, textFile, 20, 9, 10, "#000000", 100);
	}
	
	/**
	 * 按照九宫格位置添加文字水印（Windows Only）
	 * 		默认设置：位置右下角，边距10像素，不透明
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param textFile		水印文字UTF-8文本文件路径
	 * @param fontsize		字体大小
	 * @param color		颜色
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	@Deprecated
	public static void posWatermarkText(String srcPath,String distPath,String textFile,int fontsize, String color) throws IOException, InterruptedException, IM4JavaException{
		posWatermarkText(srcPath, distPath, textFile, fontsize, 9, 10, color, 100);
	}
	
	/**
	 * 按照九宫格位置添加文字水印（Windows Only）
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param textFile		水印文字UTF-8文本文件路径
	 * @param fontsize		字体大小
	 * @param position		九宫格位置[1-9]
	 * @param margin 		边距
	 * @param color		颜色
	 * @param alpha		透明度
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	@Deprecated
	public static void posWatermarkText(String srcPath,String distPath,String textFile,int fontsize,int position, int margin, String color,int alpha) throws IOException, InterruptedException, IM4JavaException{
		String textWatermarkPath = getWatermarkFile(textFile,fontsize,color);
    	if("".equals(textWatermarkPath)){
    		return;
    	}
    	posWatermarkImg(srcPath, distPath, textWatermarkPath,position,margin,alpha);
	}
	
	/**
	 * 按坐标添加文本水印（Windows Only）
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param textFile		水印文字UTF-8文本文件路径
	 * @param fontsize		字体大小
	 * @param x		水印开始X坐标
	 * @param y		水印开始Y坐标
	 * @param color		颜色
	 * @param alpha		透明度[0-100]
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	@Deprecated
	public static void watermarkText(String srcPath,String distPath,String textFile,int fontsize,int x, int y, String color,int alpha) throws IOException, InterruptedException, IM4JavaException{
		String textWatermarkPath = getWatermarkFile(textFile,fontsize,color);
    	if("".equals(textWatermarkPath)){
    		return;
    	}
    	watermarkImg(srcPath, distPath, textWatermarkPath, x, y, alpha);
	}
	
	/**
	 * 坐标添加英文文本水印（Windows Only）
	 * @param srcPath		原图片路径
	 * @param distPath		新图片路径
	 * @param text		水印英文内容
	 * @param fontsize		字体大小
	 * @param x		水印开始X坐标
	 * @param y		水印开始Y坐标
	 * @param color		颜色
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	@Deprecated
	public synchronized static void watermarkEnText(String srcPath,String distPath, String text, int fontsize,int x, int y, String color) throws IOException, InterruptedException, IM4JavaException{
		IMOperation op = new IMOperation();         
		op.font("Arial");
		op.fill(color);
		op.pointsize(fontsize);
		op.draw("text "+x+","+y+" "+text);  
		op.addImage(srcPath);  
		op.addImage(distPath);  
		System.out.println(op.toString());
		ConvertCmd convert = new ConvertCmd(true);  
		convert.run(op); 
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
		pWatermarkText(watermarkText,targetImg,7,10,Color.BLACK,20,1f,Font.BOLD,"瀹嬩綋");
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
		pWatermarkText(watermarkText,targetImg,position,10,Color.BLACK,20,1f,Font.BOLD,"瀹嬩綋");
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
		pWatermarkText(watermarkText,targetImg,position,10,color,20,1f,Font.BOLD,"瀹嬩綋");
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
		pWatermarkText(watermarkText,targetImg,position,10,color,fontSize,1f,Font.BOLD,"瀹嬩綋");
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
		pWatermarkText(watermarkText,targetImg,position,10,color,fontSize,alpha,Font.BOLD,"瀹嬩綋");
	}

	
	/**
	 * 图片添加文字水印(JDK方式实现，没有用到GraphicsMagick软件)
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
	public synchronized static void pWatermarkText(String watermarkText, String targetImg,int position,int margin, Color color, int fontSize, float alpha, int fontStyle, String fontName) throws IOException{
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
		if("png".equalsIgnoreCase(distImgType)){
			image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
			g.dispose(); 
			g = image.createGraphics(); 
		}
		int[] xy = getTextXY(position, targetImg, watermarkText, fontSize, margin);
		g.drawImage(tarImg, 0, 0, width, height, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawString(watermarkText, xy[0], xy[1] + fontSize);
		g.dispose();
		ImageIO.write(image, distImgType, new File(targetImg));
	}
	
	/**
	 * 获取是否更新文本水印图片，如果执行过一次watermarkText时后，UPDATE_TEXT_WATERMARK_IMAGE
	 * 状态会自动被设置为false，再次执行watermarkText函数时将跳过生成文字水印图片过程，直接添加水印，
	 * 以提高添加水印的执行效率
	 * @return		是否需要更新文本水印图片设置状态
	 */
	public static boolean getUPDATE_TEXT_WATERMARK_IMAGE() {
		return UPDATE_TEXT_WATERMARK_IMAGE;
	}

	/**
	 * 设置UPDATE_TEXT_WATERMARK_IMAGE状态，如果需要更新文字水印图片文件，就应该先将
	 * UPDATE_TEXT_WATERMARK_IMAGE属性设置为true，否则更新动作将被跳过不执行。
	 * @param update_text_watermark_image	是否更新文字水印图片
	 */
	public static void setUPDATE_TEXT_WATERMARK_IMAGE(
			boolean update_text_watermark_image) {
		UPDATE_TEXT_WATERMARK_IMAGE = update_text_watermark_image;
	}

	/**
	 * 获取字体文字水印字体
	 * @return		当前设置的文字水印字体
	 */
	public static String getFont() {
		return _font;
	}

	/**
	 * 设置字体文字水印字体，如果是Windows系统默认字体是“黑体”，可以根据字体名字来设置，如“黑软雅黑”，
	 * 也可以用字体路径代替，如“C:/WINDOWS/Fonts/MSYH.TTF”；
	 * 如果是Linux系统的话需要设置为字体路径，如“/usr/share/fonts/simsun.ttc”
	 * @param _font	字体名称或者路径
	 */
	public static void setFont(String font) {
		ImageGraphicsMagickHelper._font = font;
	}

	/**
	 * 判断是否已经更新了文字水印图片文件
	 * @return		当前文字水印图片文件更新状态
	 */
	public static boolean IS_UPDATED_TEXT_WATERMARK_IMAGE() {
		return IS_UPDATED_TEXT_WATERMARK_IMAGE;
	}
	
	private static int[] getTextXY(int position, String src, String text, int fontsize, int margin) throws IOException{
		int[] wh = new int[2];
		if(text.getBytes().length==text.length()){
			wh[0] = text.length()*fontsize / 2 +20;
		}else{
			wh[0]  = text.length()*fontsize;
		}
		wh[1]  = fontsize;
		return getXY(getImageSide(src),wh,position,margin,true);
	}
	
	
	private synchronized static void createTextWatermarkImage( int fontsize, String textFile, String color, String wmFile) throws IOException, InterruptedException, IM4JavaException {
		GraphicsMagickCmd  cmd = new GraphicsMagickCmd("convert");
//		ConvertCmd cmd = new ConvertCmd(false);
		IMOperation op = new IMOperation();
		op.background("none");
		op.font(_font==null?getSystemFont():_font);
		op.pointsize(fontsize);
		op.fill(color);
		op.addImage("label:@"+textFile);
		op.addImage(wmFile);
		System.out.println(op.toString());
		cmd.run(op);
		UPDATE_TEXT_WATERMARK_IMAGE = false;
		IS_UPDATED_TEXT_WATERMARK_IMAGE = true;
	}
	
    private static String getDirectory(String text) {
    	String temp = text.replaceAll("\\\\", "/");
		return temp.substring(0,temp.lastIndexOf("/")+1);
	}

	private static String getWatermarkFile(String textFile, int fontsize, String color) throws IOException, InterruptedException, IM4JavaException {
		String watermarkSavePath = getDirectory(textFile);
		String wmFile = watermarkSavePath+"watermark_text.png";
    	File f =new File(wmFile);
    	if(UPDATE_TEXT_WATERMARK_IMAGE){
    		createTextWatermarkImage(fontsize,textFile,color,wmFile);
    	}else if(!f.exists()){
    		createTextWatermarkImage(fontsize,textFile,color,wmFile);
		}
		return f.exists()?wmFile:"";
	}
	
//	获取字体，如果是Windows的话直接返回“C:/WINDOWS/Fonts/SIMSUN.TTC”(宋体)
	private static String getSystemFont() {
		if(isWindows()){
			return "C:/WINDOWS/Fonts/SIMSUN.TTC";
		}else{
			for(String path:fontPathList){
				for(String font:fontList){
					File f = new File(path+font);
					if(f.exists()){
						return path+font;
					}
				}
			}
			throw new RuntimeException("Can't get chinese font. You should put the font[simsun.ttc] in the follow directory: \"/usr/share/fonts/\" \"/usr/share/fonts/zh_CN/\" \"/usr/share/fonts/zh_CN/TrueType/\"");
		}
	}

	private static boolean isWindows() {
		return System.getProperty("os.name").indexOf("Windows")!=-1;
	}
	
	private static int[] getImageSide(String imgPath) throws IOException {
    	int[] side = new int[2];
    	Image img = ImageIO.read(new File(imgPath));
    	side[0] = img.getWidth(null);
    	side[1] =img.getHeight(null);
    	return side;
	}

	private static int[] getXY(int[] image, int[] watermark, int position, int margin, boolean isText) {
    	int[] xy = new int[2];
		if(position==1){
			xy[0] = margin;
			xy[1] = isText?margin+watermark[1]:margin;
		}else if(position==2){
			xy[0] = (image[0]-watermark[0])/2;
			xy[1] = isText?margin+watermark[1]:margin;
		}else if(position==3){
			xy[0] = image[0]-watermark[0]-margin;
			xy[1] = isText?margin+watermark[1]:margin;
		}else if(position==4){
			xy[0] = margin;
			xy[1] = (image[1]-watermark[1])/2;
		}else if(position==5){
			xy[0] = (image[0]-watermark[0])/2;
			xy[1] =  (image[1]-watermark[1])/2;
		}else if(position==6){
			xy[0] = image[0]-watermark[0]-margin;
			xy[1] = (image[1] - watermark[1])/2; 
		}else if(position==7){
			xy[0] =margin;
			xy[1] = image[1] - watermark[1] - margin;
		}else if(position==8){
			xy[0] =  (image[0]-watermark[0])/2;
			xy[1] = image[1] - watermark[1] - margin;
		}else{
			xy[0] = image[0]-watermark[0]-margin;
			xy[1] = image[1] - watermark[1] - margin;
		}
		return xy;
	}


	public static String getMagickPath() {
		return magickPath;
	}

	/**
	 * 设置GraphicsMagick和ImageMagick在Windows下的安装路径
	 * 		注意两个软件都要安装，否则将会导致部分函数无法生效
	 * @param magickPath		GraphicsMagick和ImageMagick安装路径参数，两个参数之间用“;”分隔
	 */
	public static void setMagickPath(String magickPath) {
		ImageGraphicsMagickHelper.magickPath = magickPath;
		ProcessStarter.setGlobalSearchPath(magickPath);
	}


}
