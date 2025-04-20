package br.com.fecapccp.uber_saferide.utils;

public class CaesarCipher {

    public static String encrypt(String input, int shift) {
        StringBuilder encrypted = new StringBuilder();
        shift = shift % 26;

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + shift) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();
    }

    public static String decrypt(String input, int shift) {
        return encrypt(input, 26 - (shift % 26));
    }
}
