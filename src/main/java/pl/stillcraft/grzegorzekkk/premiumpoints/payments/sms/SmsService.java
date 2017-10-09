package pl.stillcraft.grzegorzekkk.premiumpoints.payments.sms;

/**
 * Class represents one sms provider service
 */
public class SmsService {

    private int points;
    private int number;
    private String prefix;
    private double price;

    public SmsService(int points, int number, String prefix, double price) {
        this.setPoints(points);
        this.setNumber(number);
        this.setPrefix(prefix);
        this.setPrice(price);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
