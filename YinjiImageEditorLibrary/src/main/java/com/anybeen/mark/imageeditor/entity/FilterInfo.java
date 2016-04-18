package com.anybeen.mark.imageeditor.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maidou on 2016/4/12.
 */
public class FilterInfo implements java.io.Serializable {
    public int filterIndex = 0;         // 滤镜库索引（自定义唯一标识）0表示原图，可以不用做特效
    public String filterNameCh = "";    // 中文名称
    public String filterNameEn = "";    // 英文名称
    public int filterCount = 0;         // 操作次数

    public JSONObject buildFilterInfoToJSONObj () throws JSONException {
        JSONObject filterJson = new JSONObject();
        try {
            filterJson.put("filterIndex",   filterIndex);
            filterJson.put("filterNameCh",  filterNameCh);
            filterJson.put("filterNameEn",  filterNameEn);
            filterJson.put("filterCount",   filterCount);
        } catch (JSONException e) {
            System.out.println("filter json build error");
        }
        return filterJson;
    }

    public void parseJSONObjToFilterInfo (JSONObject dataJson) {
        try {
            filterIndex     = dataJson.getInt("filterIndex");
            filterNameCh    = dataJson.getString("filterNameCh");
            filterNameEn    = dataJson.getString("filterNameEn");
            filterCount     = dataJson.getInt("filterCount");
        } catch (JSONException e) {
            System.out.println("filter json parse error");
        }
    }


}
