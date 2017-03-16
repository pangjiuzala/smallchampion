package com.nuc.smallchampion.link;

import java.io.File;

import com.nuc.smallchampion.link.view.GameView;
import com.nuc.smallchampion.link.view.OnStateListener;
import com.nuc.smallchampion.link.view.OnTimerListener;
import com.nuc.smallchampion.link.view.OnToolsChangeListener;
import com.nuc.smallchampion.mainview.About;
import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Link extends Activity implements OnClickListener, OnTimerListener,
		OnStateListener, OnToolsChangeListener {

	private ImageButton btnPlay;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private ImageView imgTitle;
	private GameView gameView;
	private SeekBar progress;
	private MyDialog dialog;
	private ImageView clock;
	private TextView textRefreshNum;
	private TextView textTipNum;
	static final int NOTIFICATION_ID = 0x1123;

	private boolean isplaying;
	String time = null;
	final int FONT_10 = 0x111;

	private Bitmap shot() {
		View view = getWindow().getDecorView();
		Display display = this.getWindowManager().getDefaultDisplay();
		view.layout(0, 0, display.getWidth(), display.getHeight());
		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
		// System.out.println(view.getDrawingCache());
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.destroyDrawingCache();
		FileUtils.savePic(bmp, time);
		return bmp;
	}

	public void shareScreenshot() {
		time = "rival_" + DateUtils.getTimeNow();
		shot();
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"这是我玩的由星愿组合开发的小游戏“我是小状元”之”慧眼认字“篇，相当不错哦！上百度手机助手、豌豆荚或移动mm市场即可下载本应用！");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Link.this.startActivity(Intent.createChooser(intent, "分享"));
			}

		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// -------------向menu中添加字体大小的子菜单-------------

		menu.add(0, FONT_10, 0, "截屏分享");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单项被单击后的回调方法
	public boolean onOptionsItemSelected(MenuItem mi) {
		// 判断单击的是哪个菜单项，并针对性的作出响应。
		switch (mi.getItemId()) {
			case FONT_10:


				shareScreenshot();
				break;
		}
		return true;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					dialog = new MyDialog(Link.this, gameView, "恭喜您金榜题名！",
							gameView.getTotalTime() - progress.getProgress());
					dialog.show();
					break;
				case 1:
					dialog = new MyDialog(Link.this, gameView, "失败了哦！",
							gameView.getTotalTime() - progress.getProgress());
					dialog.show();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_link);
		btnPlay = (ImageButton) findViewById(R.id.play_btn);
		btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
		btnTip = (ImageButton) findViewById(R.id.tip_btn);
		imgTitle = (ImageView) findViewById(R.id.title_img);
		gameView = (GameView) findViewById(R.id.game_view);
		clock = (ImageView) findViewById(R.id.clock);
		progress = (SeekBar) findViewById(R.id.timer);
		textRefreshNum = (TextView) findViewById(R.id.text_refresh_num);
		textTipNum = (TextView) findViewById(R.id.text_tip_num);
		// XXX
		progress.setMax(gameView.getTotalTime());

		btnPlay.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnTip.setOnClickListener(this);
		gameView.setOnTimerListener(this);
		gameView.setOnStateListener(this);
		gameView.setOnToolsChangedListener(this);
		GameView.initSound(this);

		Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
		imgTitle.startAnimation(scale);
		btnPlay.startAnimation(scale);


		isplaying = true;
		// GameView.soundPlay.play(GameView.ID_SOUND_BACK2BG, -1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {


			finish();

			com.nuc.smallchampion.util.Assist.transActivity(Link.this,
					MainActivity.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		gameView.setMode(GameView.PAUSE);

	}

	@Override
	protected void onResume() {
		super.onResume();


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gameView.setMode(GameView.QUIT);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.play_btn:
				Animation scaleOut = AnimationUtils.loadAnimation(this,
						R.anim.scale_anim_out);
				Animation transIn = AnimationUtils.loadAnimation(this,
						R.anim.trans_in);

				btnPlay.startAnimation(scaleOut);
				btnPlay.setVisibility(View.GONE);
				imgTitle.setVisibility(View.GONE);
				gameView.setVisibility(View.VISIBLE);

				btnRefresh.setVisibility(View.VISIBLE);
				btnTip.setVisibility(View.VISIBLE);
				progress.setVisibility(View.VISIBLE);
				clock.setVisibility(View.VISIBLE);
				textRefreshNum.setVisibility(View.VISIBLE);
				textTipNum.setVisibility(View.VISIBLE);

				btnRefresh.startAnimation(transIn);
				btnTip.startAnimation(transIn);
				gameView.startAnimation(transIn);

				gameView.startPlay();
				break;
			case R.id.refresh_btn:
				Animation shake01 = AnimationUtils
						.loadAnimation(this, R.anim.shake);
				btnRefresh.startAnimation(shake01);
				gameView.refreshChange();
				break;
			case R.id.tip_btn:
				Animation shake02 = AnimationUtils
						.loadAnimation(this, R.anim.shake);
				btnTip.startAnimation(shake02);
				gameView.autoClear();
				break;
		}
	}

	@Override
	public void onTimer(int leftTime) {
		Log.i("onTimer", leftTime + "");
		progress.setProgress(leftTime);
	}

	@Override
	public void OnStateChanged(int StateMode) {
		switch (StateMode) {
			case GameView.WIN:
				handler.sendEmptyMessage(0);
				break;
			case GameView.LOSE:
				handler.sendEmptyMessage(1);
				break;
			case GameView.PAUSE:

				gameView.stopTimer();
				break;
			case GameView.PLAY:

				gameView.startPlay();

				break;
			case GameView.QUIT:

				gameView.stopTimer();
				break;
		}
	}

	@Override
	public void onRefreshChanged(int count) {
		textRefreshNum.setText("" + gameView.getRefreshNum());
	}

	@Override
	public void onTipChanged(int count) {
		textTipNum.setText("" + gameView.getTipNum());
	}

	public void quit() {
		this.finish();
		com.nuc.smallchampion.util.Assist.transActivity(Link.this,
				MainActivity.class);
	}
}