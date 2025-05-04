package br.fecap.pi.saferide_motorista.utils;

public class CaesarCipher {

    public static String encrypt(String input, int shift) {
        if (input == null) {
            return null;
        }

        StringBuilder encrypted = new StringBuilder();
        shift = shift % 26; // Normaliza o shift para letras
        int numberShift = shift % 10; // Shift separado para números

        for (char c : input.toCharArray()) {
            if (isBasicLatinLetter(c)) {
                // Aplica cifra apenas a letras A-Z e a-z sem acentos
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) (base + (c - base + shift + 26) % 26));
            } else if (Character.isDigit(c)) {
                // Aplica cifra a números
                char base = '0';
                encrypted.append((char) (base + (c - base + numberShift + 10) % 10));
            } else {
                // Mantém todos os outros caracteres inalterados (incluindo acentos)
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }

    public static String decrypt(String input, int shift) {
        if (input == null) {
            return null;
        }

        StringBuilder decrypted = new StringBuilder();
        shift = shift % 26;
        int numberShift = shift % 10;

        for (char c : input.toCharArray()) {
            if (isBasicLatinLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                decrypted.append((char) (base + (c - base - shift + 26) % 26));
            } else if (Character.isDigit(c)) {
                char base = '0';
                decrypted.append((char) (base + (c - base - numberShift + 10) % 10));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }

    private static boolean isBasicLatinLetter(char c) {
        // Verifica se é letra A-Z ou a-z sem acentos
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}