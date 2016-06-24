package com.csw.sax;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
	/***
	 * 通过http协议下载文件
	 * ***/
	private URL url = null;

	public InputStream getInputStreamFormUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection httpc = (HttpURLConnection) url.openConnection();
		return httpc.getInputStream();
	}

	public String downloadText(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader breader = null;
		try {
			url = new URL(urlStr);
			// 创建一个Http连接
			HttpURLConnection httpc = (HttpURLConnection) url.openConnection();
			// 使用IO流读取数据
			InputStream inputs = httpc.getInputStream();
			breader = new BufferedReader(new InputStreamReader(inputs));
			while ((line = breader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				breader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public int downloadFile(String urlStr, String path, String name) {
		InputStream input = null;
		try {
			FileUtils fu = new FileUtils();
			if (fu.isFileExist(path + name)) { // 是否存在文件，如果存在返回1
				return 1;
			} else {
				input = getInputStreamFormUrl(urlStr);
				File f = fu.write2SDFromInput(path, name, input);
				if (f == null) {
					return -1; // 数据有异常的时候返回-1
				}
				input.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0; // 数据正常的时候返回0
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}

}
