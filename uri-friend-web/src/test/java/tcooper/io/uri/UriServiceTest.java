package tcooper.io.uri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Given I want to interact with the URI service")
public class UriServiceTest {

    private final long singleId = 12;
    private final long schemeId = 1;
    private final long authorityId = 3;
    private final long relativePathId = 5;

    private static UriEncoder uriEncoder;
    private static UriService uriService;

    @BeforeAll
    static void setUp() {
        uriEncoder = new UriEncoder();
        uriService = new UriService(uriEncoder);
    }

    @DisplayName("When I have database ID's")
    @Nested
    class NewUrl {

        @DisplayName("then I can use the single id to make a new URI")
        @Test
        void singleIdIsEncoded(){

            URI result = uriService.shortenUri(singleId);
            assertTrue(result.toString().matches("http://example.com/m"));
        }

        @DisplayName("Then I can use these ID's to make a new URI")
        @Test
        void parseValidIdsToUri() {
            long[] databaseIds = new long[]{schemeId, authorityId, relativePathId};

            URI result = uriService.shortenUri(databaseIds);

            assertTrue(result.toString().matches("http:\\/\\/example\\.com\\/1.3.5"));
        }

        @DisplayName("Then I get an error if I don't use them all")
        @Test
        void notEnoughIdsThrowsError() {
            long[] databaseIds = new long[]{schemeId, authorityId};

            assertThrows(IllegalArgumentException.class, () -> uriService.shortenUri(databaseIds));
        }

        @DisplayName("Then I get an error if I don't supply any")
        @Test
        void noIdsThrowsError() {
            long[] databaseIds = new long[0];

            assertThrows(IllegalArgumentException.class, () -> uriService.shortenUri(databaseIds));
        }
    }

    @DisplayName("When I have a short URI")
    @Nested
    class ShortUrl{

        @DisplayName("Then I can decode this back to a full url")
        @Test
        void decode() {
            final String shortUrl = "http://example.com/m";

            long id = uriService.decomposeShortUriV2(URI.create(shortUrl));

            assertEquals(singleId, id);
        }

        @DisplayName("Then I can extract the database ID's")
        @ParameterizedTest
        @CsvSource({"http://example.com/u/1a3n5,1,3,5",
                "http://example.com/u/1a3n5s,1,3,5",
                "http://example.com/u/13a314n52,13,314,52",
                "http://example.com/u/13a314n52a,13,314,52"})
        void validShortUrl(String shortUrl, int scheme, int authority, int relativePath) throws URISyntaxException {

            URI uri = new URI(shortUrl);

            long[] results = uriService.decomposeShortUri(uri);

            assertEquals(scheme, results[0]);
            assertEquals(authority, results[1]);
            assertEquals(relativePath, results[2]);
        }

        @DisplayName("Then if the URL is invalid an exception is thrown")
        @ParameterizedTest
        @ValueSource(strings = {
                "https://test/123324",
                "http://example.com/u/--as--233--df"})
        void invalidShortUrl(String shortUrl) throws URISyntaxException {

            URI uri = new URI(shortUrl);

            assertThrows(IllegalArgumentException.class, () -> uriService.decomposeShortUri(uri));
        }
    }
}
