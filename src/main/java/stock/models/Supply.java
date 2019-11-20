package stock.models;

/**
 * Abstract StockPerson Class Seller, Buyer
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.3
 * @since 11.19.2019
 */

public class Supply extends StockObject {

    /**
     * @param price double
     * @param count int
     * @param owner Seller
     */
    Supply(double price, int count, Seller owner) {
        super(price, count, owner);

    }

    /**
     * @param count how much to consume
     */
    public void consume(int count) {
        this.setCount(getCount() - count);
        this.getOwner().notify(count * this.getPrice());
    }


}
