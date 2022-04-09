package com.quandisa.nintyml.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quandisa.nintyml.R;
import com.quandisa.nintyml.app.data.ApplicationData;
import com.quandisa.nintyml.app.data.Constants;
import com.quandisa.nintyml.dto.HotelDetails;
import com.quandisa.nintyml.dto.ReviewDetailsDTO;
import com.quandisa.nintyml.network.NetworkManager;
import com.quandisa.nintyml.network.WebserviceHelper;
import com.quandisa.nintyml.util.GPSTracker;

public class HotelDetailsActivity extends Activity {

	DisplayImageOptions	options;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (NetworkManager.isNetworkAvailable(this)) {
			setContentView(R.layout.hotel_details_screen);

			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
					.build();

			// /Set onclickListner for post button.
			setPostReviewOnClickListner();
			setMapOnTouchEventListener();

			if (ApplicationData.getHotelDetails() != null) {
				setHotelDetails(ApplicationData.getHotelDetails());
				setReviews(ApplicationData.getHotelReviewsList());
				setGoogleMapHotelLocation(ApplicationData.getHotelDetails());
			} else {

			}

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

	private void setMapOnTouchEventListener() {
		final ScrollView mainScrollView = (ScrollView) findViewById(R.id.details_scroll_view);
		ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						// Disallow ScrollView to intercept touch events.
						mainScrollView.requestDisallowInterceptTouchEvent(true);
						// Disable touch on transparent view
						return false;

					case MotionEvent.ACTION_UP:
						// Allow ScrollView to intercept touch events.
						mainScrollView.requestDisallowInterceptTouchEvent(false);
						return true;

					case MotionEvent.ACTION_MOVE:
						mainScrollView.requestDisallowInterceptTouchEvent(true);
						return false;

					default:
						return true;
				}
			}
		});

	}

	private void setHotelDetails(HotelDetails hotelDetails) {
		ImageView logoImage = (ImageView) findViewById(R.id.hotel_logo);
		ImageLoader.getInstance().displayImage(hotelDetails.getImgUrl(), logoImage, options);

		TextView hotelName = (TextView) findViewById(R.id.hotel_name);
		hotelName.setText(hotelDetails.getName());

		TextView description = (TextView) findViewById(R.id.txt_hotel_description);
		description.setText(hotelDetails.getDescription());

		TextView hotelTiming = (TextView) findViewById(R.id.hotel_timing);
		if (hotelDetails.getOpeningTime() != null && hotelDetails.getClosingTime() != null) {
			hotelTiming.setText(hotelDetails.getOpeningTime().substring(0, 5) + " to "
					+ hotelDetails.getClosingTime().substring(0, 5));
		}

		RatingBar hotelRating = (RatingBar) findViewById(R.id.ratingBar);
		LayerDrawable stars = (LayerDrawable) hotelRating.getProgressDrawable();
		// stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP); // Star background
		// Star half filled star background
		// stars.getDrawable(android.R.id.progress).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
		stars.getDrawable(2).setColorFilter(Constants.RATINGBAR_BACKGROUND_COLOR, PorterDuff.Mode.SRC_ATOP); // Star
		// progress
		// background

		if (hotelDetails.getRating() != null) {
			hotelRating.setRating(hotelDetails.getRating());
		}

		// Set contact details
		setContactDetails(hotelDetails);

		// Set gallary images.
		setHotelGallery(hotelDetails.getPhotoUrlList());
		setMenuGallery(hotelDetails.getMenuUrlList());

	}

	private void setHotelGallery(final List<String> list) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.photo_gallary_layout);
		if (list != null && !list.isEmpty()) {
			Gallery gallery = (Gallery) findViewById(R.id.hotel_gallery);
			gallery.setAdapter(new GalleryImageAdapter(this, list));
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					startImagePagerActivity(position, list);
				}
			});

			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}
	}

	private void setMenuGallery(final List<String> list) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.menu_gallary_layout);
		if (list != null && !list.isEmpty()) {
			Gallery gallery = (Gallery) findViewById(R.id.menu_gallery);
			gallery.setAdapter(new GalleryImageAdapter(this, list));
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					startImagePagerActivity(position, list);
				}
			});

			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}

	}

	private void setContactDetails(HotelDetails hotelDetails) {
		boolean isPresent = false;

		LinearLayout layout = (LinearLayout) findViewById(R.id.contact_details_layout);

		TextView contact1 = (TextView) findViewById(R.id.txt_contact1);
		TextView contact2 = (TextView) findViewById(R.id.txt_contact2);
		TextView address = (TextView) findViewById(R.id.txt_hotel_address);

		if (hotelDetails.getContactNumber1() != null) {
			contact1.setText(hotelDetails.getContactNumber1());
			contact1.setVisibility(View.VISIBLE);
			findViewById(R.id.icon_contact1).setVisibility(View.VISIBLE);

			isPresent = true;
		}
		if (hotelDetails.getContactNumber2() != null) {
			contact2.setText(hotelDetails.getContactNumber2());
			contact2.setVisibility(View.VISIBLE);
			findViewById(R.id.icon_contact2).setVisibility(View.VISIBLE);

			isPresent = true;
		}

		StringBuilder sb = new StringBuilder();
		if (hotelDetails.getStreet() != null) {
			sb.append("<b>" + hotelDetails.getStreet() + "</b> > ");
		} else if (hotelDetails.getArea() != null) {
			sb.append("<b>" + hotelDetails.getArea() + "</b> > ");
		}

		if (hotelDetails.getArea() != null) {
			sb.append("" + hotelDetails.getArea());
		}
		if (hotelDetails.getStreet() != null) {
			sb.append(", " + hotelDetails.getStreet());
		}
		if (hotelDetails.getTown() != null) {
			sb.append(", " + hotelDetails.getTown());
		}
		if (hotelDetails.getCity() != null) {
			sb.append(", " + hotelDetails.getCity());
		}

		if (!sb.toString().isEmpty()) {
			address.setText(Html.fromHtml(sb.toString()));
			address.setVisibility(View.VISIBLE);
			isPresent = true;
		}

		if (isPresent) {
			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}
	}

	private void startImagePagerActivity(int position, List<String> list) {
		String[] imageUrls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			imageUrls[i] = list.get(i);
		}

		Intent intent = new Intent(HotelDetailsActivity.this, SimpleImageActivity.class);
		intent.putExtra(Constants.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
		intent.putExtra(Constants.FRAGMENT_INDEX, ImagePagerFragment.INDEX);

		intent.putExtra(Constants.IMAGE_POSITION, position);
		intent.putExtra(Constants.SELECTED_GALLERY, imageUrls);
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private class GalleryImageAdapter extends BaseAdapter {
		private Context	mContext;
		List<String>	imageUrls;

		public GalleryImageAdapter(Context context, List<String> imageUrlList) {
			mContext = context;
			imageUrls = imageUrlList;
		}

		public int getCount() {
			return imageUrls.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int index, View view, ViewGroup viewGroup) {
			ImageView imageView = new ImageView(mContext);

			// imageView.setImageResource(mImageIds[index]);
			imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));

			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			ImageLoader.getInstance().displayImage(imageUrls.get(index), imageView, options,
					new SimpleImageLoadingListener() {
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
						}

					});

			return imageView;
		}
	}

	private void setGoogleMapHotelLocation(HotelDetails hotelDetails) {

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.map_layout);

		if (hotelDetails.getLatitude() != null && hotelDetails.getLongitude() != null) {

			GPSTracker gps = new GPSTracker(this);
			GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if GPS location can get
			if (gps.canGetLocation()) {
				Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
				double userlatitude = gps.getLatitude();
				double userlongitude = gps.getLongitude();

				LatLng yourlocation = new LatLng(userlatitude, userlongitude);
				Marker you = map.addMarker(new MarkerOptions().position(yourlocation).title("You")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red)));

			}

			LatLng hotellocation = new LatLng(hotelDetails.getLatitude(), hotelDetails.getLongitude());
			Marker hotel = map.addMarker(new MarkerOptions().position(hotellocation).title(hotelDetails.getName())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotellocation, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}
	}

	private void setReviews(List<ReviewDetailsDTO> list) {
		LinearLayout displayReviewLayout = (LinearLayout) findViewById(R.id.display_review_layout);
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int i = 0;
		for (ReviewDetailsDTO reviewDetailsDTO : list) {
			View view = vi.inflate(R.layout.display_review_layout, null);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(3, 3, 3, 3);
			view.setLayoutParams(params);

			TextView txtTitle = (TextView) view.findViewById(R.id.review_title);
			if (reviewDetailsDTO.getTitle() != null) {
				txtTitle.setText(reviewDetailsDTO.getTitle());
			}

			TextView txtComment = (TextView) view.findViewById(R.id.txt_review);
			if (reviewDetailsDTO.getComment() != null) {
				txtComment.setText(reviewDetailsDTO.getComment());
			}

			TextView txtTiming = (TextView) view.findViewById(R.id.review_timing);
			if (reviewDetailsDTO.getReviewTiming() != null) {
				txtTiming.setText(reviewDetailsDTO.getReviewTiming());
			}

			RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
			LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
			// stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP); // Star background
			// Star half filled star background
			// stars.getDrawable(android.R.id.progress).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(2).setColorFilter(Constants.RATINGBAR_BACKGROUND_COLOR, PorterDuff.Mode.SRC_ATOP); // Star
			// progress
			// background

			if (reviewDetailsDTO.getRating() != null) {
				ratingBar.setRating(reviewDetailsDTO.getRating());
			}

			if (i == (list.size() - 1)) {
				view.findViewById(R.id.btn_load_more_review).setVisibility(View.VISIBLE);
			}

			// insert into main view
			((ViewGroup) displayReviewLayout).addView(view, i++);
		}
	}

	private void setPostReviewOnClickListner() {
		final EditText edtTitle = (EditText) findViewById(R.id.edt_review_title);
		final EditText edtComment = (EditText) findViewById(R.id.edt_review_comment);
		final Button btnPost = (Button) findViewById(R.id.btn_post_review);

		final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingbar_review);
		LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
		// stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP); // Star background
		// Star half filled star background
		// stars.getDrawable(android.R.id.progress).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
		stars.getDrawable(2).setColorFilter(Constants.RATINGBAR_BACKGROUND_COLOR, PorterDuff.Mode.SRC_ATOP); // Star
																												// progress
		// background

		btnPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = edtTitle.getText().toString();
				String comment = edtComment.getText().toString();
				Float rating = ratingBar.getRating();

				if (NetworkManager.isNetworkAvailable(HotelDetailsActivity.this)) {
					if (title != null && !title.isEmpty()) {
						if (comment != null && !comment.isEmpty()) {
							if (rating != null) {
								// Call insert review service.
								ReviewDetailsDTO reviewDetailsDTO = new ReviewDetailsDTO();
								reviewDetailsDTO.setHotelId(ApplicationData.getHotelDetails().getHotelId());
								reviewDetailsDTO.setTitle(title);
								reviewDetailsDTO.setComment(comment);
								reviewDetailsDTO.setRating(rating);

								new InsertReviewAsyncTask().execute(reviewDetailsDTO);

								ratingBar.setRating(0);
								edtTitle.setText("");
								edtComment.setText("");

							} else {
								Toast.makeText(HotelDetailsActivity.this, "Please add Rating", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(HotelDetailsActivity.this, "Please add Comment", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(HotelDetailsActivity.this, "Please add Title", Toast.LENGTH_SHORT).show();
					}
				} else {
					UIHelper.showAlertDialog(HotelDetailsActivity.this, Constants.NO_INTERNET_TITLE,
							Constants.NO_INTERNET_MESSAGE, false);
				}

			}
		});
	}

	private class InsertReviewAsyncTask extends AsyncTask<ReviewDetailsDTO, Void, Boolean> {
		ProgressDialog	dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HotelDetailsActivity.this);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setMessage(Constants.PLEASE_WAIT);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(ReviewDetailsDTO... reviewDetailsDTO) {
			Boolean isInserted = null;
			try {
				isInserted = WebserviceHelper.insertReview(reviewDetailsDTO[0]);

				if (isInserted) {
					List<ReviewDetailsDTO> hotelReviewList = WebserviceHelper.getHotelReviews(reviewDetailsDTO[0]
							.getHotelId());
					ApplicationData.setHotelReviewsList(hotelReviewList);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return isInserted;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (result) {
				Toast.makeText(HotelDetailsActivity.this, "Review Added", Toast.LENGTH_SHORT).show();
				setReviews(ApplicationData.getHotelReviewsList());
			} else {
				Toast.makeText(HotelDetailsActivity.this, "Error occured during review insertion", Toast.LENGTH_SHORT)
						.show();
			}
			super.onPostExecute(result);
		}
	}

}
