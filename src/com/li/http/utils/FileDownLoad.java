package com.li.http.utils;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class FileDownLoad {
	private static final Logger logger = Logger.getLogger(FileDownLoad.class);
	 /** 
    * 下载文件保存到本地 
    *  
    * @param path 
    *            文件保存位置 
    * @param url 
    *            文件地址 
    * @throws IOException 
    */  
   @SuppressWarnings("resource")
	public static void downloadFile(String path, String url) throws IOException {  
	   logger.debug("path:" + path);  
	   logger.debug("url:" + url);  
       HttpClient client = null;  
       try {  
           // 创建HttpClient对象  
           client = new DefaultHttpClient();  
           // 获得HttpGet对象  
           HttpGet httpGet = getHttpGet(url, null, null);  
           // 发送请求获得返回结果  
           HttpResponse response = client.execute(httpGet);  
           // 如果成功  
           if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
               byte[] result = EntityUtils.toByteArray(response.getEntity());  
               BufferedOutputStream bw = null;  
               try {  
                   // 创建文件对象  
                   File f = new File(path);  
                   // 创建文件路径  
                   if (!f.getParentFile().exists())  
                       f.getParentFile().mkdirs();  
                   // 写入文件  
                   bw = new BufferedOutputStream(new FileOutputStream(path));  
                   bw.write(result);  
               } catch (Exception e) {  
            	   logger.error("保存文件错误,path=" + path + ",url=" + url, e);  
               } finally {  
                   try {  
                       if (bw != null)  
                           bw.close();  
                   } catch (Exception e) {  
                	   logger.error(  
                               "finally BufferedOutputStream shutdown close",  
                               e);  
                   }  
               }  
           }  
           // 如果失败  
           else {  
               StringBuffer errorMsg = new StringBuffer();  
               errorMsg.append("httpStatus:");  
               errorMsg.append(response.getStatusLine().getStatusCode());  
               errorMsg.append(response.getStatusLine().getReasonPhrase());  
               errorMsg.append(", Header: ");  
               Header[] headers = response.getAllHeaders();  
               for (Header header : headers) {  
                   errorMsg.append(header.getName());  
                   errorMsg.append(":");  
                   errorMsg.append(header.getValue());  
               }  
               logger.error("HttpResonse Error:" + errorMsg);  
           }  
       } catch (ClientProtocolException e) {  
    	   logger.error("下载文件保存到本地,http连接异常,path=" + path + ",url=" + url, e);  
           throw e;  
       } catch (IOException e) {  
    	   logger.error("下载文件保存到本地,文件操作异常,path=" + path + ",url=" + url, e);  
           throw e;  
       } finally {  
           try {  
               client.getConnectionManager().shutdown();  
           } catch (Exception e) {  
        	   logger.error("finally HttpClient shutdown error", e);  
           }  
       }  
   }  
   /** 
    * 获得HttpGet对象 
    *  
    * @param url 
    *            请求地址 
    * @param params 
    *            请求参数 
    * @param encode 
    *            编码方式 
    * @return HttpGet对象 
    */  
   private static HttpGet getHttpGet(String url, Map<String, String> params,  
           String encode) {  
       StringBuffer buf = new StringBuffer(url);  
       if (params != null) {  
           // 地址增加?或者&  
           String flag = (url.indexOf('?') == -1) ? "?" : "&";  
           // 添加参数  
           for (String name : params.keySet()) {  
               buf.append(flag);  
               buf.append(name);  
               buf.append("=");  
               try {  
                   String param = params.get(name);  
                   if (param == null) {  
                       param = "";  
                   }  
                   buf.append(URLEncoder.encode(param, encode));  
               } catch (UnsupportedEncodingException e) {  
            	   logger.error("URLEncoder Error,encode=" + encode + ",param="  
                           + params.get(name), e);  
               }  
               flag = "&";  
           }  
       }  
       HttpGet httpGet = new HttpGet(buf.toString());  
       return httpGet;  
   }  
 
   /** 
    * 获得HttpPost对象 
    *  
    * @param url 
    *            请求地址 
    * @param params 
    *            请求参数 
    * @param encode 
    *            编码方式 
    * @return HttpPost对象 
    */  
   @SuppressWarnings("unused")
	private static HttpPost getHttpPost(String url, Map<String, String> params,  
           String encode) {  
       HttpPost httpPost = new HttpPost(url);  
       if (params != null) {  
           List<NameValuePair> form = new ArrayList<NameValuePair>();  
           for (String name : params.keySet()) {  
               form.add(new BasicNameValuePair(name, params.get(name)));  
           }  
           try {  
               UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form,  
                       encode);  
               httpPost.setEntity(entity);  
           } catch (UnsupportedEncodingException e) {  
        	   logger.error("UrlEncodedFormEntity Error,encode=" + encode  
                       + ",form=" + form, e);  
           }  
       }  
       return httpPost;  
   }  
}
