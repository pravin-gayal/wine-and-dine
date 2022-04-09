package com.quandisa.nintyml.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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

public class HomeActivity extends ListActivity {

	DisplayImageOptions	options;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_screen);
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		setGridView();
		setRecommendedHotelList();

		final ScrollView scrollview = ((ScrollView) findViewById(R.id.home_scroll_view));
		scrollview.post(new Runnable() {
			@Override
			public void run() {
				scrollview.fullScroll(ScrollView.FOCUS_UP);
			}
		});

		final Button btnRetry = (Button) findViewById(R.id.retry);
		btnRetry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRecommendedHotelList();
			}
		});

		final LinearLayout btnSearch = (LinearLayout) findViewById(R.id.btn_search_view);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displaySearchViewDialog();
			}
		});

	}

	/**
	 * 
	 */
	private void displaySearchViewDialog() {
		// create a Dialog component
		final Dialog dialog = new Dialog(HomeActivity.this);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.search_layout);
		dialog.setTitle("Search Hotel");

		final EditText edtTitle = (EditText) dialog.findViewById(R.id.edt_search_name);

		final Spinner spiCategory = (Spinner) dialog.findViewById(R.id.spinner_category);
		spiCategory.setSelection(0);
		List<String> catList = new ArrayList<String>(ApplicationData.getCategories());
		catList.add(0, "Search by Category");

		ArrayAdapter<String> adpCategory = new ArrayAdapter<String>(HomeActivity.this,
				android.R.layout.simple_spinner_item, catList);
		adpCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiCategory.setAdapter(adpCategory);

		final Spinner spiArea = (Spinner) dialog.findViewById(R.id.spinner_area);
		spiArea.setSelection(0);
		List<String> areaList = new ArrayList<String>(ApplicationData.getAreaList());
		areaList.add(0, "Search by Area");
		areaList.add(1, "Near My Location");

		ArrayAdapter<String> adpArea = new ArrayAdapter<String>(HomeActivity.this,
				android.R.layout.simple_spinner_item, areaList);
		adpArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiArea.setAdapter(adpArea);

		Button btnSearch = (Button) dialog.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkManager.isNetworkAvailable(HomeActivity.this)) {
					SearchHotelDTO searchHotelDTO = new SearchHotelDTO();

					String hotelName = edtTitle.getText().toString();
					searchHotelDTO.setName(hotelName);

					if (spiCategory.getSelectedItemPosition() != 0) {
						String selectedCategory = spiCategory.getSelectedItem().toString();
						searchHotelDTO.setCategory(selectedCategory);
					}
					if (spiArea.getSelectedItemPosition() != 0) {
						if (spiArea.getSelectedItemPosition() == 1) {
							GPSTracker gps = new GPSTracker(HomeActivity.this);
							if (gps.canGetLocation()) {
								double userlatitude = gps.getLatitude();
								double userlongitude = gps.getLongitude();
								searchHotelDTO.setLatitude(userlatitude);
								searchHotelDTO.setLongitude(userlongitude);

								Log.d("HomeActivity", "userlatitude: " + userlatitude + "\nuserlongitude: "
										+ userlongitude);
							} else {
								UIHelper.showAlertDialog(HomeActivity.this, "GPS Status",
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
					UIHelper.showAlertDialog(HomeActivity.this, Constants.NO_INTERNET_TITLE,
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

	/**
	 * Method to set recommended listview
	 */
	private void setRecommendedHotelList() {
		LinearLayout noConnectionLayout = (LinearLayout) findViewById(R.id.no_connection_layout);

		if (NetworkManager.isNetworkAvailable(this)) {

			// new RequestTask2().execute(5);
			List<HotelDetails> list = ApplicationData.getMostPopularHotels();
			if (list != null && !list.isEmpty()) {
				SearchResultListAdapter listAdapter = new SearchResultListAdapter(HomeActivity.this, list, options);
				listAdapter.notifyDataSetChanged();
				setListAdapter(listAdapter);

			} else {

			}

			noConnectionLayout.setVisibility(View.GONE);
			getListView().setVisibility(View.VISIBLE);

		} else {
			noConnectionLayout.setVisibility(View.VISIBLE);
			getListView().setVisibility(View.GONE);
		}

		UIHelper.disableListViewScroll(getListView());
	}

	/**
	 * Method to set category gridview
	 */
	private void setGridView() {
		CustomGridAdapter gridAdapter = new CustomGridAdapter(HomeActivity.this, ApplicationData.getCategories(),
				ApplicationData.getCategoryImages());

		gridAdapter.notifyDataSetChanged();
		GridView gridView = (GridView) findViewById(R.id.grid_category);
		gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (NetworkManager.isNetworkAvailable(HomeActivity.this)) {
					SearchHotelDTO searchHotelDTO = new SearchHotelDTO();
					searchHotelDTO.setCategory(ApplicationData.getCategories().get(position));
					new SearchHotelsAsyncTask().execute(searchHotelDTO);
				} else {
					UIHelper.showAlertDialog(HomeActivity.this, Constants.NO_INTERNET_TITLE,
							Constants.NO_INTERNET_MESSAGE, false);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (NetworkManager.isNetworkAvailable(HomeActivity.this)) {
			SearchResultListAdapter adapter = (SearchResultListAdapter) l.getAdapter();
			new GetHotelAsyncTask().execute(adapter.getHotelList().get(position).getHotelId());
			super.onListItemClick(l, v, position, id);
		} else {
			UIHelper.showAlertDialog(HomeActivity.this, Constants.NO_INTERNET_TITLE, Constants.NO_INTERNET_MESSAGE,
					false);
		}
	}

	/**
	 * Class for adapter of Custom grid
	 * 
	 */
	private class CustomGridAdapter extends BaseAdapter {
		private Context				mContext;
		private final List<String>	categories;
		private final List<Integer>	imgIds;

		public CustomGridAdapter(Context c, List<String> categories, List<Integer> imgIds) {
			mContext = c;
			this.imgIds = imgIds;
			this.categories = categories;
		}

		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View grid;
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				grid = new View(mContext);
				grid = inflater.inflate(R.layout.category_view, null);
				TextView textView = (TextView) grid.findViewById(R.id.txt_category);
				ImageView imageView = (ImageView) grid.findViewById(R.id.img_category);
				textView.setText(categories.get(position));
				imageView.setImageResource(imgIds.get(position));
			} else {
				grid = (View) convertView;
			}
			return grid;
		}
	}

	public class GetHotelAsyncTask extends AsyncTask<Integer, Void, HotelDetails> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setIndeterminate(true);
			dialog.setMessage(Constants.PLEASE_WAIT);
			dialog.setCancelable(false);
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
				Intent intent = new Intent(HomeActivity.this, HotelDetailsActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(HomeActivity.this, "No Result", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class SearchHotelsAsyncTask extends AsyncTask<SearchHotelDTO, Void, List<HotelDetails>> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HomeActivity.this);
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
				Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(HomeActivity.this, "No Result", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
