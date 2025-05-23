package permissions.dispatcher.processor.base;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

/**
 * Configures and provides access to a ClassLoader
 * which is used by our processor's unit tests, and
 * extends the system ClassLoader with Android-specific classes
 * provided through a test resource file.
 * <p>
 * It works closely with our AarToJarDependencyPlugin
 * and consumes the resource file generated by that plugin
 * to inject Android-specific code into the code generation tests,
 * so that they will compile properly.
 */
final class AndroidAwareClassLoader {

    private static final String TEST_CLASSPATH_FILE_NAME = "/additional-test-classpath.txt";

    private AndroidAwareClassLoader() {
        throw new AssertionError();
    }

    static ClassLoader create() {
        try {
            InputStream stream = AndroidAwareClassLoader.class.getResourceAsStream(TEST_CLASSPATH_FILE_NAME);
            URL[] urls = IOUtils.readLines(stream, Charset.forName("UTF-8"))
                    .stream()
                    .map(AndroidAwareClassLoader::unsafeToURL)
                    .toArray(URL[]::new);

            return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static URL unsafeToURL(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
