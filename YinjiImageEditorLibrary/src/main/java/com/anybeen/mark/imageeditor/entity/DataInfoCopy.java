package com.anybeen.mark.imageeditor.entity;

import com.anybeen.mark.imageeditor.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maidou on 2016/3/29.
 */
public class DataInfoCopy implements Serializable{
    private static final long serialVersionUID = 1L;
    // 唯一标示（分类_时间戳(longtime))
    public String dataId = "";
    // 标题
    public String dataTitle = "";
    // 正文（记事为缩略文，图片为配字，提醒为提醒内容，反馈为反馈内容）
    public String dataContent = "";
    // 创建时间
    public long createTime = 0;
    // 编辑时间
    public long editTime = 0;
    // 标签 jsonArray格式
    public String dataTag = "";
    // 邮件路径
    public String mailPath = "";
    // 类别（日记、提醒、图片）
    public String dataCategory = "";

    //大图元数据
    public MetaDataPictureInfo metaDataPictureInfo = new MetaDataPictureInfo();
    // 图册元数据
    public static class MetaDataPictureInfo implements java.io.Serializable{
		private static final long serialVersionUID = 1L;
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

        public List<PictureSubElements> listData = new ArrayList<>();


        public JSONObject buildJSONObject() throws JSONException {
            JSONObject picInfoJSON = new JSONObject();
            picInfoJSON.put("fontSize", fontSize);
            //picInfoJSON.put("filterType", filterType);
            picInfoJSON.put("markLoaction", markLoaction);
            picInfoJSON.put("oriPicturePath", oriPicturePath);
            picInfoJSON.put("gpsLocation", gpsLocation);
            picInfoJSON.put("gpsLocationInfo", gpsLocationInfo);
            picInfoJSON.put("albumPath", albumPath);
            picInfoJSON.put("font", font);
            picInfoJSON.put("posX", posX);
            picInfoJSON.put("posY", posY);

            picInfoJSON.put("comPicturePath", comPicturePath);
            picInfoJSON.put("picContent", picContent);
            picInfoJSON.put("picFontSize", picFontSize);
            picInfoJSON.put("listData", buildPicSubElementsJSONArray(listData));
            return picInfoJSON;
        }
        public void parseJSONObject(JSONObject dataJSON) throws JSONException {
            fontSize = dataJSON.getInt("fontSize");
            markLoaction = dataJSON.getInt("markLoaction");
            oriPicturePath = dataJSON.getString("oriPicturePath");
            gpsLocation = dataJSON.getString("gpsLocation");
            gpsLocationInfo = dataJSON.getString("gpsLocationInfo");
            albumPath = dataJSON.getString("albumPath");
            if(dataJSON.has("font")){
                font = dataJSON.getString("font");
            }
            if(dataJSON.has("posX")){
                posX = dataJSON.getDouble("posX");
            }
            if(dataJSON.has("posY")){
                posY = dataJSON.getDouble("posY");
            }

            if(dataJSON.has("comPicturePath")){
                comPicturePath = dataJSON.getString("comPicturePath");
            }
            if(dataJSON.has("picContent")){
                picContent = dataJSON.getString("picContent");
            }
            if(dataJSON.has("picFontSize")){
                picFontSize = dataJSON.getInt("picFontSize");
            }
            if (dataJSON.has("listData")) {
                listData = parseJSONArrayToPicSubElements(dataJSON.getJSONArray("listData"));
            }
        }
    }

    // maidou add
    // 组合数组
    public static JSONArray buildPicSubElementsJSONArray(List<PictureSubElements> lists){
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < lists.size(); i++) {
                jsonArray.put(lists.get(i).buildJSONObject());
            }
        }catch(Exception e){
            LogUtil.d("parse", "parseJSONArrayToPicSubElements");
        }
        return jsonArray;
    }
    // 解析数组
    public static List<PictureSubElements> parseJSONArrayToPicSubElements(JSONArray dataArray) {
        List<PictureSubElements> lists = new ArrayList<>();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataJSON = dataArray.getJSONObject(i);
                PictureSubElements picSubElements = new PictureSubElements();
                picSubElements.parseJSONObject(dataJSON);
                lists.add(picSubElements);
            }
        }catch(JSONException e){
            LogUtil.d("parse", "parseJSONArrayToPicSubElements");
        }
        return lists;
    }

    public static class PictureSubElements implements java.io.Serializable{
        public String text 		= "";
        public int colorR 		= 0;
        public int colorG		= 0;
        public int colorB		= 0;
        public float textSize 	= 1.0f;
        public String typeface 	= "";	// 为空就默认为系统字体
        public float pLeftScale = 0.0f;
        public float pTopScale 	= 0.0f;
        public int pLeft 		= 0;
        public int pTop 		= 0;

        public JSONObject buildJSONObject(){
            JSONObject picSubElementsJson = new JSONObject();
            try {
                picSubElementsJson.put("text", text);
                picSubElementsJson.put("colorR", colorR);
                picSubElementsJson.put("colorG", colorG);
                picSubElementsJson.put("colorB", colorB);
                picSubElementsJson.put("textSize", float2Double(textSize));
                picSubElementsJson.put("typeface", typeface);
                picSubElementsJson.put("pLeftScale", float2Double(pLeftScale));
                picSubElementsJson.put("pTopScale", float2Double(pTopScale));
                picSubElementsJson.put("pLeft", pLeft);
                picSubElementsJson.put("pTop", pTop);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return picSubElementsJson;
        }
        public void parseJSONObject(JSONObject subElementsJSON) {
            try {
                text = subElementsJSON.getString("text");
                colorR = subElementsJSON.getInt("colorR");
                colorG = subElementsJSON.getInt("colorG");
                colorB = subElementsJSON.getInt("colorB");
                textSize = double2Float(subElementsJSON.getDouble("textSize"));
                typeface = subElementsJSON.getString("typeface");
                pLeftScale = double2Float(subElementsJSON.getDouble("pLeftScale"));
                pTopScale = double2Float(subElementsJSON.getDouble("pTopScale"));
                pLeft = subElementsJSON.getInt("pLeft");
                pTop = subElementsJSON.getInt("pTop");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private static double float2Double(float f) {
            return Double.parseDouble(String.valueOf(f));
        }
        private static float double2Float(double d) {
            return (float)d;
        }
    }// end inner class

}
