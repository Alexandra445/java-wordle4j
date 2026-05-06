package ru.yandex.practicum;

import java.io.*;
import java.util.Scanner;

public class Wordle {
    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", true))) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);

            WordleDictionary dictionary = loader.load("words_ru.txt");

            WordleGame game = new WordleGame(dictionary, log);
            runGame(game, log);

        } catch (Exception e) {
            System.err.println("Ошибка при запуске! Подробности в файле wordle.log");
            try (PrintWriter logWriter = new PrintWriter(new FileWriter("wordle.log", true))) {
                logWriter.println("\n--- КРИТИЧЕСКАЯ ОШИБКА ---");
                e.printStackTrace(logWriter);
                logWriter.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private static void runGame(WordleGame game, PrintWriter log) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Игра Wordle ---");
        System.out.println("Введите слово из 5 букв или нажмите Enter для подсказки.");

        while (!game.isGameOver()) {
            System.out.print("\nПопытка (" + game.getSteps() + "): ");
            String input = scanner.nextLine().trim().toLowerCase().replace('ё', 'е');

            if (input.isEmpty()) {
                System.out.println("Подсказка компьютера: " + game.getHint());
                continue;
            }

            try {
                String feedback = game.makeMove(input);
                System.out.println(input);
                System.out.println(feedback);
            } catch (WordleException e) {
                System.out.println("Внимание: " + e.getMessage());
            }
        }

        if (game.isWon()) {
            System.out.println("\nВы угадали слово! Поздравляем.");
        } else {
            System.out.println("\nХоды закончились. Было загадано: " + game.getAnswer());
        }
    }
}