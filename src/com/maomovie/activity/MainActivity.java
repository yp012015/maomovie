package com.maomovie.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;
import com.maomovie.R;
import com.maomovie.activity.mainfragment.CinemaFragment;
import com.maomovie.activity.mainfragment.DiscoverFragment;
import com.maomovie.activity.mainfragment.MovieFragment;
import com.maomovie.activity.mainfragment.UserFragment;
import com.maomovie.context.MaoApplication;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private RadioButton rButton0;
	private RadioButton rButton1;
	private RadioButton rButton2;
	private RadioButton rButton3;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	public static boolean isForeground = false;//用于标记此界面是否运行在前端
	private boolean flag = false;//确认是否需要退出应用
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 123){
				flag = false;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MaoApplication.addActivityList(this);
		// 控件初始化
		setView();
		// 给ViewPager 设置 Adapter
		setViewPagerAdapter();
		// 给ViewPager设置监听器
		setListener();
		//注册广播接收器
		registerMessageReceiver();

		//只对api19以上版本有效
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		//为状态栏着色
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.color_moviebtn_normal);
	}

	/**
	 * 设置沉侵式状态栏
	 * @param on
	 */
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}


	/**
	 * 控件初始化
	 */
	private void setView() {
		viewPager = (ViewPager) findViewById(R.id.vp_main_viewpager);
		rButton0 = (RadioButton) findViewById(R.id.radioButton0);
		rButton1 = (RadioButton) findViewById(R.id.radioButton1);
		rButton2 = (RadioButton) findViewById(R.id.radioButton2);
		rButton3 = (RadioButton) findViewById(R.id.radioButton3);
		
		rButton0.setOnClickListener(this);
		rButton1.setOnClickListener(this);
		rButton2.setOnClickListener(this);
		rButton3.setOnClickListener(this);
		//控制radioButton的drawableTop图片大小
		setDrawableSize(R.drawable.mainbtn0_backgroundselector,rButton0);
		setDrawableSize(R.drawable.mainbtn1_backgroundselector, rButton1);
		setDrawableSize(R.drawable.mainbtn2_backgroundselector, rButton2);
		setDrawableSize(R.drawable.mainbtn3_backgroundselector, rButton3);
		
	}
	/**
	 * 控制radioButton的drawableTop图片大小
	 * @param resource 图片资源
	 * @param button	RadioButton
	 */
	private void setDrawableSize(int resource,RadioButton button) {
		Drawable drawable0 = getResources().getDrawable(resource);
		drawable0.setBounds(0, 0, 40, 40);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
		button.setCompoundDrawables(null, drawable0, null, null);
	}
	/**
	 * 给ViewPager 设置适配器
	 */
	private void setViewPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new MovieFragment());
		fragments.add(new CinemaFragment());
		fragments.add(new DiscoverFragment());
		fragments.add(new UserFragment());
		FragmentManager manager = getSupportFragmentManager();
		MyAdapter myAdapter = new MyAdapter(manager);
		viewPager.setAdapter(myAdapter);
	}
	
	/**
	 * 给ViewPager设置监听器
	 */
	private void setListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
					case 0:
						rButton0.setChecked(true);
						break;
					case 1:
						rButton1.setChecked(true);
						break;
					case 2:
						rButton2.setChecked(true);
						break;
					case 3:
						rButton3.setChecked(true);
						break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v == rButton0){
			viewPager.setCurrentItem(0);
			return;
		}
		if(v == rButton1){
			viewPager.setCurrentItem(1);
			return;
		}
		if(v == rButton2){
			viewPager.setCurrentItem(2);
			return;
		}
		if(v == rButton3){
			viewPager.setCurrentItem(3);
		}
	}

	@Override
	public void onBackPressed() {
		if(flag){
			MaoApplication.exit();
		} else {
			Toast.makeText(MainActivity.this,"再按一次返回键退出应用",Toast.LENGTH_SHORT).show();
			flag = true;
		}
		handler.sendEmptyMessageDelayed(123,2000);
	}

	/**
	 * 自定义viewPager的适配器
	 */
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
		
	}

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!TextUtils.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg){
		new AlertDialog.Builder(this)
				.setMessage(msg).create().show();
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

}
