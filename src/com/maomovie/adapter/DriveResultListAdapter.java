package com.maomovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.maomovie.R;
import com.maomovie.activity.route.BusRouteDetailActivity;
import com.maomovie.util.amap.AMapUtil;

import java.util.List;

/**
 * Created by YanP on 2016/8/26.
 * 业务功能：自驾线路List适配器
 */
public class DriveResultListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DrivePath> mDrivePathList;
    private DriveRouteResult mDriveRouteResult;

    public DriveResultListAdapter(Context mContext, DriveRouteResult driveRouteResult) {
        this.mContext = mContext;
        this.mDriveRouteResult = driveRouteResult;
        mDrivePathList = driveRouteResult.getPaths();
    }

    @Override
    public int getCount() {
        return mDrivePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDrivePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_bus_result, null);
            holder.title = (TextView) convertView.findViewById(R.id.bus_path_title);
            holder.des = (TextView) convertView.findViewById(R.id.bus_path_des);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DrivePath item = mDrivePathList.get(position);
        int dis = (int) item.getDistance();
        int dur = (int) item.getDuration();
        String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
        holder.title.setText(des);
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        holder.des.setText("打车约"+taxiCost+"元");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(),
                        BusRouteDetailActivity.class);
                intent.putExtra("drive_path", item);
                intent.putExtra("drive_result", mDriveRouteResult);
                intent.putExtra("route_type",2);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView des;
    }

}
