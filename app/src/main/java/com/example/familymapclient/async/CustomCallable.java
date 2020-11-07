package com.example.familymapclient.async;

import java.util.concurrent.Callable;

import androidx.annotation.NonNull;

public interface CustomCallable<R> extends Callable<R> {
	/**
	 * Called by the <code>TaskRunner</code> before asynchronous tasks begin running.
	 */
	default void willBeginRunning() {}
	
	/**
	 * Called by the <code>TaskRunner</code> after asynchronous tasks finish running.
	 * @param result The result of the call;
	 */
	default void didFinishRunning(@NonNull R result) {}
}
