package com.ciandt.nextgen25.usermanagement.service;

import java.util.regex.Pattern;

public class EmailValidator {

    // Regex para validar o formato do email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Método para verificar se o email é válido
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Método para verificar se o domínio é ciandt
    public static boolean hasCiandtDomain(String email) {
        if (isValidEmail(email)) {
            String domain = email.substring(email.indexOf('@') + 1);
            return domain.equalsIgnoreCase("ciandt.com");
        }
        return false; // Se o email não for válido, retorna falso
    }

    public static void main(String[] args) {
        String email = "exemplo@ciandt.com"; // Substitua pelo email que deseja verificar

        // Verificação se o email é válido
        if (isValidEmail(email)) {
            System.out.println("O email é válido.");
        } else {
            System.out.println("O email é inválido.");
        }

        // Verificação se o email pertence ao domínio ciandt
        if (hasCiandtDomain(email)) {
            System.out.println("O email pertence ao domínio ciandt.com.");
        } else {
            System.out.println("O email não pertence ao domínio ciandt.com.");
        }
    }
}
