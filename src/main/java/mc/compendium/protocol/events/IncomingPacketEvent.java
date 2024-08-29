package mc.compendium.protocol.events;

import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;

public class IncomingPacketEvent<P extends Packet<?>> extends PacketEvent<P> {
    public IncomingPacketEvent(P nativePacket, InetSocketAddress address, Player player) {
        this(nativePacket, address, player, true);
    }

    public IncomingPacketEvent(P nativePacket, InetSocketAddress address, Player player, boolean cancellable) {
        super(nativePacket, address, player, cancellable);
    }
}
