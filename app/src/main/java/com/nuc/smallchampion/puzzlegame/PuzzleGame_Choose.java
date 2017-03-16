package com.nuc.smallchampion.puzzlegame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nuc.smallchampion.link.Link;
import com.nuc.smallchampion.lovingdraw.Draw;
import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path.FillType;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class PuzzleGame_Choose extends Activity {
	private static final String TAG = "==CrazyIt.org==";
	protected static final String LayoutParams = null;
	private SimpleAdapter saImageItems;
	SoundPool soundPool;
	int click;
	int[] imageIds = new int[] { R.drawable.map105, R.drawable.map106,
			R.drawable.map107, R.drawable.map108, R.drawable.map109,
			R.drawable.map110, R.drawable.map111, R.drawable.map112,
			R.drawable.map114, R.drawable.map115, R.drawable.map116,
			R.drawable.map117, R.drawable.map118, R.drawable.map119, };
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
						"这是游戏“我是小状元”中的之“拼搏人生”篇，很好玩哦！上百度手机助手、豌豆荚或移动mm市场即可下载本应用！");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PuzzleGame_Choose.this.startActivity(Intent.createChooser(intent, "分享"));
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			finish();

			com.nuc.smallchampion.util.Assist.transActivity(PuzzleGame_Choose.this,
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
		setContentView(R.layout.activity_puzzlegame_choose);
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		click = soundPool.load(PuzzleGame_Choose.this, R.raw.click6, 1);
		// 创建一个List对象，List对象的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		// 获取显示图片的ImageSwitcher

		// 创建一个SimpleAdapter

		SimpleAdapter simpleAdapter = new SimpleAdapter(this,
				listItems
				// 使用/layout/cell.xml文件作为界面布局
				, R.layout.activity_cell, new String[] { "image" },
				new int[] { R.id.image1 });
		GridView gridView = new GridView(this);
		gridView.setLayoutParams(new GridView.LayoutParams(500, 500));

		GridView grid = (GridView) findViewById(R.id.grid01);
		// 为GridView设置Adapter
		grid.setAdapter(simpleAdapter);

		// 添加列表项被选中的监听器
		grid.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// 显示当前被选中的图片

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		grid.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 显示当前被选中的图片
				switch (position) {

					case 0:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame.class);

						break;
					case 1:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame1.class);

						break;
					case 2:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame2.class);

						break;
					case 3:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame3.class);

						break;
					case 4:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame4.class);

						break;
					case 5:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame5.class);

						break;
					case 6:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame6.class);

						break;
					case 7:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame7.class);

						break;
					case 8:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame9.class);
						break;
					case 9:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame10.class);

						break;
					case 10:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame11.class);

						break;
					case 11:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame12.class);

						break;
					case 12:
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame13.class);
						break;
					case 13:

						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								PuzzleGame_Choose.this, PuzzleGame14.class);
						break;

				}
			}
		});
	}
}
// 添加列表项被单击的监听器

