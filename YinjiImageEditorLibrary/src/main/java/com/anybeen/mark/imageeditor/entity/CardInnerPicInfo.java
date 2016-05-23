package com.anybeen.mark.imageeditor.entity;

/**
 * Created by maidou on 2016/5/8.
 * 用来保存的参数，相对应与 innerPicShapeInfo
 */
public class CardInnerPicInfo implements java.io.Serializable{
    public int left;      // px
    public int top;
    public int right;
    public int bottom;

    public int width;
    public int height;

    public int innerBgColor;

    public float angle;   // 旋转的角度

    // 保存 bitmap 的参数
    public String picUrl;   // 读取图片的地址

    // 保存 matrix 的参数
    public float[]  floatArr;
}


//final StickerForCard view = new StickerForCard(mContext);
//FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//
//lp.leftMargin = leftMargin;
//        lp.topMargin = topMargin;
//        lp.rightMargin = rightMargin;
//        lp.bottomMargin = bottomMargin;
//        lp.width = widthAfterScale;
//        lp.height = heightAfterScale;
//
//        view.setLayoutParams(lp);
//        view.setBitmap(bitmap);
//        view.setOperationListener(new StickerForCard.OperationListener() {
//@Override
//public void onEdit(StickerForCard StickerForCard) {
//
//        }
//
//@Override
//public void onClick(StickerForCard StickerForCard) {
//        Toast.makeText(mContext, "left:" + StickerForCard.getLeft(), Toast.LENGTH_SHORT).show();
//        }
//        });
//        rl_bitmap_layout.addView(view);
//        mStickerForCard.add(view);
//
//        // TODO 判断是重载还是新增, 设置 matrix
//        if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
//        float[] floats = saveDataInfo.innerPicInfoList.get(0).floatArr;
//        Matrix matrix = new Matrix();
//        matrix.setValues(floats);
//        view.setMatrix(matrix);
//        }