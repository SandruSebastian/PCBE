package stock.exceptions;

/**
 * ErrorMessage Wrapper
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */

public abstract class StockMarketException extends Exception {
    StockMarketException(String errorMessage) {
        super(errorMessage);
    }
}
