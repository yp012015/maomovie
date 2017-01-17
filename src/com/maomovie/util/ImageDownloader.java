package com.maomovie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 功能：用于listView异步加载图片资源
 */
public class ImageDownloader {
	private static final String TAG = "ImageDownloader";
	private HashMap<String, MyAsyncTask> map = new HashMap<String, MyAsyncTask>();
	private Map<String, SoftReference<Bitmap>> imageCaches = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * @param url  该没ImageView对应的url
	 * @param mImageView
	 * @param path  文件的存储路径
	 * @param mActivity
	 * @param download  OnImageDownload回调接口，在onPostExcute()中被调用
	 */
	public void imageDownload(String url, ImageView mImageView, String path, Activity mActivity,OnImageDownload download) {
		SoftReference<Bitmap> currBitmap = imageCaches.get(url);
		Bitmap softRefBitmap = null;
		if (currBitmap != null) {
			softRefBitmap = currBitmap.get();
		}
		String imageName = "";
		if (url != null) {
			imageName = GetPicName.getInstance().getImageName(url);
		}
		Bitmap bitmap = getBitmapFromFile(mActivity, imageName, path);
		// 先从软引用中拿数据
		if (currBitmap != null && mImageView != null && softRefBitmap != null && url.equals(mImageView.getTag())) {
			mImageView.setImageBitmap(softRefBitmap);
		}
		// 软引用中没有，从文件中拿数据
		else if (bitmap != null && mImageView != null && url.equals(mImageView.getTag())) {
			mImageView.setImageBitmap(bitmap);
		}
		// 文件中也没有，此时根据mImageView的tag，即url去判断该url对应的task是否已经在执行，如果在执行，本次操作不创建新的线程，否则创建新的线程。
		else if (url != null && needCreateNewTask(mImageView)) {
			MyAsyncTask task = new MyAsyncTask(url, path, mImageView, download, mActivity);
			if (mImageView != null) {
				Log.i(TAG, "执行MyAsyncTask --> " + GetPicName.flag);
				GetPicName.flag++;
				task.execute();
				// 将对应的url对应的任务存起来
				map.put(url, task);
			}
		}
	}

	/**
	 * 异步下载图片的方法
	 */
	class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private String path;
		private ImageView myImageView;
		private OnImageDownload download;
		private Activity myActivity;

		public MyAsyncTask(String url, String path, ImageView myImageView, OnImageDownload download,
				Activity myActivity) {
			this.url = url;
			this.path = path;
			this.myImageView = myImageView;
			this.download = download;
			this.myActivity = myActivity;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap data = null;
			if (url != null) {
				try {
					URL c_url = new URL(url);
					InputStream bitmap_data = c_url.openStream();
					data = BitmapFactory.decodeStream(bitmap_data);
					String imageName = GetPicName.getInstance().getImageName(url);
					if (!setBitmapToFile(path, myActivity, imageName, data)) {
						removeBitmapFromFile(path, myActivity, imageName);
					}
					imageCaches.put(url, new SoftReference<Bitmap>(data.createScaledBitmap(data, 100, 100, true)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// 回调设置图片
			if (download != null) {
				download.onDownloadSucc(result, url, myImageView);
				// 该url对应的task已经下载完成，从map中将其删除
				removeTaskFormMap(url);
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 判断是否需要重新创建线程下载图片，如果需要，返回值为true。
	 * 
	 * @param mImageView
	 * @return
	 */
	private boolean needCreateNewTask(ImageView mImageView) {
		boolean b = true;
		if (mImageView != null) {
			String curr_task_url = (String) mImageView.getTag();
			if (isTasksContains(curr_task_url)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * 检查该url（最终反映的是当前的ImageView的tag，tag会根据position的不同而不同）对应的task是否存在
	 * 
	 * @param url
	 * @return
	 */
	private boolean isTasksContains(String url) {
		boolean b = false;
		if (map != null && map.get(url) != null) {
			b = true;
		}
		return b;
	}

	/**
	 * 删除map中该url的信息，这一步很重要，不然MyAsyncTask的引用会“一直”存在于map中
	 * 
	 * @param url
	 */
	private void removeTaskFormMap(String url) {
		if (url != null && map != null && map.get(url) != null) {
			map.remove(url);
			System.out.println("当前map的大小==" + map.size());
		}
	}

	/**
	 * 从文件中拿图片
	 * 
	 * @param mActivity
	 * @param imageName       图片名字
	 * @param path            图片路径
	 * @return
	 */
	private Bitmap getBitmapFromFile(Activity mActivity, String imageName, String path) {
		Bitmap bitmap = null;
		if (imageName != null) {
			File file = null;
			String real_path = "";
			try {
				if (GetPicName.getInstance().hasSDCard()) {
					real_path = GetPicName.getInstance().getExtPath()
							+ (path != null && path.startsWith("/") ? path : "/" + path);
				} else {
					real_path = GetPicName.getInstance().getPackagePath(mActivity)
							+ (path != null && path.startsWith("/") ? path : "/" + path);
				}
				file = new File(real_path, imageName);
				if (file.exists())
					bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
				bitmap = null;
			}
		}
		return bitmap;
	}

	/**
	 * 将下载好的图片存放到文件中
	 * 
	 * @param path         图片路径
	 * @param mActivity
	 * @param imageName    图片名字
	 * @param bitmap
	 *            图片
	 * @return
	 */
	private boolean setBitmapToFile(String path, Activity mActivity, String imageName, Bitmap bitmap) {
		File file = null;
		String real_path = "";
		try {
			if (GetPicName.getInstance().hasSDCard()) {
				real_path = GetPicName.getInstance().getExtPath()
						+ (path != null && path.startsWith("/") ? path : "/" + path+".png");
			} else {
				real_path = GetPicName.getInstance().getPackagePath(mActivity)
						+ (path != null && path.startsWith("/") ? path : "/" + path+".png");
			}
			file = new File(real_path, imageName);
			if (!file.exists()) {
				File file2 = new File(real_path + "/");
				file2.mkdirs();
			}
			file.createNewFile();
			FileOutputStream fos = null;
			if (GetPicName.getInstance().hasSDCard()) {
				fos = new FileOutputStream(file);
			} else {
				fos = mActivity.openFileOutput(imageName, Context.MODE_PRIVATE);
			}

			if (imageName != null && (imageName.contains(".png") || imageName.contains(".PNG"))) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			}
			fos.flush();
			if (fos != null) {
				fos.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 辅助方法，一般不调用
	 * 
	 * @param path
	 * @param mActivity
	 * @param imageName
	 */
	private void removeBitmapFromFile(String path, Activity mActivity, String imageName) {
		File file = null;
		String real_path = "";
		try {
			if (GetPicName.getInstance().hasSDCard()) {
				real_path = GetPicName.getInstance().getExtPath()
						+ (path != null && path.startsWith("/") ? path : "/" + path);
			} else {
				real_path = GetPicName.getInstance().getPackagePath(mActivity)
						+ (path != null && path.startsWith("/") ? path : "/" + path);
			}
			file = new File(real_path, imageName);
			if (file != null)
				file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存文件
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	/*public void saveFile(Bitmap bm, String fileName) throws IOException {
		String path = getSDPath() +"/revoeye/";
		File dirFile = new File(path);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}*/

}
