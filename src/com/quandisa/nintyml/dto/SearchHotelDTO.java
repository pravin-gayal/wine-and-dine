/**
 * 
 */
package com.quandisa.nintyml.dto;

/**
 * @author pravin.gayal
 *
 */
public class SearchHotelDTO {

	private Double latitude;
	private Double longitude;
	private String Name;
	private String Category;
	
	public SearchHotelDTO() {
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return Category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		Category = category;
	}
	
	
	
	
}
