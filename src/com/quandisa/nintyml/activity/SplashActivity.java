package com.quandisa.nintyml.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

import com.quandisa.nintyml.R;
import com.quandisa.nintyml.app.data.ApplicationData;
import com.quandisa.nintyml.app.data.Constants;
import com.quandisa.nintyml.dto.HotelDetails;
import com.quandisa.nintyml.network.NetworkManager;
import com.quandisa.nintyml.network.WebserviceHelper;

/**
 * This is the initial splash screen for HUB application.
 * 
 * @author pravin.gayal
 * 
 */
public class SplashActivity extends Activity {
	private static final int	TOP_N_HOTEL_COUNT	= 5;
	private static final long	DELAY				= 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		if (NetworkManager.isNetworkAvailable(this)) {
			ApplicationData.setCategories();
			ApplicationData.setCategoryImages();

			Handler handler = new Handler(Looper.getMainLooper());

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new SearchHotelsAsyncTask().execute(TOP_N_HOTEL_COUNT);
				}
			}, DELAY);

		} else {
			// No internet
			UIHelper.showAlertDialog(this, Constants.NO_INTERNET_TITLE, Constants.NO_INTERNET_MESSAGE, true);
		}
	}

	private class SearchHotelsAsyncTask extends AsyncTask<Integer, Void, List<HotelDetails>> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(SplashActivity.this);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setMessage(Constants.PLEASE_WAIT);
			// dialog.show();
			super.onPreExecute();
		}

		@Override
		protected List<HotelDetails> doInBackground(Integer... count) {
			List<HotelDetails> hotelDetails = null;
			try {
				hotelDetails = WebserviceHelper.getTopNHotels(count[0]);
				ApplicationData.setLocationMap(WebserviceHelper.getLocations());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return hotelDetails;
		}

		@Override
		protected void onPostExecute(List<HotelDetails> result) {
			// dialog.dismiss();
			super.onPostExecute(result);

			ApplicationData.setMostPopularHotels(result);

			finish();
			startActivity(new Intent(SplashActivity.this, HomeActivity.class));
		}
	}

}