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

	String fileName = "TvRes.txt"; //�ļ�����

   public  static int flagH=0;//��������ղز˵��Ƿ��ղ�����
	public void wr_file( )//Context context)
	{
		//mContext = context;
	}
	
	

	//��ȡָ��SD���ļ�����
	public String read()
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
				Log.d("���ļ��ĵ�ַ", sdCardDir
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
	public void write(String content)
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
				Log.d("д�ļ��ĵ�ַ", sdCardDir
						.getCanonicalPath()	+ FILE_NAME);
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
	

	 //�����ղ�
    public void write_shoucang(String uri)
    {
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
    	
    	
    	//��sd��TvRes.txt��������Ӧ��uri����̨����
//    	FILE_NAME = "/TvRes.tv";
    	FILE_NAME="/TvRes.txt";//�Ķ��ĵط�
    	res = read();
    	if(res == null)
    	{
    		//ʹ��toast��ʾ����ʾsd��������
    		return;
    	}
    	int uri_indexof = res.indexOf(uri);
    	//25,�Ѿ��㹻����
    	String dianshi_res = res.substring(uri_indexof-25, uri_indexof);
    	String name = dianshi_res.substring(dianshi_res.indexOf("&&")+2, 
    												dianshi_res.indexOf(res_url));
    	
    	
    	FILE_NAME = "/shoucang.txt";
    	res = read();
    	if(res == null)
    	{
    		//ʹ��toast��ʾ����ʾsd��������
    		return;
    	}
    	String shoucang_start = "**";
    	String shoucang_end = "##";
    	for(int count =0;count<15;count++)
    	{//�����ղؼУ��յ�д���ȥ��ȫ���Ļ�����ʾ�ղ�Ƶ������
    		String shoucang_start1= shoucang_start +count;
    		String shoucang_end1= shoucang_end +count;
    		if(count>=9){
    			flagH=1;
    		}
        	int yangshi_index = res.indexOf(shoucang_start1);
        	int yangshi_index2 = res.indexOf(shoucang_end1);
        	String yangshi_res = res.substring(yangshi_index, yangshi_index2);
        	if(yangshi_index2-yangshi_index == 3)
        	{//10̨һ��
        		String s = "&&"+name+"url--"+uri+"@@";
        		
        		res = res.substring(0, yangshi_index+3)+s+
        				res.substring(yangshi_index2, res.length());
        		write(res);
        		
        		break;
        	}
        	else
        	{
        		//�ж��Ƿ��Ѿ��ղصĻ����ͷ��أ���ʾ�Ѿ���Ƶ���Ѿ����ղ�
        		res_index1 = yangshi_res.indexOf(res_start);
	       	 	tv_name_index = yangshi_res.indexOf(res_url);
	       	 	res_index2 = yangshi_res.indexOf(res_end);
	       	 	if(res_index2 == -1)
	       		 break;//˵������̨��Դ�Ѿ��������ˣ�����
	       	 	Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
	       	 	if(Tv_name.equals(name))
	       	 	{
	       	 		break;
	       	 		
	       	 	}
        	}

    	}
    }
    
  //д��ָ��sd���ļ�����
  	public void write_a(String content,String file_name)
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
  					+ file_name);
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
    
}
