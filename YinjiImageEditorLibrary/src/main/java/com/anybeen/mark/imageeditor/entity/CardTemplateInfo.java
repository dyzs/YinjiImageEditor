package com.anybeen.mark.imageeditor.entity;
import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/25.
 */
public class CardTemplateInfo implements java.io.Serializable{
    public String fileName;     // 文件名称(英文名称,打包的文件夹名称)
    public String modelName;    // 卡片模板中文名称
    public String absPath;      // 文件夹的绝对路径
    public String description;  // 卡片的寄语, 卡片由来, 描述

    public String category;     // 分类(0,1,2,3,4,5,6,7文艺,古典,淡雅,大自然)
    public String shapeCount;   // 内部包含透明形状个数(0,1,2,3,4)
    public String orientation;  // 表示是横向卡片 or 竖向卡片的属性(vertical:0, horizontal:1)

    public String sampleUrl;    // 卡片的小图 url, adapter 数据, absPath, 给 adapter 使用
    public String templateUrl;  // 卡片的模板 url, absPath
    public String configUrl;    // 配置文件

    public String sampleName;   // 卡片的小图名称, 包含 suffix, 很重要啦
    public String templateName; // 卡片的模板名称, 包含 suffix

    public int backgroundColor;    // 背景颜色

    public String stickerCount; // 有多少个子贴纸
    public String carrotCount;  // 有多少个子文本

    /**
     * 模板的框框参数,左上右下
     */
    public ArrayList<CardInnerPicShapeInfo> innerPicShapeInfoList = new ArrayList<>();

    // 卡片上面包含的贴纸参数,还得对应各个贴纸, TODO 最后再设计
    public ArrayList<StickerInfo> stickerList = new ArrayList<>();

    // 卡片上面包含的文字参数
    public ArrayList<CarrotInfo> textsList = new ArrayList<>();
}
