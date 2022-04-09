package com.quandisa.nintyml.app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.quandisa.nintyml.R;
import com.quandisa.nintyml.dto.HotelDetails;
import com.quandisa.nintyml.dto.LocationDTO;
import com.quandisa.nintyml.dto.ReviewDetailsDTO;

public class ApplicationData {

	public static List<HotelDetails>		mostPopularHotels;
	public static List<HotelDetails>		searchHotelResult;
	public static HotelDetails				hotelDetails;
	public static List<ReviewDetailsDTO>	hotelReviewsList;
	public static List<String>				categories;
	public static List<Integer>				categoryImages;
	public static Map<String, LocationDTO>	locationMap	= new HashMap<String, LocationDTO>();
	public static List<String>				areaList;
	public static Context					simpleImageActivityContext;

	/**
	 * @return the searchHotelResult
	 */
	public static List<HotelDetails> getSearchHotelResult() {
		return searchHotelResult;
	}

	/**
	 * @param searchHotelResult
	 *            the searchHotelResult to set
	 */
	public static void setSearchHotelResult(List<HotelDetails> searchHotelResult) {
		ApplicationData.searchHotelResult = searchHotelResult;
	}

	/**
	 * @return the hotelDetails
	 */
	public static HotelDetails getHotelDetails() {
		return hotelDetails;
	}

	/**
	 * @param hotelDetails
	 *            the hotelDetails to set
	 */
	public static void setHotelDetails(HotelDetails hotelDetails) {
		ApplicationData.hotelDetails = hotelDetails;
	}

	/**
	 * @return the mostPopularHotels
	 */
	public static List<HotelDetails> getMostPopularHotels() {
		return mostPopularHotels;
	}

	/**
	 * @param mostPopularHotels
	 *            the mostPopularHotels to set
	 */
	public static void setMostPopularHotels(List<HotelDetails> mostPopularHotels) {
		ApplicationData.mostPopularHotels = mostPopularHotels;
	}

	/**
	 * @return the categories
	 */
	public static List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public static void setCategories() {
		categories = new ArrayList<String>();
		categories.add("Bar & Restaurant");
		categories.add("Permit Rooms");
		categories.add("Night Clubs");
		categories.add("Highway Joints");
		categories.add("Night Joints");
		categories.add("Wine & Beer Shops");

	}

	/**
	 * @return the categoryImages
	 */
	public static List<Integer> getCategoryImages() {
		return categoryImages;
	}

	/**
	 * @param categoryImages
	 *            the categoryImages to set
	 */
	public static void setCategoryImages() {
		categoryImages = new ArrayList<Integer>();
		categoryImages.add(R.drawable.cat1_barandrestaurant);
		categoryImages.add(R.drawable.cat2_permitrooms);
		categoryImages.add(R.drawable.cat3_nightclubs);
		categoryImages.add(R.drawable.cat4_highwayjoints);
		categoryImages.add(R.drawable.cat5_nightjoints);
		categoryImages.add(R.drawable.cat6_wineshoppee);
	}

	/**
	 * @return the hotelReviewsList
	 */
	public static List<ReviewDetailsDTO> getHotelReviewsList() {
		return hotelReviewsList;
	}

	/**
	 * @param hotelReviewsList
	 *            the hotelReviewsList to set
	 */
	public static void setHotelReviewsList(List<ReviewDetailsDTO> hotelReviewsList) {
		ApplicationData.hotelReviewsList = hotelReviewsList;
	}

	/**
	 * @return the locationMap
	 */
	public static Map<String, LocationDTO> getLocationMap() {
		return locationMap;
	}

	/**
	 * @param locationMap
	 *            the locationMap to set
	 */
	public static void setLocationMap(Map<String, LocationDTO> locationMap) {

		ApplicationData.locationMap = locationMap;
		areaList = new ArrayList<String>();
		areaList.addAll(ApplicationData.locationMap.keySet());

	}

	/**
	 * @return the locationMap
	 */
	public static List<String> getAreaList() {
		return areaList;
	}

	/**
	 * @return the simpleImageActivityContext
	 */
	public static Context getSimpleImageActivityContext() {
		return simpleImageActivityContext;
	}

	/**
	 * @param simpleImageActivityContext
	 *            the simpleImageActivityContext to set
	 */
	public static void setSimpleImageActivityContext(Context simpleImageActivityContext) {
		ApplicationData.simpleImageActivityContext = simpleImageActivityContext;
	}
}
