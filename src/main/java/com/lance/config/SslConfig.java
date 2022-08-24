package com.lance.config;

import com.lance.util.AES;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


@Configuration
public class SslConfig {

    private static final Logger logger = LogManager.getLogger(SslConfig.class);

    private final String keyPath;
    private final String keyStorePath;
    private final String keyStore;
    private final String aesKey;

    @Autowired
    public SslConfig(
            @Value("${server.ssl.key-store-password-path}") String keyStorePath,
            @Value("${server.ssl.key-password-path}") String keyPath,
            @Value("${server.ssl.key-store}") String keyStore,
            @Value("${server.ssl.enabled-protocols}") String protocol,
            @Value("${aes}") String aesKey,
            Environment env) {

        this.keyStorePath = keyStorePath;
        this.keyPath = keyPath;
        this.keyStore = keyStore;
        this.aesKey = aesKey;

        logger.info("ssl https config start");
        logger.info("keyStorePath: {}", keyStorePath);
    }

    /**
     * Set SSL keystore to ServerProperties
     *
     * @return ServerProperties
     */
    @Bean
    @Primary
    public ServerProperties serverProperties() {
        final ServerProperties serverProperties = new ServerProperties();
        final Ssl ssl = new Ssl();


        String keyPassword = decryptPassword(keyPath);
        String keystorePassword = decryptPassword(keyStorePath);

        ssl.setKeyPassword(keyPassword);
        ssl.setKeyStorePassword(keystorePassword);

        System.setProperty("server.ssl.key-store-password", keystorePassword);
        System.setProperty("server.ssl.key-password", keyPassword);
        System.setProperty("server.ssl.key-store", getProjectAbsolutePath() + keyStore);

        serverProperties.setSsl(ssl);
        logger.info("ssl https config done");
        return serverProperties;
    }

    /**
     * Internal method to read encrypted password to the keystore
     *
     * @param path: File path of password
     * @return String
     */
    private String decryptPassword(String path) {
        try {
            String filePath = getProjectAbsolutePath() + path;
            logger.info("filePath: {}", filePath);
            String content = Files.readString(Paths.get(filePath), StandardCharsets.US_ASCII);
            return AES.decrypt(content, aesKey);

        } catch (Exception ex) {
            logger.error("Error reading file: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    private static String getProjectAbsolutePath() {
        return Paths.get("")
                .toAbsolutePath()
                .toString();
    }
}
