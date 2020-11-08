package com.example.familymapclient.async;

import java.util.concurrent.Callable;

import androidx.annotation.NonNull;

/**
 * An object that encapsulates some work to be run.
 * @param <R> The type of result produced by a successfully-completed task.
 */
public interface CustomCallable<R> extends Callable<R> {
	/**
	 * Called by the <code>TaskRunner</code> on the main thread before the task begins running.
	 */
	void willBeginRunning();
	
	/**
	 * Called by the <code>TaskRunner</code> on the main thread after the task completes successfully.
	 * @param result The result of the call;
	 */
	void didFinishRunning(@NonNull R result);
	
	/**
	 * Called by the <code>TaskRunner</code> on the main thread if the task failed.
	 * @param error The object that was thrown, usually a subclass of <code>Exception</code>.
	 */
	void didFail(@NonNull Throwable error);
}
