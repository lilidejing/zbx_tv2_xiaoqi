package com.baidu.cyberplayersdksample1_xiaoqi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.EncodingUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayer.sdk.BVideoView;
import com.baidu.cyberplayer.sdk.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.sdk.BMediaController;


import com.csw.download.gengxin_tv_res;
import com.csw.programentity.ChannelTypeInfo;
import com.csw.programentity.ProgramInfo;
import com.csw.sax.HttpDownloader;
import com.csw.sax.XmlPaser;
import com.csw.tv.jl_tv;
import com.csw.tv.main_welcome;
import com.csw.tv.jl_tv.MovieInfo;

import com.szy.update.update_main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VideoViewPlayingActivity extends Activity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnClickListener {

	private final String TAG = "VideoViewPlayingActivity";
	// /////////////////////////////////////////////////////////////////////////////
	private static final int GRIDVIEW_COUNT = 1;
	private List<MyAdapter> mGridViewAdapters = new ArrayList<VideoViewPlayingActivity.MyAdapter>();
	private ViewPager mViewPager;
	private List<View> mAllViews = new ArrayList<View>();
	private MyViewPagerAdapter myViewPagerAdapter;

	// ////////////////////////////////
	// ///////////////////////////////////////////////
	TextView jiemu_text = null;

	private String mVideoSource = null;

	private BVideoView mVV = null;
	private BMediaController mVVCtl = null;
	private RelativeLayout mViewHolder = null;
	private LinearLayout mControllerHolder = null;
	private GridView myListView;

	private boolean mIsHwDecode = false;

	private final int UI_EVENT_PLAY = 0;
	private final int UI_TV_WEISHI = 1;
	private final int UI_TV_MENU_yincang = 2;
	private final int UI_UPDATE = 3;
	private int DOWN = 0;
	private int UP = 1;

	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "VideoViewPlayingActivity";

	// 播放状�?
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	private int mLastPos = 0;
	// 记录当前playlist的播放记�?	
	private int Cur_url_num = 0;
	// 菜单栏，是否隐藏�?
	private int TV_menu_is_yincang = 0;
	
	private View.OnClickListener mPreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG, "pre btn clicked");
		}
	};

	private View.OnClickListener mNextListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG, "next btn clicked");
		}
	};

	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UI_EVENT_PLAY:
				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				if (mLastPos != 0) {
					// 如果有记录播放位�?先seek到想要播放的位置
					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}
				// 设置播放�?			
				mVV.setVideoPath(mVideoSource);
				// �?��播放
				mVV.start();
				// 隐藏控制�?			
				mVVCtl.hide();

				break;
			// 更新卫视频道的资�?		
				case UI_TV_WEISHI:
				playList.clear();
				TV_menu_parse(playList, 0);
				Adapter.notifyDataSetChanged();
				break;

			case UI_TV_MENU_yincang:

				if (is_yincang) {
					ctrl_tv_menu_vis.setVisibility(View.GONE);
					TV_menu_is_yincang = 1;
					is_yincang = false;
				}
				break;
			case UI_UPDATE:
				update.update();
				break;
			default:
				break;
			}
		}
	};

	void init_run() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 从网络获取更新列表，然后更新
				gengxin_tv_res.gengxin_tv_res_init();
				// 初始化列表为卫视栏目
				mUIHandler.sendEmptyMessage(UI_TV_WEISHI);
				// 更新服务
				mUIHandler.sendEmptyMessage(UI_UPDATE);
			}
		}).start();
	}

	
	 update_main update = new update_main(this);
		
		private void ui_update() {
			Thread thread = new Thread(runnableup);
			thread.start();
		}
		Runnable runnableup = new Runnable() {
			public void run() {
				try {
					Thread.sleep(10);
					update.update();
					// mUIHandler.sendEmptyMessage(UPDATE_VER);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.controllerplaying);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
		Uri uriPath = getIntent().getData();
		if (null != uriPath) {
			String scheme = uriPath.getScheme();
			if (null != scheme
					&& (scheme.equals("http") || scheme.equals("https") || scheme
							.equals("rtsp"))) {
				mVideoSource = uriPath.toString();
			} else {
				mVideoSource = uriPath.getPath();
			}
		}
		jiemu_text = (TextView) this.findViewById(R.id.num);// 得到显示节目号的控件
		ui_update();
		initUI();

		/*
		 * ctrl_tv_init();
		 * 
		 * loadViews();// 加载目录数据 init_run();
		 */
		// init_run_is_yincang();

		InitDataTask initDataTask = new InitDataTask(
				VideoViewPlayingActivity.this);
		initDataTask.execute();
	}

	/**
	 * 初始化界�?	 */
	private void initUI() {
		mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
		mControllerHolder = (LinearLayout) findViewById(R.id.controller_holder);
		// 创建BVideoView和BMediaController
		mVV = new BVideoView(this);
		mVVCtl = new BMediaController(this);
		mViewHolder.addView(mVV);
		mControllerHolder.addView(mVVCtl);

		// 注册listener
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);
		mVVCtl.setPreNextListener(mPreListener, mNextListener);

		// 关联BMediaController
		mVV.setMediaController(mVVCtl);
		// 设置解码模式
		// 设置软件解码，有问题。看来只能调用硬件解码了�?	
		mVV.setDecodeMode(BVideoView.DECODE_HW);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");
		// 在停止播放前 你可以先记录当前播放的位�?以便以后可以续播
		if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		// 发起�?��播放任务,当然您不�?��要在这发�?	
		mUIHandler.sendEmptyMessage(UI_EVENT_PLAY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public boolean onInfo(int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onError");
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

		// Toast.makeText(this, "抱歉！请重新选台，频道维护中…�?", Toast.LENGTH_LONG).show();
		// //播放出错，弹出菜�?		// ctrl_tv_menu_vis.setVisibility(View.VISIBLE);
		// TV_menu_is_yincang = 0;

		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onCompletion");
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	private void loadViews() {
		int pageCount = (playList1.size() + GRIDVIEW_COUNT - 1)
				/ GRIDVIEW_COUNT;
		Log.d(TAG, "pageCount: " + pageCount);
		Log.d("vod_mlistAppInfo", "" + playList1.size());
		mGridViewAdapters.clear();
		mAllViews.clear();
		// if (pageCount==1) {
		// for (int j = 0; j < 5; j++) {
		// vod_mlistAppInfo.remove(j);
		// }
		// }else if(i==1){
		// for(int j=0;j<i*5;j++){
		// vod_mlistAppInfo.remove(j);
		// }
		// }
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < pageCount; i++) {
			View mView = inflater.inflate(R.layout.layout_gridview, null);
			GridView mGridView = (GridView) mView.findViewById(R.id.grid_up);

			MyAdapter adapter = new MyAdapter(VideoViewPlayingActivity.this, i,
					playList1);
			mGridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mGridViewAdapters.add(adapter);
			mAllViews.add(mView);

			// 控制数据
			// if (i == 0) {
			// for (int j = 0; j < 5; j++) {
			// vod_mlistAppInfo.remove(j);
			// }
			// }else if(i==1){
			// for(int j=0;j<i*5;j++){
			// vod_mlistAppInfo.remove(j);
			// }
			// }

		}
		mViewPager = (ViewPager) findViewById(R.id.viewpager1);
		myViewPagerAdapter = new MyViewPagerAdapter();
		mViewPager.setAdapter(myViewPagerAdapter);
		myViewPagerAdapter.notifyDataSetChanged();
		mViewPager.setFocusable(true);
		mViewPager.requestFocus();
		DOWN = 1;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				DOWN = 1;

				press_tv_menu(playList, arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mAllViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			mViewPager.removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mAllViews.get(position),
					new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT));
			return mAllViews.get(position);

		}
	}

	private class MyAdapter extends BaseAdapter {

		private Context mContext;
		private int pagePosition;
		private LayoutInflater mInflater;
		private List<MovieInfo> mlistAppInfo = null;

		private MyAdapter(Context context, int pagePosition,
				List<MovieInfo> apps) {
			this.mContext = context;
			this.pagePosition = pagePosition;

			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mlistAppInfo = apps;
			// if(pagePosition>0){
			// for(int i=0;i<pagePosition*5;i++){
			// apps.remove(i);
			// }
			// }

		}

		@Override
		public int getCount() {
			int size = (mlistAppInfo == null ? 0 : playList1.size()
					- GRIDVIEW_COUNT * pagePosition);
			Log.d(TAG, "size: " + size);
			if (size > GRIDVIEW_COUNT) {
				return GRIDVIEW_COUNT;
			} else {
				return size > 0 ? size : 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return mlistAppInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int nowPosition = GRIDVIEW_COUNT * pagePosition + position;
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.page_item, null);

				holder = new ViewHolder();

				holder.name = (TextView) convertView
						.findViewById(R.id.page_text);
				holder.layout = (RelativeLayout) convertView
						.findViewById(R.id.r_image_item);

				convertView.setTag(holder);
			}
			MovieInfo appInfo = (MovieInfo) getItem(nowPosition);
			holder.name.setText(appInfo.displayName);
			holder.layout.setTag(nowPosition);
			holder.layout.setOnClickListener(VideoViewPlayingActivity.this);
			// holder.name.setText("这是第_" + nowPosition);
			return convertView;
		}
	}

	static class ViewHolder {
		RelativeLayout layout;
		ImageView icon;
		TextView name;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	EditText mTextUri = null;
	Button mButtonPlay = null;

	private static int height, width;
	private LinkedList<MovieInfo> mLinkedList;
	private LayoutInflater mInflater;
	private LayoutInflater mInflater1;
	View root;
	private EditText urlInput;
	// String FILE_NAME = "/TvRes.tv";///改动的地�?
	String FILE_NAME = "/TvRes.txt";// /改动的地�?	
	String fileName = "TvRes.txt"; // 文件名字

	private static String shoucangFILE_NAME = "/shoucang.txt";// 收藏文件那个名字.................................................
	private static String shoucangFile_Name = "shoucang.txt";// 收藏文件那个名字...................................................
	// String fileName = "TvCommon.txt"; //使用read之前，改变fileName的�?

	String res = ""; // 总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里
	String udate_date = "201206082334";// 更新时间，这个将作为判断网络更新的依�?
	RelativeLayout ctrl_tv_menu_vis = null;

	// 得到手机里的视频文件存放在该列表�?
	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();
	public static LinkedList<MovieInfo> playList1 = new LinkedList<MovieInfo>();

	BaseAdapter Adapter;
	BaseAdapter Adapter1;

	public class MovieInfo {
		String displayName = "";
		String path = "";
	}

	void shoucang_toast_help() {
		Toast toast = Toast.makeText(this, "请按“菜单”键，进行收藏", Toast.LENGTH_SHORT);
		toast.show();
		Toast toast1 = Toast.makeText(this, "请按“X”取消收", Toast.LENGTH_SHORT);
		toast1.show();
	}

	public static int jiemu_num = 0;// 节目�?	
	public static int jiemu_num2 = 0;// 节目名称前面的标号，只起标识作用

	void ctrl_tv_init() {
		// setContentView(R.layout.jl_tv_dialog);

		// MyAutoUpdate update = new MyAutoUpdate(this);

		// update.check();
		ctrl_tv_menu_vis = (RelativeLayout) findViewById(R.id.ctrl_tv_menu);

		TV_leixing(playList1);
		playList.clear();
		TV_menu_parse(playList, 0);

		mLinkedList = playList;

		mInflater = getLayoutInflater();
		mInflater1 = getLayoutInflater();

		myListView = (GridView) findViewById(R.id.grid);
		Adapter = new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mLinkedList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.list, null);
				}
				// TextView text = (TextView)
				// convertView.findViewById(R.id.text);
				// text.setText(mLinkedList.get(arg0).displayName);
				TextView text = (TextView) convertView.findViewById(R.id.text);
				jiemu_num2 = arg0 + 1;
				text.setText(jiemu_num2 + "  "
						+ mLinkedList.get(arg0).displayName);
				String str = (jiemu_num2 + mLinkedList.get(arg0).displayName);

				System.out.println(str);
				return convertView;
			}

		};
		myListView.setAdapter(Adapter);

		// 视频列表监听
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DOWN = 1;// 下次还可以向下翻�?			
				Cur_url_num = arg2;
				String uri = playList.get(arg2).path;
				String name = playList.get(arg2).displayName;
				if (name == "" || name == null)
					return;

				if ((uri != null) | (name.charAt(0) == 'X')) {
					if (name.charAt(0) == 'X') {
						delete_shoucang(arg2, playList);
						Adapter.notifyDataSetChanged();
					} else {
						if (uri.length() > 0) {

							// 菜单界面消失
							ctrl_tv_menu_vis.setVisibility(View.GONE);
							TV_menu_is_yincang = 1;
							play_url(uri);

							String str3 = String.valueOf(arg2 + 1);// 点击界面列表时显示数字键
							jiemu_text.setText(str3);
							hideNum();
							// //获取�?��播放地址
							// mVideoSource = uri;
							// mVV.stopPlayback();
							// if (null != mWakeLock && (!mWakeLock.isHeld())) {
							// mWakeLock.acquire();
							// }
							// //发起�?��播放任务,当然您不�?��要在这发�?	
							// mUIHandler.sendEmptyMessage(UI_EVENT_PLAY);
							//
							// app_write(uri,"cur_uri");

						}
					}
				}
			}
		});
		myListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				UP = arg2;
				shoucangNum = arg2;// 让收藏号==这个选中的索引，以便收藏
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		myListView.requestFocus();
	}

	void press_tv_menu(final LinkedList<MovieInfo> list, int position) {
		playList.clear();
		TV_menu_parse(list, position);
		Adapter.notifyDataSetChanged();
	}

	// 读取指定SD卡文件内�?
	private String read() {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权�?			
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(
						sdCardDir.getCanonicalPath() + fileName);// 改动的地�?				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ..........................................................................
	// 读取指定SD卡收藏文件内�?	
  private String readShoucang() {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权�?	
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(
						sdCardDir.getCanonicalPath() + "/shoucang.txt");// 改动的地�?		
				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ........................................................................................................
	// 写入指定sd卡文件内�?	
  private void write(String content) {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权�?		
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath()
						+ FILE_NAME);//
				targetFile.delete();// 删除旧文件，重新写入新东�?				// 以指定文件创�?RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件记录指针移动到�?��
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.write(content.getBytes());
				raf.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 电视台栏目解�?	
	private void TV_leixing(final LinkedList<MovieInfo> list) {
		/*
		 * String str[] = { "我的收藏", "高清频道", "央视频道", "卫视频道", "港台频道", "体育频道",
		 * "    北京   ", "    上海   ", "    天津   ", "    福建   ", "    江苏   ",
		 * "    广东   ", "    重庆   ", "    湖北   ", "    海南   ", "    河北   ",
		 * "    山西   ", "    山东   ", "    陕西   ", "    四川   ", "    安徽   ",
		 * "  黑龙�?  ", "    广西   ", "    辽宁   ", "    湖南   ", "    吉林   ",
		 * "    云南   ", "    江西   ", "    河南   ", "    新疆   ", "    贵州  ", };
		 */
		list.clear();
		for (int i = 0; i < channelTypeInfosList.size(); i++) {
			MovieInfo mi = new MovieInfo();
			mi.displayName = channelTypeInfosList.get(i).getChannelTypeName();
			mi.path = null;
			list.add(mi);
		}
	}

	// 删除收藏
		private void delete_shoucang(int arg, final LinkedList<MovieInfo> list) {
			String res = ""; // 总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里

			// 清除掉之前的列表
			// list.clear();
			// 文件不存在或者sd卡不存在，返回null
			FILE_NAME = "/shoucang.txt";
			res = read();

			if (res == null) {
				return;// sd卡不存在，退出
			}

			String shoucang_start = "**";
			String shoucang_end = "##";

			int count = arg / 2;

			String shoucang_start1 = shoucang_start + count;
			String shoucang_end1 = shoucang_end + count;

			int yangshi_index = res.indexOf(shoucang_start1);
			int yangshi_index2 = res.indexOf(shoucang_end1);
			if (yangshi_index2 - yangshi_index == 3) {
				return;// 该格子无收藏
			} else {
				String s1 = res.substring(0, yangshi_index + 3);
				String s2 = res.substring(yangshi_index2, res.length());
				String shoucang_res = s1 + s2;
				write(shoucang_res);
				list.clear();
				TV_shoucang_parse(list);// 调用这个，更链表

			}

		}

	// 解析收藏TV_shoucang_parse
	private void TV_shoucang_parse(final LinkedList<MovieInfo> list) {
		String fileName = "shoucang.txt"; // 文件名字
		String res = ""; // 总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里

		// 具体解析电视台的标识
		String res_start = "&&";
		String res_end = "@@";
		String res_url = "url--";
		int res_index1;
		int res_index2;
		int tv_name_index;
		String Tv_name;
		String Tv_url;

		// 清除掉之前的列表
		list.clear();
		// 文件不存在或者sd卡不存在，返回null

		FILE_NAME = "/shoucang.txt";
		// res = read();
		res = readShoucang();// 让它读取SD卡里面的内容
		if (res == null) {
			// 读取assets里的文件
			try {
				// \assets\TvRes.txt这里有这样的文件存在
				InputStream in = getResources().getAssets().open(fileName);
				int length = in.available();
				byte[] buffer = new byte[length];
				in.read(buffer);
				// 读取的文件，在pc上面被创建的时�?，记得转�?UTF-8"
				res = EncodingUtils.getString(buffer, "UTF-8");
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					write(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String shoucang_start = "**";
		String shoucang_end = "##";

		for (int count = 0; count < 9; count++) {
			String shoucang_start1 = shoucang_start + count;
			String shoucang_end1 = shoucang_end + count;

			int yangshi_index = res.indexOf(shoucang_start1);
			int yangshi_index2 = res.indexOf(shoucang_end1);
			String yangshi_res = res.substring(yangshi_index, yangshi_index2);
			if (yangshi_index2 - yangshi_index == 3) {

				MovieInfo mi = new MovieInfo();
				mi.displayName = "         ";
				mi.path = null;
				list.add(mi);

				// 删除操作
				MovieInfo mi1 = new MovieInfo();
				mi1.displayName = "";
				mi1.path = null;
				list.add(mi1);
			} else {

				res_index1 = yangshi_res.indexOf(res_start);
				tv_name_index = yangshi_res.indexOf(res_url);
				res_index2 = yangshi_res.indexOf(res_end);
				if (res_index2 == -1)
					break;// 说明电视台资源已经不存在了，跳出
				Tv_name = yangshi_res.substring(res_index1 + 2, tv_name_index);
				Tv_url = yangshi_res.substring(tv_name_index + 5, res_index2);
				MovieInfo mi = new MovieInfo();
				mi.displayName = Tv_name;
				mi.path = Tv_url;
				list.add(mi);

				// 删除操作
				MovieInfo mi1 = new MovieInfo();
				mi1.displayName = "X";
				mi1.path = "";
				list.add(mi1);

			}

		}

	}

	/*
	 * 节目资源解析 卫视频道标示�?##weishi-start ##weishi-end
	 */
	private void TV_menu_parse(final LinkedList<MovieInfo> list, int position) {
		// String weishi_start = "##weishi-start";
		// String weishi_end = "##weishi-end";

		// 具体解析电视台的标识
		/*
		 * String res_start = "&&"; String res_end = "@@"; String res_url =
		 * "url--"; int res_index1; int res_index2; int tv_name_index; String
		 * Tv_name; String Tv_url;
		 * 
		 * // FILE_NAME = "/TvRes.tv"; FILE_NAME = "/TvRes.txt"; //
		 * 文件不存在或者sd卡不存在，返回null res = read();
		 * 
		 * if (res == null || res == "") { // 读取assets里的文件 try { //
		 * \assets\TvRes.txt这里有这样的文件存在 InputStream in =
		 * getResources().getAssets().open(fileName); int length =
		 * in.available(); byte[] buffer = new byte[length]; in.read(buffer);
		 * res = EncodingUtils.getString(buffer, "UTF-8"); if
		 * (Environment.getExternalStorageState().equals(
		 * Environment.MEDIA_MOUNTED)) { write(res); } } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * // 卫视频道解析 int yangshi_index = res.indexOf(weishi_start); int
		 * yangshi_index2 = res.indexOf(weishi_end); String yangshi_res =
		 * res.substring(yangshi_index, yangshi_index2);
		 */

		/*
		 * for (int i = 0; i < yangshi_index2 - yangshi_index;) { res_index1 =
		 * yangshi_res.indexOf(res_start); tv_name_index =
		 * yangshi_res.indexOf(res_url); res_index2 =
		 * yangshi_res.indexOf(res_end); if (res_index2 == -1) break;//
		 * 说明电视台资源已经不存在了，跳出 Tv_name = yangshi_res.substring(res_index1 + 2,
		 * tv_name_index); Tv_url = yangshi_res.substring(tv_name_index + 5,
		 * res_index2);
		 * 
		 * yangshi_res = yangshi_res.substring(res_index2 + 2,
		 * yangshi_res.length());
		 * 
		 * MovieInfo mi = new MovieInfo(); mi.displayName = Tv_name; mi.path =
		 * Tv_url; list.add(mi); i = i + (res_index2 - res_index1);//
		 * 跳过@@，开始搜索下�?��资源
		 * 
		 * }
		 */
		List<ProgramInfo> programInfoList = channelTypeInfosList.get(position)
				.getProgramInfoList();
		for (int i = 0; i < programInfoList.size(); i++) {

			MovieInfo mi = new MovieInfo();
			mi.displayName = programInfoList.get(i).getProgramName();
			mi.path = programInfoList.get(i).getProgramUrl();
			list.add(mi);
		}

	}

	// 放横幅的image，不过这个函数，可以适用�?��的imagebuttom放图�?	
	private void imagebutton_is_huangse(int id, int id2) {
		final ImageView image_shoucang = (ImageView) findViewById(id);
		// BitmapDrawable bitmapDrawable = (BitmapDrawable) image_shoucang
		// .getDrawable();
		// 就是因为这个回收的问题，造成了我的系统崩溃�?我还不明白为�?���?		// 搞好项目之后，好好�?结�?
		/*
		 * 
		 * //如果图片还未回收，先强制回收该图�?
		 * if (!bitmapDrawable.getBitmap().isRecycled()) {
		 * bitmapDrawable.getBitmap().recycle(); }
		 */
		// 改变ImageView显示的图�?	
		image_shoucang.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), id2));
	}

	// 按键处理时间,当按下返回按键时的处理方�?	
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	/*
	 * 播放入口，需要停止之前的播放，开启新的播放接�?	 */
	void play_url(String uri) {
		if (uri == null)
			return;
		// 获取�?��播放地址
		mVideoSource = uri;
		mVV.stopPlayback();
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		// 发起�?��播放任务,当然您不�?��要在这发�?	
		mUIHandler.sendEmptyMessage(UI_EVENT_PLAY);

		app_write(uri, "cur_uri");
	}

	// //电视状�?栏隐藏定时器
	// private static Boolean hasTask_yincang = false;
	// Timer tExit_yincang = new Timer();
	// TimerTask yincang_task = new TimerTask()
	// {
	// @Override
	// public void run()
	// {
	// is_yincang = true;
	// hasTask_yincang = true;
	// }
	// };
	// //电视状�?栏显示定时器
	// Timer tExit_xianshi = new Timer();
	// TimerTask xianshi_task = new TimerTask()
	// {
	// @Override
	// public void run()
	// {
	// //显示
	//
	// //5s后，关闭
	// hasTask_yincang = true;
	// }
	// };

	private static Boolean is_yincang = true;
	private static Boolean is_dongzuo = false;

	void init_run_is_yincang() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				// while(true)
				// {
				try {
					Thread.sleep(4500);

					if (!is_dongzuo)
						mUIHandler.sendEmptyMessage(UI_TV_MENU_yincang);
					// else
					// {
					// Thread.sleep(3000);
					// is_dongzuo = false;
					// }

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// }
			}
		}).start();
	}

	private static int shoucangNum;// 收藏号^_^

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 5s钟之后隐藏主菜单
		is_dongzuo = true;

		// is_yincang = false;

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			String url = playList.get(shoucangNum).path;// 改动的地�?		
			System.out.println(url);
			if (url == null) {
				return true;
			}
			com.csw.tv.wr_file file = new com.csw.tv.wr_file();
			file.write_shoucang(url);
			if (file.flagH == 1) {
				Toast toast = Toast.makeText(this, "收藏菜单已满，请清除",
						Toast.LENGTH_LONG);
			} else {

				Toast toast = Toast.makeText(this,
						playList.get(shoucangNum).displayName + "，已被收藏！",
						Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
			// super.openOptionsMenu(); // 调用这个，就可以弹出菜单
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			// is_dongzuo = false;
			// is_yincang = true;
			ctrl_tv_menu_vis.setVisibility(View.VISIBLE);
			TV_menu_is_yincang = 0;
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

			if (DOWN == 1) {
				myListView.setFocusable(true);
				myListView.requestFocus();
				DOWN = 0;
				UP = 0;
				return true;
			}

			if (TV_menu_is_yincang == 0)
				return true;
			if (Cur_url_num == playList.size() - 1) {
				Cur_url_num = 0;
			} else
				Cur_url_num++;
			String url = playList.get(Cur_url_num).path;
			if (url == null) {// 为了应付我的收藏里的x栏目�?		
				for (int i = 0; i < playList.size(); i++) {
					if (Cur_url_num == playList.size() - 1) {
						Cur_url_num = 0;
					} else
						Cur_url_num++;
					url = playList.get(Cur_url_num).path;
					if (url != null)
						break;
				}

			}
			play_url(url);
			String strDown = String.valueOf(Cur_url_num + 1);// 按下显示的数字键
			jiemu_text.setText(strDown);
			hideNum();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (UP == 0) {
				mViewPager.setFocusable(true);
				mViewPager.requestFocus();
				DOWN = 1;
				return true;
			}
			if (TV_menu_is_yincang == 0)
				return true;
			if (Cur_url_num == 0) {
				Cur_url_num = playList.size() - 1;
			} else
				Cur_url_num--;
			String url = playList.get(Cur_url_num).path;
			if (url == null) {// 为了应付我的收藏里的x栏目�?			
				for (int i = 0; i < playList.size(); i++) {
			
					if (Cur_url_num == 0) {
						Cur_url_num = playList.size() - 1;
					} else
						Cur_url_num--;
					url = playList.get(Cur_url_num).path;
					if (url != null)
						break;
				}

			}
			play_url(url);
			String strUp = String.valueOf(Cur_url_num + 1);// 按上显示的数字键
			jiemu_text.setText(strUp);
			hideNum();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DOWN = 1;// 下次按下菜单键时
			if (TV_menu_is_yincang == 0) {
				ctrl_tv_menu_vis.setVisibility(View.GONE);
				TV_menu_is_yincang = 1;
				return true;
			}
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出应用程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}

				return true;
			} else {
				finish();
				onDestroy();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;// 不返回一个之，就会调用super。效果就没了
			}
		}
		// if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
		// if (TV_menu_is_yincang == 1) {
		// send_key(KeyEvent.KEYCODE_VOLUME_UP);
		// return true;
		// }
		// }
		//
		// if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
		// if (TV_menu_is_yincang == 1) {
		// send_key(KeyEvent.KEYCODE_VOLUME_DOWN);
		// return true;
		// }
		// }
		// 声音键放大的方法
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)

		{
			return super.onKeyDown(keyCode, event);
		}
		// 声音键调小的方法
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)

		{
			return super.onKeyDown(keyCode, event);
		}
		// 显示数字的方�?		
		if (shuzi_sel_TV(keyCode, event) == true)
			return true;

		return super.onKeyDown(keyCode, event);
		// �?��，一定要做完以后返回 true，或者在弹出菜单后返回true，其他键返回super，让其他键默�?
		}

	// // 按键处理时间,当按下返回按键时的处理方�?
	Runnable jm_runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(3500);
				numHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// 隐藏节目号的方法
	void hideNum() {
		new Thread(this.jm_runnable).start();
	}

	boolean play_jiemu_num() {
		if (jiemu_num > playList.size() || jiemu_num == 0) {
			Toast toast = Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			jiemu_text.setText("");
			return false;
		}
		// jiemu_num=3;
		Cur_url_num = jiemu_num - 1;
		String uri = playList.get(jiemu_num - 1).path;
		Log.d("url", uri);
		String name = playList.get(jiemu_num - 1).displayName;
		Log.d("displayName", name);
		if (name == null)
			return false;
		// 菜单界面消失
		ctrl_tv_menu_vis.setVisibility(View.GONE);
		TV_menu_is_yincang = 1;
		play_url(uri);
		Toast.makeText(this, "正在进入" + name, Toast.LENGTH_LONG).show();
		Log.d("url", uri);
		jiemu_text.setText("");

		return true;
	}

	Handler numHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				play_jiemu_num();
			case 1:
				jiemu_text.setText("");// 让节目号消失
			default:
				break;
			}
		}
	};

	Runnable numrunnable = new Runnable() {
		public void run() {
			try {
				// Thread.sleep(100);//5秒后进入节目

				numHandler.sendEmptyMessage(0);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	void jiemuUpdate() {
		Thread thread = new Thread(numrunnable);
		thread.start();
	}

	private static Boolean isExit1 = false;
	private static Boolean hasTask1 = false;
	private static int jiemu_temp = 0;// 节目临时变量
	private static String jm_temp = String.valueOf(jiemu_temp);// 将临时节目号转换成String类型

	Timer tExit1 = new Timer();
	TimerTask task1 = new TimerTask() {
		@Override
		public void run() {

			jiemu_temp = 0;
			hasTask1 = false;
			jiemuUpdate();

		}
	};

	boolean shuzi_sel_TV(int keyCode, KeyEvent event) {

		// TextView
		// jiemu_text=(TextView)this.findViewById(R.id.num);//得到显示节目号的控件

		if (hasTask1 == false) {
			hasTask1 = true;
			if (task1 != null) {
				task1.cancel(); // 将原任务从队列中移除
			}
			TimerTask task1 = new TimerTask() {
				@Override
				public void run() {
					hasTask1 = false;
					jiemuUpdate();
					jiemu_temp = 0;
				}
			};
			tExit1.schedule(task1, 3500);

		}

		if (keyCode == KeyEvent.KEYCODE_0) {

			if (jiemu_temp == 0) {
				jiemu_num = 0;
				String jiemu = String.valueOf(jiemu_num);// 将节目号转换成String类型
				jiemu_text.setText(jiemu);

				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 0;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 0;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {

				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_1) {

			if (jiemu_temp == 0) {
				jiemu_num = 1;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);// 将节目号转换成String类型
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 1;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 1;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {

				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_2) {
			if (jiemu_temp == 0) {
				jiemu_num = 2;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 2;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 2;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {

				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_3) {
			if (jiemu_temp == 0) {
				jiemu_num = 3;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 3;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 3;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_4) {
			if (jiemu_temp == 0) {
				jiemu_num = 4;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 4;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 4;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_5) {
			if (jiemu_temp == 0) {
				jiemu_num = 5;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 5;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 5;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_6) {
			if (jiemu_temp == 0) {
				jiemu_num = 6;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 6;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 6;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_7) {
			if (jiemu_temp == 0) {
				jiemu_num = 7;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 7;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 7;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_8) {
			if (jiemu_temp == 0) {
				jiemu_num = 8;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 8;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 8;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_9) {
			if (jiemu_temp == 0) {
				jiemu_num = 9;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);

			} else if (jiemu_temp / 10 == 0 && jiemu_temp % 10 > 0) {
				jiemu_num = jiemu_temp * 10 + 9;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
			} else if (jiemu_temp / 10 > 0 && jiemu_temp / 10 < 10) {
				jiemu_num = jiemu_temp * 10 + 9;
				jiemu_temp = jiemu_num;
				String jiemu = String.valueOf(jiemu_num);
				jiemu_text.setText(jiemu);
				// Toast.makeText(this, "没有这个节目", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}

		return true;

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// 在这里做你想做的事情

		super.onOptionsMenuClosed(menu);
	}

	// Activity创建或�?从后台重新回到前台时被调�?	@Override
	protected void onStart() {
		super.onStart();

	}

	// Activity从后台重新回到前台时被调�?	@Override
	protected void onRestart() {
		super.onRestart();

	}

	// Activity窗口获得或失去焦点时被调�?在onResume之后或onPause之后
	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus); Log.i(TAG,
	 * "onWindowFocusChanged called."); }
	 */

	/**
	 * Activity被系统杀死时被调�? 例如:屏幕方向改变�?Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其�?��.
	 * 另外,当跳转到其他Activity或�?按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状�? 在onPause之前被调�?
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	/**
	 * Activity被系统杀死后再重建时被调�?
	 * 例如:屏幕方向改变�?Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其�?��,用户又启动该Activity.
	 * 这两种情况下onRestoreInstanceState都会被调�?在onStart之后.
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
	}

	public String app_read(String file_path) {
		try {
			// 打开文件输入�?		
			FileInputStream fis = openFileInput(file_path);
			byte[] buff = new byte[1024];
			int hasRead = 0;
			StringBuilder sb = new StringBuilder("");
			while ((hasRead = fis.read(buff)) > 0) {
				sb.append(new String(buff, 0, hasRead));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void app_write(String content, String file_path) {
		try {
			// 以追加模式打�?��件输出流
			FileOutputStream fos = openFileOutput(file_path, MODE_PRIVATE);
			// 将FileOutputStream包装成PrintStream
			PrintStream ps = new PrintStream(fos);
			// 输出文件内容
			ps.println(content);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 模拟按键，缺点�?度慢�?key_int KeyEvent.KEYCODE_DPAD_DOWN
	 */
	public void send_key(int key_int) {
		try {
			String keyCommand = "input keyevent " + key_int;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private final String xmlUrl = "http://www.small-seven.com/tvlive/tv_program_source.xml";

	List<ChannelTypeInfo> channelTypeInfosList = new ArrayList<ChannelTypeInfo>();

	private void initProgramInfoData() {
		if (channelTypeInfosList != null) {
			channelTypeInfosList.clear();
		}

		SAXParserFactory spf = SAXParserFactory.newInstance();
		List<ChannelTypeInfo> dianshijuInfos = new ArrayList<ChannelTypeInfo>();
		try {

			// .通过该工厂类产生�?��SAX的解析类SAXParser
			// 从SAXParser中得到一个XMLReader实例
			/*XMLReader xmlReader = spf.newSAXParser().getXMLReader();
			XmlPaser dsjTypeXmlPaser = new XmlPaser(channelTypeInfosList);

			xmlReader.setContentHandler(dsjTypeXmlPaser);
			String xml = HttpDownloadML(xmlUrl);// 获取电视剧分类信息xml
			// 将xml资源变成�?��JAVA可处理的InputStream留，解析�?��
			xmlReader.parse(new InputSource(new StringReader(xml)));

			for (Iterator iterator = dianshijuInfos.iterator(); iterator
					.hasNext();) {
				ChannelTypeInfo dianshijuInfo = (ChannelTypeInfo) iterator.next();
				System.out.println(dianshijuInfo.getChannelTypeName());
			}
			
			channelTypeInfosList = dianshijuInfos;*/

			
			  SAXParser sp=spf.newSAXParser(); 
			  XmlPaser xmlPar=new XmlPaser();
			  InputStream is=this.getClass().getClassLoader().getResourceAsStream("tv_program_source.xml"); 
			  sp.parse(is, xmlPar);
			 
			  channelTypeInfosList=xmlPar.getChannelTpyeInfoList();
			  System.out.println("111");
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 解析数据
	private String HttpDownloadML(String urlStr) {
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.downloadText(urlStr);
		return result;
	}

	ProgressDialog waitDialog;

	class InitDataTask extends AsyncTask<Void, Integer, Integer> {
		private Context context;

		InitDataTask(Context context) {
			this.context = context;
		}

		/**
		 * 运行在UI线程中，在调用doInBackground()之前执行
		 */
		@Override
		protected void onPreExecute() {

			waitDialog = ProgressDialog.show(VideoViewPlayingActivity.this,
					null, "正在加载节目，请稍后 ... ", true);
			waitDialog.setCancelable(true);
			setDialogFontSize(waitDialog, 30);
		}

		/**
		 * 后台运行的方法，可以运行非UI线程，可以执行�?时的方法
		 * 
		 * @return
		 */
		@Override
		protected Integer doInBackground(Void... params) {
			initProgramInfoData();
			return null;
		}

		/**
		 * 运行在ui线程中，在doInBackground()执行完毕后执�?		 */
		@Override
		protected void onPostExecute(Integer integer) {

			if (waitDialog != null) {
				waitDialog.dismiss();
				waitDialog = null;

			}

			ctrl_tv_init();

			loadViews();// 加载目录数据
			init_run();
		}

		/**
		 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {

		}
	}

	private void setDialogFontSize(Dialog dialog, int size) {
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		setViewFontSize(view, size);
	}

	private void setViewFontSize(View view, int size) {
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontSize(parent.getChildAt(i), size);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setTextSize(size);
		}
	}
}
