package stock.models;

import com.google.common.hash.Hashing;
import stock.core.StockMarketSingleton;

import java.nio.charset.Charset;
import java.sql.Timestamp;

/** Abstract StockPerson Class Seller, Buyer
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @since 11.19.2019
 * @version 0.0.3
 */

public abstract class StockPerson {
    final StockMarketSingleton stockMarket;
    private String identifier;
    private String name;

    /**
     *
     * @param instanceIdentifier not unique
     * @param stockMarket StockMarketSingleton
     */
    StockPerson(String instanceIdentifier, StockMarketSingleton stockMarket) {
        this.identifier = Hashing.sha256()
                .hashString(instanceIdentifier + new Timestamp(System.currentTimeMillis()).getTime(), Charset.forName("UTF-8"))
                .toString();
        this.name = instanceIdentifier;
        this.stockMarket = stockMarket;
    }

    /**
     *
     * @return id {unique}
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     *
     * @return name {!unique}
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param cost double
     */
    abstract void notify(double cost);


}
