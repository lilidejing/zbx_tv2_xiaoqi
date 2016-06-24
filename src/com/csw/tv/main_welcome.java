package com.csw.tv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import com.csw.download.HttpGetString;





import com.baidu.cyberplayersdksample1_xiaoqi.R;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.widget.Toast;


//������������Ƿ���Ҫ�����ļ��ȵ�
public class main_welcome extends Activity 
{
	 String FILE_NAME = "/TvRes.tv";
	String s = "0w1e2r3g4j5m6l7c8z9o";
	String fileName = "TvRes.txt"; //�ļ�����
	private Handler handler;                  // ����handler
	int reset_index;
	int reset_index2;
	
	int update = 0;
	
	String url_res;
	

	
	 //MAC����
    private int TV_yangshi_parse(String res,String mac)
    {
    	//TvRes.txt��Ľ�����ʶ
    	//�������¡����ӡ����ӡ��ط��ı�ʶ
    	//String yangshi_start = "##yangshi-start";
    	//String yangshi_end = "##yangshi-end";
    	String yangshi_start = "##mac-start";
    	String yangshi_end = "##mac-end";
    	//�����������̨�ı�ʶ
    	String res_start = "&&";
    	String res_end = "@@";
    	String res_url = "mac--";
    	int res_index1;
    	int res_index2;
    	int tv_name_index;
    	String Tv_name;
    	String Tv_url;
    	
    	
    	//����Ƶ������
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
        		 break;//˵������̨��Դ�Ѿ��������ˣ�����
        	 Tv_name = yangshi_res.substring(res_index1+2, tv_name_index);
        	 Tv_url = yangshi_res.substring(tv_name_index+5, res_index2);
        	 if(mac.equals(Tv_url))
        		 return 1;
        	 
        	 yangshi_res = yangshi_res.substring(res_index2+2, yangshi_res.length());       	 
        	 i=i+(res_index2-res_index1);//����@@����ʼ������һ����Դ
    	}
    	return 0;//����Ҳ���������0
    }
	
	 int createRandom(int min, int max) 
	 {
			Random random = new Random();
			return random.nextInt(max-min+1)+ min;
	}
	 
	 public String getLocalMacAddress() 
	 {
			WifiManager wifi = (WifiManager) getSystemService(main_welcome.WIFI_SERVICE);
			if(wifi == null)
				return null;
			WifiInfo info = wifi.getConnectionInfo();
			return info.getMacAddress();
	}
	 
	 void init_run()
	{
	    new Thread(new Runnable() {  
		    @Override  
		    public void run() {  
		    	  
		    	check_update();
		    }  
		}).start();  
	}
	@Override 
	protected void onCreate(Bundle savedInstanceState) 
	{
		

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
        //����Ϊ����SCREEN_ORIENTATION_LANDSCAPE ����   SCREEN_ORIENTATION_PORTRAIT
		//�������п�����Ҫ��������
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

		setContentView(R.layout.init);

		init_run();

		
	
    	

    	
    	

		
	}
	
	
	
	/**
	 * ��ָ��URL����GET����������
	 * 
	 * @param url
	 *            ���������URL
	 * @param params
	 *            ����������������Ӧ����name1=value1&name2=value2����ʽ��
	 * @return URL������Զ����Դ����Ӧ
	 */
	public static String sendGet(String url, String params)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// ����ʵ�ʵ�����
			conn.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = conn.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			for (String key : map.keySet())
			{
				System.out.println(key + "--->" + map.get(key));
			}
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += "\n" + line;
			}
		}
		catch (Exception e)
		{
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
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
		//д���ʱ�򣬻�����һ��"\n"��Ϊʲô�أ��Ժ�Ƚϵ�ʱ���ǲ��ǲ�������ȵıȽϡ�
		//����ֱ������
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
		
		public void write_update()
		{
			//��ȡ��վ�������Ϣ
	    	url_res = sendGet("http://www.csw-smart.com/TvRes.txt" , null);
			if(url_res != null)
	    	{
		    	String ver_start = "**start";
		    	String ver_end = "**end";
		    	
		    	int ver_index1 = url_res.indexOf(ver_start);
		    	int ver_index12 = url_res.indexOf(ver_end);
		    	String ver_date1 = url_res.substring(ver_index1+7, ver_index12);
		    	
		    	String ver = app_read("ver.zy");
		    	if(ver == null)
		    	{//��һ�Σ���û�����ݣ�����������İ汾��д��ȥ
		    		app_write(ver_date1,"ver.zy");
		    	}
		    	else
		    	{
		    		if(ver.indexOf(ver_date1) != -1)
		    		{//rom����������ݣ�������
		    			update = 0;
		    		}
		    		else
		    		{//rom�û�а汾�������ڣ����£�����д�������ڵ�rom��
		    			update = 1;
		    			app_write(ver_date1,"ver.zy");
		    			
		    		}
		    	}
		    	
	    	}
		}
		
  void check_update()
  {
	  
		String res=""; //����Դ���ļ���ȡ���������������֮���޸�����ַ�������д�뵽�ļ���
		String udate_date = "201206082334";//����ʱ�䣬�������Ϊ�ж�������µ�����
    	String udate_start = "%%start";
    	String udate_end = "%%end";
    	String reset_start = "!!start";
    	String reset_end = "!!end";
    	
    	//�ļ������ڻ���sd�������ڣ�����null
    	res = read();

    	if(res !=null)
    	{
    		//��������
	    	 reset_index = res.indexOf(reset_start);
	    	 reset_index2 = res.indexOf(reset_end);
    	}
    	
    	if(res == null||reset_index2 == -1)
    	{//sd�������ݣ��Լ��������������ڣ���Ҫ��д�ļ�
	    	//��ȡassets����ļ�
	    	try
	    	{ 
	    	   // \assets\TvRes.txt�������������ļ�����
	    	   InputStream in = getResources().getAssets().open(fileName);
	    	   int length = in.available();   
	    	   byte [] buffer = new byte[length];   
	    	   in.read(buffer);            
	    	   res = EncodingUtils.getString(buffer, "UTF-8");   
	    	   if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	    	   {
	    		   write(res);
	    	   }
	    	}
	    	catch(Exception e)
	    	{ 
	    		e.printStackTrace();         
	    	}
    	}
 	 reset_index = res.indexOf(reset_start);
 	 reset_index2 = res.indexOf(reset_end);
 	String reset_count = res.substring(reset_index+7, reset_index2);
 	if(reset_count.equals("5"))
 	{//���½���
 		
 		char c = reset_count.charAt(reset_count.length()-1);
 		c = '0';
 		String reset_count1 = reset_count.replace(reset_count.charAt(reset_count.length()-1), c);
 		res = res.replaceFirst(reset_count, reset_count1);
 		write(res);
 		
	    	int date_index = res.indexOf(udate_start);
	    	int date_index2 = res.indexOf(udate_end);
	    	udate_date = res.substring(date_index+7, date_index2);
			//��ȡ��վ�������Ϣ
	    	url_res = sendGet("http://www.csw-smart.com/TvRes.txt" , null);
	    	
	    	if(url_res != "")
	    	{
	        	//���½���
	        	int date_index1 = url_res.indexOf(udate_start);
	        	int date_index12 = url_res.indexOf(udate_end);
	        	String udate_date1 = url_res.substring(date_index1+7, date_index12);
	        	
	        	if(!udate_date.equals(udate_date1))
	        	{
	        		
	        		write(url_res);
	        	}
	
	    	}  
	    	

	    	
 	}
 	else
 	{
 		
 		char c = reset_count.charAt(reset_count.length()-1);
 		c++;
 		String reset_count1 = reset_count.replace(reset_count.charAt(reset_count.length()-1), c);
 		res = res.replaceFirst(reset_count, reset_count1);
 		write(res);
 	}
//		Intent intent1 = new Intent(main.this,
//				TestActivity.class);
//		startActivity(intent1);
//		main.this.finish();	
 	

 	url_res = sendGet("http://www.csw-smart.com/TvRes.txt",null);

 	if(url_res != null)
 	{
	    	String ver_start = "**start";
	    	String ver_end = "**end";
	    	
	    	int ver_index1 = url_res.indexOf(ver_start);
	    	int ver_index12 = url_res.indexOf(ver_end);
	    	String ver_date1 = url_res.substring(ver_index1+7, ver_index12);
	    	
	    	String ver = app_read("ver.zy");
	    	if(ver == null)
	    	{//��һ�Σ���û�����ݣ�����������İ汾��д��ȥ
	    		app_write(ver_date1,"ver.zy");
	    	}
	    	else
	    	{
	    		if(ver.indexOf(ver_date1) != -1)
	    		{//rom����������ݣ�������
	    			update = 0;
	    		}
	    		else
	    		{//rom�û�а汾�������ڣ����£�����д�������ڵ�rom��
	    			
	    			FILE_NAME = "/ls.tx";
	    			String gengxin_a = read();
	    			if(gengxin_a == null)
	    			{
	    				update = 1;
	    			}
	    			else
	    			{
		    			if(gengxin_a.indexOf("gengxin") != -1)
		    			{//�ڶ���������ʱ�򣬻��ٴ��ж����	
		    				update = 0;
		    				app_write(ver_date1,"ver.zy");
		    				write("sjfslkdf");//��gengxinĨ��
		    			}else
		    			{
		    				update = 1;
		    			}
	    			}
	    			
	    		}
	    	}
	    	
 	}
		Intent intent = new Intent(main_welcome.this,jl_tv.class);
		startActivity(intent);
		main_welcome.this.finish();
 	
// 	//ͨ��������жϣ��Ƿ���°汾
// 	if(update ==1)
// 	{
// 		//�汾����
// 		UpdateManager manager = new UpdateManager(this);
// 		manager.checkUpdate();    		
// 	//	MyAutoUpdate update = new MyAutoUpdate(this);
// 		
// 		//update.check();
// 	}
// 	else
// 	{
//	    	String smima = app_read("tv1.zy");
//	    	if(smima == null)
//	    	{
//	    		setContentView(R.layout.mima);
//				final EditText editText1 = (EditText)findViewById(R.id.edit1);  
//				final EditText editText2 = (EditText)findViewById(R.id.edit2);  
//				//final EditText mac_edit = (EditText)findViewById(R.id.mac_edit); 
//				Button Button = (Button) findViewById(R.id.button);
//				Button mac_Button = (Button) findViewById(R.id.mac_button);
//				
//	    		//String mac = getLocalMacAddress();
//	    		
//	    		//mac_edit.setText(mac);
//	    		
//	
//	    		mac_Button.setOnClickListener(new OnClickListener()
//				{
//					@Override
//					public void onClick(View arg0) 
//					{
//						//String mac = mac_edit.getText().toString();  
//						//��ȡ��վ�������Ϣ,���û�ж�Ӧ�ģ��͸ɵ�
//				    	//String url_mac_res = sendGet("http://www.csw-smart.com/TvMac1.txt" , null);
//				    //	int result =TV_yangshi_parse(url_mac_res,mac);
//				    //	result =1;
//				    //	if(result == 0)
//				    //	{
//							//����һ��Toast��ʾ��Ϣ
//						//	Toast toast = Toast.makeText(main.this
//							//	, "���Ƶ��ӷ�������֤����ʧ��!"
//								// ���ø�Toast��ʾ��Ϣ�ĳ���ʱ��
//							//	, Toast.LENGTH_SHORT);
//							//toast.show();
//				    		//main.this.finish();
//				    	//}
//				    	//else
//				    //	{
//							//����һ��Toast��ʾ��Ϣ
//							//Toast toast = Toast.makeText(main.this
//								//, "���Ƶ��ӷ�������֤����ɹ�!"
//								// ���ø�Toast��ʾ��Ϣ�ĳ���ʱ��
//								//, Toast.LENGTH_SHORT);
//							//toast.show();
//				    	//}
//						
//					}
//				});
//		    	
//		    	
//		    	
//				String s_new = "a"+createRandom(0,9)+createRandom(0,9)+createRandom(0,9)+
//								createRandom(0,9);
//				s_new = s_new.substring(1, 5);
//				
//	
//				
//				editText2.setText(s_new);
//				
//				Button.setOnClickListener(new OnClickListener()
//				{
//					@Override
//					public void onClick(View arg0) 
//					{
//						// TODO Auto-generated method stub
//						//TestActivity.this.finish();
//						String str = editText2.getText().toString();  
//						
//						if(str.length()== 4)
//						{
//							int a = s.indexOf(str.charAt(0));
//							int b = s.indexOf(str.charAt(1));
//							int c = s.indexOf(str.charAt(2));
//							int d = s.indexOf(str.charAt(3));
//							String new_s = "a"+s.charAt(a+1)+s.charAt(b+1)+s.charAt(c+1)+s.charAt(d+1);
//							String str1 = editText1.getText().toString(); 
//							
//							if(str1.equals(new_s))
//							{
//								app_write("!@#$%^&*","tv1.zy");
////								Intent intent = new Intent(main.this,
////										TestActivity.class);
////								startActivity(intent);
////								main.this.finish();
//							}
//							else
//								finish();
//						}
//						else
//							finish();
//					}
//					
//				});
//	    	}
//	    	else
//	    	{
//	    		if(smima.equals("!@#$%^&*\n"))
//	    		{
////					Intent intent1 = new Intent(main.this,
////							TestActivity.class);
////					startActivity(intent1);
////					main.this.finish();	
//	    		}else
//	    		{}
////	    			main.this.finish();
//	    		
//	    	}
// 	}
  }
}
