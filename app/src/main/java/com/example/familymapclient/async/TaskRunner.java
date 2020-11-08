package com.example.familymapclient.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An object that manages the multithreaded executions of several tasks. This object serves as a
 * replacement for the AsyncTask API.
 *
 * @see "https://medium.com/swlh/asynctask-is-deprecated-now-what-f30c31362761"
 */
public class TaskRunner {
	private final Handler handler;
	private final Executor executor;
	
	public TaskRunner() {
		this.handler = new Handler(Looper.getMainLooper());
		this.executor = Executors.newCachedThreadPool();
	}
	
	/**
	 * Schedules the given callable task on a background thread.
	 *
	 * @param callable The task to run. The task's <code>willBeginRunning</code> handler is called
	 *                 immediately.
	 * @param <R> The type of result returned by the task.
	 */
	public <R> void executeAsync(@NonNull CustomCallable<R> callable) {
		callable.willBeginRunning();
		executor.execute(new RunnableTask<>(handler, callable));
	}
	
	/**
	 * A runnable for the asynchronous portion of the task.
	 * @param <R> The type of result returned by the task.
	 */
	public static class RunnableTask<R> implements Runnable {
		private final Handler handler;
		private final CustomCallable<R> callable;
		
		/**
		 * Prepares a runnable for the task's main work.
		 * @param handler The thread scheduler.
		 * @param callable The task to run.
		 */
		public RunnableTask(@NonNull Handler handler, @NonNull CustomCallable<R> callable) {
			this.handler = handler;
			this.callable = callable;
		}
		
		
		/**
		 * Runs the task's main portion. If the task completes successfully, then its completion callback
		 * is scheduled on the main thread. If the task fails—that is, if the task throws—then the task's
		 * failure callback is scheduled on the main thread.
		 */
		@Override
		public void run() {
			try {
				final R result = callable.call();
				handler.post(new RunnableTaskForHandler<>(callable, result));
			} catch (Throwable e) {
				handler.post(new RunnableTaskForHandler<>(callable, e));
			}
		}
	}
	
	
	/**
	 * A runnable for the synchronous completion callback for the task.
	 * @param <R> The type of result returned by the task.
	 */
	public static class RunnableTaskForHandler<R> implements Runnable {
		private final CustomCallable<R> callable;
		private final @Nullable R result;
		private final @Nullable Throwable error;
		
		/**
		 * Prepares a runnable for the task's successful completion handler.
		 * @param callable The task that finished.
		 * @param result The result of the task.
		 */
		public RunnableTaskForHandler(@NonNull CustomCallable<R> callable, @NonNull R result) {
			this.callable = callable;
			this.result = result;
			this.error = null;
		}
		
		/**
		 * Prepares a runnable for the task's unsuccessful completion handler.
		 * @param callable The task that threw.
		 * @param error The object that the task threw.
		 */
		public RunnableTaskForHandler(@NonNull CustomCallable<R> callable, @NonNull Throwable error) {
			this.callable = callable;
			this.result = null;
			this.error = error;
		}
		
		/**
		 * Runs one of the task's completion callbacks. If the task finished by throwing, then runs the
		 * task's failure callback is run. If the task finished successfully, then runs the task's
		 * success callback.
		 */
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
