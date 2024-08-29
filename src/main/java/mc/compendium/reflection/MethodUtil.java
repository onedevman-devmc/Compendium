package mc.compendium.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class MethodUtil extends AbstractReflectObjectUtil<Method> {

    private static final MethodUtil INSTANCE = new MethodUtil();
    public static MethodUtil getInstance() { return INSTANCE; }

    //

    private MethodUtil() {}

    //

    @Override
    public <T> Method[] getAll(Class<T> target, boolean declared) {
        return declared ? target.getDeclaredMethods() : target.getMethods();
    }

    //

    public <V, T> V invoke(Object target, Predicate<Method> match, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try {
            return this.invoke((Class<T>) target.getClass(), target, match, args);
        } catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> V invoke(Class<T> base, Object target, Predicate<Method> match, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IncompatibleInheritanceException {
        V result;

        try { result = this.invoke(base, target, match, false, args); }
        catch (NoSuchMethodException ignored) { result = this.invoke(base, target, match, true, args); }

        return result;
    }

    public <V, T> V invoke(Object target, Predicate<Method> match, boolean declared, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            return this.invoke((Class<T>) target.getClass(), target, match, false, args);
        } catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> V invoke(Class<T> base, Object target, Predicate<Method> match, boolean declared, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IncompatibleInheritanceException {
        if(!base.isAssignableFrom(target.getClass())) throw new IncompatibleInheritanceException(base, target);

        Method method = this.get(base, match, declared);
        if(method == null) throw new NoSuchMethodException("Unable to find a matching method.");

        return this.invoke(target, method, args);
    }

    public <V> V invoke(Object target, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        V result;

        try { result = (V) method.invoke(target, args); }
        catch (IllegalAccessException e) {
            method.setAccessible(true);
            result = (V) method.invoke(target, args);
        }

        return result;
    }

}
