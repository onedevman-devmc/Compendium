package mc.compendium.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

public class ConstructorUtil extends AbstractReflectObjectUtil<Constructor<?>> {

    private static final ConstructorUtil INSTANCE = new ConstructorUtil();
    public static ConstructorUtil getInstance() { return INSTANCE; }

    //

    @Override
    public <T> Constructor<T>[] getAll(Class<T> target, boolean declared) {
        return (Constructor<T>[]) (declared ? target.getDeclaredConstructors() : target.getConstructors());
    }

    //

    public <T> T newInstance(Object target, Predicate<Constructor<?>> match, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
        return this.newInstance((Class<T>) target.getClass(), match, args);
    }

    public <T> T newInstance(Class<T> target, Predicate<Constructor<?>> match, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
        T result;

        try { result = this.newInstance(target, match, false, args); }
        catch(NoSuchConstructorException ignored) { result = this.newInstance(target, match, true, args); }

        return result;
    }

    public <T> T newInstance(Object target, Predicate<Constructor<?>> match, boolean declared, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
        Class<T> targetClass = (Class<T>) target.getClass();
        return this.newInstance(targetClass, match, declared, args);
    }

    public <T> T newInstance(Class<T> target, Predicate<Constructor<?>> match, boolean declared, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
        Constructor<T> constructor = (Constructor<T>) this.get(target, match, declared);
        if(constructor == null) throw new NoSuchConstructorException("Unable to find a matching constructor.");

        return this.newInstance(constructor, args);
    }

    public <T> T newInstance(Constructor<T> constructor, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        T result;

        try { result = constructor.newInstance(args); }
        catch(IllegalAccessException ignored) {
            constructor.setAccessible(true);
            result = constructor.newInstance(args);
        }

        return result;
    }

}
