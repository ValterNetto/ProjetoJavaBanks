package com.testeJavaDeveloper.JavaBanks.verificate;

public class VerificadorCnpj {
    public static boolean cnpjValido(String cnpj) {
        // Remove caracteres não numéricos do CNPJ
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // CNPJ deve ter 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        if (isAllDigitsEqual(cnpj)) {
            return false;
        }

        // Verifica os dígitos verificadores do padrão CNPJ
        int[] digitos = new int[14];
        for (int i = 0; i < 14; i++) {
            digitos[i] = Character.getNumericValue(cnpj.charAt(i));
        }

        int digitoVerificador1 = calculaDigito(cnpj.substring(0, 12));
        int digitoVerificador2 = calculaDigito(cnpj.substring(0, 13));

        return digitos[12] == digitoVerificador1 && digitos[13] == digitoVerificador2;
    }

    private static int calculaDigito(String cnpjPart) {
        int[] digitos = new int[cnpjPart.length()];
        for (int i = 0; i < cnpjPart.length(); i++) {
            digitos[i] = Character.getNumericValue(cnpjPart.charAt(i));
        }

        int peso = 2;
        int soma = 0;

        for (int i = cnpjPart.length() - 1; i >= 0; i--) {
            soma += digitos[i] * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;

        return (resto < 2) ? 0 : 11 - resto;
    }
    private static boolean isAllDigitsEqual(String cnpj) {
        char firstDigit = cnpj.charAt(0);
        for (char digit : cnpj.toCharArray()) {
            if (digit != firstDigit) {
                return false;
            }
        }
        return true;
    }
}
