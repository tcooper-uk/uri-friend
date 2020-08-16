package tcooper.io.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tcooper.io.UriShortener;
import tcooper.io.model.URIInfo;

import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given I have setup a web endpoint for my URI short app")
public class UriShortTest {

    @Mock
    private UriShortener uriShortener;

    @InjectMocks
    private UriShort uriShort;

    @Nested
    @DisplayName("When I post a full URL to the endpoint")
    class fullURI{

        @DisplayName("Then endpoint calls short service")
        @Test
        public void canPostFullUri() throws URISyntaxException {
            when(uriShortener.shortenUri(anyString())).thenReturn(new URIInfo());
            String url = "http://google.com/s?q=test";

            uriShort.shortenUri(url);

            verify(uriShortener, times(1)).shortenUri(anyString());
        }
    }

    @Nested
    @DisplayName("When I post a short URL to the endpoint")
    class shortURI {

        @DisplayName("Then the endpoint calls to resolve the URL")
        @Test
        public void canPostShortUri() throws URISyntaxException {
            when(uriShortener.resolveUri(anyString())).thenReturn(new URIInfo());
            String url = "http://google.com/s?q=test";

            uriShort.resolveUri(url);

            verify(uriShortener, times(1)).resolveUri(anyString());
        }
    }


}
