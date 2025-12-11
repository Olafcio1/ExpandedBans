package pl.olafcio.expandedbans;

public class XBLoadException extends RuntimeException {
    public XBLoadException(String message, Exception parent) {
        super(message, parent);
    }
}
