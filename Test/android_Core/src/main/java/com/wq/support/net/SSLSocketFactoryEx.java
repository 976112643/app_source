package com.wq.support.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Https协议下的数据请求
 * @author WQ 上午10:46:23
 */
public class SSLSocketFactoryEx extends SSLSocketFactory {


    public static void setSSLSocketFactory(AsyncHttpClient client, SyncHttpClient synClient){
        try {
//			 支持https请求
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); //创建默认证书
            client.setSSLSocketFactory(new SSLSocketFactoryEx(trustStore));//给网络请求对象设置一个自定义的SSL套接字工厂(告诉他https这块的链接,用我的来处理
            synClient.setSSLSocketFactory(new SSLSocketFactoryEx(trustStore));
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    SSLContext sslContext = SSLContext.getInstance("TLS");
 
    public SSLSocketFactoryEx() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        this(KeyStore.getInstance(KeyStore.getDefaultType()));
    }
 
    public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
 
        TrustManager tm = new X509TrustManager() {
 
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
 
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
 
            }
 
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
 
            }
        };
 
        sslContext.init(null, new TrustManager[]
        { tm }, null);
    }
 
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }
 
    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}