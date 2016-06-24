package com.csw.tv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.baidu.cyberplayersdksample1_xiaoqi.VideoViewPlayingActivity;




import android.os.Environment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.EditText;



public  class wr_file
{
	String FILE_NAME = "/TvRes.tv";

	String fileName = "TvRes.txt"; //文件名字

   public  static int flagH=0;//用来标记收藏菜单是否收藏满了
	public void wr_file( )//Context context)
	{
		//mContext = context;
	}
	
	

	//读取指定SD卡文件内容
	public String read()
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
				Log.d("读文件的地址", sdCardDir
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
	public void write(String content)
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
				Log.d("写文件的地址", sdCardDir
						.getCanonicalPath()	+ FILE_NAME);
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
	

	 //解析收藏
    public void write_shoucang(String uri)
    {
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
    	
    	
    	//在sd的TvRes.txt里搜索对应的uri电视台名字
//    	FILE_NAME = "/TvRes.tv";
    	FILE_NAME="/TvRes.txt";//改动的地方
    	res = read();
    	if(res == null)
    	{
    		//使用toast显示，显示sd卡不存在
    		return;
    	}
    	int uri_indexof = res.indexOf(uri);
    	//25,已经足够长了
    	String dianshi_res = res.substring(uri_indexof-25, uri_indexof);
    	String name = dianshi_res.substring(dianshi_res.indexOf("&&")+2, 
    												dianshi_res.indexOf(res_url));
    	
    	
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	if(res == null)
    	{
    		//使用toast显示，显示sd卡不存在
    		return;
    	}
    	String shoucang_start = "**";
    	String shoucang_end = "##";
    	for(int count =0;count<15;count++)
    	{//搜索收藏夹，空的写入进去，全满的话，提示收藏频道已满
    		String shoucang_start1= shoucang_start +count;
    		String shoucang_end1= shoucang_end +count;
    		if(count>=9){
    			flagH=1;
    		}
        	int yangshi_index = res.indexOf(shoucang_start1);
        	int yangshi_index2 = res.indexOf(shoucang_end1);
        	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
        	if(yangshi_index2-yangshi_index == 3)
        	{//10台一下
        		String s = "&&"+name+"url--"+uri+"@@";
        		
        		res = res.substring(0, yangshi_index+3)+s+
        				res.substring(yangshi_index2, res.length());
        		write(res);
        		
        		break;
        	}
        	else
        	{
        		//判断是否已经收藏的话，就返回，提示已经该频道已经被收藏
        		res_index1 = yangshi_res.indexOf(res_start);
	       	 	tv_name_index = yangshi_res.indexOf(res_url);
	       	 	res_index2 = yangshi_res.indexOf(res_end);
	       	 	if(res_index2 == -1)
	       		 break;//说明电视台资源已经不存在了，跳出
	       	 	Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
	       	 	if(Tv_name.equals(name))
	       	 	{
	       	 		break;
	       	 		
	       	 	}
        	}

    	}
    }
    
  //写入指定sd卡文件内容
  	public void write_a(String content,String file_name)
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
  					+ file_name);
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
    
}
