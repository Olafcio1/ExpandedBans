package pl.olafcio.expandedbans;

public class XBDatabaseException extends RuntimeException {
    public XBDatabaseException(String message, Exception parent) {
        super(message, parent);
    }
}
