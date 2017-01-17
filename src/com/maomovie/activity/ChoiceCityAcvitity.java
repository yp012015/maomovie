package com.maomovie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import com.google.gson.reflect.TypeToken;
import com.maomovie.R;
import com.maomovie.components.listsort.*;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.sqlite.service.CityService;
import com.maomovie.util.GsonUtils;
import com.maomovie.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 功能：选择影院城市
 *
 * @author YanP
 */
public class ChoiceCityAcvitity extends BaseActivity implements OnClickListener {

    private Context context;
    private ImageButton btnBack;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView txtMsg;
    private TextView txtProgress;//显示加载进度
    private ProgressBar progressBar;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private TextView[] hotCitys;
    private TextView tvCurrentCity;

    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    //声明拼音排序的比较器
    PinyinComparator pinyinComparator = new PinyinComparator();
    private List<SupportCityEntity> dataList;//数据源
    //声明数据库操作需要的服务
    private CityService cityService;

    private boolean isNeedSave = false;

    @Override
    public void onCreate() {
        MaoApplication.addActivityList(this);
        setContentView(R.layout.activity_choicecity);
        initViews();
        getDataFromCache();
    }

    /**
     * 功能：初始化界面组件
     */
    private void initViews() {
        context = this;
        cityService = new CityService();
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        txtMsg = (TextView) findViewById(R.id.titleDialog);
        txtMsg.getBackground().setAlpha(100);
        sideBar.setTextView(txtMsg);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
    }

    /**
     * 设置ListView适配器，右侧触摸监听，输入框过滤搜索
     *
     * @param headerView listView添加的头部
     */
    private void setAdapter(View headerView) {
        if (adapter == null) {
            adapter = new SortAdapter(this, dataList);
            sortListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        //设置头部点击监听
        tvCurrentCity = (TextView) headerView.findViewById(R.id.currentCityName);
        if (MaoApplication.currentLocation == null) {
            tvCurrentCity.setText("定位失败");
        } else {
            tvCurrentCity.setText(MaoApplication.currentLocation.getCity());
            tvCurrentCity.setOnClickListener(this);
        }
        TextView tvHotCity1 = (TextView) findViewById(R.id.hotCity1);
        TextView tvHotCity2 = (TextView) findViewById(R.id.hotCity2);
        TextView tvHotCity3 = (TextView) findViewById(R.id.hotCity3);
        TextView tvHotCity4 = (TextView) findViewById(R.id.hotCity4);
        TextView tvHotCity5 = (TextView) findViewById(R.id.hotCity5);
        TextView tvHotCity6 = (TextView) findViewById(R.id.hotCity6);
        TextView tvHotCity7 = (TextView) findViewById(R.id.hotCity7);
        TextView tvHotCity8 = (TextView) findViewById(R.id.hotCity8);
        TextView tvHotCity9 = (TextView) findViewById(R.id.hotCity9);
        hotCitys = new TextView[]{tvHotCity1, tvHotCity2, tvHotCity3, tvHotCity4, tvHotCity5, tvHotCity6, tvHotCity7, tvHotCity8, tvHotCity9};
        for (TextView textView : hotCitys) {
            textView.setOnClickListener(this);
        }

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                SupportCityEntity entity = (SupportCityEntity) adapter.getItem(position - 1);
                finishActivity(entity);
            }
        });
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 返回用户选择的城市信息，关闭当前界面
     *
     * @param entity 城市信息
     */
    private void finishActivity(SupportCityEntity entity) {
        Intent intent = new Intent("REFRESH_CITY");
        intent.putExtra("city", entity);
        sendBroadcast(intent);
        finish();
    }

    /**
     * 功能：从缓存里面取出城市信息
     */
    private void getDataFromCache() {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                try {
                    return cityService.queryAll(context);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            public void result(Object result) {
                //程序缓存里面没有存放城市信息时再从服务器获取
                if (result == null || result instanceof Exception) {
                    isNeedSave = true;
                    getDataFromInternet();
                } else {
                    dataList = (List<SupportCityEntity>) result;
                    if (dataList.size() == 0) {
                        isNeedSave = true;
                        getDataFromInternet();
                    } else {
                        sortData();
                    }
                }
            }

        });
    }

    private void getDataFromInternet() {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                Object result = HttpUtil.getCitys();
                if (result == null || result instanceof Exception) {
                    return result;
                } else {
                    try {
                        JSONObject resultObj = new JSONObject(result.toString());
                        JSONArray dataArray = resultObj.optJSONArray("data");
                        dataList = (List<SupportCityEntity>) GsonUtils.jsonToList(dataArray.toString(),
                                new TypeToken<List<SupportCityEntity>>() {
                                }.getType());
                        return dataList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e;
                    }
                }
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    progressBar.setVisibility(View.GONE);
                    txtProgress.setText("数据解析失败！");
                } else {
                    if (dataList == null || dataList.size() == 0) {
                        txtProgress.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.VISIBLE);
                    } else {
                        sortData();
                    }
                }
            }
        });
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */

    private void filterData(String filterStr) {
        List<SupportCityEntity> filterDateList = new ArrayList<SupportCityEntity>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = dataList;
        } else {
            filterDateList.clear();
            for (SupportCityEntity sortModel : dataList) {
                String name = sortModel.getNm();
                String pinying = sortModel.getPy();
                if (name.indexOf(filterStr) != -1 || pinying.toLowerCase().startsWith(filterStr.toLowerCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    /**
     * 按拼音首字母进行排序
     */
    private void sortData() {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                if (dataList.size() == 0) {//如果城市列表数量为0
                    return false;
                }
                Collections.sort(dataList, pinyinComparator);
                return true;
            }

            @Override
            public void result(Object result) {
                txtProgress.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                View view = LayoutInflater.from(context).inflate(R.layout.item_hotcity, null);
                sortListView.addHeaderView(view, null, true);
                if ((Boolean) result) {//如果城市列表数量大于0,加载列表
                    setAdapter(view);
                } else {//否则，显示“没有城市”
                    txtMsg.setVisibility(View.VISIBLE);
                }
                if (isNeedSave)
                    saveData();
            }
        });
    }

    /**
     * 保存数据到数据库
     */
    private void saveData() {
        threadHelper.dataHander(new Runnable() {
            @Override
            public void run() {
                new CityService().createOrUpdate(dataList, context);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == tvCurrentCity) {
            String currentCity = tvCurrentCity.getText().toString();
            currentCity = currentCity.replace("市", "");
            SupportCityEntity entity = new SupportCityEntity();
            entity.setNm(currentCity);
            finishActivity(entity);
        } else {
            for (int i = 0; i < hotCitys.length; i++) {
                if (v == hotCitys[i]) {
                    SupportCityEntity entity = new SupportCityEntity();
                    entity.setNm(hotCitys[i].getText().toString());
                    finishActivity(entity);
                    break;
                }
            }
        }
    }

}
