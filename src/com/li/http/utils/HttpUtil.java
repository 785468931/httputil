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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

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
	public static void main(String[] args) throws Exception {
//		gtype: "个人约战" || "球队约战"
//		httpRequest("http://qym.fandongkj.com/qdate/game/listAll","{\"gpsaddr\":\"113.382872,23.126194\",\"gform\":\"四人场\",\"page\":0,\"qdate\":\"2015-09-18\",\"userid\":\"1a074723-2023-4275-a001-1fe8d40d5cee\",\"addr\":\"东莞\",\"size\":20,\"isothertime\":true}");
//		httpRequest("http://127.0.0.1:8080/qdate/game/listAll","{\"gpsaddr\":\"113.382872,23.126194\",\"gtype\":\"球队约战\",\"gform\":\"四人场\",\"page\":0,\"qdate\":\"2015-09-18\",\"userid\":\"1a074723-2023-4275-a001-1fe8d40d5cee\",\"addr\":\"东莞\",\"size\":20,\"isothertime\":true}");
//		httpRequest("http://127.0.0.1:8080/qdate/game/cancel","{\"userid\":\"357deac5-32e1-42ce-b452-1b67cfba7b70\",\"gameid\":\"a6a4bb21-4266-48cc-b37b-debae17d129d\"}");
//		httpRequest("http://127.0.0.1:8080/qdate/activity/list","{\"gpsaddr\":\"113.388685,23.127251\",\"userid\":\"1a074723-2023-4275-a001-1fe8d40d5cee\",\"tab\":\"附近\",\"page\":0,\"size\":20}");
//		httpRequest("http://127.0.0.1:8080/qdate/message/list","{\"userid\":\"f2b1128d-6f01-4e05-9c2b-d8a4b490789b\",\"page\":1,\"size\":10}");
	
//		httpRequest("http://127.0.0.1:8080/qdate/comment/aboutme","{\"userid\":\"f2b1128d-6f01-4e05-9c2b-d8a4b490789b\",\"page\":0,\"size\":20}");
		
//		JSONObject json=new JSONObject();
//		json.put("bacth", "9d4c0aa494f4309ab34c00308be8");
//		httpRequest("http://qym.fandongkj.com/qdate/bacthListUser",json.toString());
//		httpRequest("http://qym.fandongkj.com/qdate/listuser",json.toString());
//		httpRequest("http://qym.fandongkj.com/qdate/sendvericode","{\"mobile\":\"13826026260\",\"page\":0,\"size\":20}");
		
		
		
//		JSONObject json=new JSONObject();
//		json.put("name", "abc");
//		json.put("mobile", "13826026260");
//		json.put("passwd", "1166");
//		json.put("gpsaddr", "113.25803,23.116447");
//		httpsRequest("http://127.0.0.1:8080/qdate/usrregis",json.toString());
//		{"userid":"f2b1128d-6f01-4e05-9c2b-d8a4b490789b","gameid":"a907c60b-0a1c-48e0-8570-a27613c89918"}
		JSONObject json=new JSONObject();
		json.put("norm", "13826026260");
		json.put("passwd", "111111");
		
//		String response = HttpUtil.httpsRequest("https://120.27.54.38:443/qdate/usrlogin", json.toString());
        String response = HttpUtil.httpRequest("http://120.27.54.38/qdate/usrlogin", json.toString());

	
	}
	
	/**
	 * https请求，请求内容存body里
	 * @param httpurl 请求地址
	 * @param sendStr 请求的数据
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
			System.out.println(response.getStatusLine());
		      InputStream in = entity.getContent(); 
				try {
					result=readResponse(in);
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
	/**
	 * https请求，请求内容存body里
	 * @param httpurl 请求地址
	 * @param sendStr 请求的数据
	 * @return
	 */
	public static String httpRequest(String httpurl,String sendStr){
		String result = "";
		try {
			CloseableHttpClient httpclient  = HttpClients.createDefault();
			
			HttpPost httpPost = new HttpPost(httpurl);
			httpPost.setEntity(new StringEntity(sendStr));
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(8000)
					.setConnectTimeout(8000).setSocketTimeout(8000).build();
			httpPost.setConfig(requestConfig);
			
			System.out.println("发送地址："+httpPost);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());
		      InputStream in = entity.getContent(); 
				try {
					result=readResponse(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			System.out.println("response message:" + result);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
        return result;
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
	 * @param jurl 请求地址
	 * @param data 请求参数， key为name ， value为值
	 * @return
	 */
	public  static String requestPost(String jurl, Map<String,String> data){
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
