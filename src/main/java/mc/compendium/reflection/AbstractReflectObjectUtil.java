package mc.compendium.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractReflectObjectUtil<ReflectObjectType> {

    protected AbstractReflectObjectUtil() {}

    //

    public abstract <T> ReflectObjectType[] getAll(Class<T> target, boolean declared);

    public <T> ReflectObjectType[] getAll(Class<T> target) {
        ReflectObjectType[] reflectObjects = this.getAll(target, false);
        ReflectObjectType[] declaredReflectObjects = this.getAll(target, true);

        ReflectObjectType[] result = Arrays.copyOf(reflectObjects, reflectObjects.length + declaredReflectObjects.length);
        System.arraycopy(declaredReflectObjects, 0, result, reflectObjects.length, declaredReflectObjects.length);

        return result;
    }

    //

    public ReflectObjectType get(Object target, Predicate<ReflectObjectType> match) {
        return this.get(target.getClass(), match);
    }

    public <T> ReflectObjectType get(Class<T> target, Predicate<ReflectObjectType> match) {
        ReflectObjectType method = this.get(target, match, false);
        return method == null ? this.get(target, match, true) : method;
    }

    public ReflectObjectType get(Object target, Predicate<ReflectObjectType> match, boolean declared) {
        return this.get(target.getClass(), match, declared);
    }

    public <T> ReflectObjectType get(Class<T> target, Predicate<ReflectObjectType> match, boolean declared) {
        ReflectObjectType result = null;
        ReflectObjectType[] methods = this.getAll(target, declared);

        for(int i = 0; i < methods.length && result == null; ++i) {
            ReflectObjectType method = methods[i];
            if(match.test(method)) result = method;
        }

        return result;
    }

    //

    public void forEach(Object target, Consumer<ReflectObjectType> action) {
        this.forEach(target.getClass(), action);
    }

    public void forEach(Object target, Consumer<ReflectObjectType> action, boolean declared) {
        this.forEach(target.getClass(), action, declared);
    }

    public <T> void forEach(Class<T> target, Consumer<ReflectObjectType> action) {
        this.forEach(target, action, false);
        this.forEach(target, action, true);
    }

    public <T> void forEach(Class<T> target, Consumer<ReflectObjectType> action, boolean declared) {
        for (ReflectObjectType reflectObject : this.getAll(target, declared))
            action.accept(reflectObject);
    }

    //

    public List<ReflectObjectType> filter(Object target, Predicate<ReflectObjectType> filter) {
        return this.filter(target.getClass(), filter);
    }

    public List<ReflectObjectType> filter(Object target, Predicate<ReflectObjectType> filter, boolean declared) {
        return this.filter(target.getClass(), filter, declared);
    }

    public <T> List<ReflectObjectType> filter(Class<T> target, Predicate<ReflectObjectType> filter) {
        List<ReflectObjectType> result = new ArrayList<>();

        result.addAll(this.filter(target, filter, false));
        result.addAll(this.filter(target, filter, true));

        return result;
    }

    public <T> List<ReflectObjectType> filter(Class<T> target, Predicate<ReflectObjectType> filter, boolean declared) {
        List<ReflectObjectType> result = new ArrayList<>();
        this.forEach(target, reflectObjectType -> { if(filter.test(reflectObjectType)) result.add(reflectObjectType); }, declared);
        return result;
    }

}
