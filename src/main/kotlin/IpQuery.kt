/*
 * Copyright © 2025.
 * This work is created by Alexander Voglsperger and is licenced under the MIT license.
 */

import com.google.gson.Gson
import info.IpInfo
import java.net.InetAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class IpQuery {
	/**
	 * The format for the response of raw queries
	 */
	enum class ResponseFormat {
		// TEXT intentionally omitted as it just returns the query IP (which is already known to the user anyway)
		JSON,
		@Suppress("unused")
		YAML,
		XML;

		override fun toString(): String {
			return name.lowercase()
		}
	}

	companion object {
		private const val BASE_URL = "https://api.ipquery.io/"
		private const val DEFAULT_FORMAT = "?format=json"
		private val client = HttpClient.newHttpClient()
		private val gson = Gson()

		// ---------------------------------------------------------------------
		/**
		 * Query the IP of the current machine
		 * @return The IP of the current machine
		 * @throws RuntimeException If the request fails
		 */
		fun queryIp(): String {
			return makeRequest(URI.create("$BASE_URL?format=text"))
		}

		// ---------------------------------------------------------------------
		/**
		 * Query the IP information of the current machine
		 * @return The IP information of the current machine
		 * @throws RuntimeException If the request fails
		 */
		fun queryIpInfo(): IpInfo {
			val responseBody = makeRequest(URI.create("$BASE_URL$DEFAULT_FORMAT"))
			return gson.fromJson(responseBody, IpInfo::class.java)
		}

		/**
		 * Query the IP of the current machine
		 * @param format The format of the response
		 * @return The IP information of the current machine in the specified format
		 */
		fun queryIpInfoRaw(format: ResponseFormat): String {
			return makeRequest(URI.create("$BASE_URL?format=${format}")).trim('\n')
		}

		// ---------------------------------------------------------------------
		/**
		 * Query the information of a specific IP address
		 * @param ip The IP address to query
		 * @return The information about the IP address
		 * @throws RuntimeException If the request fails
		 */
		fun querySpecificIp(ip: String): IpInfo {
			val responseBody = makeRequest(URI.create("$BASE_URL$ip$DEFAULT_FORMAT"))
			return gson.fromJson(responseBody, IpInfo::class.java)
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

		/**
		 * Query the information of a specific IP address
		 * @param format The format of the response
		 * @param ip The IP address to query
		 * @return The information about the IP address in the specified format
		 * @throws RuntimeException If the request fails
		 */
		fun querySpecificIpRaw(format: ResponseFormat, ip: String): String {
			return makeRequest(URI.create("$BASE_URL$ip?format=${format}")).trim('\n')
		}

		// ---------------------------------------------------------------------
		/**
		 * Query the information of multiple IP addresses
		 * @param ips The IP addresses to query
		 * @return The information about the IP addresses
		 * @throws RuntimeException If the request fails
		 */
		fun queryBulkIpsStr(ips: List<String>): List<IpInfo> {
			val responseBody = makeRequest(URI.create("$BASE_URL${ips.joinToString(",")}$DEFAULT_FORMAT"))
			return gson.fromJson(responseBody, Array<IpInfo>::class.java).toList()
		}

		/**
		 * Query the information of multiple IP addresses
		 * @param ips The IP addresses to query
		 * @return The information about the IP addresses
		 * @throws RuntimeException If the request fails
		 */
		fun queryBulkIpsStr(vararg ips: String): List<IpInfo> {
			return queryBulkIpsStr(ips.asList())
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

		/**
		 * Query the information of multiple IP addresses
		 * @param ips The InetAddresses to query
		 * @return The information about the IP addresses
		 * @throws RuntimeException If the request fails
		 */
		fun queryBulkIps(vararg ips: InetAddress): List<IpInfo> {
			return queryBulkIpsStr(ips.map { it.hostAddress })
		}

		/**
		 * Query the information of multiple IP addresses
		 * @param format The format of the response
		 * @param ips The IP addresses to query
		 * @return The information about the IP addresses in the specified format
		 */
		fun queryBulkIpsRaw(format: ResponseFormat, ips: List<String>): String {
			return makeRequest(URI.create("$BASE_URL${ips.joinToString(",")}?format=${format}")).trim('\n')
		}

		// ---------------------------------------------------------------------
		/**
		 * Perform a request to the given URI
		 * @param uri The URI to request
		 * @return The response from the request
		 * @throws RuntimeException If the request fails
		 */
		private fun makeRequest(uri: URI): String {
			val request = HttpRequest.newBuilder().uri(uri).build()
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())

			if (response.statusCode() != 200) {
				throw RuntimeException("Failed to receive own IP! ${response.statusCode()}")
			}
			return response.body()
		}
	}
}