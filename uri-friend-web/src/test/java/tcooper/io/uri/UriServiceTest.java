package tcooper.io.uri;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Given I have valid URI component ID's")
public class UriServiceTest {

    private final long schemeId = 1;
    private final long authorityId = 3;
    private final long relativePathId = 5;

    private UriService uriService;

    @BeforeEach
    void setUp() {
        uriService = new UriService();
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
        long[] databaseIds = new long[] {schemeId, authorityId};

        assertThrows(IllegalArgumentException.class, () -> uriService.shortenUri(databaseIds));
    }

    @DisplayName("Then I get an error if I don't supply any")
    @Test
    void noIdsThrowsError() {
        long[] databaseIds = new long[0];

        assertThrows(IllegalArgumentException.class, () -> uriService.shortenUri(databaseIds));
    }
}
