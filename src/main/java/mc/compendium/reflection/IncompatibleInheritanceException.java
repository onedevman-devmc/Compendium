package mc.compendium.reflection;

public class IncompatibleInheritanceException extends Exception {

    public IncompatibleInheritanceException(Class<?> base, Object target) {
        super("The target must inherit from the base class (base: " + base + "; object: " + target + ")");
    }

}
