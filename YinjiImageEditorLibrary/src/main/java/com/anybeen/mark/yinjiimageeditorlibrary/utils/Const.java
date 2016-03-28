package com.anybeen.mark.yinjiimageeditorlibrary.utils;
import android.os.Environment;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

import java.io.File;

/**
 * Created by maidou on 2016/1/13.
 */
public interface Const {

    // sd 卡根目录
    String SDCARD_ROOT = Environment.getExternalStorageDirectory().getPath();

    // 文件根目录前缀
    String FILE_PREFIX = "file://";

    // 文件的父目录
    String STICKER = FILE_PREFIX + SDCARD_ROOT + "/anybeenImageEditor/stickers/";

    // 图片保存路径
    String SAVE_DIR = SDCARD_ROOT + "/saves/";

    // 文件的子目录
    String ANIMAL           = STICKER + "dongwu";    // 动物
    String MOOD             = STICKER + "xinqing";   // 心情
    String COS              = STICKER + "cos";       // CosPlay
    String SYMBOL           = STICKER + "fuhao";     // 符号
    String DECORATION       = STICKER + "shipin";    // 饰品
    String SPRING_FESTIVAL  = STICKER + "chunjie";   // 春节
    String TEXT             = STICKER + "wenzi";     // 文字
    String NUMBER           = STICKER + "shuzi";     // 数字
    String FRAME            = STICKER + "biankuang"; // 边框
    String PROFESSION       = STICKER + "zhiye";     // 职业

    String TEMP = SDCARD_ROOT + "/anybeenImageEditor/stickers/";


    String TYPE_FACE_PATH = FILE_PREFIX + SDCARD_ROOT + "/anybeen/fonts/";
    String TYPE_DEFAULT = TYPE_FACE_PATH + "";
    String TYPE_STEADY = TYPE_FACE_PATH + "steady.ttf";
    String TYPE_HANDSOME = TYPE_FACE_PATH + "handsome.ttf";
    String TYPE_GIRLS = TYPE_FACE_PATH + "girls.ttf";
    String TYPE_SCREAM = TYPE_FACE_PATH + "scream.ttf";

    /**字体相关文件在SD卡上的路径*/
    String FONT_PATH = Environment.getExternalStorageDirectory()+ File.separator+"anybeen"+File.separator+"font";
    /**字体文件下载路径头*/
    public static final String FONT_DOWNLOAD_URL = "http://www.anybeen.com/font_down";
    //字体文件下载
    String INTENT_ACTION_FONT_DOWNLOAD = "com.anybeen.mark.app.font.download";


    // seek bar progress
    int[] COLOR_VALUES = {R.color.rainbow_red, R.color.rainbow_orange, R.color.rainbow_yellow,
            R.color.rainbow_green, R.color.rainbow_blue, R.color.rainbow_cyan,
            R.color.rainbow_purple, R.color.rainbow_white, R.color.rainbow_black};    // 红橙黄绿蓝靛紫黑白

    float[] COLOR_SPAN = {1, 1, 1, 1, 1, 1, 1, 1, 1};




    // count 7
    String[] STICKERS_NAME = {"晨", "希望", "惊", "咆哮", "闪亮", "图记", "朕已阅"};

    int[] STICKERS_VALUES = {
            R.mipmap.pic_sticker_chen,
            R.mipmap.pic_sticker_hope,
            R.mipmap.pic_sticker_jing,
            R.mipmap.pic_sticker_paoxiao,
            R.mipmap.pic_sticker_shanliang,
            R.mipmap.pic_sticker_tuji,
            R.mipmap.pic_sticker_zhenyiyue
    };

    String[] STICKERS_NAME_PINGYIN = {

    };




}
