package org.zerock.voteservice.tool.random;

import java.security.SecureRandom;

public class StringGenerator {
    public static String generateRandomString(String digest, int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(digest.length());
            code.append(digest.charAt(randomIndex));
        }

        return code.toString();
    }
}
