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

public class GetHotelDetailsResponseHandler implements ResponseHandler<HotelDetails> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http .HttpResponse)
	 */
	@Override
	public HotelDetails handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		HotelDetails hotelDetails = null;

		BufferedInputStream stream = new BufferedInputStream(httpResponse.getEntity().getContent());
		byte[] text = new byte[(int) httpResponse.getEntity().getContentLength()];

		stream.read(text);
		String resp = new String(text);
		stream.close();

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {

				JSONArray jsonArray = new JSONArray(resp);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				hotelDetails = new HotelDetails();

				if (jsonObject.has("ID")) {
					hotelDetails.setHotelId(jsonObject.getInt("ID"));
				}
				if (jsonObject.has("Name")) {
					hotelDetails.setName(jsonObject.getString("Name"));
				}
				if (jsonObject.has("Rating")) {
					hotelDetails.setRating((float) jsonObject.getDouble("Rating"));
				}

				if (jsonObject.has("CategoryID")) {
					hotelDetails.setCategoryID(jsonObject.getInt("CategoryID"));
				}
				if (jsonObject.has("CategoryName")) {
					hotelDetails.setCategoryName(jsonObject.getString("CategoryName"));
				}
				if (jsonObject.has("Logo")) {
					hotelDetails.setImgUrl(WebserviceHelper.serverUrl + jsonObject.getString("Logo"));
				}
				if (jsonObject.has("OpeningTime")) {
					hotelDetails.setOpeningTime(jsonObject.getString("OpeningTime"));
				}
				if (jsonObject.has("ClosingTime")) {
					hotelDetails.setClosingTime(jsonObject.getString("ClosingTime"));
				}
				if (jsonObject.has("EmailID")) {
					hotelDetails.setEmailID(jsonObject.getString("EmailID"));
				}
				if (jsonObject.has("ContactNumber1")) {
					hotelDetails.setContactNumber1(jsonObject.getString("ContactNumber1"));
				}
				if (jsonObject.has("ContactNumber2")) {
					hotelDetails.setContactNumber2(jsonObject.getString("ContactNumber2"));
				}
				if (jsonObject.has("websiteUrl")) {
					hotelDetails.setWebsiteUrl(jsonObject.getString("websiteUrl"));
				}
				if (jsonObject.has("street")) {
					hotelDetails.setStreet(jsonObject.getString("street"));
				}
				if (jsonObject.has("area")) {
					hotelDetails.setArea(jsonObject.getString("area"));
				}
				if (jsonObject.has("town")) {
					hotelDetails.setTown(jsonObject.getString("town"));
				}
				if (jsonObject.has("city")) {
					hotelDetails.setCity(jsonObject.getString("city"));
				}
				if (jsonObject.has("state")) {
					hotelDetails.setState(jsonObject.getString("state"));
				}
				if (jsonObject.has("country")) {
					hotelDetails.setCountry(jsonObject.getString("country"));
				}
				if (jsonObject.has("description")) {
					hotelDetails.setDescription(jsonObject.getString("description"));
				}
				if (jsonObject.has("latitude")) {
					hotelDetails.setLatitude((float) jsonObject.getDouble("latitude"));
				}
				if (jsonObject.has("longitude")) {
					hotelDetails.setLongitude((float) jsonObject.getDouble("longitude"));
				}

				if (jsonObject.has("PhotosURL")) {
					List<String> list = new ArrayList<String>();
					JSONArray photoArray = jsonObject.getJSONArray("PhotosURL");
					for (int i = 0; i < photoArray.length(); i++) {
						JSONObject obj = photoArray.getJSONObject(i);
						list.add(WebserviceHelper.serverUrl + obj.getString("hotel_image_path"));
					}
					hotelDetails.setPhotoUrlList(list);
				}

				if (jsonObject.has("MenuURl")) {
					List<String> list = new ArrayList<String>();
					JSONArray photoArray = jsonObject.getJSONArray("MenuURl");
					for (int i = 0; i < photoArray.length(); i++) {
						JSONObject obj = photoArray.getJSONObject(i);
						list.add(WebserviceHelper.serverUrl + obj.getString("menu_image_path"));
					}
					hotelDetails.setMenuUrlList(list);
				}

			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}

		httpResponse.getEntity().consumeContent();
		return hotelDetails;
	}
}
