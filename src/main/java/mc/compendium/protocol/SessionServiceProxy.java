package mc.compendium.protocol;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import mc.compendium.reflection.MethodUtil;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionServiceProxy implements InvocationHandler {

    public static MinecraftSessionService create(MinecraftSessionService original, Function<GameProfile, GameProfile> profileMapper) {
        return (MinecraftSessionService) Proxy.newProxyInstance(MinecraftSessionService.class.getClassLoader(), new Class[] { MinecraftSessionService.class }, new SessionServiceProxy(original, profileMapper));
    }

    //

    private final MinecraftSessionService original;
    private final Function<GameProfile, GameProfile> profileMapper;

    //

    private SessionServiceProxy(MinecraftSessionService original, Function<GameProfile, GameProfile> profileMapper) {
        this.original = original;
        this.profileMapper = profileMapper;
    }

    //

    public MinecraftSessionService getProxied() {
        return original;
    }

    //

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Parameter[] parameters = method.getParameters();
        Bukkit.getLogger().warning("INTERCEPT MinecraftSessionService: " + method.getReturnType() + " " + method.getName() + "(" + (parameters.length > 0 ? Arrays.asList(parameters).stream().map(p -> p.toString()).collect(Collectors.joining(", ")) : "") + ")");

        Object result = MethodUtil.getInstance().invoke(this.original, method, args);

        if(method.getReturnType().equals(ProfileResult.class) && result instanceof ProfileResult profileResult) {
            GameProfile profile = profileResult.profile();
            result = new ProfileResult(this.profileMapper.apply(profile));
        }

        return result;
    }

}
