package com.nuc.smallchampion.puzzlegame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import com.nuc.smallchampion.mainview.MainActivity;
import com.nuc.smallchampion.object.Configuration;
import com.nuc.smallchampion.puzzlegame.PuzzleGame_Choose;
import com.nuc.smallchampion.util.Assist;
import com.nuc.smallchampion.util.Constant;
import com.nuc.smallchampion.util.DateUtils;
import com.nuc.smallchampion.util.FileUtils;
import com.nuc.smallchampion.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.AlteredCharSequence;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PuzzleGame4 extends Activity {
	ImageView puzzle[] = new ImageView[9];
	int setID[] = new int[] { R.id.puzzle1, R.id.puzzle2, R.id.puzzle3,
			R.id.puzzle4, R.id.puzzle5, R.id.puzzle6, R.id.puzzle7,
			R.id.puzzle8, R.id.puzzle9 };
	int setDraw[] = new int[] { R.drawable.map109_01, R.drawable.map109_02,
			R.drawable.map109_03, R.drawable.map109_04, R.drawable.map109_05,
			R.drawable.map109_06, R.drawable.map109_07, R.drawable.map109_08,
			R.drawable.uzzlekong };
	private ImageView imageView;
	private ArrayList<Integer> a1;
	private MediaPlayer mplayer;
	private MediaPlayer mplayer1;
	private int currentLocation[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private int rightlocation[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private AlertDialog.Builder successDialog;
	private boolean isplaying;
	int click, click1, click3, click2;
	private Button pause;
	private Button pause1;
	private Button first;
	String time = null;
	private Button cancel;
	private long exitTime;
	private boolean play;
	private Timer timer = new Timer();
	private AlertDialog.Builder lostDialog;
	SoundPool soundPool;
	int gameTime = 80;
	private TextView timeTextView;
	private boolean isPlaying;

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
				PuzzleGame4.this.startActivity(Intent.createChooser(intent,
						"分享"));
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_puzzlegame4);
		Toast.makeText(PuzzleGame4.this, "点击图片打乱顺序", Toast.LENGTH_SHORT).show();

		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		click = soundPool.load(PuzzleGame4.this, R.raw.click, 1);
		click1 = soundPool.load(PuzzleGame4.this, R.raw.success, 1);
		click2 = soundPool.load(PuzzleGame4.this, R.raw.click6, 1);
		click3 = soundPool.load(PuzzleGame4.this, R.raw.click1, 1);
		successDialog = createDialog("好棒啊", "接下来你想", R.drawable.success2)
				.setNegativeButton("换一个",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								com.nuc.smallchampion.util.Assist
										.transActivity(PuzzleGame4.this,
												PuzzleGame_Choose.class);
							}

						}).setPositiveButton("返回主界面",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								com.nuc.smallchampion.util.Assist
										.transActivity(PuzzleGame4.this,
												MainActivity.class);
								finish();

							}

						});
		first = (Button) this.findViewById(R.id.first4);
		first.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				soundPool.play(click3, 1, 1, 0, 0, 1);
				first.setVisibility(View.GONE);

			}
		});

		pause = (Button) this.findViewById(R.id.bace4);
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				com.nuc.smallchampion.util.Assist.transActivity(
						PuzzleGame4.this, PuzzleGame4.class);

			}
		});

		findImgBtn();
		a1 = new ArrayList<Integer>();
		// 将0到7放入容器中
		for (int i = 0; i < 8; i++) {
			a1.add(i);
		}
		Collections.shuffle(a1); // 乱序
		for (int i = 0; i < 8; i++) {
			puzzle[i].setBackgroundResource(setDraw[a1.get(i)]);
			currentLocation[i] = a1.get(i);
		}
		// setOnClik();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			com.nuc.smallchampion.util.Assist.transActivity(PuzzleGame4.this,
					PuzzleGame_Choose.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private AlertDialog.Builder createDialog(String title, String message,
											 int imageResource) {
		return new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(imageResource);
	}

	private void stopTimer() {
		// 停止定时器
		this.timer.cancel();
		this.timer = null;
	}

	public void swapImage(int a, int b) {
		puzzle[a].setBackgroundResource(setDraw[currentLocation[b]]);
		puzzle[b].setBackgroundResource(setDraw[currentLocation[a]]);
		int temp = currentLocation[a];
		currentLocation[a] = currentLocation[b];
		currentLocation[b] = temp;
		isEnd();
	}

	private void findImgBtn() {

		for (int i = 0; i < 9; i++) {
			puzzle[i] = (ImageView) findViewById(setID[i]);

			final int flag = i;
			puzzle[flag].setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (flag) {
						case 0:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[1] == 8) {
								swapImage(flag, 1);
								play = true;

							} else if (currentLocation[3] == 8) {
								swapImage(flag, 3);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 1:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[0] == 8) {
								swapImage(flag, 0);
								play = true;

							} else if (currentLocation[2] == 8) {
								swapImage(flag, 2);
								play = true;

							} else if (currentLocation[4] == 8) {
								swapImage(flag, 4);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 2:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[1] == 8) {
								swapImage(flag, 1);
								play = true;

							} else if (currentLocation[5] == 8) {
								swapImage(flag, 5);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 3:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[0] == 8) {
								swapImage(flag, 0);
								play = true;

							} else if (currentLocation[4] == 8) {
								swapImage(flag, 4);
								play = true;

							} else if (currentLocation[6] == 8) {
								swapImage(flag, 6);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 4:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[1] == 8) {
								swapImage(flag, 1);
								play = true;

							} else if (currentLocation[3] == 8) {
								swapImage(flag, 3);
								play = true;

							} else if (currentLocation[5] == 8) {
								swapImage(flag, 5);
								play = true;

							} else if (currentLocation[7] == 8) {
								swapImage(flag, 7);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 5:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[2] == 8) {
								swapImage(flag, 2);
								play = true;

							} else if (currentLocation[4] == 8) {
								swapImage(flag, 4);
								play = true;

							} else if (currentLocation[8] == 8) {
								swapImage(flag, 8);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 6:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[3] == 8) {
								swapImage(flag, 3);
								play = true;

							} else if (currentLocation[7] == 8) {
								swapImage(flag, 7);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 7:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[4] == 8) {
								swapImage(flag, 4);
								play = true;

							} else if (currentLocation[6] == 8) {
								swapImage(flag, 6);
								play = true;

							} else if (currentLocation[8] == 8) {
								swapImage(flag, 8);
								play = true;
							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;
						case 8:
							soundPool.play(click2, 1, 1, 0, 0, 1);
							if (currentLocation[5] == 8) {
								swapImage(flag, 5);
								play = true;

							} else if (currentLocation[7] == 8) {
								swapImage(flag, 7);
								play = true;

							} else {
								Toast.makeText(PuzzleGame4.this, "坚持就是胜利",
										Toast.LENGTH_SHORT).show();
								play = true;
							}
							break;

						default:
							play = true;
							break;
					}
				}
			});
		}

	}

	public void isEnd() {

		for (int i = 0; i < 9; i++) {
			if (currentLocation[i] != rightlocation[i]) {
				return;
			}

		}
		puzzle[8].setBackgroundResource(R.drawable.map109_09);
		soundPool.play(click1, 1, 1, 0, 0, 1);
		successDialog.show();

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}
}
