package com.maomovie.activity;

import android.view.View;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.adapter.UserInfoAdapter;
import com.maomovie.view.CustomListView;
import com.maomovie.view.TitleBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by yanpeng on 2016/11/8.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener{
    private TitleBar titleBar;
    private CustomListView listView;
    private JSONObject userJson;
    public static Iterator<String> iterator;
    @Override
    public void onCreate() {
        setContentView(R.layout.activity_userinfo);
        initView();//初始化控件
        setView();//为控件赋值
    }

    private void initView() {
        String userInfoStr = getIntent().getStringExtra("User");
        try {
            userJson = new JSONObject(userInfoStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setLeftButtonListener(this);
        listView = (CustomListView) findViewById(R.id.listView);
        TextView tvInfo = new TextView(this);
        tvInfo.setHeight(70);
        tvInfo.setTextSize(20);
        tvInfo.setText("左滑信息可编辑");
        tvInfo.setPadding(20, 0, 0, 0);
        listView.addHeaderView(tvInfo,null,false);
    }

    private void setView() {
        titleBar.setTitleText("用户信息");
        iterator = userJson.keys();
        final UserInfoAdapter adapter = new UserInfoAdapter(userJson,this);
        listView.setAdapter(adapter);
        listView.setOnDeleteListener(new CustomListView.OnDeleteListener() {
            @Override
            public void onDelete(int index) {
                int position = 1;
                iterator = userJson.keys();//因为迭代器每次遍历完后，移动到末尾了，所以要重新获取迭代器
                while (iterator.hasNext()){
                    String key = iterator.next();
                    if (position == index) {
                        userJson.remove(key);
                        break;
                    }
                    position++;
                }
                iterator = userJson.keys();//因为迭代器每次遍历完后，移动到末尾了，所以要重新获取迭代器
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(listView.isDeleteShown()){
            listView.hideDelete();
            return;
        }
        super.onBackPressed();
    }
}