package stock.exceptions;

/** ErrorMessage Wrapper
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @since 11.19.2019
 * @version 0.0.3
 *
 */

public class StockMarketAlreadyRunningException extends Exception {
    public StockMarketAlreadyRunningException(String errorMessage) {
        super(errorMessage);
    }
}