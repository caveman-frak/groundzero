package uk.co.bluegecko.utility.network;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;


class NetworkAdapterTest {

	@Test
	void localHost() throws UnknownHostException {
		assertThat(InetAddress.getLocalHost().getAddress()).is(anyOf(
				new Condition<>(a -> Arrays.equals(a, new byte[]{127, 0, 0, 1}), "local"),
				new Condition<>(a -> Arrays.equals(Arrays.copyOf(a, 2),
						new byte[]{-87, -2}), "private")));
		assertThat(InetAddress.getLocalHost().getHostName()).isNotBlank();
	}

	@Test
	void loopBack() {
		assertThat(InetAddress.getLoopbackAddress().getAddress()).isEqualTo(new byte[]{127, 0, 0, 1});
		assertThat(InetAddress.getLoopbackAddress().getHostName()).isEqualTo("localhost");
	}

	@Test
	void networkInterfaces() throws SocketException {
		assertThat(NetworkInterface.networkInterfaces()
				.peek(i -> System.out.printf("%s, %s :: %s -> %s\n", i.getDisplayName(), i.getName(), i.isVirtual(),
						i.getInterfaceAddresses().getFirst().getAddress()))
				.toList()).isNotEmpty();
	}

	@Test
	void localInterface() throws SocketException, UnknownHostException {
		assertThat(NetworkInterface.getByName("lo0")).isNotNull()
				.extracting(NetworkInterface::getInterfaceAddresses, as(InstanceOfAssertFactories.COLLECTION))
				.hasSize(3)
				.extracting(i -> ((InterfaceAddress) i).getAddress())
				.contains(Inet4Address.getByAddress(new byte[]{127, 0, 0, 1}),
						Inet6Address.getByAddress(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}),
						Inet6Address.getByAddress(new byte[]{-2, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}))
				.element(0)
				.extracting(InetAddress::getHostName).isEqualTo(InetAddress.getLocalHost().getHostName());
	}

}