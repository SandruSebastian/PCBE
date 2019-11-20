package stock.models;

import com.google.common.hash.Hashing;
import stock.core.market.StockMarket;

import java.nio.charset.Charset;
import java.sql.Timestamp;

/**
 * Abstract StockPerson Class Seller, Buyer
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */

public abstract class StockPerson {
    final StockMarket stockMarket;
    private String identifier;
    private String name;

    /**
     * @param instanceIdentifier not unique
     * @param stockMarket        StockMarketSingleton
     */
    StockPerson(String instanceIdentifier, StockMarket stockMarket) {
        this.identifier = Hashing.sha256()
                .hashString(instanceIdentifier + new Timestamp(System.currentTimeMillis()).getTime(), Charset.forName("UTF-8"))
                .toString();
        this.name = instanceIdentifier;
        this.stockMarket = stockMarket;
    }

    /**
     * @return id {unique}
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return name {!unique}
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param cost double
     */
    abstract void notify(double cost);


}
