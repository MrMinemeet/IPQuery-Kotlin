/*
 * Copyright © 2025.
 * This work is created by Alexander Voglsperger and is licenced under the MIT license.
 */

package info

import java.util.*

data class LocationInfo(
	val country: String,
	val country_code: String, // Intentionally snake_case to match JSON
	val city: String,
	val state: String,
	val zipcode: String,
	val latitude: Double,
	val longitude: Double,
	val timezone: String,
	val localtime: String,
) {
	val countryCode: String
		get() = country_code
	val resolvedTimezone: TimeZone
		get() = TimeZone.getTimeZone(timezone)

	override fun toString(): String {
		return "LocationInfo(country='$country', country_code='$country_code', city='$city', state='$state', zipcode='$zipcode', latitude=$latitude, longitude=$longitude, timezone='$timezone', localtime='$localtime')"
	}

	/**
	 * Compares this location to another location.
	 * The [localtime] field is not considered in the comparison.
	 */
	fun looseEquals(other: LocationInfo): Boolean {
		return country == other.country &&
				country_code == other.country_code &&
				city == other.city &&
				state == other.state &&
				zipcode == other.zipcode &&
				latitude == other.latitude &&
				longitude == other.longitude &&
				timezone == other.timezone
	}
}