package org.zerock.voteservice.tool.hash;

import lombok.extern.log4j.Log4j2;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class Sha256 {
    private static MessageDigest getSha256MessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

    public static String sum(UserEntity userEntity) {
        String userHash = "";

        try {
            MessageDigest digest = Sha256.getSha256MessageDigest();
            String dataToHash = String.format("\"%d\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"",
                    userEntity.getUid(),
                    userEntity.getUsername(),
                    userEntity.getRealName(),
                    userEntity.getEmail(),
                    userEntity.getBirthDate().toString(),
                    userEntity.getGender(),
                    userEntity.getPhoneNumber()
            );  // Example: "2719847284"|"user123"|"홍길동"|"userMail@mail.com"|"20001209"|"M"|"01012345678"

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

            log.debug("UserDigest: {}, UserHash: {}", dataToHash, userHash);
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
        }


        return userHash;
    }
}
