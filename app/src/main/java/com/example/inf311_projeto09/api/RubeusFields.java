package com.example.inf311_projeto09.api;

public class RubeusFields {
    public enum UserEvent {
        TITLE("campopersonalizado_6_compl_proc"),
        DESCRIPTION("campopersonalizado_15_compl_proc"),
        TYPE("campopersonalizado_24_compl_proc"),
        VERIFICATION_TYPE("campopersonalizado_12_compl_proc"),
        CHECK_IN_CODE("campopersonalizado_22_compl_proc"),
        LOCATION("campopersonalizado_23_compl_proc"),
        AUTO_CHECK("campopersonalizado_14_compl_proc"),
        BEGIN_TIME("campopersonalizado_10_compl_proc"),
        END_TIME("campopersonalizado_11_compl_proc"),
        CHECK_IN_ENABLED("campopersonalizado_17_compl_proc"),
        CHECK_OUT_ENABLED("campopersonalizado_18_compl_proc"),
        CHECK_IN_TIME("campopersonalizado_19_compl_proc"),
        CHECK_OUT_TIME("campopersonalizado_20_compl_proc");

        private final String identifier;

        UserEvent(final String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }

    public enum UserAccount {
        PASSWORD("campopersonalizado_2_compl_cont"),
        TYPE("campopersonalizado_3_compl_cont"),
        ENABLE_NOTIFICATIONS("campopersonalizado_5_compl_cont");

        private final String identifier;

        UserAccount(final String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }
}
