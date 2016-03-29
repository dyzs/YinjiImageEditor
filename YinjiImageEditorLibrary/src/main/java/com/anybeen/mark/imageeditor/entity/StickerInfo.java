package com.anybeen.mark.imageeditor.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by maidou on 2016/3/15.
 * @details 用来保存贴纸的三阶矩阵的信息，把三阶矩阵拆分成 9 个 float 值并通过序列化保存。
 */
public class StickerInfo implements Serializable{
    public int      index           = 0;    // 表示贴纸的索引值
    public String   nameCh          = "";   // 中文名称
    public String   nameEn          = "";   // 拼音或英文名称
    public String   fileAbsPath     = "";   // 表示可能存在贴纸从文件夹中读取
    public int      resId           = 0;    // 资源 id  example:  R.id.pic_icon
    public float[]  floatArr;
    public JSONObject buildSticker2JSONObject() throws JSONException {
        JSONObject stickerJSON = new JSONObject();
        float[] arr = floatArr;
        try {
            stickerJSON.put("index", index);
            stickerJSON.put("nameCh", nameCh);
            stickerJSON.put("nameEn", nameEn);
            stickerJSON.put("fileAbsPath", fileAbsPath);
            stickerJSON.put("resId", resId);

            stickerJSON.put("float0", float2Double(arr[0]));
            stickerJSON.put("float1", float2Double(arr[1]));
            stickerJSON.put("float2", float2Double(arr[2]));
            stickerJSON.put("float3", float2Double(arr[3]));
            stickerJSON.put("float4", float2Double(arr[4]));
            stickerJSON.put("float5", float2Double(arr[5]));
            stickerJSON.put("float6", float2Double(arr[6]));
            stickerJSON.put("float7", float2Double(arr[7]));
            stickerJSON.put("float8", float2Double(arr[8]));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return stickerJSON;
    }

    /**
     * @details 解析 json 对象，还原 3 阶矩阵的 float 数组，再赋值给 matrix 矩阵
     */
    public void parseJSONObject2Sticker(JSONObject dataJSON) throws JSONException {
        float[] arr = new float[9];
        try {
            index = dataJSON.getInt("index");
            nameCh = dataJSON.getString("nameCh");
            nameEn = dataJSON.getString("nameEn");
            fileAbsPath = dataJSON.getString("index");
            resId = dataJSON.getInt("index");

            arr[0] = double2Float(dataJSON.getDouble("float0"));
            arr[1] = double2Float(dataJSON.getDouble("float1"));
            arr[2] = double2Float(dataJSON.getDouble("float2"));
            arr[3] = double2Float(dataJSON.getDouble("float3"));
            arr[4] = double2Float(dataJSON.getDouble("float4"));
            arr[5] = double2Float(dataJSON.getDouble("float5"));
            arr[6] = double2Float(dataJSON.getDouble("float6"));
            arr[7] = double2Float(dataJSON.getDouble("float7"));
            arr[8] = double2Float(dataJSON.getDouble("float8"));
            floatArr = arr;      // 把 3 阶矩阵的数据重新设置给 matrix
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
