import java.nio.file.Files;
import java.nio.file.Path;

class Main {
  static class MyTextUtil {
    static final String VOWELS = "aeiouyAEIOUY";
    static final String DELIMS = " ,.:;!?\n\t";

    static void printWordsSortedByVowelCount(String text) {
      String[] words = getWords(text);
      bubblesort(words);
      for (String word : words) {
        System.out.println("word: " + word);
      }
    }

    static void bubblesort(String[] words) {
      for (int i = 0; i < words.length - 1; i++) {
        for (int j = 0; j < words.length - i - 1; j++) {
          if (getVowelCount(words[j]) > getVowelCount(words[j + 1])) {
            String tmp = words[j];
            words[j] = words[j + 1];
            words[j + 1] = tmp;
          }
        }
      }
    }

    static int getVowelCount(String str) {
      int ret = 0;
      for (int i = 0; i < str.length(); i++) {
        if (VOWELS.indexOf(str.charAt(i)) != -1) {
          ret++;
        }
      }
      return ret;
    }

    static String[] getWords(String str) {
      String[] ret = new String[getWordCount(str)];
      fillWords(ret, str);
      return ret;
    }

    static int getWordCount(String str) {
      int ret = 0;

      boolean delim = true;

      for (int i = 0; i < str.length(); i++) {
        if (DELIMS.indexOf(str.charAt(i)) != -1) {
          if (delim) {
            ret++;
            delim = false;
          }
        } else {
          if (!delim) {
            delim = true;
          }
        }
      }

      return ret;
    }

    static void fillWords(String[] words, String str) {
      int next = 0;
      int s = 0;

      boolean delim = true;

      for (int i = 0; i <= str.length(); i++) {
        if (i == str.length() || DELIMS.indexOf(str.charAt(i)) != -1) {
          if (!delim) {
            words[next++] = str.substring(s, i);
            delim = true;
          }
        } else {
          if (delim) {
            s = i;
            delim = false;
          }
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("result:");
    String text = Files.readString(Path.of("input.txt"));
    MyTextUtil.printWordsSortedByVowelCount(text);
  }
}
