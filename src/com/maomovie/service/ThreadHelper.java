package com.maomovie.service;

import java.util.UUID;
import java.util.Vector;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * 线程处理对象
 * 
 * @author Administrator
 * 
 */
public class ThreadHelper {
	private Handler handler;
	private Vector<Thread> threads = new Vector<Thread>();

	public ThreadHelper(Handler handler) {
		this.handler = handler;
	}

	public int dataHander(final Runnable runnable) {
		final HandlerThread thread = new HandlerThread(UUID.randomUUID().toString());
		thread.start();
		Handler handler = new Handler(thread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				runnable.run();
			}
		};
		Message msg = handler.obtainMessage();
		msg.sendToTarget();
		threads.add(thread);
		return threads.size() - 1;
	}

	public int dataHander(final ThreadHandler threadHandler) {
		final HandlerThread thread = new HandlerThread(UUID.randomUUID().toString());
		thread.start();
		Handler handler = new Handler(thread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				final Object result = threadHandler.run();
				ThreadHelper.this.handler.post(new Runnable() {
					public void run() {
						threadHandler.result(result);
					}
				});
			}
		};
		Message msg = handler.obtainMessage();
		msg.sendToTarget();
		threads.add(thread);
		return threads.size() - 1;
	}

	public void cancel(int location) {
		HandlerThread thread = (HandlerThread) threads.get(location);
		thread.stop();
	}
}
