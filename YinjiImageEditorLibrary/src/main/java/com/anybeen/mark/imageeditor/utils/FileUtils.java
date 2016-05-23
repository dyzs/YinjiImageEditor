package com.anybeen.mark.imageeditor.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;


import com.anybeen.mark.imageeditor.entity.CardDataInfo;
import com.anybeen.mark.imageeditor.entity.CardInnerPicShapeInfo;
import com.anybeen.mark.imageeditor.entity.CardTemplateInfo;
import com.anybeen.mark.imageeditor.entity.CarrotInfo;
import com.anybeen.mark.imageeditor.entity.FilterInfo;
import com.anybeen.mark.imageeditor.entity.StickerInfo;

import org.json.JSONArray;
import org.json.JSONObject;

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
 *
 */
public class FileUtils {

    private static FileUtils instance = null;

    private static Context mContext;

    private static final String APP_DIR = "NiKlaus";

    private static final String TEMP_DIR = "NiKlaus/.TEMP";

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
            createSDDir(APP_DIR);
            createSDDir(TEMP_DIR);
        }
    }

    /**
     * 在SD卡根目录上创建目录
     *
     * @param dirName
     */
    public File createSDDir(String dirName) {
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
     * @details 保存多个贴纸的信息
     * @param stickerInfoArrayList
     */
    public static void saveSerializableStickerLists(ArrayList<StickerInfo> stickerInfoArrayList) {
        if (stickerInfoArrayList == null || stickerInfoArrayList.size() <= 0) return;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "matrix.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(stickerInfoArrayList);
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
    public static ArrayList<StickerInfo> readFileToStickerInfoLists() {
        ArrayList<StickerInfo> lists;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "matrix.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            lists = (ArrayList<StickerInfo>) ois.readObject();
            ois.close();
            return lists;
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


    public static void saveSerializableFilter(FilterInfo filterInfo) {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "filter.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(filterInfo);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static FilterInfo readFileToFilterInfo() {
        FilterInfo filterInfo;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "filter.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            filterInfo = (FilterInfo) ois.readObject();
            ois.close();
            return filterInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 传入一个文件夹名称，获取里面的卡片模板
     * @param fileName
     * @param context
     * @return
     */
    public static ArrayList<String> getImageUrlFromAssetsFile(String fileName, Context context) {
        ArrayList<String> list = new ArrayList<>();
        AssetManager am = context.getResources().getAssets();
        try {
            String[] strArr = am.list(fileName);
            if (strArr != null && strArr.length != 0) {
                for (String str : strArr) {
                    list.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获取卡片的文件目录下的模板名称列表
     * CARD_TEMPLATES
     * card_templates_sample
     * @return sample:1001xxx_image_xxx.png
     */
    @Deprecated
    public synchronized static ArrayList<String> readCardFileNameFromFilesDir(String fileName) {
        File file = new File(Const.FILE_CACHE + File.separator + fileName);
        if (!file.exists()) {
            file.mkdir();
        }
        ArrayList<String> list = null;
        if (file.isDirectory()) {
            list = new ArrayList<>();
            File[] filesArr = file.listFiles();
            for (int i = 0; i < filesArr.length; i ++) {
                System.out.println("fileName:" + filesArr[i].getName());
                if (filesArr[i].isFile() && checkCardSuffix(filesArr[i].getName())) {
                    list.add(filesArr[i].getName());
                }
            }
        }
        return list;
    }


    /**
     * @details card template folder who read from absolute path 读取绝对路径下的文件夹，表示各个卡片模板的文件夹
     * @param fileAbsPath
     * @return fileNameList. the abs path isn't any one of child directory while if return null
     */
    public static ArrayList<String> readListFromCardTemplatesDir(String fileAbsPath) {
        File file = new File(fileAbsPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        ArrayList<String> list = null;
        if (file.isDirectory()) {
            list = new ArrayList<>();
            File[] fileNamesArr = file.listFiles();
            if (fileNamesArr == null) {
                return list;
            }
            for (int i = 0; i < fileNamesArr.length; i ++) {
                // System.out.println("模板的文件夹名称:" + fileNamesArr[i].getName());
                if (fileNamesArr[i].isDirectory()) {
                    list.add(fileNamesArr[i].getName());
                }
            }
        }
        return list;
    }


    /**
     * 校验文件夹下的文件是否为图片文件
     * @param filename
     * @return
     */
    public static boolean checkCardSuffix(String filename) {
        String suffix = filename.substring(filename.lastIndexOf("."));
        boolean ret;
        System.out.println("suffix:" + suffix);
        if (suffix.equals(".png")) {
            ret = true;
        }
        else if (suffix.equals(".jpg")) {
            ret = true;
        }
        else if (suffix.equals(".jpeg")) {
            ret = true;
        }
        else {
            ret = false;
        }
        return ret;
    }

    /**
     * @details parse file config.txt, rebuild the card template after get params
     * @param configAbsUrl the absolute path include file name & file suffix
     */
    public synchronized static CardTemplateInfo parseTemplateConfig(String configAbsUrl) {
        try {
            File file = new File(configAbsUrl);
            InputStream is = new FileInputStream(file);
            CardTemplateInfo info = new CardTemplateInfo();
            byte[] bytes = new byte[1024];
            int length;
            StringBuffer sb = new StringBuffer();
            while ((length = is.read(bytes)) != -1) {
                // 以前在这出现乱码问题，后来在这设置了编码格式
                // @Warning txt 文档录入的是 GBK 格式, fuck off,改 txt 为 utf-8 保存
                sb.append(new String(bytes, 0, length,"utf-8"));
            }
            is.close();
            // System.out.println("StringBuffer:" + sb.toString());
            JSONObject jsonObject = new JSONObject(sb.toString());
            info.modelName = jsonObject.getString("modelName");
            info.description = jsonObject.getString("description");
            info.category = jsonObject.getString("category");
            info.shapeCount = jsonObject.getString("category");
            info.orientation = jsonObject.getString("orientation");
            info.stickerCount = jsonObject.getString("stickerCount");
            info.carrotCount = jsonObject.getString("carrotCount");
            info.sampleName = jsonObject.getString("sampleName");
            info.templateName = jsonObject.getString("templateName");
            info.backgroundColor = Color.parseColor(jsonObject.getString("backgroundColor"));

            // 解析 jsonArray
            JSONArray jsonArray = (JSONArray) jsonObject.get("shapes");
            JSONObject obj;
            CardInnerPicShapeInfo shapeInfo;
            for (int i = 0; i < jsonArray.length(); i++) {
                obj = (JSONObject) jsonArray.get(i);
                shapeInfo = new CardInnerPicShapeInfo();
                shapeInfo.left = obj.getInt("left");
                shapeInfo.top = obj.getInt("top");
                shapeInfo.right = obj.getInt("right");
                shapeInfo.bottom = obj.getInt("bottom");
                shapeInfo.width = obj.getInt("width");
                shapeInfo.height = obj.getInt("height");
                info.innerPicShapeInfoList.add(shapeInfo);
            }
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解析 Config 文件出错");
            return null;
        }
    }

    @Deprecated // 暂时不用
    public synchronized static JSONObject parseConfigFileToJsonObj(String configAbsUrl) {
        JSONObject jsonObject;
        try {
            File file = new File(configAbsUrl);
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024 * 2];
            int length;
            StringBuffer sb = new StringBuffer();
            while ((length = is.read(bytes)) != -1) {
                //以前在这出现乱码问题，后来在这设置了编码格式
                sb.append(new String(bytes, 0, length,"UTF-8"));
            }
            is.close();
            jsonObject = new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解析 Config 文件出错");
            return null;
        }
        return jsonObject;
    }

    /**
     * 通过 url 复制图片到新的文件夹下 tempFolder
     * @param fromUrls
     * @param toAbsPath
     * @return
     */
    public static ArrayList<String> copyFilesToNewFolderByUrl(ArrayList<String> fromUrls, String toAbsPath) {
        if (fromUrls == null)return null;
        File terminalPath = new File(toAbsPath);
        if (!terminalPath.exists()) {
            terminalPath.mkdir();
        }
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < fromUrls.size(); i ++) {
            ret.add(copyFileToFolder(fromUrls.get(i), toAbsPath));
        }
        return ret;
    }

    /**
     * @param from fileAbsPath,include file suffix
     * @param toPath file directory
     */
    public synchronized static String copyFileToFolder(String from, String toPath) {
        try {
            File terminalPath = new File(toPath);
            if (!terminalPath.exists()) {
                terminalPath.mkdir();
            }
            File file = new File(from);
            String toName = System.currentTimeMillis() + file.getName();// 加上一个时间毫秒
            String terminal = toPath + toName;
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(terminal);
            byte[] bytes = new byte[1024 * 2];
            int len = -1;
            while((len = fis.read(bytes)) != -1){
                fos.write(bytes, 0, len);
            }
            fis.close();
            fos.close();
            return terminal;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("copy failed...");
            return null;
        }
    }

    public static void deleteFileFromStringArr(String[] deleteUrls) {
        File file;
        for (int i = 0; i < deleteUrls.length; i++) {
            file = new File(deleteUrls[i]);
            file.delete();
        }
    }


    /**
     *
     * @param filePath  ep:card_templates
     * @return
     */
    public static String[] getFileNameListFromAssetsDir(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        String[] ret = null;
        try {
//            for (int i = 0; i < assetManager.list(filePath).length; i++) {
//                System.out.println("str:" + assetManager.list("card_templates")[i]);
//            }
            ret = assetManager.list(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }


    /**
     * 序列化保存 CardDataInfo
     */
    public static void saveSerializableCardDataInfo(CardDataInfo dataInfo) {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileOutputStream fs = new FileOutputStream(sd + "/" + "cardDataInfo.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(dataInfo);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static CardDataInfo readFileToCardDataInfo() {
        CardDataInfo dataInfo;
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileInputStream fs = new FileInputStream(sd + "/" + "cardDataInfo.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            dataInfo = (CardDataInfo) ois.readObject();
            ois.close();
            return dataInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 复制后缀名为 .zip 的文件到指定目录下
     * @param filename
     * @param fileAbsolutePath
     */
    public static void copyZipFile(Context context, String filename, String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        if (!file.exists() && !file.isDirectory()) {file.mkdirs();}
        try {
            String tempName = filename + ".zip";
            InputStream is = context.getAssets().open("temp/" + tempName);
            OutputStream os = new FileOutputStream(fileAbsolutePath + "/" + tempName);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unZipFile(){
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = sd + "/YinJiEditor/Window.zip";
        String unPath = sd + "/YinJiEditor";
        try {
            ZipUtils.UnZipFolder(path, unPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


























































    public static void test () {
        for (int n = 0; n < 10000; n ++) {
            if (n % 1 == 0 && n % 2 == 1 && n % 3 == 0 && n % 4 == 1 &&
                    n % 5 == 4 && n % 6 == 3 && n % 7 ==0 && n % 8 ==1 && n % 9 == 0) {
                System.out.println("i value=" + n);
                return;
            }
        }
    }

}


//// 解析 json
//JSONArray jsonArray = (JSONArray) jsonObject.get("shapes");
//JSONObject obj;0.
//ArrayList<CardInnerPicShapeInfo> list = new ArrayList<>();
//CardInnerPicShapeInfo shapeInfo;
//for (int i = 0; i < jsonArray.length(); i++) {
//        obj = (JSONObject) jsonArray.get(i);
//        shapeInfo = new CardInnerPicShapeInfo();
//        shapeInfo.left = obj.getInt("left");
//        shapeInfo.top = obj.getInt("top");
//        shapeInfo.width = obj.getInt("width");
//        shapeInfo.height = obj.getInt("height");
//        list.add(shapeInfo);
//        }
//        return list;
