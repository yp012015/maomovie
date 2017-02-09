package com.maomovie.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by yanpeng on 2017/1/19.
 *
 * 用于压缩图片
 */
public class LoadImageUtil {

    /***
     * 图片的缩放方法
     *
     * @param src   ：源图片资源
     */
    private static Bitmap scaleByViewSize(Bitmap src, ImageView imageView) {
        // 记录src的宽高
        float width = src.getWidth();
        float height = src.getHeight();
        // 创建一个matrix容器
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float scaleWidth = ((float) imageView.getWidth()) / width;
        float scaleHeight = ((float) imageView.getHeight()) / height;
        // 开始缩放
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }

    /**
     * 根据给定的宽高比例，压缩图片
     * @param src
     * @param widthScale    宽度压缩比
     * @param heightScale   高度压缩比
     * @return
     */
    private static Bitmap scaleByWidthAndHeight(Bitmap src,float widthScale, float heightScale){
        // 记录src的宽高
        float width = src.getWidth();
        float height = src.getHeight();
        //创建一个matrix容器
        Matrix matrix = new Matrix();
        // 宽高各缩放到原来的一半
        matrix.postScale(widthScale, heightScale);
        // 创建缩放后的图片
        return Bitmap.createBitmap(src,0,0,(int)width,(int)height,matrix,true);
    }




    /**
     * 根据ImageView的宽高压缩图片后，显示图片
     * @param imageView
     * @param bitmap
     */
    public static void setImageByImageView(ImageView imageView, Bitmap bitmap){
        if(bitmap == null || imageView == null){
            return;
        }
        imageView.setImageBitmap(scaleByViewSize(bitmap, imageView));
    }

    /**
     * 通过用户给定的新宽高尺寸压缩图片后，显示图片
     * @param imageView     显示照片的容器
     * @param bitmap        照片
     * @param widthScale    宽度压缩比
     * @param heightScale   高度压缩比
     */
    public static void setImageScaleBySelf(ImageView imageView,Bitmap bitmap,float widthScale, float heightScale){
        if(imageView == null || bitmap == null){
            return;
        }
        imageView.setImageBitmap(scaleByWidthAndHeight(bitmap,widthScale,heightScale));
    }
}
