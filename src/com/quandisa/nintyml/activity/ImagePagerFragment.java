package com.quandisa.nintyml.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quandisa.nintyml.R;
import com.quandisa.nintyml.app.data.ApplicationData;
import com.quandisa.nintyml.app.data.Constants;

public class ImagePagerFragment extends Fragment {

	public static final int	INDEX	= 2;

	DisplayImageOptions		options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_launcher).resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		pager.setAdapter(new ImageAdapter(getArguments().getStringArray(Constants.SELECTED_GALLERY)));

		pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
		return rootView;
	}

	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater	inflater;
		private String[]		imageUrls;

		public ImageAdapter(String[] images) {
			imageUrls = images;
			inflater = LayoutInflater.from(ApplicationData.getSimpleImageActivityContext());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			ImageLoader.getInstance().displayImage(imageUrls[position], imageView, options,
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
							Toast.makeText(ApplicationData.getSimpleImageActivityContext(), message, Toast.LENGTH_SHORT)
									.show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}