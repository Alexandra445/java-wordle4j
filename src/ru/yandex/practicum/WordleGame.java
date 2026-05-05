package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WordleGame {
    private final String answer;
    private final WordleDictionary dictionary;
    private List<String> possibleWords;
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
        if (guess.length() != 5) throw new WordleException("Нужно 5 букв!");
        if (!dictionary.contains(guess)) throw new WordleException("Такого слова нет в словаре!");

        steps--;
        String feedback = compare(guess, answer);

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
        if (possibleWords.isEmpty()) return "Нет вариантов...";
        return possibleWords.get(new Random().nextInt(possibleWords.size()));
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