package com.scho.http;

import java.io.IOException;
import java.io.InputStream;

public interface OnHttpListener {
	public void OnStart();

	public void OnProgress(String fileName, long send, int progress);

	public void OnWaitResponse();

	public void OnResponsed(InputStream stream) throws IOException;

	public void OnError(int code, String msg);

	public void OnEnd(Boolean success);
}
