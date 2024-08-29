package mc.compendium.protocol;

import io.netty.channel.*;
import mc.compendium.events.EventManagerInterface;
import mc.compendium.nms.protocol.NativeNetworkUtil;
import mc.compendium.protocol.events.ProtocolEvent;
import mc.compendium.protocol.events.ProtocolEventListener;
import mc.compendium.utils.bukkit.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ProtocolManager implements EventManagerInterface<ProtocolEvent<?>, ProtocolEventListener>, Listener {

    public static final String NATIVE_PACKET_HANDLER_KEY = "packet_handler";
    public static final String CUSTOM_PACKET_HANDLER_KEY = "compendium.protocol.packet-handler";

    //

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    private void onPlayerJoinEvent(PlayerJoinEvent event) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!this.enabled) return;

        //

        Player player = event.getPlayer();
        PacketHandler.setAssociatedPlayer(Players.getChannel(player), player);
    }

    //

    private final Plugin plugin;

    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> protocolPipelineInitializer;
    private ChannelInitializer<Channel> packetHandlerInjector;

    private final List<ProtocolEventListener> eventListeners = new ArrayList<>();
    private final Map<Channel, PacketHandler> packetHandlerMap = new WeakHashMap<>();

    private boolean enabled = false;

    //

    public ProtocolManager(Plugin plugin) {
//        super((Class<ProtocolEvent<?>>) ((Class<?>) ProtocolEvent.class));

        this.plugin = plugin;

        this.init();
    }

    //

    private void init() {
        ProtocolManager _this = this;

        //

        this.packetHandlerInjector = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.eventLoop().submit(() -> { injectCustomPacketHandler(channel); });
            }
        };

        this.protocolPipelineInitializer = new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(packetHandlerInjector);
            }
        };

        this.serverChannelHandler = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Channel channel = (Channel) msg;
                channel.pipeline().addFirst(protocolPipelineInitializer);
                ctx.fireChannelRead(msg);
            }
        };
    }

    //

    @Override
    public List<ProtocolEventListener> getListeners() {
        return this.eventListeners;
    }

    @Override
    public void addListener(ProtocolEventListener listener) {
        if(this.eventListeners.contains(listener)) return;

        this.eventListeners.add(listener);
        if(this.enabled) this.bindListener(listener);
    }

    @Override
    public void removeListener(ProtocolEventListener listener) {
        if(!this.eventListeners.contains(listener)) return;

        if(this.enabled) this.unbindListener(listener);
        this.eventListeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        this.unbindAllListeners(this.eventListeners);
        this.eventListeners.clear();
    }

    @Override
    public boolean handle(ProtocolEvent<?> event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> boolean handle(E event) {
        throw new UnsupportedOperationException();
    }

    //

    private void bindListenerOn(ProtocolEventListener listener, PacketHandler packetHandler) {
        packetHandler.addListener(listener);
    }

    private void bindListener(ProtocolEventListener listener) {
        for(PacketHandler packetHandler : packetHandlerMap.values()) this.bindListenerOn(listener, packetHandler);
    }

    private void bindAllListenersOn(List<ProtocolEventListener> listeners, PacketHandler packetHandler) {
        for(ProtocolEventListener listener : listeners) this.bindListenerOn(listener, packetHandler);
    }

    private void bindAllListeners(List<ProtocolEventListener> listeners) {
        for(PacketHandler packetHandler : packetHandlerMap.values()) this.bindAllListenersOn(listeners, packetHandler);
    }

    //

    private void unbindListenerOn(ProtocolEventListener listener, PacketHandler packetHandler) {
        packetHandler.removeListener(listener);
    }

    private void unbindListener(ProtocolEventListener listener) {
        for(PacketHandler packetHandler : packetHandlerMap.values()) this.unbindListenerOn(listener, packetHandler);
    }

    private void unbindAllListenersOn(List<ProtocolEventListener> listeners, PacketHandler packetHandler) {
        for(ProtocolEventListener listener : listeners) this.unbindListenerOn(listener, packetHandler);
    }

    private void unbindAllListeners(List<ProtocolEventListener> listeners) {
        for(PacketHandler packetHandler : packetHandlerMap.values()) this.unbindAllListenersOn(listeners, packetHandler);
    }

    //

    public void enable() {
        try {
            this.disable();

            //

            List<ChannelFuture> serverChannelFutureList = NativeNetworkUtil.ServerConnections.getChannelFutureList();
            for(ChannelFuture serverChannelFuture : serverChannelFutureList) this.proxyServerChannel(serverChannelFuture.channel());

            this.bindAllListeners(this.eventListeners);

            for(Player player : Bukkit.getOnlinePlayers()) {
                Channel channel = Players.getChannel(player);
                injectCustomPacketHandler(channel);
                PacketHandler.setAssociatedPlayer(channel, player);
            }

            Bukkit.getPluginManager().registerEvents(this, plugin);

            this.enabled = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void disable() {
        try {
            List<ChannelFuture> serverChannelFutureList = NativeNetworkUtil.ServerConnections.getChannelFutureList();
            for(ChannelFuture serverChannelFuture : serverChannelFutureList) this.unproxyServerChannel(serverChannelFuture.channel());

            this.unbindAllListeners(this.eventListeners);

            this.enabled = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //

    private void proxyServerChannel(Channel serverChannel) {
        this.unproxyServerChannel(serverChannel);
        serverChannel.pipeline().addFirst(this.serverChannelHandler);
    }

    private void unproxyServerChannel(Channel serverChannel) {
        try { serverChannel.pipeline().remove(this.serverChannelHandler); }
        catch (NoSuchElementException ignored) {}
    }

    private void injectCustomPacketHandler(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        PacketHandler storedPacketHandler = packetHandlerMap.get(channel);

        ChannelHandler currentPacketHandlerCandidate = pipeline.get(CUSTOM_PACKET_HANDLER_KEY);
        if(currentPacketHandlerCandidate instanceof PacketHandler currentPacketHandler) {
            if(currentPacketHandler.equals(storedPacketHandler)) return;

            if(storedPacketHandler != null) {
                unbindAllListenersOn(eventListeners, storedPacketHandler);
                packetHandlerMap.remove(channel);
            }

            bindAllListenersOn(eventListeners, currentPacketHandler);
            packetHandlerMap.put(channel, currentPacketHandler);
        }
        else {
            if(currentPacketHandlerCandidate != null) pipeline.remove(CUSTOM_PACKET_HANDLER_KEY);

            if(storedPacketHandler == null) {
                packetHandlerMap.put(channel, (storedPacketHandler = new PacketHandler()));
                bindAllListenersOn(eventListeners, storedPacketHandler);
            }

            pipeline.addBefore(NATIVE_PACKET_HANDLER_KEY, CUSTOM_PACKET_HANDLER_KEY, storedPacketHandler);
        }
    }

}
