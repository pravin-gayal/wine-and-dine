package com.quandisa.nintyml.network;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class InsertReviewResponseHandler implements ResponseHandler<Boolean> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http .HttpResponse)
	 */
	@Override
	public Boolean handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		boolean isInserted = false;

		BufferedInputStream stream = new BufferedInputStream(httpResponse.getEntity().getContent());
		byte[] text = new byte[(int) httpResponse.getEntity().getContentLength()];

		stream.read(text);
		String resp = new String(text);
		stream.close();

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				JSONObject jsonObject = new JSONObject(resp);

				if (jsonObject.has("ID")) {
					isInserted = true;
				}
			}
			catch (JSONException e) {
				// Log.e(TAG, "Error occured", e);
				e.printStackTrace();
			}
		}

		httpResponse.getEntity().consumeContent();
		return isInserted;
	}
}
