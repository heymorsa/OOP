import java.nio.file.Files;
import java.nio.file.Path;

class Main {
  static class Letter {
    char value;

    Letter(char value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return Character.toString(value);
    }
  }

  static class Word {
    Letter[] letters;

    Word(Letter[] letters) {
      this.letters = letters;
    }

    @Override
    public String toString() {
      String ret = "";
      for (Letter letter : letters) {
        ret += letter;
      }
      return ret;
    }
  }

  static class Punctuation {
    String value;

    Punctuation(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  static class Sentence {
    Word[] words;
    Punctuation[] punctuations;

    Sentence(Word[] words, Punctuation[] punctuations) {
      this.words = words;
      this.punctuations = punctuations;
    }

    @Override
    public String toString() {
      String ret = "";
      for (int i = 0; i < words.length; i++) {
        ret += words[i];
        ret += punctuations[i];
      }
      return ret;
    }
  }

  static class Text {
    Sentence[] sentences;

    Text(Sentence[] sentences) {
      this.sentences = sentences;
    }

    @Override
    public String toString() {
      String ret = "";
      for (Sentence sentence : sentences) {
        ret += sentence + "\n";
      }
      return ret;
    }
  }

  static class MyTextUtil {
    static final String VOWELS = "aeiouyAEIOUY";
    static final String DELIMS = " ,.:;!?\t";

    static void printWordsSortedByVowelCount(Text text) {
      Word[] words = getWords(text);
      bubblesort(words);
      for (Word word : words) {
        System.out.println("word: " + word);
      }
    }

    static void bubblesort(Word[] words) {
      for (int i = 0; i < words.length - 1; i++) {
        for (int j = 0; j < words.length - i - 1; j++) {
          if (getVowelCount(words[j]) > getVowelCount(words[j + 1])) {
            Word tmp = words[j];
            words[j] = words[j + 1];
            words[j + 1] = tmp;
          }
        }
      }
    }

    static int getVowelCount(Word word) {
      int ret = 0;
      for (int i = 0; i < word.letters.length; i++) {
        if (VOWELS.indexOf(word.letters[i].value) != -1) {
          ret++;
        }
      }
      return ret;
    }

    static Word[] getWords(Text text) {
      int wordCount = 0;
      for (Sentence sentence : text.sentences) {
        wordCount += sentence.words.length;
      }
      Word[] ret = new Word[wordCount];
      int next = 0;
      for (Sentence sentence : text.sentences) {
        for (Word word : sentence.words) {
          ret[next++] = word;
        }
      }
      return ret;
    }

    static Text stringToText(String str) {
      String[] lines = str.split("\r?\n");
      Sentence[] sentences = new Sentence[lines.length];
      for (int i = 0; i < lines.length; i++) {
        sentences[i] = new Sentence(getWords(lines[i]), getPunctuations(lines[i]));
      }
      return new Text(sentences);
    }

    static Word[] getWords(String str) {
      Word[] ret = new Word[getWordOrPunctuationCount(str, true)];
      fillWords(ret, str);
      return ret;
    }

    static Punctuation[] getPunctuations(String str) {
      Punctuation[] ret = new Punctuation[getWordOrPunctuationCount(str, false)];
      fillPunctuations(ret, str);
      return ret;
    }

    static int getWordOrPunctuationCount(String str, boolean word) {
      int ret = 0;

      boolean delim = true;

      for (int i = 0; i < str.length(); i++) {
        boolean foundDelim = DELIMS.indexOf(str.charAt(i)) != -1;
        if ((word && foundDelim) || (!word && !foundDelim)) {
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

    static void fillWords(Word[] words, String str) {
      int next = 0;
      int s = 0;

      boolean delim = true;

      for (int i = 0; i <= str.length(); i++) {
        if (i == str.length() || DELIMS.indexOf(str.charAt(i)) != -1) {
          if (!delim) {
            words[next++] = stringToWord(str.substring(s, i));
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

    static void fillPunctuations(Punctuation[] punctuations, String str) {
      int next = 0;
      int s = 0;

      boolean delim = true;

      for (int i = 0; i <= str.length(); i++) {
        if (i == str.length() || DELIMS.indexOf(str.charAt(i)) == -1) {
          if (!delim) {
            punctuations[next++] = new Punctuation(str.substring(s, i));
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

    static Word stringToWord(String str) {
      char[] chars = str.toCharArray();
      Letter[] letters = new Letter[chars.length];
      for (int i = 0; i < chars.length; i++) {
        letters[i] = new Letter(chars[i]);
      }
      return new Word(letters);
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("result:");
    Text text = MyTextUtil.stringToText(Files.readString(Path.of("input.txt")));
    MyTextUtil.printWordsSortedByVowelCount(text);
  }
}
