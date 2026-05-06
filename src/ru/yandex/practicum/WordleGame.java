package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.*;

public class WordleGame {
    private final String answer;
    private final WordleDictionary dictionary;
    private List<String> possibleWords;
    private final Set<String> usedHints = new HashSet<>();
    private int steps = 6;
    private boolean won = false;
    private final PrintWriter log;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        this.answer = dictionary.getRandomWord();
        this.possibleWords = new ArrayList<>(dictionary.getAllWords());
        log.println("Новая игра. Загадано слово: " + answer);
    }

    public String makeMove(String guess) throws WordleException {
        if (!guess.matches("^[а-яА-ЯёЁ]+$")) {
            throw new WordleException("Используйте только русские буквы!");
        }

        if (guess.length() != 5) {
            throw new InvalidWordLengthException("Слово должно состоять ровно из 5 букв.");
        }

        if (!dictionary.contains(guess)) {
            throw new WordNotFoundException("Слова '" + guess + "' нет в словаре!");
        }
        steps--;
        String feedback = WordleDictionary.compare(guess, answer);

        if (feedback.equals("+++++")) {
            won = true;
        }

        possibleWords = possibleWords.stream()
                .filter(w -> compare(guess, w).equals(feedback))
                .collect(Collectors.toList());

        return feedback;
    }

    private String compare(String guess, String target) {
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

    public String getHint() {
        List<String> unusedHints = possibleWords.stream()
                .filter(w -> !usedHints.contains(w))
                .collect(Collectors.toList());

        if (unusedHints.isEmpty()) {
            return "Варианты кончились!";
        }

        String hint = unusedHints.get(new Random().nextInt(unusedHints.size()));
        usedHints.add(hint);
        return hint;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isGameOver() {
        return steps <= 0 || won;
    }
}