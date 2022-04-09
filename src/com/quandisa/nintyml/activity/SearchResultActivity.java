package com.quandisa.nintyml.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.quandisa.nintyml.R;
import com.quandisa.nintyml.app.data.ApplicationData;
import com.quandisa.nintyml.app.data.Constants;
import com.quandisa.nintyml.dto.HotelDetails;
import com.quandisa.nintyml.dto.LocationDTO;
import com.quandisa.nintyml.dto.ReviewDetailsDTO;
import com.quandisa.nintyml.dto.SearchHotelDTO;
import com.quandisa.nintyml.network.NetworkManager;
import com.quandisa.nintyml.network.WebserviceHelper;
import com.quandisa.nintyml.util.GPSTracker;
import com.quandisa.nintyml.util.SearchResultListAdapter;

public class SearchResultActivity extends ListActivity {

	DisplayImageOptions	options;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (NetworkManager.isNetworkAvailable(this)) {
			setContentView(R.layout.search_result_screen);

			options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher).resetViewBeforeLoading(true).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
			setHotelList();

			final LinearLayout btnSearch = (LinearLayout) findViewById(R.id.btn_search_view);
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					displaySearchViewDialog();
				}
			});

		} else {
			setContentView(R.layout.no_internet_layout);
			final Button retryBtn = (Button) findViewById(R.id.retry);
			retryBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onCreate(savedInstanceState);
				}
			});
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setHotelList() {
		TextView hotelCount = (TextView) findViewById(R.id.txt_search_result);
		if (ApplicationData.getSearchHotelResult() != null && !ApplicationData.getSearchHotelResult().isEmpty()) {
			SearchResultListAdapter adapter = new SearchResultListAdapter(SearchResultActivity.this,
					ApplicationData.getSearchHotelResult(), options);
			adapter.notifyDataSetChanged();
			setListAdapter(adapter);
			UIHelper.disableListViewScroll(getListView());

			hotelCount.setText(ApplicationData.getSearchHotelResult().size() + " Results");
		} else {
			hotelCount.setText("No Result");
		}
	}

	/**
	 * 
	 */
	private void displaySearchViewDialog() {
		// create a Dialog component
		final Dialog dialog = new Dialog(SearchResultActivity.this);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.search_layout);
		dialog.setTitle("Search Hotel");

		final EditText edtTitle = (EditText) dialog.findViewById(R.id.edt_search_name);

		final Spinner spiCategory = (Spinner) dialog.findViewById(R.id.spinner_category);
		spiCategory.setSelection(0);
		List<String> catList = new ArrayList<String>(ApplicationData.getCategories());
		catList.add(0, "Search by Category");

		ArrayAdapter<String> adpCategory = new ArrayAdapter<String>(SearchResultActivity.this,
				android.R.layout.simple_spinner_item, catList);
		adpCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiCategory.setAdapter(adpCategory);

		final Spinner spiArea = (Spinner) dialog.findViewById(R.id.spinner_area);
		spiArea.setSelection(0);
		List<String> areaList = new ArrayList<String>(ApplicationData.getAreaList());
		areaList.add(0, "Search by Area");
		areaList.add(1, "Near My Location");

		ArrayAdapter<String> adpArea = new ArrayAdapter<String>(SearchResultActivity.this,
				android.R.layout.simple_spinner_item, areaList);
		adpArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiArea.setAdapter(adpArea);

		Button btnSearch = (Button) dialog.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkManager.isNetworkAvailable(SearchResultActivity.this)) {
					SearchHotelDTO searchHotelDTO = new SearchHotelDTO();

					String hotelName = edtTitle.getText().toString();
					searchHotelDTO.setName(hotelName);

					if (spiCategory.getSelectedItemPosition() != 0) {
						String selectedCategory = spiCategory.getSelectedItem().toString();
						searchHotelDTO.setCategory(selectedCategory);
					}
					if (spiArea.getSelectedItemPosition() != 0) {
						if (spiArea.getSelectedItemPosition() == 1) {
							GPSTracker gps = new GPSTracker(SearchResultActivity.this);
							if (gps.canGetLocation()) {
								double userlatitude = gps.getLatitude();
								double userlongitude = gps.getLongitude();
								searchHotelDTO.setLatitude(userlatitude);
								searchHotelDTO.setLongitude(userlongitude);

								Log.d("SearchResultActivity", "userlatitude: " + userlatitude + "\nuserlongitude: "
										+ userlongitude);
							} else {
								UIHelper.showAlertDialog(SearchResultActivity.this, "GPS Status",
										"Couldn't get location information. Please enable GPS", false);
							}
						} else {
							String selectedArea = spiArea.getSelectedItem().toString();
							LocationDTO locationDTO = ApplicationData.getLocationMap().get(selectedArea);
							if (locationDTO != null) {
								searchHotelDTO.setLatitude(locationDTO.getLatitude());
								searchHotelDTO.setLongitude(locationDTO.getLongitude());
							}
						}
					}
					new SearchHotelsAsyncTask().execute(searchHotelDTO);
					dialog.dismiss();
				} else {
					UIHelper.showAlertDialog(SearchResultActivity.this, Constants.NO_INTERNET_TITLE,
							Constants.NO_INTERNET_MESSAGE, false);
				}
			}
		});

		// Set cancel button
		Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (NetworkManager.isNetworkAvailable(SearchResultActivity.this)) {
			SearchResultListAdapter adapter = (SearchResultListAdapter) l.getAdapter();
			new GetHotelAsyncTask().execute(adapter.getHotelList().get(position).getHotelId());
			super.onListItemClick(l, v, position, id);
		} else {
			UIHelper.showAlertDialog(SearchResultActivity.this, Constants.NO_INTERNET_TITLE,
					Constants.NO_INTERNET_MESSAGE, false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public class GetHotelAsyncTask extends AsyncTask<Integer, Void, HotelDetails> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(SearchResultActivity.this);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setMessage(Constants.PLEASE_WAIT);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected HotelDetails doInBackground(Integer... hotelId) {
			HotelDetails hotelDetails = null;
			try {
				hotelDetails = WebserviceHelper.getHotelDetails(hotelId[0]);
				ApplicationData.setHotelDetails(hotelDetails);

				List<ReviewDetailsDTO> hotelReviewList = WebserviceHelper.getHotelReviews(hotelId[0]);
				ApplicationData.setHotelReviewsList(hotelReviewList);

			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return hotelDetails;
		}

		@Override
		protected void onPostExecute(HotelDetails result) {
			dialog.dismiss();
			super.onPostExecute(result);

			if (result != null) {
				Intent intent = new Intent(SearchResultActivity.this, HotelDetailsActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(SearchResultActivity.this, "No Result", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class SearchHotelsAsyncTask extends AsyncTask<SearchHotelDTO, Void, List<HotelDetails>> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(SearchResultActivity.this);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setMessage(Constants.PLEASE_WAIT);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected List<HotelDetails> doInBackground(SearchHotelDTO... searchHotelDTO) {
			List<HotelDetails> hotelDetails = null;
			try {
				hotelDetails = WebserviceHelper.searchHotel(searchHotelDTO[0]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return hotelDetails;
		}

		@Override
		protected void onPostExecute(List<HotelDetails> result) {
			dialog.dismiss();
			super.onPostExecute(result);

			ApplicationData.setSearchHotelResult(result);
			if (result != null && !result.isEmpty()) {
				Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(SearchResultActivity.this, "No Result", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
