package net.sokontokoro_factory.himono.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Authenticator {
    private static Logger logger = LogManager.getLogger(Authenticator.class);
    private static String ENCRYPT_ALGORITHM = "MD5";

    public String encryptPassword(String rawPassword){
        logger.entry(rawPassword);
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            messageDigest.update(rawPassword.getBytes());

            String encrypted = Base64.getEncoder().encodeToString(messageDigest.digest());

            return logger.traceExit(encrypted);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("暗号化出来ませんでした。");
        }
    }
}