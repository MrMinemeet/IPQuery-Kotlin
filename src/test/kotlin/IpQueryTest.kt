/*
 * Copyright © 2025.
 * This work is created by Alexander Voglsperger and is licenced under the MIT license.
 */

import info.IpInfo
import java.net.InetAddress
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.reflect.full.memberProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class IpQueryTest {
	@Test
	fun testBasicQueryWithStr() {
		val ipInfo = IpQuery.querySpecificIp("1.1.1.1")
		assertEquals("1.1.1.1", ipInfo.ip)
		assertIpInfoNotNull(ipInfo)
	}

	@Test
	fun testBasicQuery() {
		val ipInfo = IpQuery.querySpecificIp(InetAddress.getByName("1.1.1.1"))
		assertEquals("1.1.1.1", ipInfo.ip)
		assertIpInfoNotNull(ipInfo)
	}

	@Test
	fun testBulkQueryWithStr() {
		val ipInfos = IpQuery.queryBulkIpsStr(listOf("1.1.1.1", "8.8.8.8"))
		assertEquals(2, ipInfos.size)
		ipInfos.forEach { assertIpInfoNotNull(it) }
	}

	@Test
	fun testBulkQuery() {
		val ipInfos = IpQuery.queryBulkIps(listOf("1.1.1.1", "8.8.8.8").map { InetAddress.getByName(it) })
		assertEquals(2, ipInfos.size)
		ipInfos.forEach { assertIpInfoNotNull(it) }
	}

	@Test
	fun testBulkQueryVarArgsWithStr() {
		val ipInfoStr = IpQuery.queryBulkIpsStr(listOf("1.1.1.1", "8.8.8.8"))
		val ipInfoVarArgStr = IpQuery.queryBulkIpsStr("1.1.1.1", "8.8.8.8")
		assertLooseIpInfoEquals(ipInfoStr, ipInfoVarArgStr)
	}

	@Test
	fun testBulkQueryIpVarArgs() {
		val ipInfoStr = IpQuery.queryBulkIps(listOf(InetAddress.getByName("1.1.1.1"), InetAddress.getByName("8.8.8.8")))
		val ipInfoVarArgStr = IpQuery.queryBulkIps(InetAddress.getByName("1.1.1.1"), InetAddress.getByName("8.8.8.8"))
		assertLooseIpInfoEquals(ipInfoStr, ipInfoVarArgStr)
	}

	@Test
	fun testEqualStrAndInetResult() {
		assertLooseIpInfoEquals(IpQuery.querySpecificIp("1.1.1.1"), IpQuery.querySpecificIp(InetAddress.getByName("1.1.1.1")))

		assertLooseIpInfoEquals(
			IpQuery.queryBulkIpsStr("1.1.1.1", "8.8.8.8"),
			IpQuery.queryBulkIps(InetAddress.getByName("1.1.1.1"), InetAddress.getByName("8.8.8.8"))
		)
	}

	@Test
	fun testQueryOwnIp() {
		val ownIp = IpQuery.queryIp()
		assertNotNull(ownIp)
		assertTrue { ownIp.isNotBlank() }
		// Should be a valid IP address
		assertDoesNotThrow { InetAddress.getByName(ownIp) }
	}

	@Test
	fun testQueryOwnIpInfo() {
		val ownIpInfo = IpQuery.queryIpInfo()
		assertNotNull(ownIpInfo)
		assertIpInfoNotNull(ownIpInfo)
	}

	@Test
	fun testBasicQueryRawXML() {
		val raw = IpQuery.querySpecificIpRaw(IpQuery.ResponseFormat.XML, "1.1.1.1")
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("<") && raw.endsWith(">") }
	}

	@Test
	fun testBulkQueryRawXML() {
		val raw = IpQuery.queryBulkIpsRaw(IpQuery.ResponseFormat.XML, listOf("1.1.1.1", "8.8.8.8"))
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("<") && raw.endsWith(">") }
	}

	@Test
	fun testQueryOwnIpRawXML() {
		val raw = IpQuery.queryIpInfoRaw(IpQuery.ResponseFormat.XML)
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("<") && raw.endsWith(">") }
	}

	@Test
	fun testBasicQueryRawJSON() {
		val raw = IpQuery.querySpecificIpRaw(IpQuery.ResponseFormat.JSON, "1.1.1.1")
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("{") && raw.endsWith("}") }
	}

	@Test
	fun testBulkQueryRawJSON() {
		val raw = IpQuery.queryBulkIpsRaw(IpQuery.ResponseFormat.JSON, listOf("1.1.1.1", "8.8.8.8"))
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("[") && raw.endsWith("]") }
	}

	@Test
	fun testQueryOwnIpRawJSON() {
		val raw = IpQuery.queryIpInfoRaw(IpQuery.ResponseFormat.JSON)
		assertTrue { raw.isNotBlank() }
		assertTrue { raw.startsWith("{") && raw.endsWith("}") }
	}

	private fun assertIpInfoNotNull(obj: IpInfo) {
		assertMembersNotNull(obj)
		assertMembersNotNull(obj.isp)
		assertMembersNotNull(obj.location)
		assertMembersNotNull(obj.risk)
	}

	private fun assertMembersNotNull(obj: Any) {
		obj::class.memberProperties.forEach {
			val value = it.getter.call(obj)
			assertNotNull(value, "${it.name} is null!")
		}
	}

	private fun assertLooseIpInfoEquals(expected: IpInfo, actual: IpInfo) {
		assertEquals(expected.ip, actual.ip)
		assertEquals(expected.isp, actual.isp)
		assertTrue(expected.location.looseEquals(actual.location))
		assertEquals(expected.risk, actual.risk)
	}

	private fun assertLooseIpInfoEquals(expected: List<IpInfo>, actual: List<IpInfo>) {
		assertEquals(expected.size, actual.size)
		for (idx in expected.indices) {
			assertLooseIpInfoEquals(expected[idx], actual[idx])
		}
	}
}