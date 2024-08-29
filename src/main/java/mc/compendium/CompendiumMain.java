package mc.compendium;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CompendiumMain extends JavaPlugin implements Listener {

//    private AnvilInput anvilInput = null;
//    private TradeInterface tradeInterface = null;
//
//    //
//
//    @EventHandler
//    public void onMove(PlayerMoveEvent event) throws InvocationTargetException, IllegalAccessException {
//        Player player = event.getPlayer();
//
//        Location from = event.getFrom();
//        Location to = event.getTo();
//
//        if(
//            from.getBlockX() != to.getBlockX()
////            || from.getBlockY() != to.getBlockY()
//            || from.getBlockZ() != to.getBlockZ()
//        ) {
////            player.openInventory(anvilInput.toBukkit());
//            player.openMerchant(tradeInterface.toBukkit(), true);
//
//            event.setCancelled(true);
//        }
//    }
//
//    private final ProtocolManager protocolManager = new ProtocolManager(this);

    //

    @Override
    public void onEnable() {
//        try {
//            Bukkit.getPluginManager().registerEvents(this, this);
//            Bukkit.getPluginManager().registerEvents(new InterfaceEventBukkitListener(this), this);
//
//            this.anvilInput = new AnvilInput(this, new AnvilInputConfig("Test input", List.of(), "", false));
//            this.tradeInterface = new TradeInterface(new TradeInterfaceConfig("Test Interface", false));
//
//            this.tradeInterface.addListener(new InterfaceEventListener() {
//
//                @mc.compendium.events.EventHandler
//                public void onOpen(TradeInterfaceOpenEvent event) {
//                    Bukkit.getLogger().warning("" + event);
//                }
//
//                @mc.compendium.events.EventHandler
//                public void onClose(TradeInterfaceCloseEvent event) {
//                    Bukkit.getLogger().warning("" + event);
//                }
//
//                @mc.compendium.events.EventHandler
//                public void onTrade(TradeEvent event) {
//                    Bukkit.getLogger().warning("" + event);
//                }
//
//                @mc.compendium.events.EventHandler
//                public void onSelectTrade(TradeSelectEvent event) {
//
//                }
//
//            });
//
//            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.DIAMOND), 0, Integer.MAX_VALUE, false);
//            recipe.addIngredient(new ItemStack(Material.EMERALD));
//
//            this.tradeInterface.recipes().add(recipe);

//            DedicatedServer nativeServer = NMS.getNativeServer();
//            Field servicesField = FieldUtil.getInstance().get(MinecraftServer.class, field -> {
//                return field.getType().equals(Services.class);
//            });
//
//            Function<GameProfile, GameProfile> profileMapper = profile -> {
////                GameProfile finalProfile = new GameProfile(UUID.fromString("d08cb68a-b900-4191-8e20-ec2567d092a3"), "Compendium");
////                GameProfiles.copyPropertiesInto(profile, finalProfile);
////                return finalProfile;
//                return profile;
//            };
//
//            Services currentServices = FieldUtil.getInstance().getValue(nativeServer, servicesField);
//
//            MinecraftSessionService sessionServiceProxy = SessionServiceProxy.create(FieldUtil.getInstance().getValue(currentServices, field -> field.getType().equals(MinecraftSessionService.class)), profileMapper);
//
//            NMS.NativeServer.Services.setSessionService(sessionServiceProxy);

//            Constructor<?> constructor = ConstructorUtil.getInstance().get(Services.class, constructorCandidate -> true);
//            List<Object> constructorArguments = new ArrayList<>();
//            for(Parameter parameter : constructor.getParameters()) {
//                constructorArguments.add(FieldUtil.getInstance().getValue(currentServices, field -> field.getType().equals(parameter.getType())));
//            }
//            Bukkit.getLogger().warning(""+ constructorArguments);
//            constructorArguments.set(0, sessionServiceProxy);
//
//            Services newServices = (Services) ConstructorUtil.getInstance().newInstance(constructor, constructorArguments.toArray());
//
//            FieldUtil.getInstance().changeFinal(nativeServer, servicesField, newServices);

//            Bukkit.getPluginManager().registerEvents(this.protocolManager, this);
//            this.protocolManager().enable();

//            this.protocolManager().addListener(new ProtocolEventListener() {

//                @mc.compendium.events.EventHandler
//                public void onRecievingPacket(IncomingPacketEvent<?> event) {
//                    Player player = event.getPlayer();
//
////                    Bukkit.getLogger().warning("INCOMING from " + player + ": " + event.getPacket());
//                }

//                @mc.compendium.events.EventHandler
//                public void onSendingPacket(OutgoingPacketEvent<?> event) {
//                    Player player = event.getPlayer();
//
////                    Bukkit.getLogger().warning("OUTGOING to " + player + ": " + event.getPacket());
//                }

//            });
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void onDisable() {
//        try {
//            this.protocolManager().disable();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    //

//    public ProtocolManager protocolManager() {
//        return this.protocolManager;
//    }

}