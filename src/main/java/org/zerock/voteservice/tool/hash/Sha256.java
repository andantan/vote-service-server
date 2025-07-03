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
        String digest = String.format("\"%d\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"",
                userEntity.getUid(),
                userEntity.getUsername(),
                userEntity.getRealName(),
                userEntity.getEmail(),
                userEntity.getBirthDate().toString(),
                userEntity.getGender(),
                userEntity.getPhoneNumber()
        );

        return sum(digest);
    }

    public static String sum(String digest) {
        try {
            MessageDigest messageDigest = Sha256.getSha256MessageDigest();

            byte[] hashBytes = messageDigest.digest(digest.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
