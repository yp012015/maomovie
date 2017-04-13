package com.maomovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.maomovie.R;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.CinemaEntity;

import java.util.List;

/**
 * Created by YanP on 2016/4/28.
 */
public class FragmentCinemaAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CinemaEntity> cinemas;

    public FragmentCinemaAdapter(Context context, List<CinemaEntity> cinemas) {
        mInflater = LayoutInflater.from(context);
        this.cinemas = cinemas;
    }

    public void addFirst(CinemaEntity entity) {
        cinemas.add(0, entity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cinemas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return cinemas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        final CinemaEntity entity = cinemas.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_cinema, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaAdd);
            viewHolder.price = (TextView) convertView.findViewById(R.id.cinemaitem_tvTicketPrice);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.cinemaitem_tvCinemaDistance);
            viewHolder.sell = (TextView) convertView.findViewById(R.id.itemCinema_tvSell);
            viewHolder.deal = (TextView) convertView.findViewById(R.id.itemCinema_tvDeal);
            viewHolder.park = (TextView) convertView.findViewById(R.id.itemCinema_tvPark);
            viewHolder.iMax = (TextView) convertView.findViewById(R.id.itemCinema_tvIMAX);
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
            double distance = entity.getDistance();
            if (distance != 0) {
                str = String.format("%.1f", distance / 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.distance.setText(String.valueOf(str));
        viewHolder.price.setText(String.valueOf(entity.getSellPrice()));
        if (entity.getSell()) {
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
    }
}
