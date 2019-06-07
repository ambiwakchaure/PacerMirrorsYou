package com.example.pacermirror.errorHandler;

public interface AsyncResponse<T> {

	 void processFinish(T output);
	 
	 void processFinishLog(T output);
	 
}
