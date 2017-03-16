package com.nuc.smallchampion.lovingdraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.nuc.smallchampion.link.Link;
import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.util.Assist;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.nuc.smallchampion.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Draw<DrawActivity> extends Activity {
	/** Called when the activity is first created. */
	MyAbsolutelayout layout;
	Rect mr;
	private Button start;
	String time = null;
	ImageButton eraserButton;
	ImageButton eraser_big_Button;
	ImageButton lineButton;
	ImageButton saveButton;
	ImageButton saveButton2;
	private Gallery myGallery;
	private Spinner spinner1;
	private long exitTime;
	private boolean isPlaying, istouching, flag0, flag1, flag2, flag3, flag4,
			flag5, flag6, flag7;
	private MediaPlayer mplayer;
	int click_count = 1;
	String newPath;
	SoundPool soundPool;
	int click;

	int pic = 0;
	int[] imageIds = new int[] { R.drawable.next3, R.drawable.s1,
			R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5,
			R.drawable.next0, R.drawable.s6, R.drawable.s7, R.drawable.s8,
			R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12,
			R.drawable.next0, R.drawable.s13, R.drawable.s14, R.drawable.s15,
			R.drawable.s16, R.drawable.s17, R.drawable.s18, R.drawable.s19,
			R.drawable.next0, R.drawable.next0, R.drawable.s33, R.drawable.s34,
			R.drawable.s35, R.drawable.s37, R.drawable.s38, R.drawable.s39,
			R.drawable.next1, };
	int currentImageId = 0;
	private SimpleAdapter saImageItems;
	ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
	private int[] mColor = { R.drawable.chose_color, R.drawable.color01,
			R.drawable.color02, R.drawable.color03, R.drawable.color04,
			R.drawable.color05, R.drawable.color06 };
	public static int color;

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
						"这是我正在玩星愿组合开发的小游戏“我是小状元”之”水墨丹青“篇，学画画很容易，上百度手机助手、豌豆荚或移动mm市场即可下载本应用！");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Draw.this.startActivity(Intent.createChooser(intent, "分享"));
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
			mplayer.stop();
			com.nuc.smallchampion.util.Assist.transActivity(Draw.this,
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
		setContentView(R.layout.activity_draw);

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

		layout = (MyAbsolutelayout) this.findViewById(R.id.layout);
		eraserButton = (ImageButton) findViewById(R.id.eraser);
		eraserButton.setVisibility(View.GONE);
		eraser_big_Button = (ImageButton) findViewById(R.id.eraser_big);
		lineButton = (ImageButton) findViewById(R.id.line);

		changeToPicture();
		clickButton();// 各个按钮监听器
		final Animation shake = AnimationUtils
				.loadAnimation(this, R.anim.shake);
		Toast toast = Toast.makeText(this, "请先选择画笔", Toast.LENGTH_SHORT);
		toast.show();
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		for (int i = 0; i <= 6; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mColor[i]);// 添加图像资源的ID
			map.put("ItemText", "画笔" + (i));// 按序号做ItemText
			lstImageItem.add(map);
		}
		saImageItems = new SimpleAdapter(Draw.this, lstImageItem,// 数据来源
				R.layout.activity_drawmenu, new String[] { "ItemText",
				"ItemImage" }, new int[] { R.id.myItemText,
				R.id.ItemImage });
		spinner1.setAdapter(saImageItems);
		spinner1.startAnimation(shake);
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub

				MyAbsolutelayout.mPaint.setStrokeWidth(1);// 设置画笔初始大小
				switch (position) {
					case 0:
						MyAbsolutelayout.mPaint.setColor(0);
						flag0 = true;
						break;
					case 1:
						MyAbsolutelayout.mPaint.setColor(0xFF000000);
						flag1 = true;
						break;
					case 2:
						MyAbsolutelayout.mPaint.setColor(0xFF7CFC00);
						flag2 = true;
						break;
					case 3:
						MyAbsolutelayout.mPaint.setColor(0xFFFF0000);
						flag3 = true;
						break;
					case 4:
						MyAbsolutelayout.mPaint.setColor(0xFF00ffff);
						flag4 = true;
						break;
					case 5:
						MyAbsolutelayout.mPaint.setColor(0xFFFFFF00);
						flag5 = true;
						break;
					case 6:
						MyAbsolutelayout.mPaint.setColor(0xFF458B74);
						flag6 = true;
						break;
					case 7:
						MyAbsolutelayout.mPaint.setColor(0xFF330088);
						flag7 = true;
						break;
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		final Gallery gallery = (Gallery) findViewById(R.id.gallery);

		// 获取显示图片的ImageSwitcher对象 final ImageView show = (ImageView)
		// findViewById(R.id.imageview);

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
				ImageView imageView = new ImageView(Draw.this);
				imageView
						.setImageResource(imageIds[position % imageIds.length]);
				// 设置ImageView的缩放类型
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				return imageView;
			}
		};
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			// 当Gallery选中项发生改变时触发该方法
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {

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

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		mplayer.stop();

		super.onPause();
	}

	// 保存状态
	protected void saveActivityPreferences() {
		SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor01 = activityPreferences.edit();
		editor01.putInt("saveImage", pic);
		editor01.commit();
	}

	public void clickButton() {
		final Animation scaleOut = AnimationUtils.loadAnimation(this,
				R.anim.scale_anim_out);
		@SuppressWarnings("unused")
		final Animation transIn = AnimationUtils.loadAnimation(this,
				R.anim.trans_in);
		eraserButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				istouching = true;

				eraserButton.setVisibility(View.GONE);
				eraser_big_Button.setVisibility(View.VISIBLE);
				eraser_big_Button.startAnimation(scaleOut);
				spinner1.startAnimation(scaleOut);
				MyAbsolutelayout.mPaint.setColor(0xFFFFFFFF);
				MyAbsolutelayout.mPaint.setStrokeWidth(30);

			}

		});

		eraser_big_Button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				istouching = true;

				MyAbsolutelayout.mPaint.setColor(0xFFFFFFFF);
				MyAbsolutelayout.mPaint.setStrokeWidth(30);
				eraser_big_Button.setVisibility(View.GONE);
				eraserButton.setVisibility(View.VISIBLE);
				eraserButton.startAnimation(scaleOut);
				Toast.makeText(Draw.this, "谨慎改错，再按一次！", Toast.LENGTH_SHORT)
						.show();
				if (flag0 || flag1 || flag2 || flag3 || flag4 || flag5 || flag6
						|| flag7) {
					spinner1.setAdapter(saImageItems);
					spinner1.startAnimation(transIn);
				}
				;

			}

		});
		lineButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				click_count++;
				if (click_count > 10) {
					click_count = 1;
				}
				MyAbsolutelayout.mPaint.setStrokeWidth(click_count);
				Toast.makeText(Draw.this, "画笔大小为" + click_count + "px",
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		int x = (int) e.getX();
		int y = (int) e.getY() - 50;
		return super.onTouchEvent(e);
	}

	private void changeToPicture() {
		mr = layout.changeToPicture();
	}

	public void saveMyBitmap(String bitName, int percent) throws IOException {
		Bitmap bmp = MyAbsolutelayout.mBitmap;// 这里的drawable2Bitmap方法是我把ImageView中
		// 的drawable转化成bitmap，当然实验的时候可以自己创建bitmap
		File f = new File("/mnt/sdcard/" + bitName + ".jpg");// 保存路径及名字
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(Bitmap.CompressFormat.PNG, percent, fOut);// 这里选择PNG，为JPEG时背景为黑色。。。
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
