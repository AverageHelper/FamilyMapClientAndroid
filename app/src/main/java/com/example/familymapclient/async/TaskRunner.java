package com.example.familymapclient.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
				handler.post(new RunnableTaskForHandler<R>(callable, result));
			} catch (Throwable e) {
				handler.post(new RunnableTaskForHandler<R>(callable, e));
			}
		}
	}
	
	public static class RunnableTaskForHandler<R> implements Runnable {
		private final CustomCallable<R> callable;
		private final @Nullable R result;
		private final @Nullable Throwable error;
		
		public RunnableTaskForHandler(CustomCallable<R> callable, @NonNull R result) {
			this.callable = callable;
			this.result = result;
			this.error = null;
		}
		
		public RunnableTaskForHandler(CustomCallable<R> callable, @NonNull Throwable error) {
			this.callable = callable;
			this.result = null;
			this.error = error;
		}
		
		@Override
		public void run() {
			if (error != null) {
				callable.didFail(error);
				
			} else if (result != null) {
				callable.didFinishRunning(result);
			}
		}
	}
}
