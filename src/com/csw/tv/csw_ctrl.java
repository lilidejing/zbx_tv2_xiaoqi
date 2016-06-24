package com.csw.tv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayersdksample1_xiaoqi.R;
import com.baidu.cyberplayersdksample1_xiaoqi.VideoViewPlayingActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;



public class csw_ctrl extends Activity 
{
	/** Called when the activity is first created. */

	EditText mTextUri = null;
	Button mButtonPlay = null;

	
	private static int height , width;
	private LinkedList<MovieInfo> mLinkedList;
	private LayoutInflater mInflater;
	private LayoutInflater mInflater1;
	View root;
	private EditText urlInput;   
	String FILE_NAME = "/TvRes.tv";
	
	String fileName = "TvRes.txt"; //文件名字
	//String fileName = "TvCommon.txt"; //使用read之前，改变fileName的值
	
	String res=""; //总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里
	String udate_date = "201206082334";//更新时间，这个将作为判断网络更新的依据
	
	
	//得到手机里的视频文件存放在该列表里
	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();
	public static LinkedList<MovieInfo> playList1 = new LinkedList<MovieInfo>();
	
	BaseAdapter Adapter;
	BaseAdapter Adapter1;
	public class MovieInfo
	{
		String displayName;  
		String path;
	}
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) 
	{

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        //设置为横屏SCREEN_ORIENTATION_LANDSCAPE 竖屏   SCREEN_ORIENTATION_PORTRAIT
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.jl_tv_dialog);
		
		//MyAutoUpdate update = new MyAutoUpdate(this);
		
		//update.check();

		TV_leixing(playList1);
		playList.clear();
		TV_weishi_parse(playList);
		
		mLinkedList = playList;
		
		mInflater = getLayoutInflater();
		mInflater1 = getLayoutInflater();
		
		
		GridView myListView = (GridView) findViewById(R.id.grid);
		GridView myListView1 = (GridView) findViewById(R.id.grid_left);
		
		Adapter1 = new BaseAdapter()
		{

			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return playList1.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) 
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View convertView, ViewGroup arg2) 
			{
				// TODO Auto-generated method stub
				if(convertView==null)
				{
					convertView = mInflater1.inflate(R.layout.list1, null);
				}
				TextView text = (TextView) convertView.findViewById(R.id.text1);
				text.setText(playList1.get(arg0).displayName);
				
				return convertView;   
			}
			
		};
		Adapter = new BaseAdapter()
		{

			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return mLinkedList.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) 
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View convertView, ViewGroup arg2) 
			{
				// TODO Auto-generated method stub
				if(convertView==null)
				{
					convertView = mInflater.inflate(R.layout.list, null);
				}
				TextView text = (TextView) convertView.findViewById(R.id.text);
				text.setText(mLinkedList.get(arg0).displayName);
				
				return convertView;   
			}
			
		};
		myListView.setAdapter(Adapter);
		
		myListView1.setAdapter(Adapter1);
		//视频列表监听
		myListView1.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{

				switch (arg2) 
				{
					case 0: 
					{
						imagebutton_is_huangse(R.id.hengfu,R.drawable.shoucang_hengfu);
						playList.clear();
						TV_shoucang_parse(playList);
						Adapter.notifyDataSetChanged();

						break;
					}
					case 1:
					{
						imagebutton_is_huangse(R.id.hengfu,R.drawable.yangshi_hengfu);
						playList.clear();
						TV_yangshi_parse(playList);
						Adapter.notifyDataSetChanged();
						break;
					}
					case 2: 
					{
						imagebutton_is_huangse(R.id.hengfu,R.drawable.weishi_hengfu);
						playList.clear();
						TV_weishi_parse(playList);
						Adapter.notifyDataSetChanged();
						break;
					}
					case 3:
					{
						imagebutton_is_huangse(R.id.hengfu,R.drawable.difang_hengfu);
						playList.clear();
						TV_difang_parse(playList);
						Adapter.notifyDataSetChanged();
						break;
					}
					case 4: 
					{
						imagebutton_is_huangse(R.id.hengfu,R.drawable.qita_hengfu);
						playList.clear();
						TV_qita_parse(playList);
						Adapter.notifyDataSetChanged();
						break;
					}
				}
				
			}
		});		
		//视频列表监听
		myListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{

				String uri = playList.get(arg2).path;
				String name = playList.get(arg2).displayName;
				if(name == null)
					return;
				
				if((uri != null)|(name.charAt(0) == 'X'))
				{
					if(name.charAt(0) == 'X')
					{
						delete_shoucang(arg2,playList);
						Adapter.notifyDataSetChanged();
					}
					else
					{
						if (uri.length() > 0) 
						{
							
							playVideo(uri);
							//Intent intent = new Intent(TestActivity.this,
							//		PlayerActivity.class);
//							Intent intent = new Intent(); 
//							intent.setClass(jl_tv.this, VideoViewDemo.class);
//							//清除其他activity
//							//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
//							//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  
//
//							ArrayList<String> playlist = new ArrayList<String>();
//							playlist.add(uri);
//							intent.putExtra("selected", 0);
//							intent.putExtra("playlist", playlist);
//							startActivity(intent);
							//jl_tv.this.finish();
							
						}
					}
				}
			}
		});
		//退出按钮
		ImageButton iButton = (ImageButton) findViewById(R.id.cancel);

		iButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				csw_ctrl.this.finish();
			}
			
		});
		myListView.requestFocus();	
	}
	//读取指定SD卡文件内容
	private String read()
	{
		try
		{
			//如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				//获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				//获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(sdCardDir
					.getCanonicalPath()	+ FILE_NAME);
				//将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new 
					InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				while((line = br.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	//写入指定sd卡文件内容
	private void write(String content)
	{
		try
		{	
			//如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				//获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath()
					+ FILE_NAME);
				targetFile.delete();//删除旧文件，重新写入新东西
				//以指定文件创建	RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(
					targetFile , "rw");
				//将文件记录指针移动到最后
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.write(content.getBytes());
				raf.close();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//电视台栏目解析
	private void TV_leixing(final LinkedList<MovieInfo> list)
    {
		String str[] = {"我的收藏","央视频道","卫视频道","地方频道","其它频道"};
		
		list.clear();
		for(int i=0;i<5;i++)
		{
			MovieInfo mi = new MovieInfo();
	        mi.displayName = str[i];
	        mi.path = null;
	        list.add(mi); 
		}
    }
	
	//删除收藏
	private void  delete_shoucang( int arg,final LinkedList<MovieInfo> list)
	{
    	String res=""; //总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里

    	//清除掉之前的列表
    	//list.clear();
    	//文件不存在或者sd卡不存在，返回null
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	
    	if(res == null)
    	{
    		return;//sd卡不存在，退出
    	}
    	
    	String shoucang_start = "**";
    	String shoucang_end = "##";
    	
    	int count =arg/2;
    	
    		String shoucang_start1= shoucang_start +count;
    		String shoucang_end1= shoucang_end +count;
    		
        	int yangshi_index = res.indexOf(shoucang_start1);
        	int yangshi_index2 = res.indexOf(shoucang_end1);
        	if(yangshi_index2-yangshi_index == 3)
        	{
        		return;//该格子无收藏
        	}
        	else
        	{
        		String s1 = res.substring(0, yangshi_index+3);
        		String s2 = res.substring(yangshi_index2, res.length());
        		String shoucang_res = s1+s2;
        		write(shoucang_res);
        		list.clear();
        		TV_shoucang_parse(list);//调用这个，更链表
        		
        	}

	}
		
	
    //解析收藏TV_shoucang_parse
    private void TV_shoucang_parse(final LinkedList<MovieInfo> list)
    {
    	String fileName = "shoucang.txt"; //文件名字
    	String res=""; //总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里

    	//具体解析电视台的标识
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	//清除掉之前的列表
    	list.clear();
    	//文件不存在或者sd卡不存在，返回null
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	
    	if(res == null)
    	{
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);  
	    	   //读取的文件，在pc上面被创建的时候，记得转为"UTF-8"
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState()
						.equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
    	
    	String shoucang_start = "**";
    	String shoucang_end = "##";
    	
    	for(int count =0;count<10;count++)
    	{
    		String shoucang_start1= shoucang_start +count;
    		String shoucang_end1= shoucang_end +count;
    		
        	int yangshi_index = res.indexOf(shoucang_start1);
        	int yangshi_index2 = res.indexOf(shoucang_end1);
        	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
        	if(yangshi_index2-yangshi_index == 3)
        	{
	       	 	
	       	 	MovieInfo mi = new MovieInfo();
	       	 	mi.displayName = (count)+"         ";
	       	 	mi.path = null;
	       	 	list.add(mi); 
	       	 	
	       	 	//删除操作
	       	 	MovieInfo mi1 = new MovieInfo();
	       	 	mi1.displayName = null;
	       	 	mi1.path = null;
	       	 	list.add(mi1); 
        	}
        	else
        	{

	        		res_index1 = yangshi_res.indexOf(res_start);
		       	 	tv_name_index = yangshi_res.indexOf(res_url);
		       	 	res_index2 = yangshi_res.indexOf(res_end);
		       	 	if(res_index2 == -1)
		       		 break;//说明电视台资源已经不存在了，跳出
		       	 	Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
		       	 	Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
		       	 	MovieInfo mi = new MovieInfo();
		       	 	mi.displayName = (count)+" "+Tv_name;
		       	 	mi.path = Tv_url;
		       	 	list.add(mi); 
		       	 	
		       	 	//删除操作
		       	 	MovieInfo mi1 = new MovieInfo();
		       	 	mi1.displayName = "X";
		       	 	mi1.path = null;
		       	 	list.add(mi1); 
	
        	}

    	}


    }
    //获取sd卡视频数据到列表里，理论上可以播放一些常用的格式，这里只是提取了mp4和3gp，不过
    //奇怪的是flv竟然也出现列表里。
    private void TV_yangshi_parse(final LinkedList<MovieInfo> list)
    {
    	//TvRes.txt里的解析标识
    	//解析更新、央视、卫视、地方的标识
    	String yangshi_start = "##yangshi-start";
    	String yangshi_end = "##yangshi-end";
    	//具体解析电视台的标识
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	//清除掉之前的列表
    	//list.clear();
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	if(res == null)
    	{
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);            
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState()
						.equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
    	//央视频道解析
    	int yangshi_index = res.indexOf(yangshi_start);
    	int yangshi_index2 = res.indexOf(yangshi_end);
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
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());       	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//跳过@@，开始搜索下一个资源
    	}
    }
    //卫视频道解析
    private void TV_weishi_parse(final LinkedList<MovieInfo> list)
    {
    	String weishi_start = "##weishi-start";
    	String weishi_end = "##weishi-end";

    	//具体解析电视台的标识
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	FILE_NAME = "/TvRes.tv";
    	//文件不存在或者sd卡不存在，返回null
    	res = read();
    	
    	if(res == null)
    	{
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);            
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState()
						.equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
    	
    	//卫视频道解析
    	int yangshi_index = res.indexOf(weishi_start);
    	int yangshi_index2 = res.indexOf(weishi_end);
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
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//跳过@@，开始搜索下一个资源
        	 
    	}

    }
    //地方频道解析
    private void TV_difang_parse(final LinkedList<MovieInfo> list)
    {
    	String difang_start = "##difang-start";
    	String difang_end = "##difang-end";

    	//具体解析电视台的标识
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;

    	//文件不存在或者sd卡不存在，返回null
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	
    	if(res == null)
    	{
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);            
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState()
						.equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
    	
    	
    	//卫视频道解析
    	int yangshi_index = res.indexOf(difang_start);
    	int yangshi_index2 = res.indexOf(difang_end);
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
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//跳过@@，开始搜索下一个资源
        	 
    	}

    }
    
    private void TV_qita_parse(final LinkedList<MovieInfo> list)
    {
    	String difang_start = "##qita-start";
    	String difang_end = "##qita-end";

    	//具体解析电视台的标识
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;

    	//文件不存在或者sd卡不存在，返回null
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	
    	if(res == null)
    	{
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);            
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState()
						.equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
    	
    	
    	//卫视频道解析
    	int yangshi_index = res.indexOf(difang_start);
    	int yangshi_index2 = res.indexOf(difang_end);
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
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//跳过@@，开始搜索下一个资源
        	 
    	}

    }
  
    //放横幅的image，不过这个函数，可以适用所有的imagebuttom放图像
    private void imagebutton_is_huangse(int id,int id2)
    {
		final ImageView image_shoucang = (ImageView)findViewById(id);
		//BitmapDrawable bitmapDrawable = (BitmapDrawable) image_shoucang
			//	.getDrawable();
		//就是因为这个回收的问题，造成了我的系统崩溃。我还不明白为什么？
		//搞好项目之后，好好总结。
		/*
		 
			//如果图片还未回收，先强制回收该图片
			if (!bitmapDrawable.getBitmap().isRecycled())
			{
				bitmapDrawable.getBitmap().recycle();
			}
		*/
			//改变ImageView显示的图片
			image_shoucang.setImageBitmap(BitmapFactory.decodeResource(getResources()
				, id2));
    }
    

	//wjz test
	//通过遥控器0到9，控制我的收藏里相对应的电视台。
	public void shoucang_tv_shuzi(int arg2)
	{
		
		
		playList.clear();
		TV_shoucang_parse(playList);//获取收藏列表
		Adapter.notifyDataSetChanged();
		
		String uri = playList.get(arg2).path;
		String name = playList.get(arg2).displayName;
		if(name == null)
			return;
		
		if((uri != null)|(name.charAt(0) == 'X'))
		{
			if(name.charAt(0) == 'X')
			{
				delete_shoucang(arg2,playList);
				Adapter.notifyDataSetChanged();
			}
			else
			{
				if (uri.length() > 0) 
				{
					
//					active.active = true;
//					//Intent intent = new Intent(TestActivity.this,
//					//		PlayerActivity.class);
//					Intent intent = new Intent(); 
//					intent.setClass(jl_tv.this, PlayerActivity.class);
//					//清除其他activity
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
//					
//					ArrayList<String> playlist = new ArrayList<String>();
//					playlist.add(uri);
//					intent.putExtra("selected", 0);
//					intent.putExtra("playlist", playlist);
//					startActivity(intent);
//					//TestActivity.this.finish();
					
				}
			}
		}
	}
	
	// 按键处理时间,当按下返回按键时的处理方法
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask()
	{
		@Override
		public void run()
		{
			isExit = false;
			hasTask = true;
		}
	};
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
     
	     if (keyCode == KeyEvent.KEYCODE_MENU) 
	     { 
			//jl_tv.this.finish();
			return true;
			//super.openOptionsMenu();  // 调用这个，就可以弹出菜单
	     }  
	     if (keyCode == KeyEvent.KEYCODE_BACK) 
	     { 
	    	 if (isExit == false)
	    	 {
	    		 isExit = true;
	    		 Toast.makeText(this, "再按一次退出应用程序", Toast.LENGTH_SHORT).show();
	    		 if (!hasTask)
	    		 {
	    			 tExit.schedule(task, 2000);
	    		 }
	    		 
	    		return true;
	    	 } 
	    	 else
	    	 {
	    		 csw_ctrl.this.finish();
	    		 onDestroy();
	    		 System.exit(0);
	    		 android.os.Process.killProcess(android.os.Process.myPid());
	    		 return true;//不返回一个之，就会调用super。效果就没了
	    	 }
	     } 
	     if(keyCode == KeyEvent.KEYCODE_0)
	     {
	    	 shoucang_tv_shuzi(0);
	     }
	     if(keyCode == KeyEvent.KEYCODE_1)
	     {
	    	 shoucang_tv_shuzi(2);
	     }
	     if(keyCode == KeyEvent.KEYCODE_2)
	     {
	    	 shoucang_tv_shuzi(4);
	     }
	     if(keyCode == KeyEvent.KEYCODE_3)
	     {
	    	 shoucang_tv_shuzi(6);
	     }
	     if(keyCode == KeyEvent.KEYCODE_4)
	     {
	    	 shoucang_tv_shuzi(8);
	     }
	     if(keyCode == KeyEvent.KEYCODE_5)
	     {
	    	 shoucang_tv_shuzi(10);
	     }
	     if(keyCode == KeyEvent.KEYCODE_6)
	     {
	    	 shoucang_tv_shuzi(12);
	     }
	     if(keyCode == KeyEvent.KEYCODE_7)
	     {
	    	 shoucang_tv_shuzi(14);
	     }
	     if(keyCode == KeyEvent.KEYCODE_8)
	     {
	    	 shoucang_tv_shuzi(16);
	     }
	     if(keyCode == KeyEvent.KEYCODE_9)
	     {
	    	 shoucang_tv_shuzi(18);
	     }
	     
	     return super.onKeyDown(keyCode, event);
	     // 最后，一定要做完以后返回 true，或者在弹出菜单后返回true，其他键返回super，让其他键默认
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) 
    {
   //在这里做你想做的事情  

    	super.onOptionsMenuClosed(menu);
    }
    //Activity创建或者从后台重新回到前台时被调用
    @Override
    protected void onStart() 
    {
    	super.onStart();
    	
    }
    
    //Activity从后台重新回到前台时被调用
    @Override
    protected void onRestart() 
    {
    	super.onRestart();
    	
    }
    
    //Activity创建或者从被覆盖、后台重新回到前台时被调用
    @Override
    protected void onResume() {
    	super.onResume();
    
    }
    
    //Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	Log.i(TAG, "onWindowFocusChanged called.");
    }*/
    
    //Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
    	super.onPause();
    
    	//有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据
    }
    
    //退出当前Activity或者跳转到新Activity时被调用
    @Override
    protected void onStop() {
    	super.onStop();
    	
    }
    
    //退出当前Activity时被调用,调用之后Activity就结束了
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    
    }
    
    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{

		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Activity被系统杀死后再重建时被调用.
	 * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
	 * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{

		super.onRestoreInstanceState(savedInstanceState);
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
	
	
	private void playVideo(String url){

		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		mgr.initCyberPlayerEngine("DQt6mHFcXPQlIx7RXtcKFvkA", "kklisGV8kpqcMHWPufwRIpFOHGm8iMRY");
		Intent intent = new Intent(this, VideoViewPlayingActivity.class);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
}
