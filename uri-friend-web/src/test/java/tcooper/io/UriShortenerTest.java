package tcooper.io;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tcooper.io.database.UriRepository;
import tcooper.io.model.URIInfo;
import tcooper.io.uri.UriService;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Given I send a request to the URI shortener")
@ExtendWith(MockitoExtension.class)
public class UriShortenerTest {

    @Mock
    UriRepository uriRepository;

    @Spy
    UriService uriService;

    @InjectMocks
    UriShortener uriShortener;

    @DisplayName("When I have a URI to shorten")
    @Nested
    class ShortenUrlTest{

        @DisplayName("Then a short URI is created")
        @Test
        void canShortenUri() throws URISyntaxException {
            ArgumentCaptor<String> scheme = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> authority = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> relativePath = ArgumentCaptor.forClass(String.class);

            when(uriRepository.upsertScheme(anyString())).thenReturn(1L);
            when(uriRepository.upsertAuthority(anyString())).thenReturn(3L);
            when(uriRepository.upsertRelativePath(anyString())).thenReturn(5L);

            String url = "http://test.com/a_new_uri?query=test&another=test#bookmark";

            URIInfo uriInfo = uriShortener.shortenUri(url);

            verify(uriRepository).upsertScheme(scheme.capture());
            verify(uriRepository).upsertAuthority(authority.capture());
            verify(uriRepository).upsertRelativePath(relativePath.capture());
            verify(uriService, atMostOnce()).shortenUri(any(long[].class));

            assertEquals("http", scheme.getValue());
            assertEquals("test.com", authority.getValue());
            assertEquals("/a_new_uri?query=test&another=test#bookmark", relativePath.getValue());
            assertTrue(uriInfo.getShortUri().toString().matches("http:\\/\\/example\\.com\\/1.3.5"));
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
        void fullUriCanBeFound() throws URISyntaxException {

            String shortUrl = "http://example.com/1d3g5";

            when(uriRepository.getScheme(anyLong())).thenReturn("http");
            when(uriRepository.getAuthority(anyLong())).thenReturn("example.com");
            when(uriRepository.getRelativePath(anyLong())).thenReturn("/path/to/page");

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
        void idNotFoundInDatabase(){
            String shortUrl = "http://example.com/1d3g5";

            when(uriRepository.getScheme(anyLong())).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> uriShortener.resolveUri(shortUrl));
        }

        // TODO: Test more combinations of null responses from repo.
    }
}

