package com.magicrecipe.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magicrecipe.lazyloading.ImageLoader;
import com.magicrecipe.model.Response;
import com.puppy.magicrecipe.R;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Response> items;
	public ImageLoader imageLoader;

	public CustomListAdapter(Activity activity, ArrayList<Response> items) {
		this.activity = activity;
		this.items = items;
		imageLoader = new ImageLoader(activity);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int location) {
		return items.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		
		ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
		
		imageLoader.DisplayImage(items.get(position).getThumburl(), thumbNail);   //lazy loading
		
		title.setText(items.get(position).getTitle());


		return convertView;
	}

}