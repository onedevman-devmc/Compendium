package mc.compendium;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import mc.compendium.chestinterface.bukkit.BukkitChestInterfaceEventListener;
import mc.compendium.chestinterface.components.AnvilInput;
import mc.compendium.nms.NMS;
import mc.compendium.utils.reflection.FieldsUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class PluginMain extends JavaPlugin implements Listener {

    private AnvilInput x = null;

    //

    @EventHandler
    public void onMove(PlayerMoveEvent event) throws InvocationTargetException, IllegalAccessException {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        if(
            from.getBlockX() != to.getBlockX()
//            || from.getBlockY() != to.getBlockY()
            || from.getBlockZ() != to.getBlockZ()
        ) {
            player.openInventory(x.toBukkit());
            event.setCancelled(true);
        }
    }

    //

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new BukkitChestInterfaceEventListener(this), this);

        x = new AnvilInput("Test input");


//        EntityPlayer ep = NMS.getNativePlayer(player);
        try {
            DedicatedServer server = NMS.getNativeServer();
            ServerConnectionListener connection = (ServerConnectionListener) FieldsUtil.filter(server, f -> f.getType().equals(PlayerConnection.class)).get(0).get(server);
            NetworkManager netmng = (NetworkManager) FieldsUtil.filter(connection, f -> f.getType().equals(NetworkManager.class)).get(0).get(connection);
            Channel channel = (Channel) FieldsUtil.filter(netmng, f -> f.getType().equals(Channel.class)).get(0).get(netmng);

            if(channel.pipeline().get("compendium.packet_handler") != null) channel.pipeline().remove("compendium.packet_handler");
            channel.pipeline().addBefore("packet_handler", "compendium.packet_handler", new ChannelDuplexHandler() {

                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                    Bukkit.getLogger().info("out: " + msg.getClass().getSimpleName());
                    super.write(ctx, msg, promise);
                }

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    Bukkit.getLogger().info("in: " + msg.getClass().getSimpleName());
                    super.channelRead(ctx, msg);
                }
            });
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {}

}
