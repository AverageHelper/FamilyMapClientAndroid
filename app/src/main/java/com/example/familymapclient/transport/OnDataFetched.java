package com.example.familymapclient.transport;

import androidx.annotation.NonNull;

/**
 * An interface for an object that listens for start and completion events for an asynchronous task.
 * @param <R> The type of response returned by the task.
 */
public interface OnDataFetched<R> {
	/**
	 * Called by a task object when the task receives its <code>willBeginRunning</code> callback.
	 * @param task The task that will begin running.
	 * @param <Task> The type of task.
	 */
	<Task extends RequestTask<?>> void taskWillBeginRunning(@NonNull Task task);
	
	/**
	 * Called by a task object when the task receives its <code>didFinishRunning</code> callback.
	 * @param task The task that just completed.
	 * @param response The response returned by the task.
	 * @param <Task> The type of task.
	 */
	<Task extends RequestTask<?>> void taskDidFinishRunning(@NonNull Task task, @NonNull R response);
	
	/**
	 * Called by a task object when the task receives its <code>didFail</code> callback.
	 * @param task The task that failed.
	 * @param error The object that was thrown.
	 * @param <Task> The type of task.
	 */
	<Task extends RequestTask<?>> void taskDidFail(@NonNull Task task, @NonNull Throwable error);
}
