package ru.yandex.practicum;

class WordNotFoundException extends WordleException {
    public WordNotFoundException(String word) {
        super("Слово '" + word + "' отсутствует в словаре.");
    }
}