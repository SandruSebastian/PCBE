package stock.exceptions;

/**
 * ErrorMessage Wrapper
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.3
 * @since 11.19.2019
 */
@Deprecated
public class ErrorMessage {
    public String setMessage(String message) {
        return ("\n"
                + "at "
                + System.getProperty("user.dir")
                + "\\stock\\main\\java\\" + message);
    }
}
