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

import com.quandisa.nintyml.dto.ReviewDetailsDTO;

public class GetHotelReviewsResponseHandler implements ResponseHandler<List<ReviewDetailsDTO>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http .HttpResponse)
	 */
	@Override
	public List<ReviewDetailsDTO> handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		List<ReviewDetailsDTO> resultList = null;

		BufferedInputStream stream = new BufferedInputStream(httpResponse.getEntity().getContent());
		byte[] text = new byte[(int) httpResponse.getEntity().getContentLength()];

		stream.read(text);
		String resp = new String(text);
		stream.close();

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				JSONArray hotelList = new JSONArray(resp);
				if (hotelList != null) {
					resultList = new ArrayList<ReviewDetailsDTO>();
					for (int i = 0; i < hotelList.length(); i++) {
						ReviewDetailsDTO reviewDetailsDTO = new ReviewDetailsDTO();

						JSONObject jsonObject = hotelList.getJSONObject(i);

						if (jsonObject.has("ID")) {
							reviewDetailsDTO.setReviewId(jsonObject.getInt("ID"));
						}
						if (jsonObject.has("score")) {
							reviewDetailsDTO.setRating((float) jsonObject.getDouble("score"));
						}
						if (jsonObject.has("title")) {
							reviewDetailsDTO.setTitle(jsonObject.getString("title"));
						}
						if (jsonObject.has("description")) {
							reviewDetailsDTO.setComment(jsonObject.getString("description"));
						}
						if (jsonObject.has("hotel_id")) {
							reviewDetailsDTO.setHotelId(jsonObject.getInt("hotel_id"));
						}
						if (jsonObject.has("insertDate")) {
							reviewDetailsDTO.setReviewTiming(jsonObject.getString("insertDate"));
						}

						resultList.add(reviewDetailsDTO);
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
