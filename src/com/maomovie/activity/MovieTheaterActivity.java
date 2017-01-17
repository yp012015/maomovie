package com.maomovie.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.maomovie.R;
import com.maomovie.adapter.FragmentCinemaAdapter;
import com.maomovie.adapter.MoviesTheaterListAdapter;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.MoviesOfCinemaEntity;
import com.maomovie.service.LoadingDialog;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.util.GsonUtils;
import com.maomovie.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YanP on 2016/8/12.
 * 影片上映影院
 */
public class MovieTheaterActivity extends BaseActivity implements View.OnClickListener {
    //工具类
    private Context context;
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    //控件类
    private ImageButton btnBack;//返回键
    private TextView tvTitle;//标题栏
    private LinearLayout[] layDates;//播放日期的父控件
    private TextView[] tvDates;//影片播放日期
    private View[] lineViews;//用于辅助显示日期的横线
    private LinearLayout layFillter, laySearch;//影院筛选、影院搜索
    private MoviesTheaterListAdapter adapter;//ListView适配器
    private ListView listView;//ListView列表
    private Dialog loadingDialog;
    //数据类
    private int movieId;        // 影片ID
    private String movieName;   // 影片名称
    private String cityName = "成都";
    private String date;
    //声明 影片获取上映影院的场次的实体
    private MoviesOfCinemaEntity moviesOfCinemaEntity;

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_movietheater);
        getIntentData();
        findView();//初始化界面控件
        setView();

    }

    private void getIntentData() {
        //获取上一界面传递过来的影片ID
        movieId = getIntent().getIntExtra("movieId", 0);
        movieName = getIntent().getStringExtra("movieName");
    }


    /**
     * 初始化界面控件
     */
    private void findView() {
        context = this;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (movieName != null)
            tvTitle.setText(movieName);
        tvDates = new TextView[]{
                (TextView) findViewById(R.id.tvFirstDate),
                (TextView) findViewById(R.id.tvSecondDate),
                (TextView) findViewById(R.id.tvThirdDate),
                (TextView) findViewById(R.id.tvFourthDate),
                (TextView) findViewById(R.id.tvFifthDate)
        };
        lineViews = new View[]{
                findViewById(R.id.lineFirst),
                findViewById(R.id.lineSecond),
                findViewById(R.id.lineThird),
                findViewById(R.id.lineFourth),
                findViewById(R.id.lineFifth)
        };
        layDates = new LinearLayout[]{
                (LinearLayout) findViewById(R.id.layFirstDate),
                (LinearLayout) findViewById(R.id.laySecondDate),
                (LinearLayout) findViewById(R.id.layThirdDate),
                (LinearLayout) findViewById(R.id.layFourthDate),
                (LinearLayout) findViewById(R.id.layFifthDate)
        };
        //为日期控件设置监听器
        for (LinearLayout lay : layDates) {
            lay.setOnClickListener(this);
        }
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        layFillter = (LinearLayout) findViewById(R.id.cinemaFilter);
        layFillter.setOnClickListener(this);

        laySearch = (LinearLayout) findViewById(R.id.searchMovie);
        laySearch.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
    }

    /**
     * 为控件赋值
     */
    private void setView(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        //代表传递参数描述的时间
        Calendar other=Calendar.getInstance();
        for(int i=0; i<tvDates.length; i++){
            TextView textView = tvDates[i];
            if(i == 0){
                textView.setText("今天" + sdf.format(new Date()));
            } else if (i == 1){
                other.add(Calendar.DAY_OF_YEAR,1);
                textView.setText("明天" + sdf.format(other.getTime()));
            } else if (i == 2){
                other.add(Calendar.DAY_OF_YEAR,1);
                textView.setText("后天" + sdf.format(other.getTime()));
            } else {
                other.add(Calendar.DAY_OF_YEAR,1);
                int day=other.get(Calendar.DAY_OF_WEEK);
                String result="";
                switch (day) {
                    case Calendar.MONDAY:
                        result="周一";
                        break;
                    case Calendar.TUESDAY:
                        result="周二";
                        break;
                    case Calendar.WEDNESDAY:
                        result="周三";
                        break;
                    case Calendar.THURSDAY:
                        result="周四";
                        break;
                    case Calendar.FRIDAY:
                        result="周五";
                        break;
                    case Calendar.SATURDAY:
                        result="周六";
                        break;
                    case Calendar.SUNDAY:
                        result="周日";
                        break;
                }
                textView.setText(result + sdf.format(other.getTime()));
            }
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());
        loadData(date);//加载数据
    }

    /**
     * 设置下拉刷新样式及点击事件
     */
    private void initListView() {

        adapter = new MoviesTheaterListAdapter(context, moviesOfCinemaEntity);
        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CinemaEntity cinemaEntity = dataList.get(position - 1);
                Intent intent = new Intent(context, MoviesOfCinemaActivity.class);
                intent.putExtra("CinemaEntity", cinemaEntity);
                startActivity(intent);
            }
        });*/
    }

    private void loadData(final String date) {
        loadingDialog = LoadingDialog.createLoadingDialog(this);   //圆形进度条
        loadingDialog.show();
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getSessionOfCinemaByMovieid(date,cityName,movieId);
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    loadingDialog.dismiss();
                    Toast.makeText(context, "加载放映影院失败", Toast.LENGTH_SHORT).show();
                } else {
                    moviesOfCinemaEntity = (MoviesOfCinemaEntity) GsonUtils.jsonToObject(
                            result.toString(),MoviesOfCinemaEntity.class);
                    loadingDialog.dismiss();
                    adapter = new MoviesTheaterListAdapter(context, moviesOfCinemaEntity);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    private void showPopupWindow() {
        //装载R.layout.additem对应的界面布局
        final View root = getLayoutInflater().inflate(R.layout.additem, null);
        //创建PopupWindow对象
        final PopupWindow popupWindow = new PopupWindow(root, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        //设置点击屏幕其他区域，PopupWindow消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        TextView tv1 = (TextView) root.findViewById(R.id.tvFillter1);
        TextView tv2 = (TextView) root.findViewById(R.id.tvFillter2);
        TextView tv3 = (TextView) root.findViewById(R.id.tvFillter3);
        TextView tv4 = (TextView) root.findViewById(R.id.tvFillter4);
        TextView tv5 = (TextView) root.findViewById(R.id.tvFillter5);
        TextView[] tvList = new TextView[]{tv1, tv2, tv3, tv4, tv5};
        for (int i = 0; i < tvList.length; i++) {
            TextView textView = tvList[i];
            textView.setOnClickListener(new PopupListener(tvList));
        }
        //以下拉方式显示
        popupWindow.showAsDropDown(laySearch);
        //PopupWindow显示在指定位置
        popupWindow.showAtLocation(laySearch, Gravity.BOTTOM, 0, 0);
    }

    class PopupListener implements View.OnClickListener {
        private TextView[] tvList;

        public PopupListener(TextView[] tvList) {
            this.tvList = tvList;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tvList.length; i++) {
                TextView textView = tvList[i];
                if (v == textView) {
                    textView.setTextColor(getResources().getColor(R.color.color_text_selected));
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black_gray));
                    textView.setBackgroundColor(getResources().getColor(R.color.hologray));
                }
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == layFillter) {
            showPopupWindow();
        } else if (v == laySearch) {

        } else {
            setDateListener(v);
        }
    }

    /**
     * 设置用户选择的影片播放日期的监听器
     */
    private void setDateListener(View v) {
        for (int i = 0; i < layDates.length; i++) {
            if (v == layDates[i]) {
                //将选择的日期设置为红色
                tvDates[i].setTextColor(getResources().getColor(R.color.color_moviebtn_normal));
                lineViews[i].setVisibility(View.VISIBLE);
                //获取选择日期的电影场次
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,i);
                loadData(sdf.format(calendar.getTime()));
            } else {
                //未选择的日期设置为灰色
                tvDates[i].setTextColor(getResources().getColor(R.color.black_gray));
                lineViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

}