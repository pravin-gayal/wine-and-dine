package com.quandisa.nintyml.app.data;

import android.graphics.Color;

public final class Constants {

	public static final String[]	HOTEL_IMAGES	= new String[] {
			"https://d2z9qv80fklwtv.cloudfront.net/data/pictures/6/10646/c02b1de097a86f6c8dac33c9db197eaf_200_thumb.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/data/pictures/6/10646/711e33c90fb96c28977670c640a29ea5_200_thumb.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/data/reviews_photos/49a/fe24bed6b48872d3f0775b013268349a_1406790367_200_thumb.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/data/reviews_photos/77e/124faf436b3ed1dc2aa916d4880fe77e_1403438071_200_thumb.jpg" };

	public static final String[]	MENU_IMAGES		= new String[] {
			"https://d2z9qv80fklwtv.cloudfront.net/menus/646/10646/1aa8220b4eb66dd16af87cbe11a8f68d.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/menus/646/10646/9c670dffc2aff6a695215b1b069a0f92.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/menus/646/10646/fc02d01706c7fd3a4d8e1e04168e789c.jpg",
			"https://d2z9qv80fklwtv.cloudfront.net/menus/646/10646/be6ded2245c8e5f82c4925509cf2606c.jpg" };

	private Constants() {
	}

	public static class Config {
		public static final boolean	DEVELOPER_MODE	= false;
	}

	public static class Extra {
		public static final String	FRAGMENT_INDEX	= "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String	IMAGE_POSITION	= "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public static final String	FRAGMENT_INDEX				= "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
	public static final String	IMAGE_POSITION				= "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	public static final String	SELECTED_GALLERY			= "com.nostra13.example.universalimageloader.GALLERY_INDEX";
	public static final String	PLEASE_WAIT					= "Please wait...";
	public static final String	NO_INTERNET_MESSAGE			= "No Intenet Connection\nPlease try again later";
	public static final String	NO_INTERNET_TITLE			= "No Connection...";

	// Rating start color code

	public static final Integer	RATING_COLOR_RED			= 210;
	public static final Integer	RATING_COLOR_GREEN			= 63;
	public static final Integer	RATING_COLOR_BLUE			= 0;
	public static final Integer	RATINGBAR_BACKGROUND_COLOR	= Color.argb(191, Constants.RATING_COLOR_RED,
																	Constants.RATING_COLOR_GREEN,
																	Constants.RATING_COLOR_BLUE);

}
