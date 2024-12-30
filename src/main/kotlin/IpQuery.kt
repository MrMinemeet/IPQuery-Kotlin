import com.google.gson.Gson
import info.IpInfo
import java.net.InetAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class IpQuery {
	companion object {
		private const val BASE_URL = "https://api.ipquery.io/"
		private val client = HttpClient.newHttpClient()
		private val gson = Gson()

		// ---------------------------------------------------------------------
		/**
		 * Query the IP of the current machine
		 * @return The IP information of the current machine
		 * @throws RuntimeException If the request fails
		 */
		fun queryIp(): String {
			val request = HttpRequest.newBuilder()
				.uri(URI.create(BASE_URL))
				.build()
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())

			if (response.statusCode() != 200) {
				throw RuntimeException("Failed to receive own IP! ${response.statusCode()}")
			}
			return response.body()
		}

		// ---------------------------------------------------------------------
		/**
		 * Query the information of a specific IP address
		 * @param ip The IP address to query
		 * @return The information about the IP address
		 * @throws RuntimeException If the request fails
		 */
		fun querySpecificIp(ip: String): IpInfo {
			val request = HttpRequest.newBuilder()
				.uri(URI.create("$BASE_URL$ip"))
				.build()
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())

			if (response.statusCode() != 200) {
				throw RuntimeException("Failed to query for '$ip'! ${response.statusCode()}")
			}

			return gson.fromJson(response.body(), IpInfo::class.java)
		}

		/**
		 * Query the information of a specific IP address
		 * @param ip The InetAddress to query
		 * @return The information about the IP address
		 * @throws RuntimeException If the request fails
		 */
		fun querySpecificIp(ip: InetAddress): IpInfo {
			return querySpecificIp(ip.hostAddress)
		}

		// ---------------------------------------------------------------------
		/**
		 * Query the information of multiple IP addresses
		 * @param ips The IP addresses to query
		 * @return The information about the IP addresses
		 * @throws RuntimeException If the request fails
		 */
		fun queryBulkIpsStr(ips: List<String>): List<IpInfo> {
			val request = HttpRequest.newBuilder()
				.uri(URI.create("$BASE_URL${ips.joinToString(",")}"))
				.build()
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())

			if (response.statusCode() != 200) {
				throw RuntimeException("Failed to query IP information'! ${response.statusCode()}")
			}

			return gson.fromJson(response.body(), Array<IpInfo>::class.java).toList()
		}

		/**
		 * Query the information of multiple IP addresses
		 * @param ips The InetAddresses to query
		 * @return The information about the IP addresses
		 * @throws RuntimeException If the request fails
		 */
		fun queryBulkIps(ips: List<InetAddress>): List<IpInfo> {
			return queryBulkIpsStr(ips.map { it.hostAddress })
		}
	}
}