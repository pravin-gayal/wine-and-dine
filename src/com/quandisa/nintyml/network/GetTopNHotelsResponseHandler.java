package com.quandisa.nintyml.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.quandisa.nintyml.dto.HotelDetails;

public class GetTopNHotelsResponseHandler implements ResponseHandler<List<HotelDetails>> {
	private JSONTokener	mTokener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http .HttpResponse)
	 */
	@Override
	public List<HotelDetails> handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		List<HotelDetails> resultList = null;

		BufferedInputStream stream = new BufferedInputStream(httpResponse.getEntity().getContent());
		byte[] text = new byte[(int) httpResponse.getEntity().getContentLength()];

		stream.read(text);
		String resp = new String(text);
		stream.close();

		mTokener = new JSONTokener(resp);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				JSONObject obj = (JSONObject) mTokener.nextValue();
				resultList = new ArrayList<HotelDetails>();

				// TODO set hotel list
				if (obj.getString("success").equals("true")) {
					httpResponse.getEntity().consumeContent();
					return resultList;
				}
			}
			catch (JSONException e) {
				// Log.e(TAG, "Error occured", e);
			}
		}

		httpResponse.getEntity().consumeContent();
		return resultList;
	}
}
