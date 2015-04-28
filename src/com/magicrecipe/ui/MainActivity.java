package com.magicrecipe.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.magicrecipe.adapter.CustomListAdapter;
import com.magicrecipe.model.Response;
import com.magicrecipe.parser.FetchDataAsync;
import com.magicrecipe.parser.OnResponseListener;
import com.puppy.magicrecipe.R;

public class MainActivity extends ActionBarActivity implements
		OnResponseListener {

	private FetchDataAsync dataAsync;
	private ListView listView;
	private CustomListAdapter adapter;
	private ArrayList<Response> responseList = new ArrayList<Response>();
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EditText userInput = (EditText) findViewById(R.id.editText1);
		listView = (ListView) findViewById(R.id.list);

		adapter = new CustomListAdapter(this, responseList);
		listView.setAdapter(adapter);

		userInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (dataAsync != null) {
					dataAsync.cancel(true); // cancel already started thread to
											// avoid response overlapping
				}

				dataAsync = new FetchDataAsync();
				dataAsync.setDataAsyncListener(MainActivity.this);
				String query = s.toString();
				query = query.replaceAll("\n", "").trim();

				if (query.length() > 2) // remove space and set a threshold
					dataAsync.execute(query);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (responseList != null) {
					Intent intent = new Intent(MainActivity.this,
							WebviewActivity.class);
					intent.putExtra("URL", responseList.get(position).getUrl());
					startActivity(intent); // Starts a new activity to show
											// recipe details in a webview
				}

			}
		});

		mAdView = (AdView) findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
//				AdRequest.DEVICE_ID_EMULATOR).build();
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	/** Called when leaving the activity */
	@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResponse(ArrayList<Response> responseList) {
		// TODO Auto-generated method stub

		if (responseList.size() > 0) {

			this.responseList.clear();

			for (Response response : responseList) {

				this.responseList.add(response);

			}
			adapter.notifyDataSetChanged();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No results found", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}
}
