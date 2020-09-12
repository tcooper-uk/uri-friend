package tcooper.io.uri;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.net.URI;
import java.util.Optional;
import java.util.Random;
import tcooper.io.guice.ConfigurationModule.AppDomain;

/**
 * This service is used to create a short URI from three database ID's We know we can do this better
 * by just using the one ID.
 * <p>
 * This service was experimental and may be deprecated
 */
@Singleton
public class UriService {

  // We will move this out somewhere else
  private final UriEncoder uriEncoder;

  private final String applicationAuthority;

  @Inject
  public UriService(UriEncoder uriEncoder, @AppDomain String applicationAuthority) {
    this.uriEncoder = uriEncoder;
    this.applicationAuthority = applicationAuthority;
  }

  /**
   * Takes a uri, finds the path and decodes to the uri id.
   *
   * @param uri
   * @return uri id
   */
  public long decomposeShortUriV2(URI uri) {
    String path = uri.getPath();

    if (Strings.isNullOrEmpty(path)) {
      throw new IllegalStateException("No path was provided");
    }

    return uriEncoder.decode(path.substring(1));
  }

  /**
   * Take a previously shortened URI and gathers each database ID
   *
   * @param uri A uri previously shortened
   * @return Array of database ID's
   */
  @Deprecated
  public long[] decomposeShortUri(URI uri) {

    if (uri == null) {
      throw new IllegalArgumentException("Uri cannot be null");
    }

    var fullPath = uri.getPath();
    var parts = fullPath.split("/");
    var path = parts[parts.length - 1];

    long[] results = new long[3];
    int resultCounter = 0;

    int index = 0;
    int lastAlphaIndex = 0;

    for (char c : path.toCharArray()) {

      String idStr = null;

      if (index == path.length() - 1 && Character.isDigit(c)) {
        // must be last id
        idStr = path.substring(lastAlphaIndex, path.length());
      }

      // look for delimiter
      if (Character.isAlphabetic(c)) {
        idStr = path.substring(lastAlphaIndex, index);
        lastAlphaIndex = index + 1;
      }

      if (idStr != null) {

        var lValue = Longs.tryParse(idStr);
        if (lValue == null) {
          raiseUriFormatError();
        }

        results[resultCounter++] = lValue;

        if (resultCounter == results.length) {
          break;
        }
      }

      index++;
    }

    if (resultCounter != results.length) {
      raiseUriFormatError();
    }

    return results;
  }

  /**
   * Encodes the given uri id into a shortened URI.
   *
   * @param uriId
   * @return full short uri
   */
  public URI shortenUri(long uriId) {
    String path = uriEncoder.encode(uriId);
    Optional<URI> optionalURI = UriParser.parse(applicationAuthority + "/" + path);
    return optionalURI.orElse(null);
  }

  /**
   * Method takes id's of URI parts, concatenates these, and return the new URI
   *
   * @param uriPartIds a array of id [scheme, authority, relativePath,...]
   * @return a new URI
   */
  @Deprecated
  public URI shortenUri(long[] uriPartIds) {

    if (uriPartIds.length < 3) {
      throw new IllegalArgumentException("You did not supply enough ID's");
    }

    StringBuilder builder = new StringBuilder(applicationAuthority);

    builder
        .append('/')
        .append(uriPartIds[0])
        .append(getUriChar())
        .append(uriPartIds[1])
        .append(getUriChar())
        .append(uriPartIds[2]);

    var newUriResult = UriParser.parse(builder.toString());
    return newUriResult.orElse(null);
  }

  private char getUriChar() {
    Random r = new Random();
    return (char) (r.nextInt(26) + 'a');
  }

  private void raiseUriFormatError() throws IllegalArgumentException {
    throw new IllegalArgumentException("The given short URI was not in a valid format");
  }
}
