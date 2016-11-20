package org.libex.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class URIUtils {

    public static Path downloadTo(
            final URL url,
            final Path destinationFile) throws FileNotFoundException, IOException
    {
        try (
                FileOutputStream fos = new FileOutputStream(destinationFile.toFile());
                ReadableByteChannel channel = Channels.newChannel(url.openStream())) {
            fos.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
        }
        return destinationFile;
    }

    private URIUtils() {
    }

}
