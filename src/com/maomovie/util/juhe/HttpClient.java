package com.maomovie.util.juhe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import com.maomovie.R;
import com.maomovie.sqlite.service.CinemaService;
import com.maomovie.sqlite.service.TodayMovieService;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.maomovie.entity.AroundCinemaEntity;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.MovieEntity;
import com.maomovie.entity.MoviePlayingInfoEntity;
import com.maomovie.entity.MoviesOfCinemaEntity;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.entity.TodayMovieEntity;
import com.maomovie.util.GsonUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 业务功能：请求聚合数据
 */
public class HttpClient {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static HashMap<String, Object> resultMap;//存放返回结果
    private static Context context;

    public HttpClient(Context context) {
        this.context = context;
    }

    // 配置您申请的KEY
    public static final String APPKEY = "60931c3ccabc335654a9a7b3116f3111";

    // 1.按关键字检索影片信息
    public static Object getMovieByKey(String title) {
        String result = null;
        resultMap = new HashMap<String, Object>();
        String url = "http://v.juhe.cn/movie/index";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("title", title);// 需要检索的影片标题,utf8编码的urlencode
        params.put("smode","");//<font color=red>是否精确查找，精确:1 模糊:0  默认1</font>
        params.put("pagesize","");//<font color=red>每次返回条数，默认20,最大50</font>
        params.put("offset","");//<font color=red>偏移量，默认0,最大760</font>
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 1);
    }


    // 2.检索周边影院
    public static Object getCinemasAround(long lat, long lon) {
        String url = "http://v.juhe.cn/movie/cinemas.local";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("lat", lat);// 纬度，百度地图坐标系
        params.put("lon", lon);// 经度，百度地图坐标系
        params.put("radius", "");// 检索半径(米)，最大3000
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 2);
    }

    /**
     * 3.关键字影院检索
     *
     * @param cityId  城市Id
     * @param keyWord 关键字
     * @return
     */
    public static Object getCimemaByKey(String cityId, String keyWord, String pageIndex) {
        String url = "http://v.juhe.cn/movie/cinemas.search";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("cityid", cityId);// 城市ID
        params.put("keyword", keyWord);// 关键字，urlencode utf8
        params.put("page", pageIndex);// 页数，默认1
        params.put("pagesize", "");// 每页返回条数，默认20
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 3);
    }

    /**
     * 4.影院上映影片信息
     * @param cinemaID  影院ID
     * @return
     */
    public static Object getMoviesOfCinema(String cinemaID) {
        String url = "http://v.juhe.cn/movie/cinemas.movies";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("cinemaid", cinemaID);// 影院ID
        params.put("movieid", "");// 指定电影ID，默认全部电影
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 4);
    }

    /**
     * 5.今日放映影片
     * @param cityId    城市ID
     * @return
     */
    public static Object getMoviesOfToday(int cityId) {
        resultMap = new HashMap<>();
        String url = "http://v.juhe.cn/movie/movies.today";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("cityid", cityId);// 城市ID
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 5);
    }

    // 6.支持城市列表
    public static Object getSupportCitys() {
        String url = "http://v.juhe.cn/movie/citys";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 6);
    }

    /**
     * 7.影片上映影院查询
     * @param cityid    城市ID
     * @param movieid   影片ID
     * @return
     */
    public static Object getPlayingMovInCinemas(int cityid, int movieid) {
        String url = "http://v.juhe.cn/movie/movies.cinemas";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("cityid", cityid);// 城市ID
        params.put("movieid", movieid);// 影片ID
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 7);
    }

    // 8.按影片ID检索影片信息
    public static Object getMovInfoByID() {
        String url = "http://v.juhe.cn/movie/query";// 请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();// 请求参数
        params.put("movieid", "");// 需要检索的影片id
        params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml/json，默认json

        return getResult(url, params, 8);
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
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

    /**
     * 功能：联网获取数据后，进行解析，并返回解析后的结果
     *
     * @param url    接口地址
     * @param params 请求参数
     * @param method 方法类型(1.按关键字检索影片信息 2.检索周边影院 3.关键字影院检索 4.影院上映影片信息
     *               5.今日放映影片 6.支持城市列表 7.影片上映影院查询 8.按影片ID检索影片信息)
     * @return
     */
    private static Object getResult(String url, Map<String, Object> params, int method) {
        String result = null;
        resultMap = new HashMap<String, Object>();
        try {
            if (method == 5) {
                Object cityid = params.get("cityid");
                if (String.valueOf(cityid).equals("6"))
                    result = "{\"reason\":\"success\",\"result\":[{\"movieId\":\"235670\",\"movieName\":\"老阿姨\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/19\\/152744.36484935.jpg\"},{\"movieId\":\"235302\",\"movieName\":\"岛囧\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/06\\/29\\/104416.16411936.jpg\"},{\"movieId\":\"232596\",\"movieName\":\"那件疯狂的小事叫爱情\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/08\\/02\\/104016.77231451.jpg\"},{\"movieId\":\"232347\",\"movieName\":\"神秘世界历险记3\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/13\\/163907.15134658.jpg\"},{\"movieId\":\"231018\",\"movieName\":\"使徒行者\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/25\\/094359.83892335.jpg\"},{\"movieId\":\"230237\",\"movieName\":\"微微一笑很倾城\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/08\\/11\\/101330.81799318.jpg\"},{\"movieId\":\"228965\",\"movieName\":\"寒战2\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/06\\/21\\/103126.35142042.jpg\"},{\"movieId\":\"228316\",\"movieName\":\"恐怖爱情故事之死亡公路\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/12\\/100335.99832024.jpg\"},{\"movieId\":\"225283\",\"movieName\":\"我最好朋友的婚礼\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/08\\/103734.67238239.jpg\"},{\"movieId\":\"224572\",\"movieName\":\"诺亚方舟漂流记\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/27\\/100741.27854336.jpg\"},{\"movieId\":\"223848\",\"movieName\":\"爱宠大机密\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/06\\/21\\/093148.32874907.jpg\"},{\"movieId\":\"221410\",\"movieName\":\"我是证人\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2015\\/10\\/16\\/091257.73515636.jpg\"},{\"movieId\":\"218781\",\"movieName\":\"谎言西西里\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/27\\/095434.70739552.jpg\"},{\"movieId\":\"218762\",\"movieName\":\"煎饼侠\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2015\\/06\\/29\\/100849.10734933.jpg\"},{\"movieId\":\"217604\",\"movieName\":\"危城\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/14\\/094732.39398922.jpg\"},{\"movieId\":\"216992\",\"movieName\":\"夏有乔木 雅望天堂\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/14\\/123612.59566757.jpg\"},{\"movieId\":\"216896\",\"movieName\":\"横冲直撞好莱坞\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2015\\/03\\/20\\/085706.97615725.jpg\"},{\"movieId\":\"215308\",\"movieName\":\"我们诞生在中国\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/28\\/094048.21262950.jpg\"},{\"movieId\":\"214597\",\"movieName\":\"古曼\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/21\\/093646.97717809.jpg\"},{\"movieId\":\"209680\",\"movieName\":\"封神传奇\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/14\\/095810.85051815.jpg\"},{\"movieId\":\"209183\",\"movieName\":\"泰山归来：险战丛林\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/06\\/13\\/092300.34033280.jpg\"},{\"movieId\":\"209135\",\"movieName\":\"海洋之歌\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/06\\/21\\/123138.42827202.jpg\"},{\"movieId\":\"208076\",\"movieName\":\"港囧\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2015\\/09\\/21\\/113105.22486491.jpg\"},{\"movieId\":\"206431\",\"movieName\":\"冲上云霄\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2015\\/01\\/22\\/090643.72973828.jpg\"},{\"movieId\":\"203656\",\"movieName\":\"盗墓笔记\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/08\\/01\\/100832.53897274.jpg\"},{\"movieId\":\"199602\",\"movieName\":\"绝地逃亡\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2016\\/07\\/06\\/175710.17297735.jpg\"},{\"movieId\":\"164046\",\"movieName\":\"人再囧途之泰囧\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2012\\/11\\/30\\/141128.82532238.jpg\"},{\"movieId\":\"136204\",\"movieName\":\"李献计历险记\",\"pic_url\":\"http:\\/\\/img21.mtime.cn\\/mt\\/2011\\/09\\/06\\/223948.70011895.jpg\"},{\"movieId\":\"104696\",\"movieName\":\"叶问2：宗师传奇\",\"pic_url\":\"http:\\/\\/img31.mtime.cn\\/mt\\/2014\\/02\\/23\\/072513.32962062.jpg\"}],\"error_code\":0}";
                else
                    result = net(url, params, "GET");
            } else if (method == 3) {
                Object cityid = params.get("cityid");
                if (String.valueOf(cityid).equals("6"))
                    result = "{\"reason\":\"success\",\"result\":{\"data\":[{\"id\":\"551\",\"cityName\":\"成都\",\"cinemaName\":\"保利国际影城成都奥克斯店\",\"address\":\"成都市高新区锦城大道西段666号奥克斯广场4楼\",\"telephone\":\"028-85980808\",\"latitude\":\"30.58203\",\"longitude\":\"104.0693\",\"trafficRoutes\":\"地铁一号线孵化园站A口出\n软件园站：26路、84路、115路、124路、184路、188路、236路、815路\n锦城大道天府大道口站：84路、188路\n天府大道北段站：118路、501路、504路806路\"},{\"id\":\"552\",\"cityName\":\"成都\",\"cinemaName\":\"保利万和国际影城成都财富又一城店\",\"address\":\"成都市府青路二段2号财富又一城5楼\",\"telephone\":\"\",\"latitude\":\"30.68356\",\"longitude\":\"104.1014\",\"trafficRoutes\":\" 乘坐7、49路公车直达；附近车站：27、34、45、42、65、53路需步行10分钟\"},{\"id\":\"553\",\"cityName\":\"成都\",\"cinemaName\":\"成都蛟龙国际电影城\",\"address\":\"成都市蛟龙工业港双流园区蛟龙大道附1号\",\"telephone\":\"028-85738899\",\"latitude\":\"30.6095\",\"longitude\":\"103.9187\",\"trafficRoutes\":\"双流6路 368 813 818 840至双流蛟龙国际紫荆电影城下车即到\"},{\"id\":\"554\",\"cityName\":\"成都\",\"cinemaName\":\"SFC上影（成都科华店）\",\"address\":\"成都市武候区科华中路9号王府井百货2店5F\",\"telephone\":\"028-85222236\",\"latitude\":\"30.62411\",\"longitude\":\"104.0827\",\"trafficRoutes\":\"55路、76路、114路、112路，49路、97路、92路科华路二环路口站下车步行100米到达\"},{\"id\":\"555\",\"cityName\":\"成都\",\"cinemaName\":\"339电影城（原新时代高塔电影城旗舰店）\",\"address\":\"成都市成华区猛追湾四川电视塔339欢乐颂A区四楼\",\"telephone\":\"028-83525339\",\"latitude\":\"30.66685\",\"longitude\":\"104.1\",\"trafficRoutes\":\"106路、20路、237路、341路、61路、8路、76路、6路公交路线直达\"},{\"id\":\"556\",\"cityName\":\"成都\",\"cinemaName\":\"幸福蓝海国际影城（成都仁和春天光华店）\",\"address\":\"四川省成都市青羊区二环路西二段19号仁和春天广场C座5楼\",\"telephone\":\"028-61500800\",\"latitude\":\"30.67013\",\"longitude\":\"104.0274\",\"trafficRoutes\":\"35路、51路、52路、58路、59路、77路、319路、309路、111路\"},{\"id\":\"557\",\"cityName\":\"成都\",\"cinemaName\":\"成都市和平电影院\",\"address\":\"成都市草市街71号\",\"telephone\":\"028-66172970\",\"latitude\":\"30.67667\",\"longitude\":\"104.0822\",\"trafficRoutes\":\"1、3、5、18、23、45、61、62、80、106、302路\"},{\"id\":\"558\",\"cityName\":\"成都\",\"cinemaName\":\"卢米埃成都来福士影城\",\"address\":\"成都武侯区人民南路四段3号来福士广场3层\",\"telephone\":\"028-85083341-801\",\"latitude\":\"30.63765\",\"longitude\":\"104.074\",\"trafficRoutes\":\"10、12、16、19、45、72、300、303、504公交，乘坐地铁一号线至省体育馆站直达\"},{\"id\":\"559\",\"cityName\":\"成都\",\"cinemaName\":\"成都UME国际影城（成华店）\",\"address\":\"成都市成华区二环路东二段一号龙湖三千集5楼\",\"telephone\":\"028-84406111\",\"latitude\":\"30.68078\",\"longitude\":\"104.1157\",\"trafficRoutes\":\"乘坐2、6、42、51、52、65、71、75、76、108、113、114、402、412、535、537路公车到达\"},{\"id\":\"560\",\"cityName\":\"成都\",\"cinemaName\":\"中影国际影城成都高新中航城店\",\"address\":\"成都市高新区府城大道中段88号九方购物中心6楼L600\",\"telephone\":\"028-83333632\",\"latitude\":\"30.59594\",\"longitude\":\"104.0717\",\"trafficRoutes\":\"91、501、113、120、823、184、298\"}],\"page\":1,\"pagesize\":10,\"totalpage\":11},\"error_code\":0}";
                else
                    result = net(url, params, "GET");
            } else if (method == 7){
                result = "{\"reason\":\"success\",\"result\":[{\"cinemaName\":\"成都九州星辰电影城\",\"cinemaId\":\"624\",\"address\":\"成都市郫县团结镇成都理工大学广播影视学院校内艺体中心\",\"latitude\":\"30.83101\",\"longitude\":\"103.9888\"}],\"error_code\":0}";
            }
            else {
                result = net(url, params, "GET");
            }
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {//表示请求成功
                System.out.println(object.get("result"));
                result = object.getString("result");
                resultMap.put("state", "ok");
                switch (method) {
                    case 1:
                    case 8:
                        List<MovieEntity> list1 = (List<MovieEntity>) GsonUtils.jsonToList(result, new TypeToken<List<MovieEntity>>() {
                        }.getType());
                        resultMap.put("data", list1);
                        break;
                    case 2:
                        List<AroundCinemaEntity> list2 = (List<AroundCinemaEntity>) GsonUtils.jsonToList(result, new TypeToken<List<AroundCinemaEntity>>() {
                        }.getType());
                        resultMap.put("data", list2);
                        break;
                    case 3:
                        object = new JSONObject(result);
                        result = object.getString("data");
                        List<CinemaEntity> list3 = (List<CinemaEntity>) GsonUtils.jsonToList(result, new TypeToken<List<CinemaEntity>>() {
                        }.getType());
                        resultMap.put("data", list3);
                        new CinemaService().add(list3, context);
                        break;
                    case 4:
                        MoviesOfCinemaEntity entity = (MoviesOfCinemaEntity) GsonUtils.jsonToObject(result,MoviesOfCinemaEntity.class);
                        resultMap.put("data", entity);
                        break;
                    case 5:
                        List<TodayMovieEntity> list5 = (List<TodayMovieEntity>) GsonUtils.jsonToList(result, new TypeToken<List<TodayMovieEntity>>() {
                        }.getType());
                        resultMap.put("data", list5);
                        new TodayMovieService().createOrUpdateData(list5,context);
                        break;
                    case 6:
                        List<SupportCityEntity> list6 = (List<SupportCityEntity>) GsonUtils.jsonToList(result, new TypeToken<List<SupportCityEntity>>() {
                        }.getType());
                        resultMap.put("data", list6);
                        break;
                    case 7:
                        List<MoviePlayingInfoEntity> list7 = (List<MoviePlayingInfoEntity>) GsonUtils.jsonToList(result, new TypeToken<List<MoviePlayingInfoEntity>>() {
                        }.getType());
                        resultMap.put("data", list7);
                        break;
                }
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
                resultMap.put("state", "error");
                resultMap.put("data", object.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
        return resultMap;
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
}
