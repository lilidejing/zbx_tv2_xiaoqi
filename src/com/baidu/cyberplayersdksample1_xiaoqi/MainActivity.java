package com.baidu.cyberplayersdksample1_xiaoqi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayer.sdk.BEngineManager.OnEngineListener;

import com.csw.download.HttpGetString;
import com.csw.tv.jl_tv;
import com.csw.tv.main_welcome;

import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private final String TAG = "MainActivity";
	private Button mInstallBtn;
	private Button mPlayBtn;
	private TextView mInfoTV;
	private EditText mSourceET;
	
	private final int UPDATE_INFO = 0;
	private final int RESULT = 1;
	
	//您的ak
	//private String AK = "xxx";
	//您的sk的前16位
	//private String SK = "xxx";
	
	//返回值对应的含义
	String[] mRetInfo = new String[] {
			"RET_NEW_PACKAGE_INSTALLED",
			"RET_NO_NEW_PACKAGE",
			"RET_STOPPED",
			"RET_CANCELED",
			"RET_FAILED_STORAGE_IO",
			"RET_FAILED_NETWORK",
			"RET_FAILED_ALREADY_RUNNING",
			"RET_FAILED_OTHERS",
			"RET_FAILED_ALREADY_INSTALLED",
			"RET_FAILED_INVALID_APK"
	};
	
	
	
	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_INFO:
				mInfoTV.setText((String)msg.obj);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化BCyberPlayerFactory, 在其他任何接口调用前需要先对BCyberPlayerFactory进行初始化
		BCyberPlayerFactory.init(this);
		
//    	String smima = app_read("tv1.zy");
//    	if(smima == null)
//    	{
//    		init_mac();
//    	}else
//    	{
			setContentView(R.layout.activity_main);
			initUI();
			checkEngineInstalled();
