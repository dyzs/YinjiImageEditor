package com.anybeen.mark.imageeditor.entity;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/27.
 */
public class CardDataInfo implements java.io.Serializable{
    public String saveId = "";              // 保存的文件的 id
    public String saveCreateTime = "";      // 保存卡片创建时间

    public String saveCardName = "";        // 保存生成后的卡片的文件名,包含文件后缀名
    public String saveCardGenerateUrl = ""; // 保存生成后的卡片的本地 Url

    // 可有可无的保存项// 不需要保存，模板的背景颜色是固定的，加载模板就有，只需要获取并且在Canvas绘制的时候使用，that's it
    public int saveCardBackgroundColor = -1;

    // 保存的时候应该保存的是每张 innerPic 对应的 innerPicShape 的关系映射, 然而 innerPicShape 是读取 json 的
    // 固定顺序, 所以图 innerPic 1 应该对应 innerPicShapeInfoList 的第 0 个 item
    // （错误方法！被推翻）解决方法就是: 把每个 rect 参数直接存储到保存项中, 可以直接重新加载


    /**
     * 保存template的名称，跟读取的 list 做匹配，如果存在，这加载，不存在加载默认模板，并提示下载
     * 同时保存各个文件的名称，做校验
     */
    public String saveFileName = "";        // cardTemplateInfo-->fileName
    public String saveTemplateName = "";    // cardTemplateInfo-->templateName
    public String saveSampleName = "";      // xxSample.png

    // 保存包括 shape 参数和对应的图片的 local url
    public ArrayList<CardInnerPicInfo> innerPicInfoList = new ArrayList<>();

    // 保存卡片上面包含的贴纸参数
    public ArrayList<StickerInfo> stickerList = new ArrayList<>();
    // 卡片上面包含的文字参数
    public ArrayList<CarrotInfo> textsList = new ArrayList<>();
}
