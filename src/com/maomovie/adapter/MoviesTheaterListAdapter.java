package com.maomovie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.maomovie.R;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.MoviesOfCinemaEntity;

import java.util.List;

/**
 * Created by YanP on 2016/8/29.
 */
public class MoviesTheaterListAdapter extends BaseAdapter{
    private Context context;
    private List<MoviesOfCinemaEntity.DataBean.ShowsBean> dataList;

    public MoviesTheaterListAdapter(Context context, MoviesOfCinemaEntity moviesOfCinemaEntity) {
        this.context = context;
        MoviesOfCinemaEntity.DataBean dataBean = moviesOfCinemaEntity.getData();
        dataList = dataBean.getShows();
    }

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
        ViewHolder viewHolder = null;
        final MoviesOfCinemaEntity.DataBean.ShowsBean entity = dataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_movie_theater, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaAdd);
            viewHolder.price = (TextView) convertView.findViewById(R.id.cinemaitem_tvTicketPrice);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaDistance);
            viewHolder.sell = (TextView) convertView.findViewById(R.id.itemCinema_tvSell);
            viewHolder.deal = (TextView) convertView.findViewById(R.id.itemCinema_tvDeal);
            viewHolder.park = (TextView) convertView.findViewById(R.id.itemCinema_tvPark);
            viewHolder.iMax = (TextView) convertView.findViewById(R.id.itemCinema_tvIMAX);
            viewHolder.tvNearbySession = (TextView) convertView.findViewById(R.id.tvNearNearbySession);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(entity.getNm());
        String address = entity.getAddr();
        //将地址中“……区”前面的字段去除
        int index = address.indexOf("区") > 0 ? address.indexOf("区") : address.indexOf("市");
        if (index > 0) {
            address = address.substring(index + 1);
        }
        viewHolder.address.setText(address);
        String str = "_ _";
        try {
            LatLng latLng1 = new LatLng(MaoApplication.currentLocation.getLatitude(),MaoApplication.currentLocation.getLongitude());
            LatLng latLng2 = new LatLng(entity.getLat(),entity.getLng());
            double distance = AMapUtils.calculateLineDistance(latLng1,latLng2);
            if (distance != 0) {
                str = String.format("%.1f", distance / 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.distance.setText(String.valueOf(str));
        viewHolder.price.setText(String.valueOf(entity.getSellPrice()));
        if (entity.getSell() == 1) {
            viewHolder.sell.setVisibility(View.VISIBLE);
        } else{
            viewHolder.sell.setVisibility(View.GONE);
        }
        if (entity.getDeal() == 1) {
            viewHolder.deal.setVisibility(View.VISIBLE);
        }else{
            viewHolder.deal.setVisibility(View.GONE);
        }
        if (entity.getPark() == null || entity.getPark().equals("null")) {
            viewHolder.park.setVisibility(View.GONE);
        } else {
            viewHolder.park.setVisibility(View.VISIBLE);
        }
        if (entity.getImax() == 1) {
            viewHolder.iMax.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iMax.setVisibility(View.GONE);
        }
        StringBuffer nearbyStr = new StringBuffer();
        for(int i=0; i<entity.getPlist().size(); i++){
            MoviesOfCinemaEntity.DataBean.ShowsBean.PlistBean plistBean = entity.getPlist().get(i);
            if(i != 0){
                nearbyStr.append(" | ");
            }
            nearbyStr.append(plistBean.getTm());
        }
        viewHolder.tvNearbySession.setText(nearbyStr.toString());
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView address;
        TextView price;
        TextView distance;
        TextView sell;
        TextView deal;
        TextView park;
        TextView iMax;
        TextView tvNearbySession;
    }
}
