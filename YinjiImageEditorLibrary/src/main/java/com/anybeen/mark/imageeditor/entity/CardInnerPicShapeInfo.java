package com.anybeen.mark.imageeditor.entity;

/**
 * Created by maidou on 2016/4/28.
 * 分别表示图形框的左上右下与卡片图片的比例
 * 可能是三角形,圆形,梯形等透明框,但是比例取得是边界的那个点
 */
public class CardInnerPicShapeInfo {
    public int left;      // px
    public int top;
    public int right;
    public int bottom;

    public int width;
    public int height;

    public float angle;   // 旋转的角度

    // 保存 bitmap 的参数
    public String picUrl;   // 读取图片的地址
}
