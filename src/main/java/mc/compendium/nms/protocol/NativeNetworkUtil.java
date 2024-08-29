package mc.compendium.nms.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import mc.compendium.nms.NMS;
import mc.compendium.reflection.FieldUtil;
import mc.compendium.reflection.IncompatibleInheritanceException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class NativeNetworkUtil {

    public static ServerConnection getServerConnection() throws IllegalAccessException, NoSuchFieldException, IncompatibleInheritanceException, InvocationTargetException {
        return getServerConnection(NMS.getNativeServer());
    }

    public static ServerConnection getServerConnection(MinecraftServer server) throws IllegalAccessException, NoSuchFieldException, IncompatibleInheritanceException {
        return FieldUtil.getInstance().getValue(MinecraftServer.class, server, f -> f.getType().equals(ServerConnection.class));
    }

    public static final class ServerConnections {

        private static boolean isNetworkManagerListField(Field field) {
            if (!(field.getGenericType() instanceof ParameterizedType ptype)) return false;

            Type[] typeArgs = ptype.getActualTypeArguments();
            return ptype.getRawType().equals(List.class) && typeArgs.length == 1 && typeArgs[0].equals(NetworkManager.class);
        }

        public static List<NetworkManager> getNetworkManagerList(ServerConnection serverConnection) throws IllegalAccessException, NoSuchFieldException {
            return FieldUtil.getInstance().getValue(serverConnection, ServerConnections::isNetworkManagerListField, true);
        }

        public static void setNetworkManagerList(ServerConnection serverConnection, List<NetworkManager> networkManagerList) throws IllegalAccessException, NoSuchFieldException {
            FieldUtil.getInstance().setValue(serverConnection, ServerConnections::isNetworkManagerListField, networkManagerList, true);
        }

        //

        private static boolean isChannelFutureListField(Field field) {
            if (!(field.getGenericType() instanceof ParameterizedType ptype)) return false;

            Type[] typeArgs = ptype.getActualTypeArguments();
            return ptype.getRawType().equals(List.class) && typeArgs.length == 1 && typeArgs[0].equals(ChannelFuture.class);
        }

        public static List<ChannelFuture> getChannelFutureList() throws IllegalAccessException, NoSuchFieldException, IncompatibleInheritanceException, InvocationTargetException {
            return getChannelFutureList(getServerConnection());
        }

        public static List<ChannelFuture> getChannelFutureList(ServerConnection serverConnection) throws IllegalAccessException, NoSuchFieldException {
            return FieldUtil.getInstance().getValue(serverConnection, ServerConnections::isChannelFutureListField, true);
        }

        public static void setChannelFutureList(List<ChannelFuture> channelFutureList) throws IllegalAccessException, NoSuchFieldException, IncompatibleInheritanceException, InvocationTargetException {
            setChannelFutureList(getServerConnection(), channelFutureList);
        }

        public static void setChannelFutureList(ServerConnection serverConnection, List<ChannelFuture> channelFutureList) throws IllegalAccessException, NoSuchFieldException {
            FieldUtil.getInstance().setValue(serverConnection, ServerConnections::isChannelFutureListField, channelFutureList, true);
        }

    }

    //
    
    public static final class NetworkManagers {

        public static Channel getChannel(NetworkManager networkManager) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            return FieldUtil.getInstance().getValue(NetworkManager.class, networkManager, f -> f.getType().equals(Channel.class));
        }

        public static void setChannel(NetworkManager networkManager, Channel channel) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            FieldUtil.getInstance().setValue(NetworkManager.class, networkManager, f -> f.getType().equals(Channel.class), channel);
        }

        public static boolean isConnected(NetworkManager networkManager) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            Channel channel = getChannel(networkManager);
            return channel != null && channel.isOpen();
        }

        public static PacketListener getPacketListener(NetworkManager networkManager) throws IllegalAccessException {
            return FieldUtil.getInstance().getValue(
                networkManager,
                FieldUtil.getInstance().filter(
                    NetworkManager.class,
                    field -> field.getType().equals(PacketListener.class)
                ).getLast()
            );
        }

        public static void setPacketListener(NetworkManager networkManager, PacketListener listener) throws IllegalAccessException {
            FieldUtil.getInstance().setValue(
                networkManager,
                FieldUtil.getInstance().filter(
                    NetworkManager.class,
                    field -> field.getType().equals(PacketListener.class)
                ).getLast(),
                listener
            );
        }
        
    }

}
