package com.anybeen.mark.imageeditor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.anybeen.mark.imageeditor.view.MovableTextView2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * BitmapUtils
 * 
 * @author panyi
 */
public class BitmapUtils {
	/** Used to tag logs */
	@SuppressWarnings("unused")
	private static final String TAG = "BitmapUtils";

	public static final long MAX_SZIE = 1024 * 512;// 500KB

	public static Bitmap loadImageByPath(final String imagePath, int reqWidth,
			int reqHeight) {
		File file = new File(imagePath);
		// if (file.length() < MAX_SZIE) {
			return getSampledBitmap(imagePath, reqWidth, reqHeight);
//		} else {// 压缩图片
//			return getImageCompress(imagePath);
//		}
	}

	public static int getOrientation(final String imagePath) {
		int rotate = 0;
		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public static Bitmap getSampledBitmap(String filePath, int reqWidth,
			int reqHeight) {
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = (int) FloatMath
						.floor(((float) height / reqHeight) + 0.5f);
			} else {
				inSampleSize = (int) FloatMath
						.floor(((float) width / reqWidth) + 0.5f);
			}
		}
		// System.out.println("inSampleSize--->"+inSampleSize);

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static BitmapSize getBitmapSize(String filePath) {
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		return new BitmapSize(options.outWidth, options.outHeight);
	}

	public static BitmapSize getScaledSize(int originalWidth,
			int originalHeight, int numPixels) {
		float ratio = (float) originalWidth / originalHeight;

		int scaledHeight = (int) FloatMath.sqrt((float) numPixels / ratio);
		int scaledWidth = (int) (ratio * FloatMath.sqrt((float) numPixels
				/ ratio));

		return new BitmapSize(scaledWidth, scaledHeight);
	}

	public static class BitmapSize {
		public int width;
		public int height;

		public BitmapSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	public static byte[] bitmapTobytes(Bitmap bitmap) {
		ByteArrayOutputStream a = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 30, a);
		return a.toByteArray();
	}

