package mc.compendium.protocol.events;

import mc.compendium.events.WrapperEvent;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;

public class PacketEvent<P extends Packet<?>> extends ProtocolEvent<P> {

    private Packet<?> packet;
    private final InetSocketAddress address;
    private final Player player;

    //

    public PacketEvent(P nativePacket, InetSocketAddress address, Player player) {
        this(nativePacket, address, player, true);
    }

    public PacketEvent(P nativePacket, InetSocketAddress address, Player player, boolean cancellable) {
        super(nativePacket, cancellable);

        this.packet = this.getWrapped();
        this.address = address;
        this.player = player;
    }

    //

    public P getPacket() {
        try { return (P) this.packet; }
        catch (ClassCastException ignored) { return null; }
    }

    public Packet<?> getRawPacket() { return this.packet; }

    public void setPacket(P packet) {
        this.packet = packet;
    }

    public void setRawPacket(Packet<?> packet) {
        this.packet = packet;
    }

    //

    public InetSocketAddress getAddress() { return this.address; }

    public Player getPlayer() { return this.player; }

}
