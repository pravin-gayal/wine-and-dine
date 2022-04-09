package com.quandisa.nintyml.dto;

import java.util.List;

public class HotelDetails {
	private Integer			hotelId;
	private String			name;
	private String			imgUrl;
	private Float			rating;
	private String			openingTime;
	private String			closingTime;

	private Integer			CategoryID;
	private String			CategoryName;
	private String			EmailID;
	private String			ContactNumber1;
	private String			ContactNumber2;
	private List<String>	photoUrlList;
	private List<String>	menuUrlList;
	private String			websiteUrl;
	private String			street;
	private String			area;
	private String			town;
	private String			city;
	private String			state;
	private String			country;
	private Float			latitude;
	private Float			longitude;
	private String			description;

	public HotelDetails() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl
	 *            the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return the rating
	 */
	public Float getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Float rating) {
		this.rating = rating;
	}

	/**
	 * @return the openingTime
	 */
	public String getOpeningTime() {
		return openingTime;
	}

	/**
	 * @param openingTime
	 *            the openingTime to set
	 */
	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	/**
	 * @return the closingTime
	 */
	public String getClosingTime() {
		return closingTime;
	}

	/**
	 * @param closingTime
	 *            the closingTime to set
	 */
	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}

	/**
	 * @return the hotelId
	 */
	public Integer getHotelId() {
		return hotelId;
	}

	/**
	 * @param hotelId
	 *            the hotelId to set
	 */
	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	/**
	 * @return the categoryID
	 */
	public Integer getCategoryID() {
		return CategoryID;
	}

	/**
	 * @param categoryID
	 *            the categoryID to set
	 */
	public void setCategoryID(Integer categoryID) {
		CategoryID = categoryID;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return CategoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return EmailID;
	}

	/**
	 * @param emailID
	 *            the emailID to set
	 */
	public void setEmailID(String emailID) {
		EmailID = emailID;
	}

	/**
	 * @return the contactNumber1
	 */
	public String getContactNumber1() {
		return ContactNumber1;
	}

	/**
	 * @param contactNumber1
	 *            the contactNumber1 to set
	 */
	public void setContactNumber1(String contactNumber1) {
		ContactNumber1 = contactNumber1;
	}

	/**
	 * @return the contactNumber2
	 */
	public String getContactNumber2() {
		return ContactNumber2;
	}

	/**
	 * @param contactNumber2
	 *            the contactNumber2 to set
	 */
	public void setContactNumber2(String contactNumber2) {
		ContactNumber2 = contactNumber2;
	}

	/**
	 * @return the photoUrlList
	 */
	public List<String> getPhotoUrlList() {
		return photoUrlList;
	}

	/**
	 * @param photoUrlList
	 *            the photoUrlList to set
	 */
	public void setPhotoUrlList(List<String> photoUrlList) {
		this.photoUrlList = photoUrlList;
	}

	/**
	 * @return the menuUrlList
	 */
	public List<String> getMenuUrlList() {
		return menuUrlList;
	}

	/**
	 * @param menuUrlList
	 *            the menuUrlList to set
	 */
	public void setMenuUrlList(List<String> menuUrlList) {
		this.menuUrlList = menuUrlList;
	}

	/**
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	/**
	 * @param websiteUrl
	 *            the websiteUrl to set
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the latitude
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
