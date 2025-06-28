package org.zerock.voteservice.tool.hash;

import lombok.extern.log4j.Log4j2;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class Sha256 {
    private static MessageDigest getSha256MessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

    public static String generateUserHash(Integer userId, UserRegisterRequestDto dto) {
        String userHash = "";

        try {
            MessageDigest digest = Sha256.getSha256MessageDigest();
            String dataToHash = String.format("\"%d\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"",
                    userId,
                    dto.getUsername(),
                    dto.getRealName(),
                    dto.getEmail(),
                    dto.getResidentRegistrationNumberPart(),
                    dto.getPhoneNumber()
            );  // Example: 2719847284|user123|홍길동|userMail@mail.com|001209-3|01012345678

            byte[] hashBytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            userHash = hexString.toString();

            log.info("UserDigest: {}, UserHash: {}", dataToHash, userHash);
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
        }


        return userHash;
    }
}
