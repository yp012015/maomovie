package com.maomovie.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.util.ImageDownloader;
import com.maomovie.util.LoadImageUtil;
import com.maomovie.util.OnImageDownload;
import com.maomovie.view.DragImageView;
import com.maomovie.view.HackyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 功能：浏览图片的Activity
 * Created by wem on 2015/9/14.
 */
public class GalleryActivity extends Activity {

    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewPager photoViewPager;
    private int currentIndex = 0;
    private String[] urlArray;
    /**
     * 手机振动器
     */
    private Vibrator vibrator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        // 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void init() {
        urlArray = getIntent().getStringArrayExtra("imgUrls");
        currentIndex = getIntent().getIntExtra("currentIndex", 0);
        initViewPager(urlArray);
    }

    private void initViewPager(String[] srcArray) {
        photoViewPager = new HackyViewPager(this);
        setContentView(photoViewPager);
        photoViewPager.setAdapter(new SamplePagerAdapter(srcArray));
        photoViewPager.setCurrentItem(currentIndex);
    }

    class SamplePagerAdapter extends PagerAdapter {
        private ImageDownloader imageDownloader;

        DisplayImageOptions options;

        private String[] data;

        public SamplePagerAdapter(String[] data) {
            this.data = data;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.color.gray)
                    .showImageForEmptyUri(R.color.gray)
                    .showImageOnFail(R.color.gray).cacheInMemory(true)
                    .cacheOnDisc(false).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            imageDownloader = new ImageDownloader();
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(GalleryActivity.this).inflate(R.layout.gallery_item, null);
            /** 获取可見区域高度 **/
            WindowManager manager = getWindowManager();
            window_width = manager.getDefaultDisplay().getWidth();
            window_height = manager.getDefaultDisplay().getHeight();
            TextView txtCount = (TextView) view.findViewById(R.id.txtCount);
            TextView txtIndex = (TextView) view.findViewById(R.id.txtIndex);
            if (data.length < 10) {
                txtCount.setText("/0" + data.length);
            } else {
                txtCount.setText("/" + data.length);
            }
            if (position < 10) {
                txtIndex.setText("0" + (position + 1));
            } else {
                txtIndex.setText("" + (position + 1));
            }
            final DragImageView photoView = (DragImageView) view.findViewById(R.id.imgView);
            // 设置默认图片
            photoView.setImageResource(R.color.gray);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

            if(imageDownloader != null){
                DownloadImageTask task = new DownloadImageTask();
                imageDownloader.imageDownload(data[position], photoView, "/maomovie/cache/avatarImg/",
                        GalleryActivity.this,task);
            }
            photoView.setTag(data[position]);
            photoView.setTag(data[position]);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GalleryActivity.this.finish();
                }
            });
            return view;
        }

        class DownloadImageTask implements OnImageDownload {
            @Override
            public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
                LoadImageUtil.setImageScaleBySelf(imageView,bitmap,0.5f,0.5f);
                        ((DragImageView) imageView).setmActivity(GalleryActivity.this);//注入Activity.
                /** 测量状态栏高度 **/
                ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
                viewTreeObserver
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                if (state_height == 0) {
                                    // 获取状况栏高度
                                    Rect frame = new Rect();
                                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                                    state_height = frame.top;
                                    ((DragImageView)imageView).setScreen_H(window_height - state_height);
                                    ((DragImageView)imageView).setScreen_W(window_width);
                                }
                            }
                        });
            }
        }
    }

    /**
     * 控件点击事件
     * @param v
     */
    public void onClick(View v){
        finish();
    }
}
