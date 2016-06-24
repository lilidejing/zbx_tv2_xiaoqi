package com.csw.download;  
  
import java.io.IOException;  
import java.util.List;  
  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.params.BasicHttpParams;  
import org.apache.http.params.HttpConnectionParams;  
import org.apache.http.params.HttpParams;  
  
import android.util.Log;  
  
public class gengxin_tv_res {  

    
    
    public static void gengxin_tv_res_init()
    {
    	String url_res = HttpGetString.sendGet("http://www.csw-smart.com/TvRes.txt" , null);
    	if(url_res !=""||url_res != null)
    		sd_w_r.write(url_res);
    
    }
      

}  
