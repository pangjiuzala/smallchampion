package com.nuc.smallchampion.write;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.nuc.smallchampion.link.Link;
import com.nuc.smallchampion.mainview.Loading_Link;
import com.nuc.smallchampion.mainview.Loading_PuzzleGame_Main;
import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.puzzlegame.PuzzleGame_Choose;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.DONUT)
public class AddGesture6 extends Activity {
	EditText editText;
	GestureOverlayView gestureView;
	GestureLibrary gestureLibrary;
	SoundPool soundPool;
	int click,click1;
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
						"这是游戏“我是小状元”之“挥斥方遒”篇，练习书法So easy！上百度手机助手、豌豆荚或移动mm市场即可下载本应用！");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				AddGesture6.this.startActivity(Intent.createChooser(intent, "分享"));
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

	@TargetApi(Build.VERSION_CODES.DONUT)
	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			finish();
			com.nuc.smallchampion.util.Assist.transActivity(AddGesture6.this,
					MainActivity.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_add6);
		// 获取文本编辑框

		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		click = soundPool.load(AddGesture6.this, R.raw.music, 1);
		click1 = soundPool.load(AddGesture6.this, R.raw.success, 1);

		final TextView text = (TextView) findViewById(R.id.te1);

		editText = (EditText) findViewById(R.id.gesture_name);
		// 获取汉字编辑视图
		gestureView = (GestureOverlayView) findViewById(R.id.gesture);
		// 设置汉字的绘制颜色
		gestureView.setGestureColor(Color.RED);
		// 设置汉字的绘制宽度
		gestureView.setGestureStrokeWidth(10);
		// 为gesture的汉字完成事件绑定事件监听器
		// 为汉字编辑组件绑定事件监听器

		gestureView.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
										   final Gesture gesture) {
				// 加载save.xml界面布局代表的视图
				View saveDialog = getLayoutInflater().inflate(
						R.layout.activity_add_save, null);
				// 获取saveDialog里的show组件
				ImageView imageView = (ImageView) saveDialog
						.findViewById(R.id.show);
				// 获取saveDialog里的gesture_name组件
				final EditText gestureName = (EditText) saveDialog
						.findViewById(R.id.gesture_name);

				// 根据Gesture包含的汉字创建一个位图
				Bitmap bitmap = gesture.toBitmap(128, 128, 10,
						0xFFFF0000);
				imageView.setImageBitmap(bitmap);
				// 使用对话框显示saveDialog组件
				GestureLibrary gestureLib = GestureLibraries
						.fromFile("/sdcard/hanzi");
				// 添加汉字
				gestureLib.addGesture(gestureName.getText().toString(),
						gesture);
				// 保存汉字库
				gestureLib.save();

				gestureLibrary = GestureLibraries
						.fromFile("/sdcard/hanzi");
				if (!gestureLibrary.load()) {
					soundPool.play(click, 1, 1, 0, 0, 1);

				}
				// 获取汉字编辑组件
				ArrayList<Prediction> predictions = gestureLibrary
						.recognize(gesture);
				ArrayList<String> result = new ArrayList<String>();
				// 遍历所有找到的Prediction对象
				for (Prediction pred : predictions) {
					// 只有相似度大于2.0的汉字才会被输出
					if (pred.score > 0.0) {
						result.add("您的分数为" + pred.score);
						soundPool.play(click1, 1, 1, 0, 0, 1);
					}
					else if (pred.score <= 0.0) {

						soundPool.play(click, 1, 1, 0, 0, 1);
					}

				}
				if (result.size() > 0) {
					ArrayAdapter adapter = new ArrayAdapter(
							AddGesture6.this,
							android.R.layout.simple_dropdown_item_1line,
							result.toArray());
					// 使用一个带List的对话框来显示所有匹配的汉字
					new AlertDialog.Builder(AddGesture6.this)
							.setAdapter(adapter, null)
							.setPositiveButton("下一关",
									new OnClickListener() {
										@SuppressLint("NewApi")
										@TargetApi(Build.VERSION_CODES.DONUT)
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 获取指定文件对应的汉字库

											com.nuc.smallchampion.util.Assist
													.transActivity(
															AddGesture6.this,
															AddGesture7.class);

										}
									}).show();
				} else {
					Toast.makeText(AddGesture6.this, "请认真书写！",  Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}
}
