package pl.stillcraft.grzegorzekkk.premiumpoints.payments;

import java.util.Date;

/**
 * Class represents database entry of one payment.
 */
public class Payment {
    private int id;
    private String user;
    private int pp;
    private String type;
    private Date time;
    private boolean isCollected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = new Date(time);
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
