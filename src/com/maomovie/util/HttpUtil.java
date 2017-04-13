package com.maomovie.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YanP on 2016/8/17.
 */
public class HttpUtil {
    private static final String DEF_CHATSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static String apiUrl = "http://m.maoyan.com/";
    private String eamUrl = "http://Mobileapproval.scmcc.com.cn:8999/EAMManagement/{actionPath}";

    /**
     * 1.获取城市列表
     * @return
     */
    public static Object getCitys(){
        String url = apiUrl + "changecity.json";
        try {
            return net(url,null,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 后台会自动根据IP返回热播电影
     * type=hot 类型
     * @param offset : 数据开始位置
     * limit ：偏移量
     * @return
     */
    public static Object getHotMovie(int offset){
        String url = "http://m.maoyan.com/movie/list.json?type=hot&offset=" +offset+ "0&limit=20";
        try {
            return net(url,null,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 通过电影Id获取预告片资源
     * @param movieId
     * @return
     */
    public static String getMovieVideoUrl(int movieId){
        String url = "http://api.meituan.com/mmdb/movie/list/info.json?ci=59&headline=0&movieIds={movieId}&__vhost=api.maoyan.com";
        url = url.replace("{movieId}",String.valueOf(movieId));
        try {
            Object result = net(url,null,"GET");
            if(result == null){
                return null;
            }
            JSONObject resultJson = new JSONObject(result.toString());
            JSONObject dataJson = resultJson.optJSONObject("data");
            JSONArray moviesJson = dataJson.optJSONArray("movies");
            String videoUrl = moviesJson.optJSONObject(0).optString("videourl");
            return videoUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 2.获取城市影院列表
     * @param cityName 城市名称(如北京)
     * @return
     */
    public static Object getCinemasOfCity(String cityName){
        String url = apiUrl + "cinemas.json";
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("ct",cityName);
        try {
            return net(url,params,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    /**
     * 3.获取正在上映电影
     * @param cityName 城市名称(如北京)
     * @return
     */
    public static Object getPlayingMovies(String cityName){
        String url = apiUrl + "movies.json";
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("ct",cityName);
        try {
            return net(url,params,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    /**
     * 4.请求影院某日影讯
     * @param date      日期(今/明/后天,格式如2013-07-16)
     * @param cinemaId  影院id
     * @return
     */
    public static Object getMoviesOfCinemaByDate(String date,int cinemaId){
        String url = apiUrl + "/v1/cinema/{cinemaId}/shows.json";
        url = url.replace("{cinemaId}",String.valueOf(cinemaId));
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("dt",date);
        try {
            return net(url,params,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 5.根据影片获取上映影院的场次
     * @param date      日期(今/明/后天,格式如2013-07-16)
     * @param cityName  城市名称(如北京)
     * @param movieId   影片id
     * @return
     */
    public static Object getSessionOfCinemaByMovieid(String date,String cityName,int movieId){
        String url = apiUrl + "movie/{movieId}/shows.json";
        url = url.replace("{movieId}", String.valueOf(movieId));
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("dt",date);
        params.put("ct",cityName);
        try {
            return net(url,params,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 6.根据影院获取上映电影和场次
     * @param cinemaId  影院id
     * @return
     */
    public static Object getPlayingMoviesByCinemaId(int cinemaId){
        String url = apiUrl + "/v1/cinema/{cinemaId}/movies/shows.json";
        url = url.replace("{cinemaId}", String.valueOf(cinemaId));
        try {
            return net(url,null,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 7.查出电影详情(包含评论)
     * @param movieId
     * @return
     */
    public static Object getMovieDetailById(int movieId){
        String url = "http://m.maoyan.com/movie/{movieId}.json";
        url = url.replace("{movieId}",String.valueOf(movieId));
        try {
            return net(url,null,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     *
     * @param movieId   电影id
     * @param limit     每页查询条数
     * @param offset    查询第几页
     * @return
     */
    public static Object getCommentsJson(int movieId,int limit,int offset){
        String url = "http://m.maoyan.com/comments.json?movieid={movieId}&limit={limit}&offset={offset}";
        url = url.replace("{movieId}",String.valueOf(movieId));
        url = url.replace("{limit}",String.valueOf(limit));
        url = url.replace("{offset}",String.valueOf(offset));
        try {
            return net(url,null,"GET");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    private static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if ((method == null || method.equals("GET")) && params != null) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private HttpClient getHttpClient() {
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
        HttpParams httpParams = new BasicHttpParams();
        // 设置连接超时和 读取超时，以及 Socket 缓存大小
        HttpConnectionParams.setConnectionTimeout(httpParams, 300 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 100 * 1000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        // 设置重定向，缺省为 true
        HttpClientParams.setRedirecting(httpParams, true);
        // 创建一个 HttpClient 实例
        // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
        // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

        return new DefaultHttpClient(httpParams);
    }

    /**
     * 获取HttpClient
     *
     * @return
     */
    private HttpClient getHttpClientForHttps() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory factory = SSLSocketFactoryImp.getSocketFactory();
            // 这里就是我们最需要的也是最关键的一步，设置主机认证,通过API发现有一个常量就是允许所有认证通过。
            factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
            HttpParams httpParams = new BasicHttpParams();
            // 设置连接超时和 读取超时，以及 Socket 缓存大小
            HttpConnectionParams.setConnectionTimeout(httpParams, 300 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 100 * 1000);
            HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
            //通过Http适配器设置必要参数，现在通用HTTP1.1协议,和UTF-8字符。
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
//            HttpProtocolParams.setUseExpectContinue(httpParams, true);
            // 设置http https支持
            SchemeRegistry registry = new SchemeRegistry();
            //添加http协议
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            // SSL/TSL的认证过程，端口为443
            registry.register(new Scheme("https", factory, 443));
            // ClientConnectionManager是一个借口，就实现他的子类,同样需要2个参数，第一个我们熟悉，
            // 第二个就是让我们自定义自己的一套方案协议，继续在上面一步一步完成；
            ClientConnectionManager conman = new SingleClientConnManager(httpParams, registry);
            // 返回我们需要的一个默认Httpclient，为了把之前做的关联起来，就new最多参数的构造函数，需要2个参数，Httpparams是我们熟悉的，
            // 发现ClientConnectionManager不太熟悉，通过API发现这是客服端连接管理者,既然这样，就在上面一步一步完成。
            return new DefaultHttpClient(conman, httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpClient();
    }



    /**
     * 功能:通过HttpClient发起Post请求 ---- 使用此方法调用EAM系统后台服务
     * @param jsonParams
     * @param actionPath
     * @return
     */
    public Object invoke(String jsonParams,String actionPath) {
        try {
            eamUrl = eamUrl.replace("{actionPath}",actionPath);
            HttpPost httpRequest = new HttpPost(eamUrl);
            jsonParams = URLEncoder.encode(jsonParams,"utf-8");
            httpRequest.setEntity(new ByteArrayEntity(jsonParams.getBytes()));
//            HttpClient httpClient = getHttpClient();
            HttpClient httpClient = getHttpClientForHttps();
            HttpResponse response = httpClient.execute(httpRequest);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                return new RuntimeException("通信出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

}
