package mc.compendium.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class FieldUtil extends AbstractReflectObjectUtil<Field> {

    private static final FieldUtil INSTANCE = new FieldUtil();
    public static FieldUtil getInstance() { return INSTANCE; }

    //

    private FieldUtil() {}

    //

    @Override
    public <T> Field[] getAll(Class<T> target, boolean declared) {
        return declared ? target.getDeclaredFields() : target.getFields();
    }

    //

    public <V, T> V getValue(Object target, Predicate<Field> match) throws NoSuchFieldException, IllegalAccessException {
        try {
            return this.getValue((Class<T>) target.getClass(), target, match);
        } catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> V getValue(Class<T> base, Object target, Predicate<Field> match) throws NoSuchFieldException, IllegalAccessException, IncompatibleInheritanceException {
        V result;

        try { result = this.getValue(base, target, match, false); }
        catch(NoSuchFieldException ignored) { result = this.getValue(base, target, match, true); }

        return result;
    }

    public <V, T> V getValue(Object target, Predicate<Field> match, boolean declared) throws NoSuchFieldException, IllegalAccessException {
        try {
            return this.getValue((Class<T>) target.getClass(), target, match, declared);
        } catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> V getValue(Class<T> base, Object target, Predicate<Field> match, boolean declared) throws NoSuchFieldException, IllegalAccessException, IncompatibleInheritanceException {
        if(target != null && !base.isAssignableFrom(target.getClass())) throw new IncompatibleInheritanceException(base, target);

        Field field = this.get(base, match, declared);
        if(field == null) throw new NoSuchFieldException("Unable to find a matching field.");

        return this.getValue(target, field);
    }

    public <V, T> V getValue(Object target, Field field) throws IllegalAccessException {
        V result;

        try { result = (V) field.get(target); }
        catch (IllegalAccessException ignored) {
            field.setAccessible(true);
            result = (V) field.get(target);
        }

        return result;
    }

    //

    public <V, T> void setValue(Object target, Predicate<Field> match, V value) throws NoSuchFieldException, IllegalAccessException {
        try { this.setValue((Class<T>) target.getClass(), target, match, value, false); }
        catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> void setValue(Class<T> base, Object target, Predicate<Field> match, V value) throws NoSuchFieldException, IllegalAccessException, IncompatibleInheritanceException {
        try { this.setValue(base, target, match, value, false); }
        catch(NoSuchFieldException ignored) { this.setValue(base, target, match, value, true); }
    }

    public <V, T> void setValue(Object target, Predicate<Field> match, V value, boolean declared) throws NoSuchFieldException, IllegalAccessException {
        try { this.setValue((Class<T>) target.getClass(), target, match, value, declared); }
        catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> void setValue(Class<T> base, Object target, Predicate<Field> match, V value, boolean declared) throws NoSuchFieldException, IllegalAccessException, IncompatibleInheritanceException {
        if(target != null && !base.isAssignableFrom(target.getClass())) throw new IncompatibleInheritanceException(base, target);

        Field field = this.get(base, match, declared);
        if(field == null) throw new NoSuchFieldException("Unable to find a matching field.");

        this.setValue(target, field, value);
    }

    public <V> void setValue(Object target, Field field, V value) throws IllegalAccessException {
        try { field.set(target, value); }
        catch (IllegalAccessException ignored) {
            field.setAccessible(true);
            field.set(target, value);
        }
    }

    //

    public <V, T> void changeFinal(Object target, Predicate<Field> match, V value) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        try { this.changeFinal((Class<T>) target.getClass(), target, match, value); }
        catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> void changeFinal(Class<T> base, Object target, Predicate<Field> match, V value) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IncompatibleInheritanceException {
        try { this.changeFinal(base, target, match, value, false); }
        catch (NoSuchFieldException ignored) { this.changeFinal(base, target, match, value, true); }
    }

    public <V, T> void changeFinal(Object target, Predicate<Field> match, V value, boolean declared) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        try { this.changeFinal((Class<T>) target.getClass(), target, match, value, declared); }
        catch (IncompatibleInheritanceException e) { throw new RuntimeException(e); }
    }

    public <V, T> void changeFinal(Class<T> base, Object target, Predicate<Field> match, V value, boolean declared) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IncompatibleInheritanceException {
        if(target != null && !base.isAssignableFrom(target.getClass())) throw new IncompatibleInheritanceException(base, target);
        Field field = this.get(base, match, declared);
        if(field == null) throw new NoSuchFieldException("Unable to find a matching field.");
        this.changeFinal(target, field, value);
    }

    public <V> void changeFinal(Object target, Field field, V value) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
//        field.setAccessible(true);
//
//        Object fieldBase = unsafe.staticFieldBase(field);
//        long fieldOffset = unsafe.staticFieldOffset(field);
//
//        unsafe.putObject(fieldBase, fieldOffset, value);

        //

        Method offset = Class.forName("jdk.internal.misc.Unsafe").getMethod("objectFieldOffset", Field.class);
        UnsafeUtil.unsafe().putBoolean(offset, 12, true);

        UnsafeUtil.unsafe().putObject(target, (long) offset.invoke(UnsafeUtil.internalUnsafe(), field), value);
    }

}
