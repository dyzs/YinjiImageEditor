package com.anybeen.mark.imageeditor.entity;
import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/25.
 */
public class CardTemplateInfo implements java.io.Serializable{
    public String templateUrl;          // absPath
    public String nameCh;         // 卡片模板名称(中文名称)
    public String nameEn;       // 英文名称
    public String fileName;     // 文件名称,包含 suffix
    public String fileSuffix;   // 文件后缀名,包含特殊字符点(.png)

    public String sampleName;   // 卡片的小图名称,包含 suffix
    public String sampleUrl;    // 卡片的小图 url, adapter 数据, absPath

    public String category;     // 卡片分类(文艺,古典,淡雅,大自然)
    public String type;         // 卡片包含的子图片分类(0,1,2,3,4)分别表示
    public String orientation;  // 表示是横向卡片 or 竖向卡片的属性(vertical, horizontal)



    // TODO 待添加，卡片低下显示的图片，包含多个参数

    // 卡片上面包含的贴纸参数
    public ArrayList<StickerInfo> stickerList = new ArrayList<>();
    // 卡片上面包含的文字参数
    public ArrayList<CarrotInfo> textsList = new ArrayList<>();
}
