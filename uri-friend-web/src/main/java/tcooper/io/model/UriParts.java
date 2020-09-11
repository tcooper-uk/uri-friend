package tcooper.io.model;

public class UriParts {

  private final String scheme;
  private final String authority;
  private final String relativePath;

  public UriParts(String scheme, String authority, String relativePath) {
    this.scheme = scheme;
    this.authority = authority;
    this.relativePath = relativePath;
  }

  public String getScheme() {
    return scheme;
  }

  public String getAuthority() {
    return authority;
  }

  public String getRelativePath() {
    return relativePath;
  }
}
