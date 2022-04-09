package com.quandisa.nintyml.network;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONStringer;

import com.quandisa.nintyml.dto.HotelDetails;
import com.quandisa.nintyml.dto.LocationDTO;
import com.quandisa.nintyml.dto.ReviewDetailsDTO;
import com.quandisa.nintyml.dto.SearchHotelDTO;

/**
 * @author pravin.gayal
 * 
 */
public class WebserviceHelper {
	public static String				serverUrl	= "http://90ml.club/";
	public static String				serviceUrl	= serverUrl + "Service";

	private static final BasicHeader	JSON_HEADER	= new BasicHeader("Content-type", "application/json");

	public static List<HotelDetails> searchHotel(SearchHotelDTO searchHotelDTO) {
		List<HotelDetails> result = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl + "/getHotelsbyCriteria");
		httpPost.addHeader(JSON_HEADER);

		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();
			if (searchHotelDTO.getCategory() != null) {
				stringer.key("Category").value(searchHotelDTO.getCategory());
			}

			if (searchHotelDTO.getName() != null) {
				stringer.key("Name").value(searchHotelDTO.getName());
			}

			if (searchHotelDTO.getLatitude() != null) {
				stringer.key("latitude").value(searchHotelDTO.getLatitude());
			} else {
				stringer.key("latitude").value(-1);
			}

			if (searchHotelDTO.getLongitude() != null) {
				stringer.key("longitude").value(searchHotelDTO.getLongitude());
			} else {
				stringer.key("longitude").value(-1);
			}

			stringer.endObject();

			httpPost.setEntity(new StringEntity(stringer.toString()));
			result = httpClient.execute(httpPost, new SearchHotelResponseHandler());

		}
		catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		catch (JSONException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}

	public static HotelDetails getHotelDetails(Integer hotelId) {
		HotelDetails result = null;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(serviceUrl + "/GetHotelDetails");
			httpPost.addHeader(JSON_HEADER);

			// Post Data
			JSONStringer stringer = new JSONStringer();
			stringer.object();
			stringer.key("ID").value(hotelId);
			stringer.endObject();
			httpPost.setEntity(new StringEntity(stringer.toString()));

			result = httpClient.execute(httpPost, new GetHotelDetailsResponseHandler());
		}
		catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		catch (JSONException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}

	public static List<HotelDetails> getTopNHotels(Integer count) {
		List<HotelDetails> result = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl + "/getTopNHotels");
		httpPost.addHeader(JSON_HEADER);

		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();
			stringer.key("n").value(count);
			stringer.endObject();

			httpPost.setEntity(new StringEntity(stringer.toString()));
			result = httpClient.execute(httpPost, new SearchHotelResponseHandler());

		}
		catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		catch (JSONException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}

	public static boolean insertReview(ReviewDetailsDTO reviewDetailsDTO) {
		Boolean result = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl + "/storeReviews");
		httpPost.addHeader(JSON_HEADER);

		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();
			// Set hotel Id
			stringer.key("ID").value(reviewDetailsDTO.getHotelId());
			stringer.key("title").value(reviewDetailsDTO.getTitle());
			stringer.key("Description").value(reviewDetailsDTO.getComment());
			stringer.key("Score").value(reviewDetailsDTO.getRating());
			stringer.endObject();

			httpPost.setEntity(new StringEntity(stringer.toString()));

			result = httpClient.execute(httpPost, new InsertReviewResponseHandler());

		}
		catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		catch (JSONException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}

	public static List<ReviewDetailsDTO> getHotelReviews(Integer hotelId) {
		List<ReviewDetailsDTO> result = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl + "/getReviews");
		httpPost.addHeader(JSON_HEADER);

		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();
			stringer.key("ID").value(hotelId);
			stringer.endObject();

			httpPost.setEntity(new StringEntity(stringer.toString()));

			// result = new ArrayList<ReviewDetailsDTO>();
			// for (int i = 5; i >= 1; --i) {
			// ReviewDetailsDTO reviewDetailsDTO = new ReviewDetailsDTO();
			// reviewDetailsDTO.setTitle("Title - " + i);
			// reviewDetailsDTO.setComment("Comment - " + i);
			// reviewDetailsDTO.setRating((float) (i));
			// reviewDetailsDTO.setReviewId(i);
			// reviewDetailsDTO.setHotelId(hotelId);
			// reviewDetailsDTO.setReviewTiming("Nov " + i + " 2014");
			// result.add(reviewDetailsDTO);
			// }

			result = httpClient.execute(httpPost, new GetHotelReviewsResponseHandler());

		}
		catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		catch (JSONException e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}

	public static Map<String, LocationDTO> getLocations() {
		Map<String, LocationDTO> result = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(serviceUrl + "/getAreas");
		httpGet.addHeader(JSON_HEADER);

		try {
			result = httpClient.execute(httpGet, new GetLocationsResponseHandler());
		}
		catch (Exception e) {
			e.printStackTrace();
			// Log.e(TAG, "Error occured", e);
		}
		return result;
	}
}
