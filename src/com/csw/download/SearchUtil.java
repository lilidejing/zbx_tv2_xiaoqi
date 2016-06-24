package com.csw.download;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class SearchUtil {

	
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		 return request;
	}
	
	public static HttpPost getHttpPost(String url){
		 HttpPost request = new HttpPost(url);
		 return request;
	}
	
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	
	public static String queryStringForPost(String url){
		HttpPost request = SearchUtil.getHttpPost(url);
		String result = null;
		try {
			HttpResponse response = SearchUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "ÍøÂçÒì³££¡-1";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "ÍøÂçÒì³££¡-2";
			return result;
		}
        return null;
    }
	
	public static String queryStringForPost(HttpPost request){
		String result = null;
		try {
			HttpResponse response = SearchUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			System.out.println("1111111111");
			result = "ÍøÂçÒì³££¡-1";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("222222222");
			result = "ÍøÂçÒì³££¡-2";
			return result;
		}
        return null;
    }
	
	public static  String queryStringForGet(String url)
	{
		HttpGet request = SearchUtil.getHttpGet(url);
		String result = null;
		try {
			
			HttpResponse response = SearchUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			System.out.println("1111111111");
			result = "ÍøÂçÒì³££¡-1";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("222222222");
			result = "ÍøÂçÒì³££¡-2";
			return result;
		}
        return null;
    }
}
