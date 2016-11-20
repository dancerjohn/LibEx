package org.libex.net;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.libex.test.TestBaseLocal;

@RunWith(Enclosed.class)
public class InetAddressesExTest extends TestBaseLocal {

    @RunWith(Parameterized.class)
    public static class ToBinaryTest extends TestBaseLocal {

        @Parameters(name = "{index}: {1}")
        public static Collection<Object[]> getData()
        {
            return Arrays
                    .asList(
                            new Object[] { "IPv4 1", "192.168.1.5", "1100 0000  1010 1000  0000 0001  0000 0101" },
                            new Object[] { "IPv4 2", "1.2.3.4", "0000 0001  0000 0010  0000 0011  0000 0100" },
                            new Object[] {
                                    "IPv6",
                                    "2001:0db8:0a0b:12f0:0000:0000:0000:0001",
                                    "001000000000000100001101101110000000101000001011000100101111   00000000000000000 00000000000000000 00000000000000000 00000000000000001 " },
                            new Object[] {
                                    "IPv6 with ::",
                                    "2001:0db8:0a0b:12f0::0001",
                                    "001000000000000100001101101110000000101000001011000100101111   00000000000000000 00000000000000000 00000000000000000 00000000000000001 " },
                            new Object[] {
                                    "IPv6 with ::",
                                    "2001:db8:a0b:12f0::1",
                                    "001000000000000100001101101110000000101000001011000100101111   00000000000000000 00000000000000000 00000000000000000 00000000000000001 " }
                    );
        }

        @Parameter(0)
        public String name;

        @Parameter(1)
        public String ipAddress;

        @Parameter(2)
        public String expectedOutput;

        @Test
        public void testToBinary() throws UnknownHostException
        {
            // setup
            InetAddress address = InetAddress.getByName(ipAddress);

            // test
            String result = InetAddressesEx.toBinary(address);


            // verify
            String updatedExpected = expectedOutput.replaceAll("\\s", "");
            assertThat(result, equalTo(updatedExpected));
        }
    }
}
