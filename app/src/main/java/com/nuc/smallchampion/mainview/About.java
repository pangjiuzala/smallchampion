package com.nuc.smallchampion.mainview;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.nuc.smallchampion.link.Link;
import com.nuc.smallchampion.lovingdraw.Draw;
import com.nuc.smallchampion.util.Assist;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends Activity {

	private ImageView imageView;
	private Button start3, liu, zhao, zb,lin;
	MediaPlayer mediaPlayer;
	private long exitTime;
	private MediaPlayer mplayer;
	private boolean isPlaying = false;

	SoundPool soundPool;
	int click;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_about);
		// 设置竖屏
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		click = soundPool.load(About.this, R.raw.click6, 1);

		final Animation anim1 = AnimationUtils
				.loadAnimation(this, R.anim.shuofang);
		final Animation anim3 = AnimationUtils
				.loadAnimation(this, R.anim.touming);

		liu = (Button) findViewById(R.id.liu);
		liu.setOnClickListener(new OnClickListener() {
			public void onClick(View source) {
				soundPool.play(click, 1, 1, 0, 0, 1);
				com.nuc.smallchampion.util.Assist.transActivity(
						About.this, About_Liu.class);
			}
		});


	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			com.nuc.smallchampion.util.Assist.transActivity(About.this,
					MainActivity.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
