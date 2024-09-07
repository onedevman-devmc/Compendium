package mc.compendium.protocol;

import mc.compendium.nms.protocol.PacketUtil;
import mc.compendium.reflection.DefaultValue;
import mc.compendium.reflection.FieldUtil;
import mc.compendium.reflection.NoSuchConstructorException;
import net.minecraft.network.protocol.Packet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketWrapper<P extends Packet<?>> {

    private static final Class<?>[] DATATYPES = new Class<?>[]{
        byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, String.class,
        byte[].class, int[].class, long[].class
    };

    //

    private final Map<Class<?>, List<?>> dataMap = new HashMap<>();

    private final P packet;

    //

    public PacketWrapper(P packet) throws IllegalAccessException {
        this.packet = packet;

        for(Class<?> datatype : DATATYPES) dataMap.put(datatype, new ArrayList<>());

        for(Field dataField : FieldUtil.getInstance().filter(this.packet, field -> {
            int modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        })) {
            this.dataMap.get(dataField.getType()).add(FieldUtil.getInstance().getValue(this.packet, dataField));
        }
    }

    //

    public P getOriginalPacket() {
        return this.packet;
    }

    public P getFinalPacket() throws NoSuchConstructorException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Map<Class<?>, List<?>> copiedDataMap = new HashMap<>();
        for(Class<?> datatype : this.dataMap.keySet()) copiedDataMap.put(datatype, new ArrayList<>(this.dataMap.get(datatype)));

        P finalPacket = PacketUtil.clone(this.getOriginalPacket());

        for(Field dataField : FieldUtil.getInstance().filter(this.packet, field -> {
            int modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        })) {
            List<?> dataValues = copiedDataMap.get(dataField.getType());
            Object dataValue = dataValues.size() > 0 ? dataValues.remove(0) : DefaultValue.get(dataField.getType());
            FieldUtil.getInstance().changeFinal(finalPacket, dataField, dataValue);
        }

        return finalPacket;
    }

    //

    public <T> List<T> all(Class<T> datatype) {
        return (List<T>) this.dataMap.get(datatype);
    }

}
