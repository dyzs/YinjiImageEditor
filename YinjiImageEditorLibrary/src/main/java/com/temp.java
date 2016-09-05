//package com.dyzs.nurse.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.view.Gravity;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.ScaleAnimation;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.dyzs.nurse.R;
//import com.dyzs.nurse.adapter.SoftwareManagerBaseAdapter;
//import com.dyzs.nurse.db.dao.WatchDogDao;
//import com.dyzs.nurse.engine.AppEngine;
//import com.dyzs.nurse.entity.AppInfo;
//import com.dyzs.nurse.utils.AppUtils;
//import com.dyzs.nurse.utils.LogPrinter;
//import com.dyzs.nurse.utils.MyAsyncTask;
//
//public class SoftwareManagerActivity extends Activity implements View.OnClickListener{
//    private Context mContext;
//    private ListView lv_softwaremanager_listdata;
//    private ProgressBar pb_softwaremanager_loading;
//    private SoftwareManagerBaseAdapter softwareManagerBaseAdapter;
//    private List<AppInfo> list;
//    //定义两个集合，用来给应用分类
//    private List<AppInfo> listUserAppInfo;
//    private List<AppInfo> listSysAppInfo;
//    private AppInfo appInfo;				//用来存放分类后的 app 的数据
//
//    private TextView tv_softwaremanager_remind;
//    private TextView tv_softwaremanager_storage;
//
//    //弹出气泡菜单
//    private PopupWindow mPopupWindow;
//
//    private WatchDogDao mWatchDogDao;
//    private int longClickCount = 0;
//
//    //双击事件
//    long[] mHits = new long[2];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_softwaremanager);
//        mContext = this;
//        lv_softwaremanager_listdata = (ListView) findViewById(R.id.lv_softwaremanager_listdata);
//        pb_softwaremanager_loading = (ProgressBar) findViewById(R.id.pb_softwaremanager_loading);
//        tv_softwaremanager_remind = (TextView) findViewById(R.id.tv_softwaremanager_remind);
//        tv_softwaremanager_storage = (TextView) findViewById(R.id.tv_softwaremanager_storage);
//
//        loadData();
//
//        //设置 listView 的滚动事件，提示当前的是系统应用还是用户应用
//        setOnScrollRemind();
//
//        //长按事件，加锁和解锁
//        setItemLongClick();
//        //设置 listView 的点击事件，弹出气泡 mPopupWindow
//        setItemClickPopupWindow();
//
//
//
//        //当全部加载完成时，得到存储详情，当 listView 被点击的时候才显示它，再次单机就消失
//        getStorageInfo();
//    }
//
//
//
//
//
//    /**
//     * 异步加载数据
//     */
//    private void loadData() {
//        new MyAsyncTask() {
//            @Override
//            public void onPreExecute() {
//                pb_softwaremanager_loading.setVisibility(View.VISIBLE);
//                //list = new ArrayList<AppInfo>();
//                //！！！！！！一定要初始化 list 集合；
//                if(listUserAppInfo == null){
//                    listUserAppInfo = new ArrayList<AppInfo>();
//                }
//                if(listSysAppInfo == null){
//                    listSysAppInfo = new ArrayList<AppInfo>();
//                }
//            }
//            @Override
//            public void doInBackground() {
//                //if(list == null){
//                list = AppEngine.getAppInfo(mContext);
//                LogPrinter.w("listload", "done!");
//                //遍历集合进行分类
//                for (AppInfo appInfo : list) {
//                    if(appInfo.isUserApp()){
//                        listUserAppInfo.add(appInfo);
//                    }else{
//                        LogPrinter.w("foreach", appInfo.getName());
//                        listSysAppInfo.add(appInfo);
//                    }
//                }
//                //}
//                //在 splash 界面中把所有的应用包名信息存入到 watchdog.db 中
//            }
//            @Override
//            public void onPostExecute() {
//                //softwareManagerBaseAdapter = new SoftwareManagerBaseAdapter(mContext,list);
//                if(softwareManagerBaseAdapter == null){
//                    softwareManagerBaseAdapter = new SoftwareManagerBaseAdapter(mContext,listUserAppInfo,listSysAppInfo);
//                    lv_softwaremanager_listdata.setAdapter(softwareManagerBaseAdapter);
//                }else{
//                    System.out.println("else running");
//                    softwareManagerBaseAdapter.notifyDataSetChanged();
//                }
//                pb_softwaremanager_loading.setVisibility(View.INVISIBLE);
//            }
//        }.execute();
//    }
//
//    private void setOnScrollRemind() {
//        lv_softwaremanager_listdata.setOnScrollListener(new AbsListView.OnScrollListener() {
//            //当 lv 滚动的时候也隐藏气泡 ，监听当前滚动的状态，当滚动状态发生改变的时候
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                hidePopupWindow();
//                switch (scrollState) {
//                    case SCROLL_STATE_FLING:
//                        tv_softwaremanager_remind.setVisibility(View.VISIBLE);
//                        break;
//                    case SCROLL_STATE_IDLE:
//                        tv_softwaremanager_remind.setVisibility(View.INVISIBLE);
//                        break;
//                    case SCROLL_STATE_TOUCH_SCROLL:
//                        tv_softwaremanager_remind.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//            //当listview滚动的时候调用
//            //view : listview
//            //firstVisibleItem : 界面第一个可见的条目的位置
//            //visibleItemCount : 可见条目的个数
//            //totalItemCount :　条目的总个数，可见条目＋不可见条目
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                //TODO 整一个跟小米手机联系人差不多的效果
//                //View contentView = View.inflate(mContext, R.layout.xxxx, null);
//                //当 lv 在滚动的时候弹出提示框
//                //PopupWindow popupWindow = new PopupWindow(contentView, width, height);
//
//                //为空的原因：listview在初始化的时候就会调用onscroll
//                if(listUserAppInfo != null && listSysAppInfo != null){
//                    if( firstVisibleItem >= listUserAppInfo.size() + 1){
//                        tv_softwaremanager_remind.setText(" 系统程序("+listSysAppInfo.size()+") ");
//                    }else{
//                        tv_softwaremanager_remind.setText(" 用户程序("+listUserAppInfo.size()+") ");
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 长按显示
//     */
//    private void setItemLongClick(){
//        lv_softwaremanager_listdata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                if(position == 0 || position == listUserAppInfo.size() +1){
//                    return false;
//                }
//                mWatchDogDao = new WatchDogDao(mContext);
//                appInfo = (AppInfo) parent.getItemAtPosition(position);
//                //2.自己给自己加锁的问题，在listview的item长按点击事件中的加锁处理中进行判断
//                //加锁
//                if (appInfo.getPackageName().equals(getPackageName())) {
//                    Toast.makeText(getApplicationContext(), "自己不能给自己加锁!!", 0).show();
//                    return false;
//                }
//                String packageName = appInfo.getPackageName();
//                System.out.println("-->"+packageName);
//                System.out.println("==>"+mWatchDogDao.queryOneAppIsLock(packageName));
//                //加锁解锁
//                if (mWatchDogDao.queryOneAppIsLock(packageName)) {
//                    //解锁
//                    mWatchDogDao.unLockApp(packageName);
//                }else{
//                    //加锁
//                    mWatchDogDao.addLockApp(packageName);
//                }
//                softwareManagerBaseAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });
//    }
//
//    /**
//     * 如果不给 ppw 设置背景，那么在 ppw 上设置的动画效果就不生效了，因为动画是执行在背景的基础上的，
//     * 如果没有背景，动画就不知道在哪里执行，因为动画是依附于背景执行的
//     * 注意：没有背景，动画是不会执行的
//     */
//    //弹出气泡的点击事件
//    private void setItemClickPopupWindow(){
//        //mPopupWindow = new PopupWindow(contentView, width, height);
//        lv_softwaremanager_listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //5 屏蔽用户程序多少个和系统程序多少个点击弹出气泡，查看 adapter 处理
//                if(position == 0 || position == listUserAppInfo.size() +1){
//                    return ;
//                }
//                //6 得到当前 item 的 appInfo 的 bean 对象
//                appInfo = (AppInfo) parent.getItemAtPosition(position);
//                //7 在弹出气泡之前，隐藏上一个气泡
//                hidePopupWindow();
//                //1.2 创建 contentView 对象
//                View contentView = View.inflate(mContext, R.layout.ppw_softwaremanager_main, null);
//                //3 初始化四个 LinearLayout ，添加点击事件
//                LinearLayout ll_ppw_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_ppw_uninstall);
//                LinearLayout ll_ppw_start = (LinearLayout) contentView.findViewById(R.id.ll_ppw_start);
//                LinearLayout ll_ppw_share = (LinearLayout) contentView.findViewById(R.id.ll_ppw_share);
//                LinearLayout ll_ppw_detail = (LinearLayout) contentView.findViewById(R.id.ll_ppw_detail);
//                ll_ppw_uninstall.setOnClickListener(SoftwareManagerActivity.this);
//                ll_ppw_start.setOnClickListener(SoftwareManagerActivity.this);
//                ll_ppw_share.setOnClickListener(SoftwareManagerActivity.this);
//                ll_ppw_detail.setOnClickListener(SoftwareManagerActivity.this);
//
//                //1.1 初始化 mPopupWindow
//                //contentView : 显示的view对象
//                //width : 宽度
//                //height : 高度
//                //LayoutParams ： 根据 xml 视图匹配的，如果 xml 跟节点是 linearlayout 那么它就是一个 linearlayout 对象
//                mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                //1.4 获取坐标 x y ，通过得到条目的位置，因为只获取两个坐标轴，所以直接定义参数个数为 2 个
//                int[] location = new int[2];
//                view.getLocationInWindow(location);
//                int x = location[0];
//                int y = location[1];
//                //1.3 显示 mPopupWindow
//                //parent : 设置mPopupWindow挂载在那个控件中，挂载在listview中,退出报错，跟dialog找不到挂载界面异常相似
//                //gravity, x, y : 设置popupwindow显示的位置
//                mPopupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP , x + 100, y);
//                //2 设置 mPopupWindow 的动画效果
//                //缩放动画
//                //前四个：设置popupwindow从没有开始逐渐变化到整个控件大小，  动画中 0：没有   1：整个控件
//                //后四个：设置动画是以空间自身变化，还是以父控件进行变化
//                //Animation.RELATIVE_TO_SELF : 以自身进行变化
//                //Animation.RELATIVE_TO_PARENT : 以父控件进行变化
//                ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
//                sa.setDuration(300);
//                //渐变动画
//                //从透明渐变到不透明
//                AlphaAnimation aa = new AlphaAnimation(0.3f, 1f);
//                aa.setDuration(300);
//                //组合动画
//                //shareInterpolator :是否共享动画插入器，true:共享	false:各自用各自的
//                AnimationSet animSet = new AnimationSet(true);
//                animSet.addAnimation(sa);
//                animSet.addAnimation(aa);
//                //执行动画
//                //动画根据背景进行的一些计算，它是依附于背景来执行的，如果没有背景的话，动画是不会执行的，popupwindow默认是不设置动画
//                contentView.setAnimation(animSet);
//                // 因为动画执行需要背景，所以个popupwindow设置背景，但是设置背景就看到很丑的背景，那么我们直接设置要给透明背景吧
//                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            }
//        });
//    }
//    /**
//     * 隐藏 pppw
//     */
//    protected void hidePopupWindow() {
//        //判断，当 ppw 不为空的时候再 dismiss
//        if(mPopupWindow != null){
//            mPopupWindow.dismiss();
//            //重置 ppw
//            mPopupWindow = null;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_ppw_uninstall:
//                System.out.println("卸载。。。。");
//                uninstall();
//                break;
//            case R.id.ll_ppw_start:
//                System.out.println("启动。。。。");
//                start();
//                break;
//            case R.id.ll_ppw_share:
//                System.out.println("分享。。。。");
//                share();
//                break;
//            case R.id.ll_ppw_detail:
//                System.out.println("详细信息。。。。");
//                details();
//                break;
//        }
//        //当点击事件执行完成，就把 ppw 隐藏了
//        hidePopupWindow();
//    }
//
//    private void share() {
//        /**
//         * Intent
//         {
//         act=android.intent.action.SEND   action
//         typ=text/plain flg=0x3000000 	type
//         cmp=com.android.mms/.ui.ComposeMessageActivity (has extras)  has extras：表示intent里面存放有数据
//         } from pid 228
//         */
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.SEND");
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, "发现了一个非常刘备的应用：[ "+appInfo.getName()+" ]，快去 www.dyzs.com.cn 搜索吧！");
//        startActivityForResult(intent, 1);
//    }
//
//
//    private void details() {
//        if(appInfo.getPackageName().equals(getPackageName())){
//            Toast.makeText(mContext, "当前应用不让你看！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //跳转到详情界面
//        /**
//         * Intent
//         {
//         act=android.settings.APPLICATION_DETAILS_SETTINGS     action
//         dat=package:com.itcast.cache 	data,com.itcast.cache:要打开哪个应用程序的详情界面
//         cmp=com.android.settings/.applications.InstalledAppDetails    cmp:跳转到的界面
//         } from pid 228
//         */
//        Intent intent = new Intent();
//        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//        intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
//        startActivity(intent);
//    }
//
//
//    /**
//     * 安卓中是基于 linux 内核进行开发的 ：
//     * 		安卓中不是所有的应用程序都是用 java 来写的，所以用 java 启动会出错比如说系统核心程序，
//     * 	那是无法操作打开的，因为它是用 C 写的，但是也不是所有的系统应用程序都无法打开。系统核心应用
//     * 	是没有清单文件，没有 activity 的 launcher ，所以会报null 指针异常。
//     */
//    private void start() {
//        //判断当前应用是不是用户自己的应用，
//        //这边不需要报空指针异常，得当程序正常运行，所以把它写前面
//        if(!appInfo.getPackageName().equals(getPackageName())){
//            //得到包管理者
//            PackageManager packageManager = getPackageManager();
//            Intent intent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName());
//            //判断当前是应用是不是系统核心应用，因为系统核心应用的话是无法得到包名的
//            if(intent != null){
//                startActivity(intent);
//            }else{
//                Toast.makeText(mContext, "系统核心应用，无法打开！", Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            Toast.makeText(mContext, "打开自己？脑子有毛病啊！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void uninstall() {
//        /**
//         * <intent-filter>
//         * 		<action android:name="android.intent.action.VIEW" />
//         * 		<action android:name="android.intent.action.DELETE" />
//         * 		<category android:name="android.intent.category.DEFAULT" />
//         * 		<data android:scheme="package" /> package:+包名 tel:+号码
//         * </intent-filter>
//         */
//        //判断 bean 对象，是用户程序还是系统程序
//        if(appInfo.isUserApp()){
//            //在判断当前得到的包名是不是自己的包名，如果是，不删
//            //System.out.println(appInfo.getPackageName()+"/"+getPackageName());
//            if(appInfo.getPackageName().equals(getPackageName())){
//                Toast.makeText(mContext, "卧槽，别这样行不！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            intent.setAction("android.intent.action.DELETE");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
//            startActivityForResult(intent, 0);
//        }else{
//            Toast.makeText(mContext, "这是系统程序，请先 root，要不然神也帮不了你！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("requestCode:"+requestCode);//发出去的就回来了
//        //if(requestCode == 0){
//        //更新界面
//        loadData();
//        LogPrinter.w("tag", "done");
//        //}
//    }
//
//
//
//    /**
//     * 得到存储详情，当单机的时候才显示它，再次单机就消失
//     */
//    private void getStorageInfo() {
//        LogPrinter.w("tag", "==========================");
//        //得到 sd 卡总存储
//        String totalSizeSD = AppUtils.getTotalSize(getApplicationContext(), AppUtils.TOTAL_SD);
//        //得到 sd 卡的可用，剩余空间
//        String availableSD = AppUtils.getAvailable(getApplicationContext(), AppUtils.AVAILABLE_SD);
//        //得到 ROM
//        String totalSizeROM = AppUtils.getTotalSize(getApplicationContext(), AppUtils.TOTAL_ROM);
//        String availableROM = AppUtils.getAvailable(getApplicationContext(), AppUtils.AVAILABLE_ROM);
//        tv_softwaremanager_storage.setText("sd 卡剩余："+availableSD+"/"+totalSizeSD+
//                "\n机身剩余："+availableROM+"/"+totalSizeROM);
//        //tv_softwaremanager_storage.setVisibility(View.VISIBLE);
//    }
//
//
//    //4. 在界面退出的时候，隐藏气泡，
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        hidePopupWindow();
//        mWatchDogDao = null;
//    }
//
//    //标题的双击事件
//    public void tv_btnnnnnnnnnnn(View v){
//        /**
//         * src the source array to copy the content. 拷贝的原数组 srcPos the
//         * starting index of the content in src. 从原数组的那个位置开始拷贝 dst the
//         * destination array to copy the data into. 拷贝目标数组 dstPos the
//         * starting index for the copied content in dst. 从目标数组的那个位置去写
//         * length the number of elements to be copied. 拷贝的长度
//         */
//        //表示从原数组的 1 拷贝给 0 的位置，拷贝一个，所以长度为 length -1
//        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
//        //获取离开机的时间，毫秒值，不包含手机休眠时间
//        mHits[mHits.length-1] = SystemClock.uptimeMillis();
//        //判断数组的第一个元素去是否再次获取离开机的时间再减去300毫秒大，如果大于执行点击操作，小于则不执行
//        if(mHits[0] >= (SystemClock.uptimeMillis() - 300)){
//            System.out.println("我被点击了");
//            if(longClickCount == 0){
//                tv_softwaremanager_storage.setVisibility(View.INVISIBLE);
//                longClickCount ++ ;
//            }else{
//                tv_softwaremanager_storage.setVisibility(View.VISIBLE);
//                longClickCount -- ;
//            }
//        }
//    }
//
//
//}