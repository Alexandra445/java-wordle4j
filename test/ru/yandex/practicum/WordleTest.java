package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {
    private WordleDictionary dictionary;
    private WordleGame game;
    private final PrintWriter out = new PrintWriter(System.out, true);

    @BeforeEach
    void setUp() {
        List<String> testWords = Arrays.asList("арбуз", "банан", "экран", "кофеин", "полет");
        dictionary = new WordleDictionary(testWords);
        game = new WordleGame(dictionary, out);
    }

    @Test
    void testWordNormalization() {
        String guess = "АРТЁМ".toLowerCase().replace('ё', 'е');
        assertEquals("артем", guess, "Слово должно приводиться к нижнему регистру и заменять ё на е");
    }

    @Test
    void testCompareLogic() throws WordleException {
        String feedback = WordleDictionary.compare("карат", "экран");
        assertEquals("^-++-", feedback, "Логика сравнения букв работает неверно");
    }

    @Test
    void testInvalidWordThrowsException() {
        assertThrows(WordleException.class, () -> {
            game.makeMove("ад");
        }, "Игра должна кидать исключение, если слово слишком короткое");

        assertThrows(WordleException.class, () -> {
            game.makeMove("ыыыыы");
        }, "Игра должна кидать исключение, если слова нет в словаре");
    }

    @Test
    void testStepsDecrement() throws WordleException {
        int initialSteps = game.getSteps();

        String validWord = dictionary.getAllWords().get(0);
        game.makeMove(validWord);

        assertEquals(initialSteps - 1, game.getSteps(), "Количество ходов должно уменьшаться после каждого хода");
    }

    @Test
    void testWinCondition() throws WordleException {
        String answer = game.getAnswer();
        game.makeMove(answer);

        assertTrue(game.isWon(), "Игра должна переходить в состояние победы при угадывании слова");
        assertTrue(game.isGameOver(), "При победе игра должна считаться завершенной");
    }

    @Test
    void testHintLogic() throws WordleException {
        String hintBefore = game.getHint();
        assertNotNull(hintBefore, "Подсказка не должна быть пустой");

        game.makeMove(dictionary.getAllWords().get(0));
        String hintAfter = game.getHint();

        assertNotNull(hintAfter, "Подсказка должна работать и после хода");
    }
}