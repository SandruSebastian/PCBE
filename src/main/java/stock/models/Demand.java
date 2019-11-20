package stock.models;

/** Demand Class
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @since 11.19.2019
 * @version 0.0.3
 *
 */

public class Demand extends StockObject {

    /**
     *
     * @param price double
     * @param count int
     * @param owner Buyer
     */
    public Demand(double price, int count, Buyer owner) {
        super(price, count, owner);
    }

    /**
     *
     * @param count how much to consume
     */
    public void consume(int count) {
        this.setCount(this.getCount() - count);
        this.getOwner().notify(count);
    }

}
