package com.quandisa.nintyml.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.quandisa.nintyml.app.data.ApplicationData;
import com.quandisa.nintyml.app.data.Constants;

public class SimpleImageActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		ApplicationData.setSimpleImageActivityContext(SimpleImageActivity.this);

		int frIndex = getIntent().getIntExtra(Constants.FRAGMENT_INDEX, 0);
		Fragment fr;
		String tag;
		switch (frIndex) {
			default:
			case ImagePagerFragment.INDEX:
				tag = ImagePagerFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImagePagerFragment();
					fr.setArguments(getIntent().getExtras());
				}
				break;
		}

		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}