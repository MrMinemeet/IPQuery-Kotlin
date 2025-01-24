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
		println(ipInfo.ip)
	}

	@Test
	fun testBasicQuery() {
		val ipInfo = IpQuery.querySpecificIp(InetAddress.getByName("1.1.1.1"))
		assertEquals("1.1.1.1", ipInfo.ip)
		assertIpInfoNotNull(ipInfo)
		println(ipInfo.ip)
	}

	@Test
	fun testBulkQueryWithStr() {
		val ipInfos = IpQuery.queryBulkIpsStr(listOf("1.1.1.1", "8.8.8.8"))
		assertEquals(2, ipInfos.size)
		ipInfos.forEach { assertIpInfoNotNull(it) }
		ipInfos.forEach { println(it.ip) }
	}

	@Test
	fun testBulkQuery() {
		val ipInfos = IpQuery.queryBulkIps(listOf("1.1.1.1", "8.8.8.8").map { InetAddress.getByName(it) })
		assertEquals(2, ipInfos.size)
		ipInfos.forEach { assertIpInfoNotNull(it) }
		ipInfos.forEach { println(it.ip) }
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
}