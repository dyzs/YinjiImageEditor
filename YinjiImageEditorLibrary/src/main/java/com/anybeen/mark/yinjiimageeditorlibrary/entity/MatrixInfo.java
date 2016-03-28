package com.anybeen.mark.yinjiimageeditorlibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by maidou on 2016/3/15.
 * @details 用来保存贴纸的三阶矩阵的信息，把三阶矩阵拆分成 9 个 float 值并通过序列化保存。
 */
public class MatrixInfo implements Serializable{
    public int index;               // 表示贴纸的索引值
    public String nameCH;           // 中文名称
    public String nameEN;           // 拼音或英文名称
    public String fileAbsPath;      // 表示可能存在贴纸从文件夹中读取
    public int resId;               // 资源 id  example:  R.id.pic_icon

    public float[] floatArr;
    public JSONObject buildMatrix2JSONObject() throws JSONException {
        JSONObject matrixJSON = new JSONObject();
        float[] arr = floatArr;
        try {
            matrixJSON.put("float0", float2Double(arr[0]));
            matrixJSON.put("float1", float2Double(arr[1]));
            matrixJSON.put("float2", float2Double(arr[2]));
            matrixJSON.put("float3", float2Double(arr[3]));
            matrixJSON.put("float4", float2Double(arr[4]));
            matrixJSON.put("float5", float2Double(arr[5]));
            matrixJSON.put("float6", float2Double(arr[6]));
            matrixJSON.put("float7", float2Double(arr[7]));
            matrixJSON.put("float8", float2Double(arr[8]));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return matrixJSON;
    }

    /**
     * @details 解析 json 对象，还原 3 阶矩阵的 float 数组，再赋值给 matrix 矩阵
     */
    public void parseJSONObject2Matrix(JSONObject dataJSON) throws JSONException {
        float[] arr = new float[9];
        try {
            arr[0] = double2Float(dataJSON.getDouble("float0"));
            arr[1] = double2Float(dataJSON.getDouble("float1"));
            arr[2] = double2Float(dataJSON.getDouble("float2"));
            arr[3] = double2Float(dataJSON.getDouble("float3"));
            arr[4] = double2Float(dataJSON.getDouble("float4"));
            arr[5] = double2Float(dataJSON.getDouble("float5"));
            arr[6] = double2Float(dataJSON.getDouble("float6"));
            arr[7] = double2Float(dataJSON.getDouble("float7"));
            arr[8] = double2Float(dataJSON.getDouble("float8"));
            floatArr = arr;      // 把 3阶矩阵的数据重新设置给 matrix
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
