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
}