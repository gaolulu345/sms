package com.tp.admin.manage.impl;

import com.tp.admin.common.ConfigUtil;
import com.tp.admin.common.MiniConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class ClientCustomSSL {

	private static final Logger logger = LoggerFactory.getLogger(ClientCustomSSL.class);

	/**
	 * 请求，只请求一次，不做重试
	 * @param domain
	 * @param data
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @param useCert 是否使用证书，针对退款、撤销等操作
	 * @return
	 * @throws Exception
	 */
	public static String requestOnce(final String domain, String data, int connectTimeoutMs, int readTimeoutMs, boolean
			useCert) throws Exception {
		BasicHttpClientConnectionManager connManager;
		if (useCert) {
			// 证书
			char[] password = MiniConstant.WxMchID.toCharArray();
//			File sertFile = ResourceUtils.getFile("classpath:cert"+ System.getProperty("file.separator") +
//					"apiclient_cert.p12");
//			InputStream certStream = new FileInputStream(sertFile);
			ClassPathResource resource = new ClassPathResource("cert/application.yml");
			InputStream certStream = resource.getInputStream();
			if (null == certStream) {
				logger.error(" logger null ");
			}
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(certStream, password);
			// 实例化密钥库 & 初始化密钥工厂
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);
			// 创建 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					sslContext,
					new String[]{"TLSv1"},
					null,
					new DefaultHostnameVerifier());

			connManager = new BasicHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory>create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", sslConnectionSocketFactory)
							.build(),
					null,
					null,
					null
			);
		}
		else {
			connManager = new BasicHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory>create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", SSLConnectionSocketFactory.getSocketFactory())
							.build(),
					null,
					null,
					null
			);
		}
		HttpClient httpClient = HttpClientBuilder.create()
				.setConnectionManager(connManager)
				.build();
		String url = domain ;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
		httpPost.setConfig(requestConfig);
		StringEntity postEntity = new StringEntity(data, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", ConfigUtil.USER_AGENT + " " + MiniConstant.WxMchID);
		httpPost.setEntity(postEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");

	}
}
