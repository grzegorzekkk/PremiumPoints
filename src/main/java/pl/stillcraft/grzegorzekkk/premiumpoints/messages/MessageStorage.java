package pl.stillcraft.grzegorzekkk.premiumpoints.messages;

import pl.stillcraft.grzegorzekkk.premiumpoints.utils.Storage;

public class MessageStorage extends Storage {

    private static MessageStorage instance;

    private MessageStorage() {
    }

    public static MessageStorage getInstance() {
        if (instance == null) {
            instance = new MessageStorage();
        }
        return instance;
    }

    public String getMessage(Locale l) {
        return config.getString(l.getKey());
    }
}
