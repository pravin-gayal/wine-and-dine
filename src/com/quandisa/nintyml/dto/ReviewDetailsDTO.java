package com.quandisa.nintyml.dto;

public class ReviewDetailsDTO {
	private Integer	reviewId;
	private Integer	hotelId;
	private String	title;
	private String	comment;
	private Float	rating;
	private String	reviewTiming;

	public ReviewDetailsDTO() {
		super();
	}

	/**
	 * @return the reviewId
	 */
	public Integer getReviewId() {
		return reviewId;
	}

	/**
	 * @param reviewId
	 *            the reviewId to set
	 */
	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
	 * @return the reviewTiming
	 */
	public String getReviewTiming() {
		return reviewTiming;
	}

	/**
	 * @param reviewTiming
	 *            the reviewTiming to set
	 */
	public void setReviewTiming(String reviewTiming) {
		this.reviewTiming = reviewTiming;
	}

}
