package com.anybeen.mark.imageeditor.component;

import android.app.IntentService;
import android.content.Intent;

import com.anybeen.mark.imageeditor.utils.Const;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wjk on 2015/8/11.
 * 文件下载服务
 */
public class DownloadService extends IntentService {


    public DownloadService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    int completeSize;
    int fileSize;


    /*private SelectableView.OnProgressListener getListenerByTag(String tag){
        if(listeners!=null&&listeners.size()>0){
            for (SelectableView.OnProgressListener listener:listeners){
                if (listener.getTag().equals(tag)){
                    return listener;
                }
            }
        }
        return null;
    }*/


    @Override
    protected void onHandleIntent(Intent intent) {
        completeSize = 0;
        final String fontName = intent.getStringExtra("fontName");
        final String url = Const.FONT_DOWNLOAD_URL + File.separator + fontName;

        String  templateName = intent.getStringExtra("templateName");
        String templateUrl = intent.getStringExtra("templateUrl");

        String pluginName = intent.getStringExtra("pluginName");
        String pluginUrl = intent.getStringExtra("pluginUrl");

        if(null!=fontName&&""!=fontName && null!=url&&""!=url){
            downLoadFont(fontName,url);
            return;
        }
//        else if(null!=templateName&&""!=templateName && null!=templateUrl&&""!=templateUrl){
//            downLoadTemplate(templateName, templateUrl);
//        }else if(null!=pluginName&&""!=pluginName && null!=pluginUrl&&""!=pluginUrl){
//            downLoadplugin(pluginName, pluginUrl);
//        }
    }



//    /**
//     * 下载模板
//     * @param templateName
//     * @param templateUrl
//     */
//    private void downLoadTemplate(String templateName, String templateUrl) {
//        if (templateName != null) {
//            HttpURLConnection connection = null;
//            InputStream is = null;
//            RandomAccessFile randomAccessFile = null;
//            Intent intentBrod = new Intent();
//            try {
//                connection = (HttpURLConnection) new URL(templateUrl).openConnection();
//                connection.setConnectTimeout(5000);
//                connection.setRequestMethod("GET");
//                fileSize = connection.getContentLength();
//                File dirs = new File(ResourceManager.TEMPLATE_PATH);
//                if (!dirs.exists()) {
//                    dirs.mkdirs();
//                }
//                File file = new File(dirs.getPath(), templateName);
//                if(file.exists()){
//                    file.delete();
//                }
//                file.createNewFile();//新建文件
//                randomAccessFile= new RandomAccessFile(file, "rwd");
//                randomAccessFile.setLength(fileSize);
//                is = connection.getInputStream();
//                //读取大文件
//                int length = -1;
//                byte[] buffer = new byte[4 * 1024];
//                intentBrod.setAction("com.anybeen.mark.app.download_progress_template");
//                intentBrod.putExtra("templateName",templateName);
//                while ((length = is.read(buffer)) != -1) {
//                    randomAccessFile.write(buffer, 0, length);
//                    completeSize += length;
//                    if (fileSize == completeSize) {//下载完成
//                        intentBrod.putExtra("progress_template",100);
//                    }else{
//                        intentBrod.putExtra("progress_template", completeSize * 100 / fileSize);
//                    }
//                    sendBroadcast(intentBrod);
//                }
//            } catch (Exception e) {
//                intentBrod.putExtra("progress_template",-2);
//                sendBroadcast(intentBrod);
//            } finally {
//                try {
//                    if (is != null) {
//                        is.close();
//                    }
//                    if(randomAccessFile!=null) {
//                        randomAccessFile.close();
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /**
     * 下载字体
     * @param fontName
     * @param url
     */
    private void downLoadFont(String fontName,String url) {
        if (fontName != null) {
            HttpURLConnection connection = null;
            InputStream is = null;
            RandomAccessFile randomAccessFile = null;
            Intent intentBrod = new Intent();
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                fileSize = connection.getContentLength();
                File dirs = new File(Const.FONT_PATH);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                File file = new File(dirs.getPath(), fontName);
                if(file.exists()){
                    file.delete();
                }
                file.createNewFile();//新建文件
                randomAccessFile= new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(fileSize);
                is = connection.getInputStream();
                //读取大文件
                int length = -1;
                byte[] buffer = new byte[4 * 1024];
                intentBrod.setAction("com.anybeen.mark.app.download_progress");
                String tag = fontName.replace("zip","ttf");
                intentBrod.putExtra("tag",tag);
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    completeSize += length;
                   /* if (getListenerByTag(fontName) != null) {
                        getListenerByTag(fontName).onProgressChange(completeSize * 100 / fileSize);
                    }*/
                    if (fileSize == completeSize) {//下载完成
                        intentBrod.putExtra("progress",100);
                    }else{
                        intentBrod.putExtra("progress", completeSize * 100 / fileSize);
                    }
                    sendBroadcast(intentBrod);
                }
            } catch (Exception e) {
                /*if (getListenerByTag(fontName) != null) {
                    getListenerByTag(fontName).onDownloadFailed(e);
                }*/
                intentBrod.putExtra("progress",-2);
                sendBroadcast(intentBrod);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if(randomAccessFile!=null) {
                        randomAccessFile.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    /**
//     * 下载插件
//     * @param pluginName
//     * @param url
//     */
//    private void downLoadplugin(String pluginName,String url) {
//        if (pluginName != null) {
//            HttpURLConnection connection = null;
//            InputStream is = null;
//            RandomAccessFile randomAccessFile = null;
//            Intent intentBrod = new Intent();
//            try {
//                connection = (HttpURLConnection) new URL(url).openConnection();
//                connection.setConnectTimeout(5000);
//                connection.setRequestMethod("GET");
//                fileSize = connection.getContentLength();
//                File dirs = new File(ResourceManager.PLUGIN_PATH);
//                if (!dirs.exists()) {
//                    dirs.mkdirs();
//                }
//                File file = new File(dirs.getPath(), pluginName);
//                if(file.exists()){
//                    file.delete();
//                }
//                file.createNewFile();//新建文件
//                randomAccessFile= new RandomAccessFile(file, "rwd");
//                randomAccessFile.setLength(fileSize);
//                is = connection.getInputStream();
//                //读取大文件
//                int length = -1;
//                byte[] buffer = new byte[4 * 1024];
//                intentBrod.setAction("com.anybeen.mark.app.download_progress_plugin");
//                intentBrod.putExtra("pluginName",pluginName);
//                while ((length = is.read(buffer)) != -1) {
//                    randomAccessFile.write(buffer, 0, length);
//                    completeSize += length;
//                    if (fileSize == completeSize) {//下载完成
//                        intentBrod.putExtra("progress_plugin",100);
//                    }else{
//                        intentBrod.putExtra("progress_plugin", completeSize * 100 / fileSize);
//                    }
//                    sendBroadcast(intentBrod);
//                }
//            } catch (Exception e) {
//                intentBrod.putExtra("progress_plugin",-2);
//                sendBroadcast(intentBrod);
//            } finally {
//                try {
//                    if (is != null) {
//                        is.close();
//                    }
//                    if(randomAccessFile!=null) {
//                        randomAccessFile.close();
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    /**
//     * 下载插件
//     * @param pluginName
//     * @param url
//     */
//    private void downLoadpluginManager(String pluginName,String url) {
//        if (pluginName != null) {
//            HttpURLConnection connection = null;
//            InputStream is = null;
//            RandomAccessFile randomAccessFile = null;
//            Intent intentBrod = new Intent();
//            try {
//                connection = (HttpURLConnection) new URL(url).openConnection();
//                connection.setConnectTimeout(5000);
//                connection.setRequestMethod("GET");
//                fileSize = connection.getContentLength();
//                File dirs = new File(ResourceManager.PLUGIN_PATH);
//                if (!dirs.exists()) {
//                    dirs.mkdirs();
//                }
//                File file = new File(dirs.getPath(), pluginName);
//                if(file.exists()){
//                    file.delete();
//                }
//                file.createNewFile();//新建文件
//                randomAccessFile= new RandomAccessFile(file, "rwd");
//                randomAccessFile.setLength(fileSize);
//                is = connection.getInputStream();
//                //读取大文件
//                int length = -1;
//                byte[] buffer = new byte[4 * 1024];
//                intentBrod.setAction("com.anybeen.mark.app.download_progress_plugin_manager");
//                intentBrod.putExtra("pluginName",pluginName);
//                while ((length = is.read(buffer)) != -1) {
//                    randomAccessFile.write(buffer, 0, length);
//                    completeSize += length;
//                    if (fileSize == completeSize) {//下载完成
//                        intentBrod.putExtra("progress_plugin",100);
//                    }else{
//                        intentBrod.putExtra("progress_plugin", completeSize * 100 / fileSize);
//                    }
//                    sendBroadcast(intentBrod);
//                }
//            } catch (Exception e) {
//                intentBrod.putExtra("progress_plugin",-2);
//                sendBroadcast(intentBrod);
//            } finally {
//                try {
//                    if (is != null) {
//                        is.close();
//                    }
//                    if(randomAccessFile!=null) {
//                        randomAccessFile.close();
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}