package com.example.familymapclient.transport;

import androidx.annotation.NonNull;

/**
 * An interface for an object that listens for start and completion events for an asynchronous task.
 * @param <R> The type of result returned by the task.
 * @param <Task> The type of task.
 */
public interface OnDataFetched<R, Task extends RequestTask<?>> {
	/**
	 * Called by a task object when the task receives its <code>willBeginRunning</code> callback.
	 * @param task The task that will begin running.
	 */
	public void taskWillBeginRunning(@NonNull Task task);
	
	/**
	 * Called by a task object when the task receives its <code>didFinishRunning</code> callback.
	 * @param task The task that just completed.
	 * @param result The result of the task.
	 */
	public void taskDidFinishRunning(@NonNull Task task, @NonNull R result);
}
