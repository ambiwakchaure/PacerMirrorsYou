package com.example.pacermirror.errorHandler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.example.pacermirror.R;

import java.util.ArrayList;
import java.util.List;


public class ErrorActivity extends Activity implements AsyncResponse<String> {

	TextView error;

    String errorE,jsonDetails;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_error);


		error = (TextView) findViewById(R.id.error);


		errorE = getIntent().getStringExtra("error");
		jsonDetails = getIntent().getStringExtra("json");

		Log.e("ERROR_LOG",""+errorE);

		error.setText(Html.fromHtml(errorE));


	}


	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
	}

	public void processFinishLog(String output) {
		
		
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		System.exit(0);
		finish();
	}

	public void goBack(View view) {

		finish();
	}
}
