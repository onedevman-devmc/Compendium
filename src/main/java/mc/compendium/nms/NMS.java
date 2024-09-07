package mc.compendium.nms;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import mc.compendium.reflection.FieldUtil;
import mc.compendium.reflection.IncompatibleInheritanceException;
import mc.compendium.reflection.MethodUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class NMS {

    public static DedicatedServer getNativeServer() throws InvocationTargetException, IllegalAccessException {
        return getNativeServer(Bukkit.getServer());
    }

    public static DedicatedServer getNativeServer(Server server) throws InvocationTargetException, IllegalAccessException {
        return (DedicatedServer) MethodUtil.getInstance().get(server, m -> m.getName().equals("getServer")).invoke(server);
    }

    public static class NativeServer {

        public static DedicatedPlayerList getPlayerList() throws InvocationTargetException, IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            return (DedicatedPlayerList) getPlayerList(getNativeServer());
        }

        public static PlayerList getPlayerList(MinecraftServer nativeServer) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            return FieldUtil.getInstance().getValue(MinecraftServer.class, nativeServer, field -> {
                int modifiers = field.getModifiers();
                return Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && field.getType().equals(PlayerList.class);
            });
        }

        public static void setPlayerList(PlayerList playerList) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException, InvocationTargetException {
            setPlayerList(getNativeServer(), playerList);
        }

        public static void setPlayerList(MinecraftServer nativeServer, PlayerList playerList) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
            FieldUtil.getInstance().setValue(MinecraftServer.class, nativeServer, field -> {
                int modifiers = field.getModifiers();
                return Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && field.getType().equals(PlayerList.class);
            }, playerList);
        }

        //

        public static net.minecraft.server.Services getServices() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
            return getServices(getNativeServer());
        }

        public static net.minecraft.server.Services getServices(MinecraftServer nativeServer) throws IllegalAccessException, NoSuchFieldException {
            try {
                return FieldUtil.getInstance().getValue(MinecraftServer.class, nativeServer, field -> field.getType().equals(net.minecraft.server.Services.class));
            } catch (IncompatibleInheritanceException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setServices(MinecraftServer nativeServer, net.minecraft.server.Services services) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
            try {
                FieldUtil.getInstance().changeFinal(MinecraftServer.class, nativeServer, field -> field.getType().equals(net.minecraft.server.Services.class), services);
            } catch (IncompatibleInheritanceException e) {
                throw new RuntimeException(e);
            }
        }

        public static class Services {

            private static boolean isSessionServiceField(Field field) {
                return field.getType().equals(MinecraftSessionService.class);
            }

            //

            public static MinecraftSessionService getSessionService() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException, IncompatibleInheritanceException {
                return getSessionService(getServices());
            }

            public static MinecraftSessionService getSessionService(net.minecraft.server.Services services) throws IllegalAccessException, IncompatibleInheritanceException, NoSuchFieldException {
                return FieldUtil.getInstance().getValue(services, f -> f.getType().equals(MinecraftSessionService.class), true);
            }

            public static void setSessionService(MinecraftSessionService sessionService) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IncompatibleInheritanceException {
                setSessionService(getServices(), sessionService);
            }

            public static void setSessionService(net.minecraft.server.Services services, MinecraftSessionService sessionService) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IncompatibleInheritanceException {
                FieldUtil.getInstance().changeFinal(services, Services::isSessionServiceField, sessionService);
            }

        }

    }

    //

    public static EntityPlayer getNativePlayer(Player player) throws InvocationTargetException, IllegalAccessException {
        return (EntityPlayer) MethodUtil.getInstance().get(player, m -> m.getName().equals("getHandle")).invoke(player);
    }

}
