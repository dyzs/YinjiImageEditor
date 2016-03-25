package com.anybeen.mark.yinjiimageeditorlibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maidou on 2016/3/24.
 * @details 用来保存文本编辑控件的值
 */
public class CarrotInfo implements java.io.Serializable{
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
}