//    	}
//		initUI();
	}
	
	void initUI(){
		mInstallBtn = (Button)findViewById(R.id.installBtn);
		mPlayBtn = (Button)findViewById(R.id.playBtn);
		mPlayBtn.setVisibility(View.GONE);
		mInfoTV = (TextView)findViewById(R.id.infoTV);
		mSourceET = (EditText)findViewById(R.id.getET);
		mSourceET.setVisibility(View.GONE);
		mSourceET.setText("http://95.211.187.232:8080/hls/bbc_world_news_1000.m3u8");
	//	playVideo();
		mInstallBtn.setOnClickListener(this);
		mPlayBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.installBtn:
			//检测engine是否安装,如果没安装先安装engine
			checkEngineInstalled();
			break;
		case R.id.playBtn:
			//播放一个视频
			playVideo();
			break;
		
		default:
			break;
		}
	}
	
	private void playVideo(){
		//如果engine没安装,进行提示
		if(!isEngineInstalled()){
			checkEngineInstalled();
			setInfo("播放引擎未安装，\n 请安装！");
		}else{//如果已经安装
			String source = mSourceET.getText().toString();
			if(source == null || source.equals("")){
				//简单检测播放源的合法性,不合法不播放
				setInfo("请输入一个合法的播放地址");
			}else{
				//播放前需要用ak,sk来初始化BEngineManager, 播放的时候会对ak,sk进行权限认证
				//当前您也可以到VideoViewPlayingActivity进行初始化
				BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
				mgr.initCyberPlayerEngine("DQt6mHFcXPQlIx7RXtcKFvkA", "kklisGV8kpqcMHWPufwRIpFOHGm8iMRY");
				Intent intent = new Intent(this, VideoViewPlayingActivity.class);
				intent.setData(Uri.parse(source));
				startActivity(intent);
			}
		}
	}
	
	/**
	 * 检测engine是否安装
	 * @return
	 */
	private boolean isEngineInstalled(){
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		return mgr.EngineInstalled();
	}
	
	/**
	 * 下载并安装engine
	 */
	private void installEngine(){
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		mgr.installAsync(mEngineListener);
	}
	
	/**
	 * 检测engine是否安装,如果没有安装需要安装engine
	 */
	private void checkEngineInstalled(){
		if(isEngineInstalled()){
			
			play_current();
		}else{
			//安装engine
			installEngine();
		}
	}

	private OnEngineListener mEngineListener = new OnEngineListener(){
		String info = "";
		
		String dlhead = "安装播放引擎：正在下载……";
		String dlbody = "";
		@Override
		public boolean onPrepare() {
			// TODO Auto-generated method stub
			info = "安装播放引擎：预备安装\n";
			setInfo(info);
			return true;
		}

		@Override
		public int onDownload(int total, int current) {
			// TODO Auto-generated method stub
			if(dlhead != null){
				info += dlhead;
				dlhead = null;
			}
			dlbody = current + "/" + total;
			setInfo(info + dlbody + "\n");
			return DOWNLOAD_CONTINUE;
		}
		
		@Override
		public int onPreInstall() {
			// TODO Auto-generated method stub
			info += dlbody;
			info += "\n";
			info += "安装播放引擎：预备安装中\n";
			setInfo(info);
			
			return DOWNLOAD_CONTINUE;
		}

		@Override
		public void onInstalled(int result) {
			// TODO Auto-generated method stub
			info += "安装播放引擎：安装完毕, ret = " + mRetInfo[result] + "\n";
			setInfo(info);
			if(result == OnEngineListener.RET_NEW_PACKAGE_INSTALLED){
				//安装完成
				play_current();
			}
		}		
	};

	
	private void setInfo(String info){
		Message msg = new Message();
		msg.what = UPDATE_INFO;
		msg.obj = info;
		mUIHandler.sendMessage(msg);
	}
	
	
	private void playVideo_tv(String url){

		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		mgr.initCyberPlayerEngine("DQt6mHFcXPQlIx7RXtcKFvkA", "kklisGV8kpqcMHWPufwRIpFOHGm8iMRY");
		Intent intent = new Intent(this, VideoViewPlayingActivity.class);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
	
	void play_current()
	{
		String  uri = app_read("cur_uri");
		if(uri == null || uri.indexOf("null\n") != -1)
			uri = "http://live.3gv.ifeng.com/live/hongkong.m3u8";
		playVideo_tv(uri);
		MainActivity.this.finish(); 
	}
	
	public String app_read( String file_path)
	{
		try
		{
			// 打开文件输入流
			FileInputStream fis = openFileInput(file_path);
			byte[] buff = new byte[1024];
			int hasRead = 0;
			StringBuilder sb = new StringBuilder("");
			while ((hasRead = fis.read(buff)) > 0)
			{
				sb.append(new String(buff, 0, hasRead));
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	String s = "0w1e2r3g4j5m6l7c8z9o";
	
	 public String getLocalMacAddress() 
	 {
			WifiManager wifi = (WifiManager) getSystemService(main_welcome.WIFI_SERVICE);
			if(wifi == null)
				return null;
			WifiInfo info = wifi.getConnectionInfo();
			return info.getMacAddress();
	}
	 
	void init_mac()
	{
		
    	String smima = app_read("tv1.zy");
    	if(smima == null)
    	{
    		setContentView(R.layout.mima);

			final EditText mac_edit = (EditText)findViewById(R.id.mac_edit); 

			Button mac_Button = (Button) findViewById(R.id.mac_button);
			
    		String mac = getLocalMacAddress();
    		
    		mac_edit.setText(mac);
    		final String url_mac_res = HttpGetString.sendGet("http://www.csw-smart.com/TvMac.txt" , null);
    		int result =TV_mac_parse(url_mac_res,mac);
    		if(result == 1)
    		{
				app_write("!@#$%^&*","tv1.zy");
				Intent intent = new Intent(MainActivity.this,MainActivity.class);
				startActivity(intent);
    		}
    		else
    		{
	
	    		mac_Button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						String mac = mac_edit.getText().toString();  
					
				    	int result =TV_mac_parse(url_mac_res,mac);
				    	if(result == 0)
				    	{
						
							Toast toast = Toast.makeText(MainActivity.this
								, "网络直播服务器认证激活失败!"
							
								, Toast.LENGTH_SHORT);
							toast.show();
				    		 finish();
				    		 onDestroy();
				    		 System.exit(0);
				    		 android.os.Process.killProcess(android.os.Process.myPid());
				    	}
				    	else
				    	{
							
							Toast toast = Toast.makeText(MainActivity.this
								, "网络直播服务器认证激活成功!"
								, Toast.LENGTH_SHORT);
							toast.show();
							app_write("!@#$%^&*","tv1.zy");
							Intent intent = new Intent(MainActivity.this,MainActivity.class);
							startActivity(intent);
							
				    	}
						
					}
				});
    		}
	    	
    	}
    	else
    	{
    		if(smima.equals("!@#$%^&*\n"))
    		{
//				Intent intent1 = new Intent(main.this,
//						TestActivity.class);
//				startActivity(intent1);
//				main.this.finish();	
    		}else
    		{}
//    			main.this.finish();
    		
    	}
	
	}
	
	//写入的时候，会增加一个"\n"，为什么呢？以后比较的时候，是不是不能用相等的比较。
			//而是直接搜索
			public void app_write(String content,String file_path)
			{
				try
				{
					// 以追加模式打开文件输出流
					FileOutputStream fos = openFileOutput(file_path, MODE_APPEND);
					// 将FileOutputStream包装成PrintStream
					PrintStream ps = new PrintStream(fos);
					// 输出文件内容
					ps.println(content);
					ps.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			
			 int createRandom(int min, int max) 
			 {
					Random random = new Random();
					return random.nextInt(max-min+1)+ min;
			}
			 
			 
			 //MAC解析
			    private int TV_mac_parse(String res,String mac)
			    {
			    	//TvRes.txt里的解析标识
			    	//解析更新、央视、卫视、地方的标识
			    	//String yangshi_start = "##yangshi-start";
			    	//String yangshi_end = "##yangshi-end";
			    	String yangshi_start = "##mac-start";
			    	String yangshi_end = "##mac-end";
			    	//具体解析电视台的标识
			    	String res_start = "&&";
			    	String res_end = "@@";
			    	String res_url = "mac--";
			    	int res_index1;
			    	int res_index2;
			    	int tv_name_index;
			    	String Tv_name;
			    	String Tv_url;
			    	
			    	
			    	//央视频道解析
			    	int yangshi_index = res.indexOf(yangshi_start);
			    	int yangshi_index2 = res.indexOf(yangshi_end);
			    	if(yangshi_index == -1)
			    		return 0;
			    	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
			    	
			    	for(int i=0;i<yangshi_index2-yangshi_index;)
			    	{
			        	 res_index1 = yangshi_res.indexOf(res_start);
			        	 tv_name_index = yangshi_res.indexOf(res_url);
			        	 res_index2 = yangshi_res.indexOf(res_end);
			        	 if(res_index2 == -1)
			        		 break;//说明电视台资源已经不存在了，跳出
			        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
			        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
			        	 if(mac.equals(Tv_url))
			        		 return 1;
			        	 
			        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());       	 
			        	 i=i+(res_index2-res_index1);//跳过@@，开始搜索下一个资源
			    	}
			    	return 0;//如果找不到，就是0
			    }		 
	
}
