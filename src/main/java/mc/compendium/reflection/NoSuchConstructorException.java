package mc.compendium.reflection;

public class NoSuchConstructorException extends ReflectiveOperationException {

    public NoSuchConstructorException() { super(); }
    public NoSuchConstructorException(String message) { super(message); }

}
