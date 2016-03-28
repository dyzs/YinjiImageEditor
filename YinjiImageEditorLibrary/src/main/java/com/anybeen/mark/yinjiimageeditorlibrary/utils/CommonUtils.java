package com.anybeen.mark.yinjiimageeditorlibrary.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.anybeen.mark.yinjiimageeditorlibrary.view.MovableTextView2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by maidou on 2016/3/16.
 */
public class CommonUtils {

    public static void closeKeyboard(MovableTextView2 mtv, Context context) {
        InputMethodManager inputManger = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManger.hideSoftInputFromWindow(mtv.getWindowToken(), 0);
    }

    public static void closeKeyboard(EditText et, Context context) {
        InputMethodManager inputManger = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManger.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void closeKeyboard(View view, Context context) {
        InputMethodManager inputManger = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * @details 当键盘打开的时候，点击则关闭 && 当键盘关闭的时候，点击则打开
     */
    public static void hitKeyboardOpenOrNot(Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


//    public static int listenKeyboard(final View view) {
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                view.getWindowVisibleDisplayFrame(r);
//                int visibleHeight = r.height();
//                if (mVisibleHeight == 0) {
//                    mVisibleHeight = visibleHeight;
//                    return;
//                }
//                if (mVisibleHeight == visibleHeight) {
//                    return;
//                }
//                return visibleHeight;
//                System.out.println("mVisibleHeight-----:" + mVisibleHeight);
//            }
//        });
//        return 0;
//    }

    public static int matchedColor(int seekBarProgress) {
        int ret = 0;
        if (seekBarProgress >= 0 && seekBarProgress < 10) {
            ret = 0;
        }
        if (seekBarProgress >= 10 && seekBarProgress < 20) {
            ret = 1;
        }
        if (seekBarProgress >= 20 && seekBarProgress < 30) {
            ret = 2;
        }
        if (seekBarProgress >= 30 && seekBarProgress < 40) {
            ret = 3;
        }
        if (seekBarProgress >= 40 && seekBarProgress < 50) {
            ret = 4;
        }
        if (seekBarProgress >= 50 && seekBarProgress < 60) {
            ret = 5;
        }
        if (seekBarProgress >= 60 && seekBarProgress < 70) {
            ret = 6;
        }
        if (seekBarProgress >= 70 && seekBarProgress < 80) {
            ret = 7;
        }
        if (seekBarProgress >= 80 && seekBarProgress < 90) {
            ret = 8;
        }
        return ret;
    }



    /**
     * 获取配置文件键值对<p>用法：getFontConfigProperties(fileName).getProperty("key");</p>
     * @param propertyFileName 配置文件名
     * @param context context
     * @return Properties
     */
    public static Properties getFontConfigProperties(File propertyFileName,Context context) {
        Properties props = new Properties();
        try {
            FileInputStream s = new FileInputStream(propertyFileName);
            props.load(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFiles2Assets(Context context,String oldPath,String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFiles2Assets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断网络连接类型，只有在3G或wifi里进行数据发送
     */
    public static boolean isNetAvailable(Context context){
        return checkNet(context);
    }
    public static boolean checkNet(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("==>" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * @details 通过字体名称获取字体
     * @param fontName
     * @return
     */
    public static Typeface getTypeface(String fontName) {
        Typeface typeface = null;
        if (fontName.equals("default")) {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        } else {
            File file = new File(Const.FONT_PATH + File.separator + fontName);
            if (file.exists()) {
                try {
                    typeface = Typeface.createFromFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return typeface;
    }
}
