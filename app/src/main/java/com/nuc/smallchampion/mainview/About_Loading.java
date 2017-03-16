package com.nuc.smallchampion.mainview;

import com.nuc.smallchampion.util.Assist;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class About_Loading extends Activity {

	private boolean isInit = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {


			finish();
			com.nuc.smallchampion.util.Assist.transActivity(About_Loading.this,
					MainActivity.class);
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
		setContentView(R.layout.view_loading);
		isInit  = true;		// 初始化完毕
		new Monitor().start();		// 开启监听线程

	}



	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			startActivity(new Intent(About_Loading.this, About.class));
			finish();
		}
	};

	private class Monitor extends Thread {

		@Override
		public void run() {
			boolean isRun = true;
			while(isRun) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isInit) {		// 如果初始化完毕，开始进入游戏
					isRun = false;
					mHandler.sendMessage(new Message());
				}
			}
		}
	}
}
