package com.wq.support.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
/** {@value} */
// milliseconds
/** {@value} */
// milliseconds
// init sf here
//自定义SSL套接字工厂
// TODO Auto-generated catch block
// TODO Auto-generated catch block
//设置请求时不去检查证书域名与请求域名是否一致
public  class SecureImageDownloader extends BaseImageDownloader {
 
        /** {@value} */
        public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
        /** {@value} */
        public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds
 
        private int connectTimeout;
        private int readTimeout;
 
        private SSLSocketFactory sf;
 
        public SecureImageDownloader(Context context) {
            this(context, DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
        }
 
        public SecureImageDownloader(Context context, int connectTimeout, int readTimeout) {
            super(context);
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            initSSLSocketFactory();
        }
 
        private void initSSLSocketFactory() {
            // init sf here
            try {
                sf = new ImgSSLSocketFactoryEx();//自定义SSL套接字工厂
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
 
        @Override
        public InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) new URL(imageUri).openConnection();
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            if (conn instanceof HttpsURLConnection) {
                //设置请求时不去检查证书域名与请求域名是否一致
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                });
                ((HttpsURLConnection) conn).setSSLSocketFactory(sf);
            }
 
            return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
        }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        imageUri="file://"+imageUri;
        return super.getStreamFromFile(imageUri, extra);
    }

    /**
         * https协议下的图片加载
         * @author WQ 上午10:44:55
         */
        public static class ImgSSLSocketFactoryEx extends SSLSocketFactory {
            SSLContext sslContext = SSLContext.getInstance("TLS");
         
            public ImgSSLSocketFactoryEx() throws NoSuchAlgorithmException, KeyManagementException{
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
            public String[] getDefaultCipherSuites() {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().getDefaultCipherSuites();
            }
         
            @Override
            public String[] getSupportedCipherSuites() {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().getSupportedCipherSuites();
            }
         
            @Override
            public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
                // TODO Auto-generated method stub
                return  sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
            }
         
            @Override
            public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().createSocket(host, port);
            }
         
            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
            }
         
            @Override
            public Socket createSocket(InetAddress host, int port) throws IOException {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().createSocket(host, port);
            }
         
            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
                // TODO Auto-generated method stub
                return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
            }
         
        }
    }

