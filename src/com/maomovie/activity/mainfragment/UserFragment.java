package com.maomovie.activity.mainfragment;

import android.widget.LinearLayout;
import com.maomovie.R;
import com.maomovie.activity.login.LoginActivity;
import com.maomovie.activity.MsgActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserFragment extends Fragment implements OnClickListener {

    private Context context;
    private View view;
    private RelativeLayout container_News;//我的消息
    private RelativeLayout container_Vip;            // 猫眼会员
    private RelativeLayout container_Achievement;    // 电影成就
    private RelativeLayout container_Community;        // 我的社区
    private RelativeLayout container_MeiTuanWallet;    // 美团钱包
    private RelativeLayout container_Coupon;        // 我的优惠券
    private RelativeLayout container_MaoyanStore;    // 猫眼商城
    private RelativeLayout container_BuightGoods;    // 我购买的商品
    private RelativeLayout container_Settings;        // 设置
    private LinearLayout layLogin;//登录

    private TextView tvMyTicket, tvMyMovie, tvMyComment;    // 我的电影票、我的电影、我的影评

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_user, null);
            context = getActivity();
            // 控件初始化
            setView();
        } else if (view !=null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViewsInLayout();
        }
        return view;
    }

    /**
     * 功能：初始化控件
     */
    private void setView() {
        tvMyTicket = (TextView) view.findViewById(R.id.tvMyTicker);//我的电影票
        tvMyMovie = (TextView) view.findViewById(R.id.tvMyMovie);//我的电影
        tvMyComment = (TextView) view.findViewById(R.id.tvMyComment);//我的影评
        tvMyTicket.setOnClickListener(this);
        tvMyMovie.setOnClickListener(this);
        tvMyComment.setOnClickListener(this);

        setDrawableTop(R.drawable.ic_mine_my_ticket, tvMyTicket);
        setDrawableTop(R.drawable.ic_mine_my_movie, tvMyMovie);
        setDrawableTop(R.drawable.ic_mine_my_comment, tvMyComment);

        container_Vip = (RelativeLayout) view.findViewById(R.id.container_vip);
        TextView tvVip = (TextView) view.findViewById(R.id.tvVip);//猫眼会员
        setDrawableLeft(R.drawable.ic_mine_vip, tvVip);
        tvVip.setCompoundDrawablePadding(20);
        container_Vip.setOnClickListener(this);

        container_Achievement = (RelativeLayout) view.findViewById(R.id.container_Achievement);
        TextView tvAchievement = (TextView) view.findViewById(R.id.tvAchievement);//电影成就
        setDrawableLeft(R.drawable.ic_mine_task, tvAchievement);
        tvAchievement.setCompoundDrawablePadding(20);
        container_Achievement.setOnClickListener(this);

        container_Community = (RelativeLayout) view.findViewById(R.id.container_myCommunity);
        TextView tvCommunity = (TextView) view.findViewById(R.id.tvCommunity);//我的社区
        setDrawableLeft(R.drawable.ic_mine_my_post, tvCommunity);
        tvCommunity.setCompoundDrawablePadding(20);
        container_Community.setOnClickListener(this);

        container_MeiTuanWallet = (RelativeLayout) view.findViewById(R.id.container_meituanWallet);
        TextView tvMeiTuanWallet = (TextView) view.findViewById(R.id.tvMeiTuanWallet);//美团钱包
        setDrawableLeft(R.drawable.wallet, tvMeiTuanWallet);
        tvMeiTuanWallet.setCompoundDrawablePadding(20);
        container_MeiTuanWallet.setOnClickListener(this);

        container_Coupon = (RelativeLayout) view.findViewById(R.id.container_coupon);
        TextView tvMyCoupon = (TextView) view.findViewById(R.id.tvCoupon);//我的优惠券
        setDrawableLeft(R.drawable.ic_mine_my_coupon, tvMyCoupon);
        tvMyCoupon.setCompoundDrawablePadding(20);
        container_Coupon.setOnClickListener(this);

        container_MaoyanStore = (RelativeLayout) view.findViewById(R.id.container_maoyanStore);
        TextView tvMaoyanStore = (TextView) view.findViewById(R.id.tvMaoyanStore);//猫眼商城
        setDrawableLeft(R.drawable.ic_mine_my_shopping_center, tvMaoyanStore);
        tvMaoyanStore.setCompoundDrawablePadding(20);
        container_MaoyanStore.setOnClickListener(this);

        container_BuightGoods = (RelativeLayout) view.findViewById(R.id.container_myBuightGoods);
        TextView tvMyBuightGoods = (TextView) view.findViewById(R.id.tvMyBuightGoods);//我购买的商品
        setDrawableLeft(R.drawable.ic_mine_my_goods, tvMyBuightGoods);
        tvMyBuightGoods.setCompoundDrawablePadding(20);
        container_BuightGoods.setOnClickListener(this);

        container_Settings = (RelativeLayout) view.findViewById(R.id.container_settings);
        TextView tvSettings = (TextView) view.findViewById(R.id.tvSettings);//设置
        setDrawableLeft(R.drawable.ic_mine_setting, tvSettings);
        tvSettings.setCompoundDrawablePadding(20);
        container_Settings.setOnClickListener(this);

        container_News = (RelativeLayout) view.findViewById(R.id.container_news);
        container_News.setOnClickListener(this);
        TextView tvMyNews = (TextView) view.findViewById(R.id.tvMyNews);
        setDrawableLeft(R.drawable.ic_mine_my_message, tvMyNews);
        tvMyNews.setCompoundDrawablePadding(20);
        container_News.setOnClickListener(this);

        layLogin = (LinearLayout) view.findViewById(R.id.layLogin);
        layLogin.setOnClickListener(this);
    }

    /**
     * 设置drawableTop
     *
     * @param resource 图片资源
     */
    private void setDrawableTop(int resource, TextView textView) {
        Drawable drawable0 = getResources().getDrawable(resource);
        //控制图片大小
        drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        textView.setCompoundDrawables(null, drawable0, null, null);
    }

    /**
     * 控制drawableLeft图片大小
     *
     * @param resource 图片资源
     */
    private void setDrawableLeft(int resource, TextView textView) {
        Drawable drawable0 = getResources().getDrawable(resource);
        drawable0.setBounds(0, 0, 25, 25);
        textView.setCompoundDrawables(drawable0, null, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v == container_News) {//我的消息
            Intent intent = new Intent(context, MsgActivity.class);
            startActivity(intent);
        } else {
            startLoginActivity();
        }
    }

    /**
     * 跳转到登录界面
     */
    private void startLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}
