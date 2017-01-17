package com.maomovie.components.listsort;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by YanP on 2016/9/7.
 * 每次都ViewHolder repeat,repeat,repeat  累啊。
 * 所以，有这么一种简洁的写法分享给大家，先声明，从国外网站上看的，不是自己原创的，但确实很喜欢这个简洁的设计。
 */
public class ViewHolder {
    // I added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray viewHolder = (SparseArray) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray();
            view.setTag(viewHolder);
        }
        View childView = (View) viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }


    //    在getView里这样
/*    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.banana_phone, parent, false);
        }
        ImageView bananaView = ViewHolder.get(convertView, R.id.banana);
        TextView phoneView = ViewHolder.get(convertView, R.id.phone);

        BananaPhone bananaPhone = getItem(position);
        phoneView.setText(bananaPhone.getPhone());
        bananaView.setImageResource(bananaPhone.getBanana());

        return convertView;
    }*/
}
