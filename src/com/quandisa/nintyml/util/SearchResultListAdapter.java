package com.quandisa.nintyml.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quandisa.nintyml.R;
import com.quandisa.nintyml.app.data.Constants;
import com.quandisa.nintyml.dto.HotelDetails;

public class SearchResultListAdapter extends BaseAdapter {
	private Context				mContext;
	private List<HotelDetails>	list	= new ArrayList<HotelDetails>();
	DisplayImageOptions			options;

	public SearchResultListAdapter(Context mContext, List<HotelDetails> mResults, DisplayImageOptions options) {
		super();
		this.mContext = mContext;
		this.list = mResults;
		this.options = options;
	}

	public void setListItems(List<HotelDetails> results) {
		list = results;
		notifyDataSetChanged();
	}

	/**
	 * @return the list
	 */
	public List<HotelDetails> getHotelList() {
		return list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {

		HotelDetails hotelDetails = list.get(pos);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_result_view, null);
		}

		TextView hotelName = (TextView) convertView.findViewById(R.id.hotel_name);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.hotel_logo);
		final ProgressBar spinner = (ProgressBar) convertView.findViewById(R.id.loading);
		TextView hotelTiming = (TextView) convertView.findViewById(R.id.hotel_timing);

		RatingBar hotelRating = (RatingBar) convertView.findViewById(R.id.hotel_rating);
		LayerDrawable stars = (LayerDrawable) hotelRating.getProgressDrawable();
		// stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP); // Star background
		// Star half filled star background
		// stars.getDrawable(android.R.id.progress).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
		stars.getDrawable(2).setColorFilter(Constants.RATINGBAR_BACKGROUND_COLOR, PorterDuff.Mode.SRC_ATOP); // Star//
																												// progress
		// background

		// Set all details
		hotelName.setText(hotelDetails.getName());

		String o = hotelDetails.getOpeningTime();
		String c = hotelDetails.getClosingTime();

		if (hotelDetails.getOpeningTime() != null && hotelDetails.getClosingTime() != null) {
			if (hotelDetails.getOpeningTime().length() >= 5 && hotelDetails.getClosingTime().length() >= 5) {
				hotelTiming.setText(hotelDetails.getOpeningTime().substring(0, 5) + " to "
						+ hotelDetails.getClosingTime().substring(0, 5));
			}
		}
		if (hotelDetails.getRating() != null) {
			hotelRating.setRating(hotelDetails.getRating());
		}

		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

		ImageLoader.getInstance().displayImage(hotelDetails.getImgUrl(), imageView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
						}

						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);
					}

				});
		return convertView;
	}
}