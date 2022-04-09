package com.quandisa.nintyml.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UIHelper {

	/**
	 * 
	 */
	public static void disableListViewScroll(ListView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);
		// print height of adapter on log
	}

	public static void showAlertDialog(final Context context, String title, String message, final boolean isLogin) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);

		TextView msg = new TextView(context);
		msg.setText(message);
		msg.setPadding(10, 10, 10, 10);
		msg.setGravity(Gravity.CENTER);
		msg.setTextSize(18);
		builder.setView(msg);

		// builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (isLogin) {
					((Activity) context).finish();
				}
			}
		});
		AlertDialog dialog = builder.create();
		// TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
		// messageText.setGravity(Gravity.CENTER);

		dialog.show();
	}
}
