package com.example.inf311_projeto09.api;

public class RubeusFields {
    public enum UserEvent {
        TITLE("campopersonalizado_6_compl_proc"),
        DESCRIPTION("campopersonalizado_15_compl_proc"),
        TYPE("campopersonalizado_9_compl_proc"),
        VERIFICATION_TYPE("campopersonalizado_12_compl_proc"),
        CHECK_IN_CODE("campopersonalizado_22_compl_proc"),
        CHECK_OUT_CODE("campopersonalizado_23_compl_proc"),
        REFER_EVENT_ID("campopersonalizado_21_compl_proc"),
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
}
