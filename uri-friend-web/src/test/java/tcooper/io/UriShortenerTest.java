package tcooper.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tcooper.io.database.UriRepository;
import tcooper.io.model.URIInfo;
import tcooper.io.model.UriParts;
import tcooper.io.uri.UriService;

@DisplayName("Given I send a request to the URI shortener")
@ExtendWith(MockitoExtension.class)
public class UriShortenerTest {

    @Mock
    UriRepository uriRepository;

    @Mock
    UriService uriService;

    @InjectMocks
    UriShortener uriShortener;

    @DisplayName("When I have a URI to shorten")
    @Nested
    class ShortenUrlTest{

        @DisplayName("Then a short URI is created")
        @Test
        void canShortenUri() throws URISyntaxException, SQLException {
            ArgumentCaptor<String> scheme = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> authority = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> relativePath = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Long> shortUrlId = ArgumentCaptor.forClass(Long.class);

            when(uriRepository.upsertScheme(anyString())).thenReturn(1L);
            when(uriRepository.upsertAuthority(anyString())).thenReturn(3L);
            when(uriRepository.upsertRelativePath(anyString())).thenReturn(5L);
            when(uriRepository.insertShortUrl(any(ZonedDateTime.class), anyLong(), anyLong(), anyLong()))
                .thenReturn(123L);

            when(uriService.shortenUri(anyLong())).thenReturn(URI.create("http://example.com/a"));

            String url = "http://test.com/a_new_uri?query=test&another=test#bookmark";

            URIInfo uriInfo = uriShortener.shortenUri(url);

            verify(uriRepository).upsertScheme(scheme.capture());
            verify(uriRepository).upsertAuthority(authority.capture());
            verify(uriRepository).upsertRelativePath(relativePath.capture());
            verify(uriService).shortenUri(shortUrlId.capture());

            assertEquals("http", scheme.getValue());
            assertEquals("test.com", authority.getValue());
            assertEquals("/a_new_uri?query=test&another=test#bookmark", relativePath.getValue());
            assertEquals( 123L, shortUrlId.getValue());
            assertTrue(uriInfo.getShortUri().toString().matches("http://example.com/a"));
        }

        @DisplayName("Then an error is thrown when the URI is not valid")
        @ParameterizedTest
        @ValueSource(strings = {"12345-$%", "test", "check-this"})
        void cannotShortenInvalidUri(String url) {
            assertThrows(URISyntaxException.class, () -> uriShortener.shortenUri(url));
        }
    }

    @DisplayName("When I have a URI to resolve")
    @Nested
    class ResolveUrlTest {

        @DisplayName("Then a full URI can be resolved from database")
        @Test
        void fullUriCanBeFound() throws URISyntaxException, SQLException {

            String shortUrl = "http://example.com/a";

            when(uriService.decomposeShortUriV2(any(URI.class))).thenReturn(123L);
            when(uriRepository.getUriParts(anyLong())).thenReturn(new UriParts("http", "example.com", "/path/to/page"));

            URIInfo uriInfo = uriShortener.resolveUri(shortUrl);

            assertEquals(shortUrl, uriInfo.getShortUri().toString());
            assertEquals("http://example.com/path/to/page", uriInfo.getOriginalUri().toString());
        }

        @DisplayName("Then an error is thrown when the URI is not valid")
        @Test
        void cannotShortenInvalidUri() {
            String url = "12345-$%";

            assertThrows(URISyntaxException.class, () -> uriShortener.resolveUri(url));
        }

        @DisplayName("Then an error is thrown when the ID cannot be found")
        @Test
        void idNotFoundInDatabase() throws SQLException {
            String shortUrl = "http://example.com/a";

            when(uriService.decomposeShortUriV2(any(URI.class))).thenReturn(123L);
            when(uriRepository.getUriParts(anyLong())).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> uriShortener.resolveUri(shortUrl));
        }

        // TODO: Test more combinations of null responses from repo.
    }
}

