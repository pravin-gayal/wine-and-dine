package com.quandisa.nintyml.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quandisa.nintyml.dto.HotelDetails;

public class SearchHotelResponseHandler implements ResponseHandler<List<HotelDetails>> {

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

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				JSONArray hotelList = new JSONArray(resp);
				if (hotelList != null) {
					resultList = new ArrayList<HotelDetails>();
					for (int i = 0; i < hotelList.length(); i++) {
						HotelDetails hotelDetails = new HotelDetails();

						JSONObject jsonObject = hotelList.getJSONObject(i);

						if (jsonObject.has("ID")) {
							hotelDetails.setHotelId(jsonObject.getInt("ID"));
						}
						if (jsonObject.has("rating")) {
							hotelDetails.setRating((float) jsonObject.getDouble("rating"));
						}
						if (jsonObject.has("imgUrl")) {
							hotelDetails.setImgUrl(WebserviceHelper.serverUrl + jsonObject.getString("imgUrl"));
						}
						if (jsonObject.has("Name")) {
							hotelDetails.setName(jsonObject.getString("Name"));
						}
						if (jsonObject.has("OpeningTime")) {
							hotelDetails.setOpeningTime(jsonObject.getString("OpeningTime"));
						}
						if (jsonObject.has("ClosingTime")) {
							hotelDetails.setClosingTime(jsonObject.getString("ClosingTime"));
						}

						resultList.add(hotelDetails);
					}
				}
			}
			catch (JSONException e) {
				// Log.e(TAG, "Error occured", e);
				e.printStackTrace();
			}
		}

		httpResponse.getEntity().consumeContent();
		return resultList;
	}
}
