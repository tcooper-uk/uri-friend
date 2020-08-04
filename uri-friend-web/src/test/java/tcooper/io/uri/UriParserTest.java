package tcooper.io.uri;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UriParserTest {


    @ParameterizedTest
    @MethodSource
    void testValidUrlsAreParsed(String url, String expectedScheme,
                                String expectedAuthority, int expectedPort,
                                String expectedPath, String expectedQuery,
                                String expectedFragment) {

        Optional<URI> uriOpt = UriParser.parse(url);

        assertTrue(uriOpt.isPresent());

        URI uri = uriOpt.get();

        assertEquals(expectedScheme, uri.getScheme());
        assertEquals(expectedAuthority, uri.getAuthority());
        assertEquals(expectedPort, uri.getPort());
        assertEquals(expectedPath, uri.getPath());
        assertEquals(expectedQuery, uri.getQuery());
        assertEquals(expectedFragment, uri.getFragment());
    }

    @Test
    void badUrlFormatCannotBeParsed() {
        final String url = "1234://1234";

        Optional<URI> uri = UriParser.parse(url);

        assertFalse(uri.isPresent());
    }

    @Test
    void nullUrlCannotBeParsed() {
        assertThrows(IllegalArgumentException.class, () -> UriParser.parse(null));
    }

    private static Stream<Arguments> testValidUrlsAreParsed() {
        return Stream.of(
                Arguments.of("http://test.com", "http", "test.com", -1, "", null, null),
                Arguments.of("https://test.com", "https", "test.com", -1, "", null, null),
                Arguments.of("file://test.com", "file", "test.com", -1, "", null, null),
                Arguments.of("http://test.com/", "http", "test.com", -1, "/", null, null),
                Arguments.of("http://test.com:80", "http", "test.com:80", 80, "", null, null),
                Arguments.of("http://test.com:443", "http", "test.com:443", 443, "", null, null),
                Arguments.of("http://test.com:443/u/test", "http", "test.com:443", 443, "/u/test", null, null),
                Arguments.of("http://test.com:443/u/test?p=query", "http", "test.com:443", 443, "/u/test", "p=query", null),
                Arguments.of("http://test.com:443/u/test?p=query&t=test", "http", "test.com:443", 443, "/u/test", "p=query&t=test", null),
                Arguments.of("http://test.com:443/u/test?p=query&t=test#bookmark", "http", "test.com:443", 443, "/u/test", "p=query&t=test", "bookmark")
        );
    }
}
