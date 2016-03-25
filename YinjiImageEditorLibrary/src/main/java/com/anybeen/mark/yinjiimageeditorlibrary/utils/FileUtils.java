package com.anybeen.mark.yinjiimageeditorlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;


import com.anybeen.mark.yinjiimageeditorlibrary.entity.CarrotInfo;
import com.anybeen.mark.yinjiimageeditorlibrary.entity.MatrixInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Abner on 15/9/22.
 * QQ 230877476
 * Email nimengbo@gmail.com
 * github https://github.com/nimengbo
 */
public class FileUtils {

    private static FileUtils instance = null;

    private static Context mContext;

    private static final String APP_DIR = "Abner";

    private static final String TEMP_DIR = "Abner/.TEMP";

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    mContext = context.getApplicationContext();
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 保存图像到本地
     *
     * @param bm
     * @return
     */
    public static String saveBitmapToLocal(Bitmap bm, Context context) {
        String path;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    /**
     * @param prefix
     * @param extension
     * @return
     * @throws IOException
     */
    public File createTempFile(String prefix, String extension)
            throws IOException {
        File file = new File(getAppDirPath() + ".TEMP/" + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;
    }

    /**
     * 得到当前应用程序内容目录,外部存储空间不可用时返回null
     *
     * @return
     */
    public String getAppDirPath() {
        String path = null;
        if (getLocalPath() != null) {
            path = getLocalPath() + APP_DIR + "/";
        }
        return path;
    }

    /**
     * 得到当前app的目录
     *
     * @return
     */
    private static String getLocalPath() {
        String sdPath = null;
        sdPath = mContext.getFilesDir().getAbsolutePath() + "/";
        return sdPath;
    }

    /**
     * 检查sd卡是否就绪并且可读写
     *
     * @return
     */
    public boolean isSDCanWrite() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory().canWrite()
                && Environment.getExternalStorageDirectory().canRead()) {
            return true;
        } else {
            return false;
        }
    }

    private FileUtils() {
        // 创建应用内容目录
        if (isSDCanWrite()) {
            creatSDDir(APP_DIR);
            creatSDDir(TEMP_DIR);
        }
    }

    /**
     * 在SD卡根目录上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(getLocalPath() + dirName);
        dir.mkdirs();
        return dir;
    }

    // ---------------
    /**
     * @details 保存多个文本信息到本次磁盘中
     * @param carrotInfoArrayList
     */
    public static void saveSerializableCarrotLists(ArrayList<CarrotInfo> carrotInfoArrayList) {
        if (carrotInfoArrayList == null || carrotInfoArrayList.size() <= 0) return;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "carrot.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(carrotInfoArrayList);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @details 从文件中读取多个文本控件的信息
     * @return MatrixInfoLists
     */
    public static ArrayList<CarrotInfo> readFileToCarrotInfoLists() {
        ArrayList<CarrotInfo> lists;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "carrot.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            lists = (ArrayList<CarrotInfo>) ois.readObject();
            ois.close();
            return lists;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * @details 保存多个贴纸的矩阵信息
     * @param matrixInfoArrayList
     */
    public static void saveSerializableMatrixLists(ArrayList<MatrixInfo> matrixInfoArrayList) {
        if (matrixInfoArrayList == null || matrixInfoArrayList.size() <= 0) return;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "matrix.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(matrixInfoArrayList);
//            for (MatrixInfo matrixInfo : matrixInfoArrayList) {
//                os.writeObject(matrixInfo);
//            }
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @details 从文件中读取多个贴纸的矩阵信息
     * @return MatrixInfoLists
     */
    public static ArrayList<MatrixInfo> readFileToMatrixInfoLists() {
        ArrayList<MatrixInfo> lists;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "matrix.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            lists = (ArrayList<MatrixInfo>) ois.readObject();
            ois.close();
            return lists;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * @details 保存单个贴纸的矩阵信息到文件中
     * @param matrixInfo
     */
    public static void saveSerializableMatrix(MatrixInfo matrixInfo) {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "matrix.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(matrixInfo);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @details 从文件中读取单个贴纸的矩阵信息
     * @return {@link MatrixInfo}
     */
    public static MatrixInfo readFileToMatrixInfo() {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "matrix.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            MatrixInfo matrixInfo = (MatrixInfo) ois.readObject();
            ois.close();
            return matrixInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 根据流生成一个文件
    public static File saveStreamToFile(InputStream stream,File file){
        try {
            OutputStream outStream = new FileOutputStream(file);
            byte[] bs = new byte[stream.available()];
            while(stream.read(bs)!=-1){
                outStream.write(bs);
                outStream.flush();
            }
            outStream.close();
        } catch (Exception e) {
            System.out.println("error:" + e.toString());
        }
        return file;
    }

}
