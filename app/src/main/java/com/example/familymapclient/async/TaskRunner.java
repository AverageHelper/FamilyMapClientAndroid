package com.example.familymapclient.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * @see "https://medium.com/swlh/asynctask-is-deprecated-now-what-f30c31362761"
 */
public class TaskRunner {
	private final Handler handler;
	private final Executor executor;
	
	public TaskRunner() {
		this.handler = new Handler(Looper.getMainLooper());
		this.executor = Executors.newCachedThreadPool();
	}
	
	public <R> void executeAsync(@NonNull CustomCallable<R> callable) {
		callable.willBeginRunning();
		executor.execute(new RunnableTask<R>(handler, callable));
	}
	
	public static class RunnableTask<R> implements Runnable {
		private final Handler handler;
		private final CustomCallable<R> callable;
		
		public RunnableTask(Handler handler, CustomCallable<R> callable) {
			this.handler = handler;
			this.callable = callable;
		}
		
		@Override
		public void run() {
			try {
				final R result = callable.call();
				handler.post(new RunnableTaskForHandler<>(callable, result));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class RunnableTaskForHandler<R> implements Runnable {
		private final CustomCallable<R> callable;
		private final R result;
		
		public RunnableTaskForHandler(CustomCallable<R> callable, R result) {
			this.callable = callable;
			this.result = result;
		}
		
		@Override
		public void run() {
			callable.didFinishRunning(result);
		}
	}
}
