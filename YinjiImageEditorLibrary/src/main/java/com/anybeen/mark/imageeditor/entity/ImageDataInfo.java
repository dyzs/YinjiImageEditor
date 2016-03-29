package com.anybeen.mark.imageeditor.entity;

/**
 * Created by maidou on 2016/3/25.
 */
public class ImageDataInfo {
    /**
     *
     */
    //		private static final long serialVersionUID = 1L;
    public String gpsLocation = "";		// gps信息 longitude,latitude
    public String gpsLocationInfo = ""; // 地点位置信息 北京市 大望路...

    public String albumPath = "";       	//图片：原始图片相册路径
    public String oriPicturePath = "";  //原图绝对路径
    public String comPicturePath = "";  //合成后的图片地址

    //图片外标注属性
    public String Content = "";		//图片外标注的内容
    public String font = "";            	// 字体样式
    public int fontSize = 0;				// 字体大小

    //图片内标注属性
    public String picContent = "";		//图片内标注的内容
    public int picFontSize = 0;			//图片内标注的字体大小
    public int markLoaction = 0;		 // 标注位置
    public double posX = -1;             // x 坐标
    public double posY = -1;             // y 坐标

}
