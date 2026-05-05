package ru.yandex.practicum;

class InvalidWordLengthException extends WordleException {
    public InvalidWordLengthException() {
        super("Слово должно состоять ровно из пяти букв.");
    }
}
