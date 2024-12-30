package info

data class RiskInfo(
	// Intentionally snake_case to match JSON
	val is_mobile: Boolean,
	val is_vpn: Boolean,
	val is_tor: Boolean,
	val is_proxy: Boolean,
	val is_datacenter: Boolean,
	val risk_score: Int,
)
{
	val isMobile: Boolean
		get() = is_mobile
	val isVpn: Boolean
		get() = is_vpn
	val isTor: Boolean
		get() = is_tor
	val isProxy: Boolean
		get() = is_proxy
	val isDatacenter: Boolean
		get() = is_datacenter
	val riskScore: Int
		get() = risk_score
}