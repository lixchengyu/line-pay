package com.lance.util;


public final class MsgConf {

    public enum ResponseCode {
        ER0001("ER0001", "Unexpected error"),
        ER0101("ER0101", "No response from LINE Pay - post request"),
        ER0102("ER0102", "No response from LINE Pay - post confirm"),
        ER0103("ER0103", "No response from LINE Pay - post refund"),
        ER0104("ER0104", "No response from LINE Pay - get payment");

        private final String code;
        private final String message;

        ResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
