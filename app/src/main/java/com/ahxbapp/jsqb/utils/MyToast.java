package com.ahxbapp.jsqb.utils;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.jsqb.R;


public class MyToast {

	private static Toast toast;
	private static Handler handler = new Handler();

	private static Runnable r = new Runnable() {

		@Override
		public void run() {
			toast.cancel();
		}
	};

	/**
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 *            显示的秒数
	 */
	public static void showToast(Context context, String message, int duration) {
		handler.removeCallbacks(r);

		if (toast != null) {
			View view = toast.getView();
			TextView textView = (TextView)view.findViewById(R.id.tv_content);
			textView.setText(message);
			//toast.setText(message);
		} else {
			toast = new Toast(context);
			View view = LayoutInflater.from(context).inflate(R.layout.toast_view,null);
			TextView textView = (TextView)view.findViewById(R.id.tv_content);
			textView.setText(message);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_LONG);

		}
		toast.show();

		handler.postDelayed(r, duration * 1000);
	}


	public static void showToast(Context context, String message) {
		showToast(context,message,3);
	}

}
