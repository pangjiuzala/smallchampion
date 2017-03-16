package com.nuc.smallchampion.mainview;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import com.nuc.smallchampion.link.SplashscreenActivity;
import com.nuc.smallchampion.lovingdraw.Draw;
import com.nuc.smallchampion.puzzlegame.PuzzleGame;
import com.nuc.smallchampion.puzzlegame.PuzzleGame1;
import com.nuc.smallchampion.puzzlegame.PuzzleGame10;
import com.nuc.smallchampion.puzzlegame.PuzzleGame11;
import com.nuc.smallchampion.puzzlegame.PuzzleGame12;
import com.nuc.smallchampion.puzzlegame.PuzzleGame13;
import com.nuc.smallchampion.puzzlegame.PuzzleGame14;
import com.nuc.smallchampion.puzzlegame.PuzzleGame2;
import com.nuc.smallchampion.puzzlegame.PuzzleGame3;
import com.nuc.smallchampion.puzzlegame.PuzzleGame4;
import com.nuc.smallchampion.puzzlegame.PuzzleGame5;
import com.nuc.smallchampion.puzzlegame.PuzzleGame6;
import com.nuc.smallchampion.puzzlegame.PuzzleGame7;
import com.nuc.smallchampion.puzzlegame.PuzzleGame9;
import com.nuc.smallchampion.puzzlegame.PuzzleGame_Choose;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {
	SoundPool soundPool;
	int click, click1;
	String time = null;
	private boolean isPlaying;
	static final int NOTIFICATION_ID = 0x1123;
	private MediaPlayer mplayer;
	private ImageView imageView;
	private Button imgBtnStart, pause, start, start1, start2, start3, web,
			share, about, google;
	MediaPlayer mediaPlayer;

	private long exitTime;
	int currentImageId = 0;
	private SharedPreferences mSharedPreferences;
	WebView show;
	final int FONT_8 = 0x110;
	final int FONT_10 = 0x111;
	final int FONT_12 = 0x112;
	final int FONT_14 = 0x113;
	private String mInitParams;
	final int FONT_16 = 0x114;

	int[] imageIds = new int[] { R.drawable.game1, R.drawable.game2,
			R.drawable.game3, R.drawable.game4, };


	public void shareScreenshot() {
		time = "rival_" + DateUtils.getTimeNow();
		shot();
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"我正在玩星愿组合开发的小游戏“我是小状元”，赶快支持星愿组合一下吧！上百度手机助手、豌豆荚或移动mm市场即可下载本应用！");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainActivity.this.startActivity(Intent.createChooser(intent,
						"分享"));
			}

		}).start();
	}

	// 分享图标
	public void shareIcon() {

		time = "rival_" + DateUtils.getTimeNow();
		FileUtils.savePic(
				BitmapFactory.decodeResource(getResources(), R.drawable.icon),
				time);
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"我正在玩星愿组合开发的小游戏“我是小状元”，赶快支持星愿组合一下吧！百度搜索“我是小状元 中北大学”即可下载!");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainActivity.this.startActivity(Intent.createChooser(intent,
						"分享"));
			}

		}).start();
	}

	/**
	 * 截屏方法
	 *
	 * @return
	 */
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mplayer.stop();
			new AlertDialog.Builder(this)
					// .setTitle("Message Box")
					.setMessage("您确定要离开我是小状元吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int which) {
									AssetManager am = getAssets();
									AssetFileDescriptor afd = null;
									try {
										afd = am.openFd("music2.mp3");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									mplayer = new MediaPlayer();
									try {
										mplayer.setDataSource(afd.getFileDescriptor(),
												afd.getStartOffset(), afd.getLength());
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									try {
										mplayer.prepare();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									;
									mplayer.start();
									mplayer.setLooping(true);
									isPlaying = true;
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int whichButton) {

									finish();
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onPause() {

		if (isPlaying) {
			mplayer.stop();

		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 取消通知
		notificationManager.cancel(NOTIFICATION_ID);
		mplayer.start();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// -------------向menu中添加字体大小的子菜单-------------

		menu.add(0, FONT_10, 0, "关于");
		menu.add(0, FONT_12, 0, "分享");
		menu.add(0, FONT_14, 0, "静音");
		menu.add(0, FONT_16, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单项被单击后的回调方法
	public boolean onOptionsItemSelected(MenuItem mi) {
		// 判断单击的是哪个菜单项，并针对性的作出响应。
		switch (mi.getItemId()) {
			case FONT_10:

				finish();
				com.nuc.smallchampion.util.Assist.transActivity(MainActivity.this,
						About.class);
				break;

			case FONT_12:

				Intent addIntent = new Intent(
						"com.android.launcher.action.INSTALL_SHORTCUT");
				String title = getResources().getString(R.string.app_name);
				// 加载快捷方式的图标
				Parcelable icon = Intent.ShortcutIconResource.fromContext(
						MainActivity.this, R.drawable.icon);
				// 创建点击快捷方式后操作Intent,该处当点击创建的快捷方式后，再次启动该程序
				Intent myIntent = new Intent(MainActivity.this,
						SplashscreenActivity.class);
				// 设置快捷方式的标题
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
				// 设置快捷方式的图标
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				// 设置快捷方式对应的Intent
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
				// 发送广播添加快捷方式
				sendBroadcast(addIntent);
				shareScreenshot();

				break;
			case FONT_14:

				mplayer.stop();

				break;
			case FONT_16:

				new AlertDialog.Builder(this)
						// .setTitle("Message Box")
						.setMessage("欢迎支持我们，把应用分享给好友吧")
						.setNegativeButton("下次",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {
										finish();

									}
								})
						.setPositiveButton("好的",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int whichButton) {

										shareScreenshot();
										finish();
									}
								}).show();

				break;

		}
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		// 发送通知
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		click = soundPool.load(MainActivity.this, R.raw.click6, 1);

		AssetManager am = getAssets();
		AssetFileDescriptor afd = null;
		try {
			afd = am.openFd("music2.mp3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mplayer = new MediaPlayer();
		try {
			mplayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mplayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		mplayer.start();
		mplayer.setLooping(true);
		isPlaying = true;

		final TextView text = (TextView) findViewById(R.id.textView2);
		final Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.weiyishang);

		final Animation anim1 = AnimationUtils.loadAnimation(this,
				R.anim.xuanzhuan);
		final Animation anim2 = AnimationUtils.loadAnimation(this,
				R.anim.weiyiyou);

		final Gallery gallery = (Gallery) findViewById(R.id.gallery);

		// 获取显示图片的ImageSwitcher对象 final ImageView show = (ImageView)
		// findViewById(R.id.imageview);
		final Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 如果该消息是本程序所发送的
				if (msg.what == 0x1233) {
					// 动态地修改所显示的图片

					text.startAnimation(anim2);
					if (currentImageId >= imageIds.length) {
						currentImageId = 0;
					}
				}
			}
		};
		// 定义一个计时器，让该计时器周期性地执行指定任务
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// 新启动的线程无法访问该Activity里的组件
				// 所以需要通过Handler发送信息
				Message msg = new Message();
				msg.what = 0x1233;
				// 发送消息
				myHandler.sendMessage(msg);
			}
		}, 0, 2000);

		// 创建一个BaseAdapter对象，该对象负责提供Gallery所显示的图片
		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return imageIds.length;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			// 该方法的返回的View就是代表了每个列表项
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// 创建一个ImageView
				ImageView imageView = new ImageView(MainActivity.this);
				imageView
						.setImageResource(imageIds[position % imageIds.length]);
				// 设置ImageView的缩放类型
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(
						Gallery.LayoutParams.FILL_PARENT,
						Gallery.LayoutParams.FILL_PARENT));
				TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
				imageView.setBackgroundResource(typedArray.getResourceId(
						R.styleable.Gallery_android_galleryItemBackground, 0));
				return imageView;
			}
		};
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			// 当Gallery选中项发生改变时触发该方法
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				soundPool.play(click1, 1, 1, 0, 0, 1);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 显示当前被选中的图片
				switch (position) {

					case 0:
						finish();
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								MainActivity.this, Loading_Draw.class);

						break;
					case 1:
						finish();
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								MainActivity.this, Loading_PuzzleGame_Main.class);

						break;
					case 2:
						finish();
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								MainActivity.this, Loading_Link.class);
						break;
					case 3:
						finish();
						soundPool.play(click, 1, 1, 0, 0, 1);
						com.nuc.smallchampion.util.Assist.transActivity(
								MainActivity.this, Loading_AddGesture.class);
						break;

				}
			}
		});
	}
}
