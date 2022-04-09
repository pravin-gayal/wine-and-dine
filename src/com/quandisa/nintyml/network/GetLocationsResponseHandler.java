package com.quandisa.nintyml.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quandisa.nintyml.dto.LocationDTO;

public class GetLocationsResponseHandler implements ResponseHandler<Map<String, LocationDTO>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http .HttpResponse)
	 */
	@Override
	public Map<String, LocationDTO> handleResponse(HttpResponse httpResponse) throws ClientProtocolException,
			IOException {
		Map<String, LocationDTO> resultMap = new HashMap<String, LocationDTO>();

		BufferedInputStream stream = new BufferedInputStream(httpResponse.getEntity().getContent());
		byte[] text = new byte[(int) httpResponse.getEntity().getContentLength()];

		stream.read(text);
		String resp = new String(text);
		stream.close();

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				JSONArray hotelList = new JSONArray(resp);
				if (hotelList != null) {

					for (int i = 0; i < hotelList.length(); i++) {
						LocationDTO locationDTO = new LocationDTO();

						JSONObject jsonObject = hotelList.getJSONObject(i);

						if (jsonObject.has("ID")) {
							locationDTO.setID(jsonObject.getInt("ID"));
						}
						if (jsonObject.has("Latitude")) {
							locationDTO.setLatitude(jsonObject.getDouble("Latitude"));
						}
						if (jsonObject.has("Longitude")) {
							locationDTO.setLongitude(jsonObject.getDouble("Longitude"));
						}
						if (jsonObject.has("Area1")) {
							locationDTO.setArea(jsonObject.getString("Area1"));
						}
						resultMap.put(locationDTO.getArea(), locationDTO);
					}
				}
			}
			catch (JSONException e) {
				// Log.e(TAG, "Error occured", e);
				e.printStackTrace();
			}
		}

		httpResponse.getEntity().consumeContent();
		return resultMap;
	}
}
