package info

import java.net.InetAddress

/**
 * Represents the information about an IP address.
 * The data structure represents the JSON response from the API but already parsed into a Kotlin data class.
 *
 * Additionally, some additional non-backed properties are added for convenience.
 */
data class IpInfo(
	val ip: String,
	val isp: IspInfo,
	val location: LocationInfo,
	val risk: RiskInfo
) {
	val ipAddress: InetAddress?
		get() = InetAddress.getByName(ip)

	override fun toString(): String {
		return "IpInfo(ip='$ip', isp=$isp, location=$location, risk=$risk)"
	}
}