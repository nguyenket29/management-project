package com.hau.ketnguyen.it.common.constant;

public class Constants {
    public static final String ANONYMOUS = "Anonymous";
    public static final String ADMIN = "ADMIN";

    public static class StatusCode {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "FAILED";
    }

    public static class Notification {
        public enum Status {
            SENT, UNSENT
        }

        public enum Type {
            COMMENT
        }
    }

    public static class Comment {
        public enum Status {
            REMIND, REPLY
        }
    }

    public static final String ATTRIBUTE_DELIMITER = "#_@_@_#";
    public static final String FORMAT_DATE = "dd/MM/YYYY";
}
