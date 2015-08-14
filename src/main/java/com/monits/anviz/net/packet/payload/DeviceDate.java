package com.monits.anviz.net.packet.payload;

import com.monits.jpack.annotation.Encode;
import com.monits.jpack.annotation.Unsigned;

/**
 * Device Date Model
 * The date format we need is: yy, MM or M, dd or d, h or hh, mm, ss (1 value per byte)
 */
public class DeviceDate {

	@Encode(0)
	@Unsigned
	private short year;

	@Encode(1)
	@Unsigned
	private short month;

	@Encode(2)
	@Unsigned
	private short day;

	@Encode(3)
	@Unsigned
	private short hour;

	@Encode(4)
	@Unsigned
	private short minute;

	@Encode(5)
	@Unsigned
	private short second;

	/**
	 * Get the year
	 * @return year short
	 */
	public short getYear() {
		return year;
	}

	/**
	 * Set the year
	 * @param year short
	 */
	public void setYear(final short year) {
		this.year = year;
	}

	/**
	 * Get the month
	 * @return month short
	 */
	public short getMonth() {
		return month;
	}

	/**
	 * Set month
	 * @param month short
	 */
	public void setMonth(final short month) {
		this.month = month;
	}

	/**
	 * Get the day
	 * @return day short
	 */
	public short getDay() {
		return day;
	}

	/**
	 * Set the day
	 * @param day short
	 */
	public void setDay(final short day) {
		this.day = day;
	}

	/**
	 * Get the hour
	 * @return hour short
	 */
	public short getHour() {
		return hour;
	}

	/**
	 * Set the hour
	 * @param hour short
	 */
	public void setHour(final short hour) {
		this.hour = hour;
	}

	/**
	 * Get the minutes
	 * @return minute short
	 */
	public short getMinute() {
		return minute;
	}

	/**
	 * Set the minutes
	 * @param minute short
	 */
	public void setMinute(final short minute) {
		this.minute = minute;
	}

	/**
	 * Get the seconds
	 * @return seconds short
	 */
	public short getSecond() {
		return second;
	}

	/**
	 * Set the seconds
	 * @param second short
	 */
	public void setSecond(final short second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "DeviceDate [year=" + year + ", month=" + month + ", day=" + day
				+ ", hour=" + hour + ", minute=" + minute + ", second="
				+ second + " ]";
	}
}