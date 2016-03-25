package com.anybeen.mark.yinjiimageeditorlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 单例模式的 Toast
 * V ? by dyzs
 */
public class ToastUtil {
	private static Toast toast;
	public static void makeText(Context context,String text){
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}
}
