package org.libex.compress;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.FileType;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class CompressionUtils {

    public static List<Path> decompressTo(
            final Path compressedFile,
            final Path destination) throws IOException
    {
        File compressed = compressedFile.toFile();
        Archiver archiver = ArchiverFactory.createArchiver(FileType.get(compressed));
        archiver.extract(compressed, destination.toFile());

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(destination)) {
            return newArrayList(ds.iterator());
        }
    }

    private CompressionUtils() {
    }

}
