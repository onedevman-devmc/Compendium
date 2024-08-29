package mc.compendium.nms.protocol;

import io.netty.buffer.Unpooled;
import mc.compendium.reflection.*;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.network.protocol.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class PacketUtil {

    private enum CodecSystem {
        STREAM_CODEC(PacketUtil::encodeWithStreamCodec, PacketUtil::decodeWithStreamCodec),
        WRITE_METHOD(PacketUtil::encodeWithWriteMethod, PacketUtil::decodeWithWriteMethod);

        //

        public static final CodecSystem[] ALL = CodecSystem.values();

        //

        private final EncodingMethod encodingMethod;
        private final DecodingMethod decodingMethod;

        //

        CodecSystem(EncodingMethod encodingMethod, DecodingMethod decodingMethod) {
            this.encodingMethod = encodingMethod;
            this.decodingMethod = decodingMethod;
        }

        //

        public PacketDataSerializer encode(Packet<?> packet) { return this.encodingMethod.encode(packet); }
        public <P extends Packet<?>> P decode(PacketDataSerializer serializer, Class<P> packetClass) {
            return this.decodingMethod.decode(serializer, packetClass);
        }

    }

    //

    private interface EncodingMethod {

        PacketDataSerializer doEncode(Packet<?> packet) throws Exception;

        default PacketDataSerializer encode(Packet<?> packet) {
            try { return this.doEncode(packet); }
            catch (NoSuchFieldException ignored) { return null; }
            catch (Exception e) { throw new RuntimeException(e); }
        }

    }

    private interface DecodingMethod {

        <P extends Packet<?>> P doDecode(PacketDataSerializer serializer, Class<P> packetClass) throws Exception;

        default <P extends Packet<?>> P decode(PacketDataSerializer serializer, Class<P> packetClass) {
            try { return this.doDecode(serializer, packetClass); }
            catch (NoSuchFieldException ignored) { return null; }
            catch (Exception e) { throw new RuntimeException(e); }
        }

    }

    //

    private static CodecSystem USED_ENCODING_SYSTEM = null;
    private static CodecSystem USED_DECODING_SYSTEM = null;

    //

    public static PacketDataSerializer encode(Packet<?> packet) {
        PacketDataSerializer result = null;

        if(USED_ENCODING_SYSTEM != null) result = tryEncodingMethod(USED_ENCODING_SYSTEM, packet);
        if(result == null) {
            for(int i = 0; i < CodecSystem.ALL.length && result == null; ++i)
                result = tryEncodingMethod(CodecSystem.ALL[i], packet);
        }

        return result;
    }

    private static PacketDataSerializer tryEncodingMethod(CodecSystem system, Packet<?> packet) {
        PacketDataSerializer result = system.encode(packet);
        USED_ENCODING_SYSTEM = system;
        return result;
    }

    private static PacketDataSerializer encodeWithStreamCodec(Packet<?> packet) throws IncompatibleInheritanceException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        StreamCodec<PacketDataSerializer, Packet<?>> streamCodec = (StreamCodec<PacketDataSerializer, Packet<?>>) getStreamCodec(packet.getClass());

        MethodUtil.getInstance().invoke(StreamEncoder.class, streamCodec, method -> {
            int modifiers = method.getModifiers();
            Parameter[] parameters = method.getParameters();
            return (
                Modifier.isPublic(modifiers)
                && method.getReturnType().equals(void.class)
                && parameters.length == 2
            );
        }, serializer, packet);

        return serializer;
    }

    private static PacketDataSerializer encodeWithWriteMethod(Packet<?> packet) throws IncompatibleInheritanceException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());

        MethodUtil.getInstance().invoke(packet, method -> {
            Parameter[] parameters = method.getParameters();
            return (
                method.getReturnType().equals(void.class)
                && parameters.length == 1
                && parameters[0].getType().equals(PacketDataSerializer.class)
            );
        }, serializer);

        return serializer;
    }

    //

    public static <P extends Packet<?>> P decode(PacketDataSerializer serializer, Class<P> packetClass) throws NoSuchConstructorException, InvocationTargetException, InstantiationException, IllegalAccessException {
        P result = null;

        if(USED_DECODING_SYSTEM != null) result = tryDecodingMethod(USED_DECODING_SYSTEM, serializer, packetClass);
        if(result == null) {
            for(int i = 0; i < CodecSystem.ALL.length && result == null; ++i)
                result = tryDecodingMethod(CodecSystem.ALL[i], serializer, packetClass);
        }

        return result;
    }

    public static <P extends Packet<?>> P tryDecodingMethod(CodecSystem system, PacketDataSerializer serializer, Class<P> packetClass) throws NoSuchConstructorException, InvocationTargetException, InstantiationException, IllegalAccessException {
        P result = system.decode(serializer, packetClass);
        USED_ENCODING_SYSTEM = system;
        return result;
    }

    public static <P extends Packet<?>> P decodeWithStreamCodec(PacketDataSerializer serializer, Class<P> packetClass) throws IncompatibleInheritanceException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        StreamCodec<PacketDataSerializer, P> streamCodec = getStreamCodec(packetClass);

        return MethodUtil.getInstance().invoke(StreamDecoder.class, streamCodec, method -> {
            int modifiers = method.getModifiers();
            Parameter[] parameters = method.getParameters();
            return (
                Modifier.isPublic(modifiers)
                && !method.getReturnType().equals(void.class)
                && parameters.length == 1
            );
        }, serializer);
    }

    public static <P extends Packet<?>> P decodeWithWriteMethod(PacketDataSerializer serializer, Class<P> packetClass) throws NoSuchConstructorException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ConstructorUtil.getInstance().newInstance(packetClass, constructor -> {
            Parameter[] parameters = constructor.getParameters();
            return parameters.length == 1 && parameters[0].getType().equals(PacketDataSerializer.class);
        }, serializer);
    }

    //

    private static <P extends Packet<?>> StreamCodec<PacketDataSerializer, P> getStreamCodec(Class<P> packetClass) throws IncompatibleInheritanceException, NoSuchFieldException, IllegalAccessException {
        StreamCodec<PacketDataSerializer, P> result = null;

        Class<?> cls = packetClass;
        for(int i = 0; i < 2 && result == null; i++) {
            try {
                result = FieldUtil.getInstance().getValue(cls, null, field -> {
                    int modifiers = field.getModifiers();
                    return (
                        Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                        && field.getType().equals(StreamCodec.class)
                    );
                });
            } catch (NoSuchFieldException ignored) {}

            cls = cls.getSuperclass();
        }

        if(result == null) throw new NoSuchFieldException("Unable to find Stream Codec for this packet.");

        return result;
    }

    //

    public static <P extends Packet<?>> P clone(P packet) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
        return decode(PacketUtil.encode(packet), (Class<P>) packet.getClass());
    }

//    public static <P extends Packet<?>> P proxyPacket(P packet, InvocationHandler invocationHandler) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchConstructorException {
//        PacketDataSerializer serializer = PacketUtil.encode(packet);
//
//        Class<?> proxyClass = new ByteBuddy()
//            .subclass(packet.getClass())
//            .method(ElementMatchers.any())
//            .intercept(InvocationHandlerAdapter.of(invocationHandler))
//            .make().load(packet.getClass().getClassLoader()).getLoaded();
//
//        return decode(serializer, (Class<P>) proxyClass);
//    }

}
