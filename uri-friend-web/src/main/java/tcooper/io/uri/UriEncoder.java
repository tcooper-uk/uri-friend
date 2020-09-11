package tcooper.io.uri;

import com.google.inject.Singleton;

/**
 * This service deals with encoding an ID to a short for path
 * to be used in a short URI.
 */
@Singleton
public class UriEncoder {

  private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private final char[] allowedCharacters = allowedString.toCharArray();
  private final int base = allowedCharacters.length;

  public String encode(long input){
    var encodedString = new StringBuilder();

    if(input == 0) {
      return String.valueOf(allowedCharacters[0]);
    }

    while (input > 0) {
      encodedString.append(allowedCharacters[(int) (input % base)]);
      input = input / base;
    }

    return encodedString.reverse().toString();
  }

  public long decode(String input) {
    var characters = input.toCharArray();
    var length = characters.length;

    int decoded = 0;

    //counter is used to avoid reversing input string
    int counter = 1;
    for (char character : characters) {
      decoded += allowedString.indexOf(character) * Math.pow(base, length - counter);
      counter++;
    }
    return decoded;
  }

}
