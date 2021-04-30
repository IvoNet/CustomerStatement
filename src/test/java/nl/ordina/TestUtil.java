package nl.ordina;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil {
    public static String readTestResourceAsString(final String fileName) {
        final String abspath = new File(".").getAbsolutePath();
        final String absolutePath = new File(
              abspath.substring(0, abspath.length() - 1) + "src/test/resources/" + fileName).getAbsolutePath();
        try {
            return new String(Files.readAllBytes(Paths.get(absolutePath)));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


}
