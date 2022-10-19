package com.hau.ketnguyen.it.config.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationPropertise {
    private String defaultPassword;
    private String defaultPasswordDecode;
    private Mail mail;
    private StorageFile storageFile;

    @Data
    public static class Mail {
        private boolean enable;
        private String[] addEmailsInToAddress;
        private String[] addEmailsInCcAddress;
    }

    @Data
    public static class StorageFile{
        private String directory;
        private String font;
    }
}
