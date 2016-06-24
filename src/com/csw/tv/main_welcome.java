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


//在这个软件里搞是否需要更新文件等等
public class main_welcome extends Activity 
{
	 String FILE_NAME = "/TvRes.tv";
	String s = "0w1e2r3g4j5m6l7c8z9o";
	String fileName = "TvRes.txt"; //文件名字
	private Handler handler;                  // 声明handler
	int reset_index;
	int reset_index2;
	
	int update = 0;
	
	String url_res;
	

	
	 //MAC解析
    private int TV_yangshi_parse(String res,String mac)
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
		
		
        //设置为横屏SCREEN_ORIENTATION_LANDSCAPE 竖屏   SCREEN_ORIENTATION_PORTRAIT
		//横屏，有可能需要启动两次
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

		setContentView(R.layout.init);

		init_run();

		
	
    	

    	
    	

		
	}
	
	
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String params)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet())
			{
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应
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
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
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
		
		public void write_update()
		{
			//获取网站上面的信息
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
		    	{//第一次，还没有数据，将网络上面的版本，写进去
		    		app_write(ver_date1,"ver.zy");
		    	}
		    	else
		    	{
		    		if(ver.indexOf(ver_date1) != -1)
		    		{//rom里，有网络数据，不更新
		    			update = 0;
		    		}
		    		else
		    		{//rom里，没有版本更新日期，更新，并且写入新日期到rom里
		    			update = 1;
		    			app_write(ver_date1,"ver.zy");
		    			
		    		}
		    	}
		    	
	    	}
		}
		
  void check_update()
  {
	  
		String res=""; //总资源，文件读取出来，就是在这里，之后修改这个字符串，就写入到文件里
		String udate_date = "201206082334";//更新时间，这个将作为判断网络更新的依据
    	String udate_start = "%%start";
    	String udate_end = "%%end";
    	String reset_start = "!!start";
    	String reset_end = "!!end";
    	
    	//文件不存在或者sd卡不存在，返回null
    	res = read();

    	if(res !=null)
    	{
    		//启动次数
	    	 reset_index = res.indexOf(reset_start);
	    	 reset_index2 = res.indexOf(reset_end);
    	}
    	
    	if(res == null||reset_index2 == -1)
    	{//sd卡无数据，以及启动次数不存在，都要重写文件
	    	//读取assets里的文件
	    	try
	    	{ 
	    	   // \assets\TvRes.txt这里有这样的文件存在
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
 	{//更新解析
 		
 		char c = reset_count.charAt(reset_count.length()-1);
 		c = '0';
 		String reset_count1 = reset_count.replace(reset_count.charAt(reset_count.length()-1), c);
 		res = res.replaceFirst(reset_count, reset_count1);
 		write(res);
 		
	    	int date_index = res.indexOf(udate_start);
	    	int date_index2 = res.indexOf(udate_end);
	    	udate_date = res.substring(date_index+7, date_index2);
			//获取网站上面的信息
	    	url_res = sendGet("http://www.csw-smart.com/TvRes.txt" , null);
	    	
	    	if(url_res != "")
	    	{
	        	//更新解析
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
	    	{//第一次，还没有数据，将网络上面的版本，写进去
	    		app_write(ver_date1,"ver.zy");
	    	}
	    	else
	    	{
	    		if(ver.indexOf(ver_date1) != -1)
	    		{//rom里，有网络数据，不更新
	    			update = 0;
	    		}
	    		else
	    		{//rom里，没有版本更新日期，更新，并且写入新日期到rom里
	    			
	    			FILE_NAME = "/ls.tx";
	    			String gengxin_a = read();
	    			if(gengxin_a == null)
	    			{
	    				update = 1;
	    			}
	    			else
	    			{
		    			if(gengxin_a.indexOf("gengxin") != -1)
		    			{//第二次启动的时候，会再次判断这个	
		    				update = 0;
		    				app_write(ver_date1,"ver.zy");
		    				write("sjfslkdf");//将gengxin抹除
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
 	
// 	//通过上面的判断，是否更新版本
// 	if(update ==1)
// 	{
// 		//版本更新
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
//						//获取网站上面的信息,如果没有对应的，就干掉
//				    	//String url_mac_res = sendGet("http://www.csw-smart.com/TvMac1.txt" , null);
//				    //	int result =TV_yangshi_parse(url_mac_res,mac);
//				    //	result =1;
//				    //	if(result == 0)
//				    //	{
//							//创建一个Toast提示信息
//						//	Toast toast = Toast.makeText(main.this
//							//	, "智云电视服务器认证激活失败!"
//								// 设置该Toast提示信息的持续时间
//							//	, Toast.LENGTH_SHORT);
//							//toast.show();
//				    		//main.this.finish();
//				    	//}
//				    	//else
//				    //	{
//							//创建一个Toast提示信息
//							//Toast toast = Toast.makeText(main.this
//								//, "智云电视服务器认证激活成功!"
//								// 设置该Toast提示信息的持续时间
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
