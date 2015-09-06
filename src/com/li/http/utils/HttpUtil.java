package com.li.http.utils;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

@SuppressWarnings({ "deprecation", "resource" })
public class HttpUtil {
	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	
	public static String readResponse(InputStream in) throws Exception{  
		StringBuilder buider=new StringBuilder();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
	    String line = null;  
	    while ((line = reader.readLine()) != null) {  
	        buider.append(line);
	    }  
	    return buider.toString();
	}  
	public static void main(String[] args) throws Exception {
		
	}
	
	/**
	 * https请求，请求内容存body里
	 * @param httpurl
	 * @param sendStr
	 * @return
	 */
	public static String httpsRequest(String httpurl,String sendStr){
		String result = "";
		try {
			SSLContext sslContext = buildSSLContext();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
			        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			
			HttpPost httpPost = new HttpPost(httpurl);
			httpPost.setEntity(new StringEntity(sendStr));
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(8000)
					.setConnectTimeout(8000).setSocketTimeout(8000).build();
			httpPost.setConfig(requestConfig);
			
			System.out.println("发送地址："+httpPost);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
		      InputStream in = entity.getContent(); 
				try {
					result=readResponse(in);
					System.out.println(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			System.out.println("response message:" + result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
	}
  
  private static SSLContext buildSSLContext()
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException {
        SSLContext sslcontext = SSLContexts.custom()
                .setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustStrategy() {
                    public boolean isTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        return true;
                    }
                })
                .build();
        return sslcontext;
    }

	 /** 
   * 下载文件保存到本地 
   *  
   * @param path 
   *            文件保存位置 
   * @param url 
   *            文件地址 
   * @throws IOException 
   */  
	public static void downloadFile(String path, String url) throws IOException {  
      logger.error("path:" + path);  
      logger.error("url:" + url);  
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
	
	/**
	 * http 请求
	 * @param jurl
	 * @param data
	 * @return
	 */
	public  static String requestJoysim(String jurl, Map<String,String> data){
		HttpPost post = new HttpPost(jurl); 
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		for(String name: data.keySet()) {
			params.add(new BasicNameValuePair(name, data.get(name)));  
		}
		String str="";
		try {
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient http = new DefaultHttpClient();  
			HttpResponse response = http.execute(post); 
			HttpEntity entity = response.getEntity();  
			InputStream in = entity.getContent(); 
			str=readResponse(in);
			return str;  
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			return "";  
		}  
	}
	
	
}
