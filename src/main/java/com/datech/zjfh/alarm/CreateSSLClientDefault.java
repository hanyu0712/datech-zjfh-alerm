package com.datech.zjfh.alarm;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CreateSSLClientDefault {
    /**
     * HTTPS请求绕过证书检测
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            // 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            // 信任所有
            X509TrustManager x509mgr = new X509TrustManager() {
                // 该方法检查客户端的证书，若不信任该证书则抛出异常
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }

                // 该方法检查服务端的证书，若不信任该证书则抛出异常
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }

                // 返回受信任的X509证书数组。
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509mgr}, null);
            // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建默认的httpClient实例.
        return HttpClients.createDefault();
    }
}
