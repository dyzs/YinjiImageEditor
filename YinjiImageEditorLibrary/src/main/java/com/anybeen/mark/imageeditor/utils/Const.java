package com.anybeen.mark.imageeditor.utils;
import android.os.Environment;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

import java.io.File;

/**
 * Created by maidou on 2016/1/13.
 */
public interface Const {

    // sd 卡根目录
    String SDCARD_ROOT = Environment.getExternalStorageDirectory().getPath();

    // assets 根目录前缀
    String ASSETS_PREFIX = "asset://";

    // http 前缀
    String HTTP_PREFIX = "http://";

    // Content provider 前缀
    String PROVIDER_PREFIX = "content://";

    // res 前缀
    String RES_PREFIX = "res://";

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
    String[] STICKERS_NAME = {
            "晨",
            "希望",
            "惊",
            "咆哮",
            "闪亮",
            "图记",
            "朕已阅"
    };

    String[] STICKERS_NAME_EN = {
            "morning",
            "hope",
            "surprise",
            "roar",
            "blink",
            "graphics",
            "majesty_read"
    };

    int[] STICKERS_VALUES = {
            R.mipmap.pic_sticker_chen,
            R.mipmap.pic_sticker_hope,
            R.mipmap.pic_sticker_jing,
            R.mipmap.pic_sticker_paoxiao,
            R.mipmap.pic_sticker_shanliang,
            R.mipmap.pic_sticker_tuji,
            R.mipmap.pic_sticker_zhenyiyue
    };

    String[] STICKER_FILE_ABS_PATH = {
            "11",
            "22",
            "33",
            "44",
            "55",
            "66",
            "77",
    };


//    int KEYBOARD_STATE_OPEN = 1;
//    int KEYBOARD_STATE_HIDE = 0;

    // ------------Card_Template----------------separator line-------------------------------
    String MARK_CATEGORY_CARD_TEMPLATES = "1006";
    String FILE_CACHE = SDCARD_ROOT + File.separator + "Android" + File.separator + "data"
            + File.separator + "com.anybeen.mark.app" + File.separator + "files" + File.separator;

    String FILE_TEMP = SDCARD_ROOT + "/YinJiEditor/";

    // 卡片文件夹：/YinJiEditor/card_templates/
    String CARD_TEMPLATES = FILE_TEMP + "card_templates" + File.separator;

    // 卡片模板 sample 文件夹
    String C_SAMPLE = "sample" + File.separator;
    // 卡片模板 templates 文件夹
    String C_TEMPLATE = "template" + File.separator;
    /**
     * 卡片对应的 params 文件夹
     * tomato.txt      content:json 格式{"category":"大自然","name":"","type":"1","orientation":"vertical"}
     */
    String C_CONFIG = "config.txt";

    // 复制相册选择的图片到此目录下,再加载
    String C_TEMP_PIC_FOLDER = CARD_TEMPLATES + "tempPicFolder" + File.separator;


    // 卡片的悬浮贴纸  包含：matrix.txt      sticker1.png        sticker2.jpg
    String C_STICKER = "stickers" + File.separator;
    // 卡片的悬浮文本  包含：carrot.txt
    String C_CARROT = "carrot" + File.separator;


    // 卡片包含的子图片个数选择不同的模板
    String CARD_TYPE_ZERO_FOLDER = "type0" + File.separator;
    String CARD_TYPE_ONE_FOLDER = "type1" + File.separator;
    String CARD_TYPE_TWO_FOLDER = "type2" + File.separator;
    String CARD_TYPE_THREE_FOLDER = "type3" + File.separator;
    String CARD_TYPE_FOUR_FOLDER = "type4" + File.separator;
    String[] CARD_TYPE_FOLDER = {
            CARD_TYPE_ZERO_FOLDER,
            CARD_TYPE_ONE_FOLDER,
            CARD_TYPE_TWO_FOLDER,
            CARD_TYPE_THREE_FOLDER,
            CARD_TYPE_FOUR_FOLDER
    };

    // 卡片包含的子图片个数
    String CARD_TYPE_ZERO_PIC = "0";
    String CARD_TYPE_ONE_PIC = "1";
    String CARD_TYPE_TWO_PIC = "2";
    String CARD_TYPE_THREE_PIC = "3";
    String CARD_TYPE_FOUR_PIC = "4";
    String[] CARD_TYPE = {
            CARD_TYPE_ZERO_PIC,
            CARD_TYPE_ONE_PIC,
            CARD_TYPE_TWO_PIC,
            CARD_TYPE_THREE_PIC,
            CARD_TYPE_FOUR_PIC
    };


    String CARD_CATEGORY_NATURE = "大自然";
    String CARD_CATEGORY_FRESH = "清新";
    String CARD_CATEGORY_CLASSICAL = "古典";
    String[] CARD_CATEGORY = {
            CARD_CATEGORY_NATURE,
            CARD_CATEGORY_FRESH,
            CARD_CATEGORY_CLASSICAL
    };

    String CARD_ORIENTATION_VERTICAL = "横版";
    String CARD_ORIENTATION_HORIZONTAL = "竖版";
    String[] CARD_ORIENTATION = {
            CARD_ORIENTATION_VERTICAL,
            CARD_ORIENTATION_HORIZONTAL
    };


    String[] TEST_PIC = {
            Const.SDCARD_ROOT + "/saves/testPic001.jpg",
            Const.SDCARD_ROOT + "/saves/testPic002.jpg",
            Const.SDCARD_ROOT + "/saves/testPic003.jpg",
            Const.SDCARD_ROOT + "/saves/testPic004.jpg"
    };

    String PICS_LIST = "pics_list";

    String C_IMAGE_EDIT_TYPE = "imageEditType";
    String C_IMAGE_EDIT_TYPE_NEW_ADD = "cardImageForNewAdd";
    String C_IMAGE_EDIT_TYPE_RE_EDIT = "cardImageForReEdit";

    String TEMPLATE_PIC_LOAD_MORE_IN_ASSETS = "asset:///card_templates/pic_templates_more.png";

    String C_ZIP_COPYDIR = CARD_TEMPLATES + "";


    // ------------Card_Template----------------separator line-------------------------------


    /**
     * 国家代码:en ch zh-CN en-US zh-TW en ca da fa   and so on
     * for (String str : mContext.getAssets().getLocales()) {
     *     System.out.println("str:" + str);
     * }
     */
}
