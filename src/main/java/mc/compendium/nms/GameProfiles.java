package mc.compendium.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.util.Map;

public class GameProfiles {

    public static GameProfile copyPropertiesInto(GameProfile sourceProfile, GameProfile destinationProfile) {
        if(sourceProfile == null || destinationProfile == null)
            throw new RuntimeException("Source profile & destination profile shouldn't be null.");

        for(Map.Entry<String, Property> entry : sourceProfile.getProperties().entries()) {
            destinationProfile.getProperties().removeAll(entry.getKey());
            destinationProfile.getProperties().put(entry.getKey(), entry.getValue());
        }

        return destinationProfile;
    }

}
