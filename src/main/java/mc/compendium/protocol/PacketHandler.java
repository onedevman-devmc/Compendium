package mc.compendium.protocol;

import io.netty.channel.*;
import io.netty.util.AttributeKey;
import mc.compendium.events.EventManager;
import mc.compendium.events.EventManagerDelegation;
import mc.compendium.protocol.events.*;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;

@ChannelHandler.Sharable
public class PacketHandler extends ChannelDuplexHandler implements EventManagerDelegation<ProtocolEvent<?>, ProtocolEventListener> {

    private static final AttributeKey<Player> ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY;

    //

    static {
        if(AttributeKey.exists("compendium.associatedPlayer")) ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY = AttributeKey.valueOf("compendium.associatedPlayer");
        else ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY = AttributeKey.newInstance("compendium.associatedPlayer");
    }

    //

    public static Player getAssociatedPlayer(Channel channel) {
        channel.attr(ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY).setIfAbsent(null);
        return channel.attr(ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY).get();
    }

    public static void setAssociatedPlayer(Channel channel, Player player) {
        channel.attr(ASSOCIATED_PLAYER_CHANNEL_ATTRIBUTE_KEY).set(player);
    }

    //

    private final EventManager<ProtocolEvent<?>, ProtocolEventListener> eventManager;

    //

    public PacketHandler() {
        this.eventManager = new EventManager<>((Class<ProtocolEvent<?>>) ((Class<?>) PacketEvent.class));
    }

    //

    @Override
    public EventManager<ProtocolEvent<?>, ProtocolEventListener> delegate() {
        return this.eventManager;
    }

    //

    private InetSocketAddress getAddress(Channel channel) {
        return channel.remoteAddress() instanceof InetSocketAddress ? (InetSocketAddress) channel.remoteAddress() : null;
    }

    //

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(!(msg instanceof Packet<?> packet)) {
            super.write(ctx, msg, promise);
            return;
        }

        Channel channel = ctx.channel();

        OutgoingPacketEvent<?> event = new OutgoingPacketEvent<>(packet, getAddress(channel), getAssociatedPlayer(channel));
        boolean accepted = this.handle(event);
        if(accepted) super.write(ctx, event.getPacket(), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof Packet<?> packet)) {
            super.channelRead(ctx, msg);
            return;
        }

        Channel channel = ctx.channel();

        IncomingPacketEvent<?> event = new IncomingPacketEvent<>(packet, getAddress(channel), getAssociatedPlayer(channel));
        boolean accepted = this.handle(event);
        if(accepted) super.channelRead(ctx, event.getPacket());
    }

}
