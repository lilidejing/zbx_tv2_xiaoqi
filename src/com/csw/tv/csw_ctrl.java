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
	
	String fileName = "TvRes.txt"; //�ļ�����
	//String fileName = "TvCommon.txt"; //ʹ��read֮ǰ���ı�fileName��ֵ
	
	String res=""; //����Դ���ļ���ȡ���������������֮���޸�����ַ�������д�뵽�ļ���
	String udate_date = "201206082334";//����ʱ�䣬�������Ϊ�ж�������µ�����
	
	
	//�õ��ֻ������Ƶ�ļ�����ڸ��б���
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
		
        //����Ϊ����SCREEN_ORIENTATION_LANDSCAPE ����   SCREEN_ORIENTATION_PORTRAIT
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
		//��Ƶ�б����
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
		//��Ƶ�б����
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
//							//�������activity
//							//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //ע�Ȿ�е�FLAG����
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
		//�˳���ť
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
	//��ȡָ��SD���ļ�����
	private String read()
	{
		try
		{
			//����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				//��ȡSD����Ӧ�Ĵ洢Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();
				//��ȡָ���ļ���Ӧ��������
				FileInputStream fis = new FileInputStream(sdCardDir
					.getCanonicalPath()	+ FILE_NAME);
				//��ָ����������װ��BufferedReader
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
	//д��ָ��sd���ļ�����
	private void write(String content)
	{
		try
		{	
			//����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				//��ȡSD����Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath()
					+ FILE_NAME);
				targetFile.delete();//ɾ�����ļ�������д���¶���
				//��ָ���ļ�����	RandomAccessFile����
				RandomAccessFile raf = new RandomAccessFile(
					targetFile , "rw");
				//���ļ���¼ָ���ƶ������
				raf.seek(targetFile.length());
				// ����ļ�����
				raf.write(content.getBytes());
				raf.close();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//����̨��Ŀ����
	private void TV_leixing(final LinkedList<MovieInfo> list)
    {
		String str[] = {"�ҵ��ղ�","����Ƶ��","����Ƶ��","�ط�Ƶ��","����Ƶ��"};
		
		list.clear();
		for(int i=0;i<5;i++)
		{
			MovieInfo mi = new MovieInfo();
	        mi.displayName = str[i];
	        mi.path = null;
	        list.add(mi); 
		}
    }
	
	//ɾ���ղ�
	private void  delete_shoucang( int arg,final LinkedList<MovieInfo> list)
	{
    	String res=""; //����Դ���ļ���ȡ���������������֮���޸�����ַ�������д�뵽�ļ���

    	//�����֮ǰ���б�
    	//list.clear();
    	//�ļ������ڻ���sd�������ڣ�����null
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	
    	if(res == null)
    	{
    		return;//sd�������ڣ��˳�
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
        		return;//�ø������ղ�
        	}
        	else
        	{
        		String s1 = res.substring(0, yangshi_index+3);
        		String s2 = res.substring(yangshi_index2, res.length());
        		String shoucang_res = s1+s2;
        		write(shoucang_res);
        		list.clear();
        		TV_shoucang_parse(list);//���������������
        		
        	}

	}
		
	
    //�����ղ�TV_shoucang_parse
    private void TV_shoucang_parse(final LinkedList<MovieInfo> list)
    {
    	String fileName = "shoucang.txt"; //�ļ�����
    	String res=""; //����Դ���ļ���ȡ���������������֮���޸�����ַ�������д�뵽�ļ���

    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	//�����֮ǰ���б�
    	list.clear();
    	//�ļ������ڻ���sd�������ڣ�����null
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	
    	if(res == null)
    	{
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);  
	    	   //��ȡ���ļ�����pc���汻������ʱ�򣬼ǵ�תΪ"UTF-8"
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
	       	 	
	       	 	//ɾ������
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
		       		 break;//˵������̨��Դ�Ѿ��������ˣ�����
		       	 	Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
		       	 	Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
		       	 	MovieInfo mi = new MovieInfo();
		       	 	mi.displayName = (count)+" "+Tv_name;
		       	 	mi.path = Tv_url;
		       	 	list.add(mi); 
		       	 	
		       	 	//ɾ������
		       	 	MovieInfo mi1 = new MovieInfo();
		       	 	mi1.displayName = "X";
		       	 	mi1.path = null;
		       	 	list.add(mi1); 
	
        	}

    	}


    }
    //��ȡsd����Ƶ���ݵ��б�������Ͽ��Բ���һЩ���õĸ�ʽ������ֻ����ȡ��mp4��3gp������
    //��ֵ���flv��ȻҲ�����б��
    private void TV_yangshi_parse(final LinkedList<MovieInfo> list)
    {
    	//TvRes.txt��Ľ�����ʶ
    	//�������¡����ӡ����ӡ��ط��ı�ʶ
    	String yangshi_start = "##yangshi-start";
    	String yangshi_end = "##yangshi-end";
    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	//�����֮ǰ���б�
    	//list.clear();
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	if(res == null)
    	{
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
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
    	//����Ƶ������
    	int yangshi_index = res.indexOf(yangshi_start);
    	int yangshi_index2 = res.indexOf(yangshi_end);
    	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
    	
    	for(int i=0;i<yangshi_index2-yangshi_index;)
    	{
        	 res_index1 = yangshi_res.indexOf(res_start);
        	 tv_name_index = yangshi_res.indexOf(res_url);
        	 res_index2 = yangshi_res.indexOf(res_end);
        	 if(res_index2 == -1)
        		 break;//˵������̨��Դ�Ѿ��������ˣ�����
        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());       	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//����@@����ʼ������һ����Դ
    	}
    }
    //����Ƶ������
    private void TV_weishi_parse(final LinkedList<MovieInfo> list)
    {
    	String weishi_start = "##weishi-start";
    	String weishi_end = "##weishi-end";

    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	FILE_NAME = "/TvRes.tv";
    	//�ļ������ڻ���sd�������ڣ�����null
    	res = read();
    	
    	if(res == null)
    	{
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
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
    	
    	//����Ƶ������
    	int yangshi_index = res.indexOf(weishi_start);
    	int yangshi_index2 = res.indexOf(weishi_end);
    	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
    	
    	for(int i=0;i<yangshi_index2-yangshi_index;)
    	{
        	 res_index1 = yangshi_res.indexOf(res_start);
        	 tv_name_index = yangshi_res.indexOf(res_url);
        	 res_index2 = yangshi_res.indexOf(res_end);
        	 if(res_index2 == -1)
        		 break;//˵������̨��Դ�Ѿ��������ˣ�����
        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//����@@����ʼ������һ����Դ
        	 
    	}

    }
    //�ط�Ƶ������
    private void TV_difang_parse(final LinkedList<MovieInfo> list)
    {
    	String difang_start = "##difang-start";
    	String difang_end = "##difang-end";

    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;

    	//�ļ������ڻ���sd�������ڣ�����null
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	
    	if(res == null)
    	{
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
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
    	
    	
    	//����Ƶ������
    	int yangshi_index = res.indexOf(difang_start);
    	int yangshi_index2 = res.indexOf(difang_end);
    	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
    	
    	for(int i=0;i<yangshi_index2-yangshi_index;)
    	{
        	 res_index1 = yangshi_res.indexOf(res_start);
        	 tv_name_index = yangshi_res.indexOf(res_url);
        	 res_index2 = yangshi_res.indexOf(res_end);
        	 if(res_index2 == -1)
        		 break;//˵������̨��Դ�Ѿ��������ˣ�����
        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//����@@����ʼ������һ����Դ
        	 
    	}

    }
    
    private void TV_qita_parse(final LinkedList<MovieInfo> list)
    {
    	String difang_start = "##qita-start";
    	String difang_end = "##qita-end";

    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "url--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;

    	//�ļ������ڻ���sd�������ڣ�����null
    	FILE_NAME = "/TvRes.tv";
    	res = read();
    	
    	if(res == null)
    	{
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
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
    	
    	
    	//����Ƶ������
    	int yangshi_index = res.indexOf(difang_start);
    	int yangshi_index2 = res.indexOf(difang_end);
    	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
    	
    	for(int i=0;i<yangshi_index2-yangshi_index;)
    	{
        	 res_index1 = yangshi_res.indexOf(res_start);
        	 tv_name_index = yangshi_res.indexOf(res_url);
        	 res_index2 = yangshi_res.indexOf(res_end);
        	 if(res_index2 == -1)
        		 break;//˵������̨��Դ�Ѿ��������ˣ�����
        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());
        	 
        	 MovieInfo mi = new MovieInfo();
        	 mi.displayName = Tv_name;
        	 mi.path = Tv_url;
        	 list.add(mi); 
        	 i=i+(res_index2-res_index1);//����@@����ʼ������һ����Դ
        	 
    	}

    }
  
    //�ź����image��������������������������е�imagebuttom��ͼ��
    private void imagebutton_is_huangse(int id,int id2)
    {
		final ImageView image_shoucang = (ImageView)findViewById(id);
		//BitmapDrawable bitmapDrawable = (BitmapDrawable) image_shoucang
			//	.getDrawable();
		//������Ϊ������յ����⣬������ҵ�ϵͳ�������һ�������Ϊʲô��
		//�����Ŀ֮�󣬺ú��ܽᡣ
		/*
		 
			//���ͼƬ��δ���գ���ǿ�ƻ��ո�ͼƬ
			if (!bitmapDrawable.getBitmap().isRecycled())
			{
				bitmapDrawable.getBitmap().recycle();
			}
		*/
			//�ı�ImageView��ʾ��ͼƬ
			image_shoucang.setImageBitmap(BitmapFactory.decodeResource(getResources()
				, id2));
    }
    

	//wjz test
	//ͨ��ң����0��9�������ҵ��ղ������Ӧ�ĵ���̨��
	public void shoucang_tv_shuzi(int arg2)
	{
		
		
		playList.clear();
		TV_shoucang_parse(playList);//��ȡ�ղ��б�
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
//					//�������activity
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //ע�Ȿ�е�FLAG����
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
	
	// ��������ʱ��,�����·��ذ���ʱ�Ĵ�����
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
			//super.openOptionsMenu();  // ����������Ϳ��Ե����˵�
	     }  
	     if (keyCode == KeyEvent.KEYCODE_BACK) 
	     { 
	    	 if (isExit == false)
	    	 {
	    		 isExit = true;
	    		 Toast.makeText(this, "�ٰ�һ���˳�Ӧ�ó���", Toast.LENGTH_SHORT).show();
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
	    		 return true;//������һ��֮���ͻ����super��Ч����û��
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
	     // ���һ��Ҫ�����Ժ󷵻� true�������ڵ����˵��󷵻�true������������super����������Ĭ��
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) 
    {
   //��������������������  

    	super.onOptionsMenuClosed(menu);
    }
    //Activity�������ߴӺ�̨���»ص�ǰ̨ʱ������
    @Override
    protected void onStart() 
    {
    	super.onStart();
    	
    }
    
    //Activity�Ӻ�̨���»ص�ǰ̨ʱ������
    @Override
    protected void onRestart() 
    {
    	super.onRestart();
    	
    }
    
    //Activity�������ߴӱ����ǡ���̨���»ص�ǰ̨ʱ������
    @Override
    protected void onResume() {
    	super.onResume();
    
    }
    
    //Activity���ڻ�û�ʧȥ����ʱ������,��onResume֮���onPause֮��
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	Log.i(TAG, "onWindowFocusChanged called.");
    }*/
    
    //Activity�����ǵ������������ʱ������
    @Override
    protected void onPause() {
    	super.onPause();
    
    	//�п�����ִ����onPause��onStop��,ϵͳ��Դ���Ž�Activityɱ��,�����б�Ҫ�ڴ˱���־�����
    }
    
    //�˳���ǰActivity������ת����Activityʱ������
    @Override
    protected void onStop() {
    	super.onStop();
    	
    }
    
    //�˳���ǰActivityʱ������,����֮��Activity�ͽ�����
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    
    }
    
    /**
     * Activity��ϵͳɱ��ʱ������.
     * ����:��Ļ����ı�ʱ,Activity���������ؽ�;��ǰActivity���ں�̨,ϵͳ��Դ���Ž���ɱ��.
     * ����,����ת������Activity���߰�Home���ص�����ʱ�÷���Ҳ�ᱻ����,ϵͳ��Ϊ�˱��浱ǰView�����״̬.
     * ��onPause֮ǰ������.
     */
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{

		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Activity��ϵͳɱ�������ؽ�ʱ������.
	 * ����:��Ļ����ı�ʱ,Activity���������ؽ�;��ǰActivity���ں�̨,ϵͳ��Դ���Ž���ɱ��,�û���������Activity.
	 * �����������onRestoreInstanceState���ᱻ����,��onStart֮��.
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
			// ���ļ�������
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
			// ��׷��ģʽ���ļ������
			FileOutputStream fos = openFileOutput(file_path, MODE_APPEND);
			// ��FileOutputStream��װ��PrintStream
			PrintStream ps = new PrintStream(fos);
			// ����ļ�����
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
