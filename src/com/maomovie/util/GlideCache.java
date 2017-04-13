package com.maomovie.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by yanpeng on 2017/2/9.
 */
public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        //设置磁盘缓存目录（和创建的缓存目录相同）
        String downloadDirectoryPath="/maomovie/ImageCache";
        if(hasSDCard()){
            downloadDirectoryPath = getExtPath() + downloadDirectoryPath;
        } else {
            downloadDirectoryPath = getPackagePath((Activity) context) + downloadDirectoryPath;
        }
        //设置缓存的大小为100M
        int cacheSize = 100*1000*1000;
        glideBuilder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

    /**
     * 判断是否有sdcard
     * @return
     */
    public boolean hasSDCard(){
        boolean b = false;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            b = true;
        }
        return b;
    }

    /**
     * 得到sdcard路径
     * @return
     */
    public String getExtPath(){
        String path = "";
        if(hasSDCard()){
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    /**
     * 得到/data/data/yanbin.imagedownload目录
     * @param mActivity
     * @return
     */
    public String getPackagePath(Activity mActivity){
        return mActivity.getFilesDir().toString();
    }
}
