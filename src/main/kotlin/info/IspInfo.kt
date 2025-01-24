/*
 * Copyright © 2025.
 * This work is created by Alexander Voglsperger and is licenced under the MIT license.
 */

package info

data class IspInfo(
	val asn: String,
	val org: String,
	val isp: String,
) {
	override fun toString(): String {
		return "IspInfo(asn='$asn', org='$org', isp='$isp')"
	}
}
