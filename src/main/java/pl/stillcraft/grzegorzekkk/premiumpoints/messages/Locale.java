package pl.stillcraft.grzegorzekkk.premiumpoints.messages;

public enum Locale {
    PLUGIN_HEADER("header"),
    NO_PERMISSION("no_permission"),
    INVALID_ARG_ORDER("invalid_arg_order"),
    INVALID_ARG_AMOUNT("invalid_arg_amount"),
    PLAYER_NEED("player_needed"),
    CONFIG_RELOADED("config_reloaded"),
    COMMAND_NOT_FOUND("command_not_found"),
    SMS_SUCCESSFULL("sms_payment_succ"),
    SMS_FAILED("sms_payment_fail"),
    SMS_SUCC_BROADCAST("sms_payment_broadcast"),
    ADD_RECEIVED_SUCC("add_received_succ"),
    ADD_SEND_SUCC("add_send_succ"),
    HISTORY_EMPTY("history_empty"),
    TOP_EMPTY("top_empty"),
    COLLECT("collect"),
    COLLECT_SUCC("collect_succ"),
    COLLECT_FAIL("collect_fail"),
    HISTORY("history"),
    HISTORY_EACH("history_each"),
    PAY("pay"),
    PAY_SUCC("pay_succ"),
    PAY_FAIL("pay_fail"),
    PAY_NOT_ENOUGH("pay_not_enough"),
    PAY_RECEIVED("pay_received"),
    TOP("top"),
    TOP_EACH("top_each"),
    INFO("info"),
    PSC_INFO("psc"),
    PSC_INVALID("psc_invalid"),
    PSC_VALID("psc_valid"),
    SMS_INFO("sms"),
    SMS_INFO_CHOICE("sms_choice");

    private String key;

    Locale(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
