package pl.stillcraft.grzegorzekkk.premiumpoints.configuration.email;

public enum EmailSettings {
    SMTP("email.smtp"),
    PORT("email.port"),
    USERNAME("email.username"),
    PASSWORD("email.password"),
    RECEIVER("email.receiver");

    private String key;

    EmailSettings(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
