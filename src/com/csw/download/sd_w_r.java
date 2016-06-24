package com.csw.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.os.Environment;





public class sd_w_r{
	 static String FILE_NAME = "/TvRes.tv";
	//д��ָ��sd���ļ�����
	public static void write(String doc_str)
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
				raf.write(doc_str.getBytes());
				raf.close();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//��ȡָ��SD���ļ�����
	public static  String read()
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
	
}