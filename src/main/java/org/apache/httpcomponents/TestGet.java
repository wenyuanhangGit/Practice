package org.apache.httpcomponents;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.SocketOptions;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TestGet {
    private static SSLConnectionSocketFactory SSL = null;
    public static void main(String[] args) throws IOException {
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(20000)
                .setTcpNoDelay(true)
                .setSndBufSize(SocketOptions.SO_SNDBUF * 2)
                .setSoKeepAlive(true)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultSocketConfig(socketConfig)
                .setSSLSocketFactory(createSSL()).build();
        HttpGet httpGet = new HttpGet("https://cn.element14.com/search?st=RC0402FR-0710KL");
//    httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//    httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.9");
//    httpGet.addHeader("Cache-Control","no-cache");
//    httpGet.addHeader("Connection","keep-alive");
////    httpGet.addHeader("Content-Type","application/json; charset=UTF-8");
//    httpGet.addHeader("Pragma","no-cache");
//    httpGet.addHeader("Upgrade-Insecure-Requests","1");
////    httpGet.addHeader("Host","so.ly.com");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
//    httpGet.addHeader("Cookie","NewProvinceId=31; NCid=383; NewProvinceName=%E6%B5%99%E6%B1%9F; NCName=%E6%9D%AD%E5%B7%9E; route=581ff9fa6fdeddf48c21a163d3b26cd3; td_sid=MTUyOTk5MTEyMCxiZTI5MmVlMDlkN2IxMDM4OTRiYzhiZWJmZWVkZjBjYTRiYzg2YTliMGJlZjJjYjM0NzVmNjJhZTFlMDU2ZjAyLGYxZTJhMzI2ODcwODhkNDEwOTFkZjFmOTgwZDQyNjc0OTc4YzRjNjBiM2ViZWE2MzkxZDc2OWY2NTBiMzZmYjc=; k_st=122.235.86.119|1529991120; td_did=RuTJROMcPn3fs0k0Av7K0fEuNYYWRMoKtU2lBmnu07gi7XpbiQQrLRMCEKWi1%2BB%2FdEoRuOuj8GEyQBU30%2FC3633arR6Qdp3r21X579p7ND5R3g1%2B0821YtXsRPHZT2PZ6LHx1jTYKvurLyAdnSRt1yH6%2FOzRk7pnnMI1VvCDwhFGrkKcSIKlBadg9BjkTwI964jImuv0rROtz0YLEu02ia5a8F91GLcGscFdb%2BKNikZUxh1%2BrGVNsdwhlT2hnx3E; t_q=1529991117667; qdid=-9999; 17uCNRefId=RefId=0&SEFrom=&SEKeyWords=; TicketSEInfo=RefId=0&SEFrom=&SEKeyWords=; CNSEInfo=RefId=0&tcbdkeyid=&SEFrom=&SEKeyWords=&RefUrl=; Hm_lvt_c6a93e2a75a5b1ef9fb5d4553a2226e5=1529991118; Hm_lpvt_c6a93e2a75a5b1ef9fb5d4553a2226e5=1529991118; __tctmd=144323752.737325; __tctma=144323752.1529991119797045.1529991119714.1529991119714.1529991119714.1; __tctmu=144323752.0.0; __tctmz=144323752.1529991119714.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); longKey=1529991119797045; __tctrack=0; dujiaUserToken=615e7aa5-b243-439d-9164-8436f3dc9664; gny_city_info=%7B%22CityId%22%3A383%2C%22CityArea%22%3A%22%E5%8D%8E%E4%B8%9C%22%2C%22CityName%22%3A%22%E6%9D%AD%E5%B7%9E%22%2C%22FullPinyinName%22%3A%22hangzhou%22%2C%22FirstZiMu%22%3A%22H%22%2C%22ProvinceId%22%3A31%2C%22ProvinceName%22%3A%22%E6%B5%99%E6%B1%9F%22%2C%22ShortPy%22%3A%22hz%22%2C%22TcShortPy%22%3A%22hz%22%7D; udc_feedback=%7B%22url%22%3A%20%22https%3A%2F%2Fgny.ly.com%2F%22%2C%22platform%22%3A%20%22PC%22%2C%22channel%22%3A%20%22%E5%9B%BD%E5%86%85%E6%B8%B8%22%2C%22page%22%3A%20%22%E5%9B%BD%E5%86%85%E5%88%97%E8%A1%A8%E9%A1%B5%22%7D; pagestate=1; dj-koalaman=aa730fcdcd5480c91478d262e8a44dc0; __tctmc=144323752.223197849; _ga=GA1.2.1796302114.1529991169");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() == 200) {
            String responseContent = EntityUtils.toString(entity);
            System.out.println("responseContent: \n" + responseContent);
        }
    }
    private static SSLConnectionSocketFactory createSSL() {
        if (SSL != null) return SSL;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            // Allow TLSv1 protocol only
            SSL = new SSLConnectionSocketFactory(sslcontext,
                    new String[] { "TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch (Exception e) {

        }
        if (SSL == null) SSL = SSLConnectionSocketFactory.getSocketFactory();
        return SSL;
    }
}
