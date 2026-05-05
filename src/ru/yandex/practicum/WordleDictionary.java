package ru.yandex.practicum;

import java.util.List;
import java.util.Random;

public class WordleDictionary {
    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    public List<String> getAllWords() {
        return words;
    }

    public static String compare(String guess, String target) {
        char[] res = new char[5];
        boolean[] targetUsed = new boolean[5];
        boolean[] guessUsed = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                res[i] = '+';
                targetUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (guessUsed[i]) continue;
            res[i] = '-';
            for (int j = 0; j < 5; j++) {
                if (!targetUsed[j] && guess.charAt(i) == target.charAt(j)) {
                    res[i] = '^';
                    targetUsed[j] = true;
                    break;
                }
            }
        }
        return new String(res);
    }
}