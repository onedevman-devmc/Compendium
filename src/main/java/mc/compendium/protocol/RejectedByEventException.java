package mc.compendium.protocol;

public class RejectedByEventException extends RuntimeException {
    public RejectedByEventException() { super(); }
    public RejectedByEventException(String message) { super(message); }
    public RejectedByEventException(Throwable cause) { super(cause); }
    public RejectedByEventException(String message, Throwable cause) { super(message, cause); }
}
