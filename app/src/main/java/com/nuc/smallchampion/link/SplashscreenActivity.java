package com.nuc.smallchampion.link;






import java.util.Timer;
import java.util.TimerTask;

import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashscreenActivity extends Activity {

	private boolean isInit = false;
	private float curX = 0;
	private float curY = 30;
	// 记录蝴蝶ImageView下一个位置的座标
	float nextX = 0;
	float nextY = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			finish();


			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.view_splashscreen);
		isInit = true; // 初始化完毕
		new Monitor().start(); // 开启监听线程
		final ImageView imageView = (ImageView) findViewById(R.id.butterfly);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					// 横向上一直向右飞
					if (nextX > 320) {
						curX = nextX = 0;
					} else {
						nextX += 8;
					}
					// 纵向上可以随机上下
					nextY = curY + (float) (Math.random() * 10 - 5);
					// 设置显示蝴蝶的ImageView发生位移改变
					TranslateAnimation anim = new TranslateAnimation(curX,
							nextX, curY, nextY);
					curX = nextX;
					curY = nextY;
					anim.setDuration(200);
					// 开始位移动画

					imageView.startAnimation(anim);
				}
			}
		};
		final AnimationDrawable butterfly = (AnimationDrawable) imageView
				.getBackground();
		butterfly.start();
		// 通过定制器控制每0.2秒运行一次TranslateAnimation动画
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0x123);
			}

		}, 0, 200);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			startActivity(new Intent(SplashscreenActivity.this,
					MainActivity.class));
			finish();
		}
	};

	private class Monitor extends Thread {

		@Override
		public void run() {
			boolean isRun = true;
			while (isRun) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isInit) { // 如果初始化完毕，开始进入游戏
					isRun = false;
					mHandler.sendMessage(new Message());
				}
			}
		}
	}
}
