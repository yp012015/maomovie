package com.maomovie.activity.mainfragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.os.Handler;
import android.widget.*;
import com.maomovie.R;
import com.maomovie.components.listsort.ViewHolder;
import com.maomovie.entity.DiscMovieCommentEntity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.maomovie.service.LoadingDialog;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.util.HttpUtil;
import com.maomovie.util.ToastUtil;
import com.maomovie.util.juhe.HttpClient;

public class DiscoverFragment extends Fragment implements OnClickListener{

	private ThreadHelper threadHelper = new ThreadHelper(new Handler());
	private View view;
	private LinearLayout hotMovie;//热门电影
	private LinearLayout currentTicket;//实时票房
	private ListView listView;
	private List<DiscMovieCommentEntity> dataList = new ArrayList<DiscMovieCommentEntity>();
	private MyAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_discover,null);
		} else if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeAllViewsInLayout(); 
		}
		initView();// 控件初始化
		loadData();//加载数据
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
		}
	}

	/**
	 * 功能：初始化控件
	 */
	private void initView() {
		//热门电影
		hotMovie = (LinearLayout)view.findViewById(R.id.container_hotMovie);
		hotMovie.setOnClickListener(this);
		//实时票房
		currentTicket = (LinearLayout)view.findViewById(R.id.container_currentTicket);
		currentTicket.setOnClickListener(this);
		listView = (ListView)view.findViewById(R.id.discover_listView);
		//声明ListView的头部
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_discover_today, null);
		//为ListView添加头部
		listView.addHeaderView(headerView,null,false);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToastUtil.show(getActivity(),"未找到对应接口，功能不可用！");
			}
		});
	}
	/**
	 * 功能：加载数据
	 */
	private void loadData(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		DiscMovieCommentEntity entity = new DiscMovieCommentEntity();
		entity.setTitile("50部最佳同性题材电影《断背山》居榜首");
		entity.setTime("");
		for(int i=0; i<10; i++){
			entity.setTime(sdf.format(new Date()));
			entity.setComment((int) (Math.random()*100));
			entity.setZan((int) (Math.random()*100));
			dataList.add(entity);
		}
	}

	@Override
	public void onClick(View v) {
		if(v == hotMovie){
			ToastUtil.show(getActivity(),"未找到对应接口，功能不可用！");
		} else if(v == currentTicket){
			ToastUtil.show(getActivity(),"未找到对应接口，功能不可用！");
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final DiscMovieCommentEntity entity = dataList.get(position);
			if(convertView == null){
				convertView = View.inflate(getActivity(), R.layout.item_discover, null);
			}
			TextView tvTitle = ViewHolder.get(convertView,R.id.disitem_tvTitle);//标题
			TextView tvZan = ViewHolder.get(convertView,R.id.disitem_tvZan);//赞
			ImageView imgZan = ViewHolder.get(convertView,R.id.disitem_imgZan);//点赞图标
			TextView tvComment = ViewHolder.get(convertView,R.id.disitem_tvComment);//评论数目
			ImageView imgComment = ViewHolder.get(convertView,R.id.disitem_imgcomment);//评论图标
			TextView tvTime = ViewHolder.get(convertView,R.id.disitem_tvTime);//评论时间
			//赋值
			tvTitle.setText(entity.getTitile());
			tvTime.setText(entity.getTime());
			tvZan.setText(entity.getZan()+"");
			tvComment.setText(entity.getComment()+"");
			//设置监听器
			imgZan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					entity.setZan(entity.getZan()+1);
					adapter.notifyDataSetChanged();
				}
			});
			imgComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					entity.setComment(entity.getComment()+1);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
		
	}
}
