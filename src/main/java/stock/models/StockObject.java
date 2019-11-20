package stock.models;

/**
 * Abstract StockObject Class Supply, Demand
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.3
 * @since 11.19.2019
 */

abstract class StockObject {
    private double price;
    private int count;
    private StockPerson owner;

    /**
     * @param price int
     * @param count count
     * @param owner StockPerson
     */
    StockObject(double price, int count, StockPerson owner) {
        this.setPrice(price);
        this.setCount(count);
        this.setOwner(owner);
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price double
     */
    private void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count int
     */
    void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the owner of the supply
     */
    public StockPerson getOwner() {
        return owner;
    }

    /**
     * @param owner set the owner of the supply
     */
    private void setOwner(StockPerson owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "{\"price\": " + price + ", \"count\": " + count + "\"owner\": " + owner.getName() + "}";
    }


}
