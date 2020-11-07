package com.example.familymapclient.transport;

import androidx.annotation.NonNull;

public interface OnDataFetched<R> {
	public void taskWillBeginRunning();
	public void taskDidFinishRunning(@NonNull R result);
}
