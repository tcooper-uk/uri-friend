package tcooper.io.uri;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Given I have url Id's to encode")
public class UriEncoderTest {

  private UriEncoder uriEncoder = new UriEncoder();

  @DisplayName("Then I can encode and decode correctly")
  @ParameterizedTest
  @ValueSource(longs = {1203L, 1223214L, 4314L, 43252436L})
  void canEncodeAndDecodeUriId(long uriId) {

    String encoded = uriEncoder.encode(uriId);
    long decodedId = uriEncoder.decode(encoded);

    assertEquals(uriId, decodedId);
  }

}
