package ru.yandex.practicum;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String filePath) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = normalize(line);
                if (normalized.length() == 5) {
                    words.add(normalized);
                }
            }
        } catch (IOException e) {
            log.println("Ошибка при загрузке словаря: " + e.getMessage());
            throw e;
        }

        if (words.isEmpty()) {
            throw new RuntimeException("Словарь пуст или не содержит подходящих слов.");
        }

        return new WordleDictionary(words);
    }

    private String normalize(String word) {
        return word.trim().toLowerCase().replace('ё', 'е');
    }
}