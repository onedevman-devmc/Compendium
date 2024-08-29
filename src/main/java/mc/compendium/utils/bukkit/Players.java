package mc.compendium.utils.bukkit;

import io.netty.channel.Channel;
import mc.compendium.reflection.FieldUtil;
import mc.compendium.reflection.MethodUtil;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Players {

    public static InetSocketAddress getAddress(Player player) throws InvocationTargetException, IllegalAccessException {
        if(!(player.getClass().getName().equals(Player.class.getName())))
            return (InetSocketAddress) MethodUtil.getInstance().get(player, m -> m.getName().equals("getAddress")).invoke(player);

        return player.getAddress();
    }

    public static String getIp(Player player) throws InvocationTargetException, IllegalAccessException {
        return getAddress(player).getAddress().getHostAddress();
    }

    //

    public static Channel getChannel(Player player) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        ServerPlayer serverPlayer = MethodUtil.getInstance().invoke(player, method -> method.getReturnType().equals(ServerPlayer.class) && method.getParameterCount() == 0);
        ServerGamePacketListenerImpl packetListener = FieldUtil.getInstance().getValue(serverPlayer, field -> field.getType().equals(ServerGamePacketListenerImpl.class));
        Connection connection = FieldUtil.getInstance().getValue(packetListener, field -> field.getType().equals(Connection.class));
        return FieldUtil.getInstance().getValue(connection, field -> field.getType().equals(Channel.class));
    }

    //

    public static UUID getOfflineUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
    }

}
