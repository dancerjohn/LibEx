package org.libex.net;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Strings;

/**
 * Provides utilities for {@link InetAddress}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class InetAddressesEx {

    public static final String DEFAULT_HOSTNAME = "UNKNOWN";
    private static AtomicReference<String> hostName = new AtomicReference<String>();

    /**
     * @return the local hostname
     */
    public static String getLocalHostName()
    {
        if (hostName.get() == null) {
            String retrievedHostname = retrieveLocalHostName();
            hostName.compareAndSet(null, retrievedHostname);
        }
        return hostName.get();
    }

    private static String retrieveLocalHostName()
    {
        try {
            // determine the hostname for the current system.
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return DEFAULT_HOSTNAME;
        }
    }

    public static String toBinary(
            final InetAddress address)
    {
        byte[] bytes = address.getAddress();
        String result = new BigInteger(1, bytes).toString(2);

        int expectedLength = (address instanceof Inet6Address) ? 128 : 32;
        result = Strings.padStart(result, expectedLength, '0');
        return result;
    }

    private InetAddressesEx() {
    }
}
