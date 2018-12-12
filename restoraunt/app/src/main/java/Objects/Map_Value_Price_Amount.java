package Objects;

public class Map_Value_Price_Amount {

    float price;
    int amount;

    public Map_Value_Price_Amount(float price, int amount) {
        this.price = price;
        this.amount = amount;

    }
    public void addPrice(float price)
    {
        this.price += price;
    }
    public void addAmount()
    {
        amount++;
    }
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmout() {
        return amount;
    }

    public void setAmout(int amout) {
        this.amount = amout;
    }
}
