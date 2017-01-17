package com.maomovie.util;

import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by yanpeng on 2016/12/12.
 * 如果要进行证书认证,可以在该方法中实现
 */
public class SSLSocketFactoryImp extends SSLSocketFactory {
    private SSLContext mCtx;

    public class SSLTrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,boolean autoClose) throws IOException, UnknownHostException {
        return mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mCtx.getSocketFactory().createSocket();
    }

    public SSLSocketFactoryImp(KeyStore truststore) throws Throwable {
        super(truststore);
        try {
            SSLTrustAllManager manager = new SSLTrustAllManager();
            mCtx = SSLContext.getInstance("SSL");
            mCtx.init(null, new TrustManager[]{manager}, null);
        } catch (Exception ex) {
        }
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
            SSLSocketFactory factory = new SSLSocketFactoryImp(trustStore);
            return factory;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
