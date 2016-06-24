package com.csw.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

//import com.yunba.ives.util.ILog;

public class AsyncImageLoader {
	// SoftReference是软引用，是为了更好的为了系统回收变量
	//private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader(Context context) {
		//imageCache = new HashMap<String, SoftReference<Drawable>>();
		baseImageCatchPath = context.getFilesDir()+File.separator;
	}
//

	public synchronized Drawable loadDrawable(final String imageUrl,final LinearLayout linearLayout, 
			final ImageCallback imageCallback) {
			//ILog.w("thread:"+imageUrl);
			final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, linearLayout,imageUrl);
			}
		};
		// 建立新一个新的线程下载图片
		new Thread() {
			@Override
			public void run() {
				try{
				loadImageFromUrl_Catch(imageUrl);
				//imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		return null;
	}

	public static synchronized InputStream loadImageFromUrl(URL url) {
		InputStream i = null;
		try {
			i = (InputStream) url.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * download Image from url
	 * 
	 * @param url
	 * @return
	 */
	public static synchronized Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		if (null != d) {
			return d;
		} else {
			System.out.println("image is null");
			return null;
		}
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable,
				LinearLayout linearLayout, String imageUrl);
	}

	public static Drawable loadImageFromUrl1(String url) {
        URL m;
        InputStream i = null;
        BitmapDrawable bd = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream out =null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
            bis = new BufferedInputStream(i,1024 * 8);
            out = new ByteArrayOutputStream();
            int len=0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();
            byte[] data = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //Drawable d = Drawable.createFromStream(i, "src");
            bd = new BitmapDrawable(bitmap);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。  
        return bd;
    }
	

	public static String baseImageCatchPath;
	/**
	 * 检查缓存中是否存在某张图片
	 * @param url
	 * @return
	 */
	private static void loadImageFromUrl_Catch(String url){
		
		//得到图片名字
		String imageName = url.substring(url.lastIndexOf('/')+1);
		String imagePath = baseImageCatchPath+imageName;
		File file = new File(imagePath);
		if(file.exists() && file.isFile()){
			try{
				 drawable = new BitmapDrawable(BitmapFactory.decodeFile(imagePath));
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("problem");
				//无法加载，说明此图片有问题，删除，然后重新下载
			}
			
		}
		//直接在网上进行下载
		URL m;
        InputStream i = null;
        BufferedInputStream bis = null;
      //  ByteArrayOutputStream out =null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
            bis = new BufferedInputStream(i,1024 * 8);
            //out = new ByteArrayOutputStream();
            int len=0;
            byte[] buffer = new byte[1024];
            FileOutputStream fileout = new FileOutputStream(imagePath);
            while((len = bis.read(buffer)) != -1){
                //out.write(buffer, 0, len);
                fileout.write(buffer, 0, len);
            }
            fileout.close();
            //out.close();
            bis.close();
            
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return ;
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        //得到保存后的图片
         drawable= null;
         drawable = new BitmapDrawable(BitmapFactory.decodeFile(imagePath));
	}
	public static Drawable drawable;
	
}