	public static byte[] bitmapTobytesNoCompress(Bitmap bitmap) {
		ByteArrayOutputStream a = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, a);
		return a.toByteArray();
	}

	public static Bitmap genRotateBitmap(byte[] data) {
		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		// 自定义相机拍照需要旋转90预览支持竖屏
		Matrix matrix = new Matrix();// 矩阵
		matrix.reset();// 设置为单位矩阵
		matrix.postRotate(90);// 旋转90度
		Bitmap bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
				bMap.getHeight(), matrix, true);
		bMap.recycle();
		bMap = null;
		System.gc();
		return bMapRotate;
	}

	public static Bitmap byteToBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * 将view转为bitmap
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.WHITE);
		view.draw(canvas);
		return returnedBitmap;
	}

	// 按大小缩放
	@Deprecated
	public static Bitmap getImageCompress(final String srcPath) {
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 图片按比例大小压缩。压缩图片太占用内存，丢弃
	@Deprecated
	public static Bitmap compress(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 图片质量压缩
	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;

		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
//			System.out.println("options--->" + options + "    "
//					+ (baos.toByteArray().length / 1024));
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public void printscreen_share(View v, Activity context) {
		View view1 = context.getWindow().getDecorView();
		Display display = context.getWindowManager().getDefaultDisplay();
		view1.layout(0, 0, display.getWidth(), display.getHeight());
		view1.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
	}

	/**
	 * 把图片转换成文件
	 */
	public static boolean saveBitmap2file(Bitmap bmp, String filepath) {
		CompressFormat format = CompressFormat.PNG;
		int quality = 100;
		OutputStream stream = null;
		try {
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				// 错误提示，SDCard未挂载
				return false;
			}
			// 检查SDcard空间
			File SDCardRoot = Environment.getExternalStorageDirectory();
			if (SDCardRoot.getFreeSpace() < 10000) {
				// 弹出对话框提示用户空间不够
				Log.e("Utils", "存储空间不够");
				return false;
			}
			// 在SDcard创建文件夹及文件
			File bitmapFile = new File(SDCardRoot.getPath() + filepath);
			bitmapFile.getParentFile().mkdirs();// 创建文件夹
			stream = new FileOutputStream(SDCardRoot.getPath() + filepath);// "/sdcard/"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bmp.compress(format, quality, stream);
	}

	/**
	 * 截屏
	 * @param activity
	 * @return
	 */
	public static Bitmap getScreenViewBitmap(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	/**
	 * 得到一个 View的图像，如一个TextView
	 * @param view
	 * @return bitmap
	 */
	public static Bitmap getViewBitmap(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}



	/**
	 * 保存Bitmap图片到指定文件路径。绝对路径
	 * @param bm
	 */
	public static void saveBitmap2(Bitmap bm, String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(f);
			bm.compress(CompressFormat.JPEG, 20, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("保存文件--->" + f.getAbsolutePath());
	}


	/**
	 * 将生成的图片保存到内存中
	 * @return string fileAbsPath
	 */
	public static String saveBitmap(Bitmap bitmap, String name) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(Const.SAVE_DIR);
			if (!dir.exists()) dir.mkdir();
			File file = new File(Const.SAVE_DIR + name + ".jpg");
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				return file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getScaleBitmap(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale); //长和宽放大缩小的比例
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
		return resizeBmp;
	}

	public static float getScale(Bitmap bitmap, Context context) {
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		DisplayMetrics outMetrics = getScreenPixels(context);
		int displayW = outMetrics.widthPixels;
		int displayH = outMetrics.heightPixels;
		float scale = bitmapHeight > bitmapWidth ? displayH / (bitmapHeight * 1f) : displayW / (bitmapWidth * 1f);
		return scale;
	}
	public static DisplayMetrics getScreenPixels(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}

	/**
	 * 获取父控件的宽高，设置给bitmap
	 * @param resId
	 * @param contentView
	 * @param context
	 * @return
	 */
	public static Bitmap compressionFiller(int resId, View contentView, Context context) {
		Options opt = new Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opt);
		int layoutHeight = contentView.getHeight();
		int layoutWidth = contentView.getWidth();
		float scale = 0f;
		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		scale = bitmapHeight > bitmapWidth
				? layoutHeight / (bitmapHeight * 1f)
				: layoutWidth / (bitmapWidth * 1f);
		Bitmap resizeBmp;
		if (scale != 0) {
			int bitmapheight = bitmap.getHeight();
			int bitmapwidth = bitmap.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale); // 长和宽放大缩小的比例
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
					bitmapheight, matrix, true);
		} else {
			resizeBmp = bitmap;
		}
		System.out.println("=========>" + scale);
		HashMap<Float, Bitmap> hm = new HashMap();
		hm.put(scale, resizeBmp);

		return resizeBmp;
	}


	/**
	 * 通过传入url获取位图的方法
	 */
	public static  Bitmap getNetBitmap(String url){
		URL newUrl = null;
		Bitmap bitmap = null;
		try {
			newUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) newUrl.openConnection();
			//使用 URL 连接进行输入
			connection.setDoInput(true);
			connection.connect();
			connection.setReadTimeout(30000);
			final int responseCode = connection.getResponseCode();
			if (responseCode==200){
				final InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 通过传入位图,新的宽.高比进行位图的缩放操作
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Drawable resizeImage(Bitmap bitmap, int newWidth, int newHeight) {


		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();

		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return new BitmapDrawable(resizedBitmap);
	}


	/**
	 * 获得圆角图片的方法
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		//创建位图
		Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap
				.Config.ARGB_8888);

		/**
		 * 画布的相关设置
		 */
		Canvas canvas = new Canvas(outputBitmap);
		canvas.drawARGB(0, 0, 0, 0);

		// outputBitmap.recycle();

		/**
		 * 画笔的相关设置
		 */
		final int color = 0xff424242;
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);


		/**
		 * public void drawRoundRect (RectF rect, float rx, float ry, Paint paint)
		 * 参数说明
		 * rect：RectF对象。
		 * rx：x方向上的圆角半径。
		 * ry：y方向上的圆角半径。
		 * paint：绘制时所使用的画笔
		 */
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		/**
		 * 设置两张图片相交时的模式
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return outputBitmap;
	}


	/**
	 * 将彩色图转换为灰度图
	 * @param bitmap 位图
	 * @return  返回转换好的位图
	 */
	public static  Bitmap convertGreyImg(Bitmap bitmap) {
		//获取位图的宽
		int width = bitmap.getWidth();
		//获取位图的高
		int height = bitmap.getHeight();

		//通过位图的大小创建像素点数组
		int []pixels = new int[width * height];

		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for(int i = 0; i < height; i++)  {
			for(int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey  & 0x00FF0000 ) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}
	/**
	 * 获得带倒影的图片方法
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * 获取水印照片
	 * @param bitmap
	 * @param watermarkBitmap
	 * @return
	 */
	public Bitmap getWaterMarkBitmap( Bitmap bitmap, Bitmap watermarkBitmap )
	{

		if( bitmap == null )
		{
			return null;
		}

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		int watermarkBitmapWidth = watermarkBitmap.getWidth();
		int watermarkBitmapHeight = watermarkBitmap.getHeight();
		//create the new blank bitmap
		//创建一个新的和SRC长度宽度一样的位图
		Bitmap newb = Bitmap.createBitmap( bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas( newb );
		//draw src into
		//在 0，0坐标开始画入src
		canvas.drawBitmap( bitmap, 0, 0, null );
		//draw watermark into
		canvas.drawBitmap(watermarkBitmap, bitmapWidth - watermarkBitmapWidth + 5, bitmapHeight - watermarkBitmapHeight +
				5, null);
		//在src的右下角画入水印
		//save all clip
		canvas.save(Canvas.ALL_SAVE_FLAG);//保存
		//store
		canvas.restore();//存储
		return newb;
	}
	/** 重新编码Bitmap
	 *
	 * @param srcBitmap
	 *          需要重新编码的Bitmap
	 *
	 * @param format
	 *          编码后的格式（目前只支持png和jpeg这两种格式）
	 *
	 * @param quality
	 *          重新生成后的bitmap的质量
	 *
	 * @return
	 *          返回重新生成后的bitmap
	 */
	private static Bitmap codec(Bitmap srcBitmap, CompressFormat format,
								int quality) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		srcBitmap.compress(format, quality, outputStream);

		byte[] array = outputStream.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	/**
	 * 把一个View的对象转换成bitmap
	 * @param view
	 * @return
	 */
	public static Bitmap getViewTransforBitmap(View view) {
		//清除焦点
		view.clearFocus();
		view.setPressed(false);

		//能画缓存就返回false
		boolean willNotCache = view.willNotCacheDrawing();
		view.setWillNotCacheDrawing(false);
		int color = view.getDrawingCacheBackgroundColor();
		view.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			view.destroyDrawingCache();
		}
		view.buildDrawingCache();
		Bitmap cacheBitmap = view.getDrawingCache();
		if (cacheBitmap == null) {

			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		view.destroyDrawingCache();
		view.setWillNotCacheDrawing(willNotCache);
		view.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/**
	 * 图片透明度处理
	 *
	 * @param sourceBitmap
	 *            原始图片
	 * @param number
	 *            透明度
	 * @return
	 */
	public static Bitmap getAlphaBitmap(Bitmap sourceBitmap, int number) {
		int[] argb = new int[sourceBitmap.getWidth() * sourceBitmap.getHeight()];
		sourceBitmap.getPixels(argb, 0, sourceBitmap.getWidth(), 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
		}
		sourceBitmap = Bitmap.createBitmap(argb, sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		return sourceBitmap;
	}

	/**
	 * 绘画出字体：多行文本
	 */
	public static Bitmap regenerateBitmap(Bitmap srcBm, MovableTextView2 mtv) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(mtv.getTextSize());
		// paint.setTypeface(getTypefaceObj());
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL);
		paint.setDither(true);
		paint.setFlags(Paint.SUBPIXEL_TEXT_FLAG);
		// String lines[] = text.split("\n");
		int textWidth = (int) paint.measureText(mtv.getText().toString());
//		for (String str : lines)
//		{
//			int temp = (int) paint.measureText(str);
//			if (temp > textWidth)
//				textWidth = temp;
//		}
		if (textWidth < 1)
			textWidth = 1;
		if (srcBm != null)
			srcBm.recycle();
		srcBm = Bitmap.createBitmap(textWidth, (int) mtv.getTextSize(),
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(srcBm);
		canvas.drawText(mtv.getText().toString(), 10, 100, paint);
//		for (int i = 1; i <= lines.length; i++)
//		{
//			canvas.drawText(lines[i - 1], 0, i * textSize, paint);
//		}
		// setCenter();
		return  srcBm;
	}

//	/**
//	 * 计算中心点的坐标
//	 */
//	protected void setCenter()
//	{
//		double delX = getWidth() * mScale / 2;
//		double delY = getHeight() * mScale / 2;
//		R = (float) Math.sqrt((delX * delX + delY * delY));
//		centerRotation = (float) Math.toDegrees(Math.atan(delY / delX));
//	}

	private Bitmap getImageFromFileSystem(String fileAbsPath) {
		Bitmap image = null;
		File file = new File(fileAbsPath);
		try {
			InputStream is = new FileInputStream(file);
			image = BitmapFactory.decodeStream(is);
		} catch (Exception e) {}
		return image;
	}

	public static Bitmap loadImage(Context context, int resId, ViewGroup viewGroup){
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, opts);
		float imgWidth = opts.outWidth * 1.0f;
		float imgHeight = opts.outHeight * 1.0f;
		viewGroup.measure(
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display defaultDisplay = wm.getDefaultDisplay();
//		float screenWidth = defaultDisplay.getWidth();
//		float screenHeight = defaultDisplay.getHeight();
		float parentWidth = viewGroup.getMeasuredWidth() * 1.0f;
		float parentHeight = viewGroup.getMeasuredHeight() * 1.0f;
		System.out.println(parentWidth + "::::" + parentHeight);
		float scaleX = imgWidth / parentWidth;
		float scaleY = imgHeight / parentHeight;
		float scale = 1.0f; //? 1 还是 0
		if(scaleX > scale && scaleY > scale){
			scale = (scaleX>scaleY)?scaleX:scaleY;
		}
		System.out.println("得到的缩放比例为："+scale);
		opts.inSampleSize = (int) scale;
		opts.inJustDecodeBounds = false;
		Bitmap copyImg = BitmapFactory.decodeResource(context.getResources(), resId, opts);
		return copyImg;
	}


	public static Bitmap loadImage(Context context, int resId, int imgWidth, int imgHeight){
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, opts);
		final int height = opts.outHeight;
		final int width = opts.outWidth;
		int inSampleSize = 1;
		if (height > imgHeight || width > imgWidth) {
			if (width > height) {
				inSampleSize = (int) FloatMath
						.floor(((float) height / imgHeight) + 0.5f); // Math.round((float)height
			} else {
				inSampleSize = (int) FloatMath
						.floor(((float) width / imgWidth) + 0.5f); // Math.round((float)width
			}
		}
		opts.inSampleSize = inSampleSize;
		opts.inJustDecodeBounds = false;
		System.out.println("得到的缩放比例为：" + inSampleSize);
		Bitmap copyImg = BitmapFactory.decodeResource(context.getResources(), resId, opts);
		return copyImg;
	}

	public static Bitmap bitmapScaleSelf(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(1 / scale, 1 / scale); //长和宽放大缩小的比例
		Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return temp;
	}
}
