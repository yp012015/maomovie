package com.maomovie.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.activity.route.ShowMapActivity;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.MoviesOfCinemaEntity;

/**
 * Created by YanP on 2016/8/10.
 * 影院上映影片信息界面
 */
public class MoviesOfCinemaActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton btnBack;     //返回键
    private TextView tvTitle;   //标题
    private TextView tvCinameName,tvCinemaAddr;//影院名称、地址
    private LinearLayout layShowMap;//地图按钮
    private TextView tvTrafficRoutes;//交通线路
    private MoviesOfCinemaEntity movieEntity;
    private CinemaEntity cinemaEntity;
    private Context context;
    @Override
    public void onCreate() {
        setContentView(R.layout.activity_moviesofcinema);
        //接收上一界面传递过来的数据实体
        movieEntity = (MoviesOfCinemaEntity) getIntent().getSerializableExtra("MoviesOfCinemaEntity");
        cinemaEntity = (CinemaEntity) getIntent().getSerializableExtra("CinemaEntity");
        findView();//初始化控件
        setView();//为控件赋值
    }


    /**
     * 初始化控件
     */
    private void findView() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCinameName = (TextView) findViewById(R.id.tvCinemaName);
        tvCinemaAddr = (TextView) findViewById(R.id.tvCinemaAddr);

        layShowMap = (LinearLayout) findViewById(R.id.layShowMap);
        layShowMap.setOnClickListener(this);

        //显示公交线路
        tvTrafficRoutes = (TextView) findViewById(R.id.tvCinemaTransfer);
    }

    /**
     * 为控件赋值
     */
    private void setView(){
        context = this;
        if(cinemaEntity == null)
            return;
        String name = cinemaEntity.getNm();
        tvTitle.setText(name);
        tvCinameName.setText(name);
        String address = cinemaEntity.getAddr();
        tvCinemaAddr.setText(address.replace("成都市",""));
        String transferLine = cinemaEntity.getBus();
        tvTrafficRoutes.setText(transferLine);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == btnBack){
            finish();
        } else if (v == layShowMap){
            Intent intent = new Intent(context,ShowMapActivity.class);
            intent.putExtra("MoviesOfCinemaEntity",movieEntity);
            intent.putExtra("CinemaEntity",cinemaEntity);
            startActivity(intent);
        }
    }
}