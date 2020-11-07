package com.example.familymapclient.async;

import java.util.concurrent.Callable;

import androidx.annotation.NonNull;

public interface CustomCallable<R> extends Callable<R> {
	/**
	 * Called by the <code>TaskRunner</code> on the main thread before asynchronous tasks begin running.
	 */
	void willBeginRunning();
	
	/**
	 * Called by the <code>TaskRunner</code> on the main thread after asynchronous tasks finish running.
	 * @param result The result of the call;
	 */
	void didFinishRunning(@NonNull R result);
	
	/**
	 * Called by the <code>TaskRunner</code> on the main thread if the asynchronous tasks failed.
	 * @param error The object that was thrown.
	 */
	void didFail(@NonNull Throwable error);
}
