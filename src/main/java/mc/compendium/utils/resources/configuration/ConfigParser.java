package mc.compendium.utils.resources.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser {

    public interface Path {
        String getPath();

        <T> T defaultValue();
        List<String> comments();
        boolean areInlineComments();
    }

    //

    private static String parseWithToken(String str, String tokenKey, Object tokenValue) {
        return str.replaceAll("%" + tokenKey.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}") + "%", String.valueOf(tokenValue));
    }

    //

    public enum NativeToken {
        DataFolder("datafolder");

        //

        public final String tokenKey;

        public final Map<ConfigParser, String> tokenValueByParser = new HashMap<>();

        //

        NativeToken(String tokenKey) {
            this.tokenKey = tokenKey;
        }

        //

        public void setValue(ConfigParser parser, String tokenValue) {
            this.removeValue(parser);
            this.tokenValueByParser.put(parser, tokenValue);
        }

        public void removeValue(ConfigParser parser) {
            this.tokenValueByParser.remove(parser);
        }

        public String parse(String str, ConfigParser parser) {
            String tokenValue = this.tokenValueByParser.get(parser);
            return this.parse(str, tokenValue == null ? "" : tokenValue);
        }

        public String parse(String str, Object tokenValue) {
            return ConfigParser.parseWithToken(str, this.tokenKey, tokenValue);
        }

    }

    //

    public final Plugin pluginInstance;

    private YamlConfiguration config;
    private ConfigurationSection tokenSection = null;

    private final Map<String, Object> tokenMap = new HashMap<>();

    //

    public ConfigParser(Plugin pluginInstance) {
        this.pluginInstance = pluginInstance;

        //

        NativeToken.DataFolder.setValue(this, this.pluginInstance.getDataFolder().getPath());
    }

    //

    public YamlConfiguration config() { return this.config; }
    public void setConfig(YamlConfiguration config) {
        this.config = config;
        this.tokenMap.clear();
        this.mapTokens();
    }

    //

    public ConfigParser setTokenSection(ConfigurationSection tokenSection) {
        this.tokenSection = tokenSection;
        return this;
    }

    public ConfigurationSection getTokenSection() { return tokenSection; }

    //

    private void mapTokens() {
        if(this.tokenSection == null) return;

        for(String tokenKey : tokenSection.getKeys(false))
            this.tokenMap.put(tokenKey, tokenSection.getString(tokenKey));

        for(String configKey : this.config().getKeys(true)) {
            this.tokenMap.put("#" + configKey, this.config().get(configKey));
            if(configKey.startsWith("."))
                this.tokenMap.put("#" + configKey.substring(1), this.config().get(configKey));
        }
    }

    //

    public String parse(String message) {
        return this.parse(message, this.tokenMap);
    }

    public String parse(String message, Map<String, Object> tokens) {
        for(NativeToken nativeToken : NativeToken.values())
            message = nativeToken.parse(message, this);

        for(Map.Entry<String, Object> token : tokens.entrySet())
            message = ConfigParser.parseWithToken(message, token.getKey(), token.getValue());

        return message;
    }

    //

    public String get(String path) {
        String message = this.config().getString(path);

        if(message == null)
            return null;

        return this.parse(message);
    }

    //

    public String getString(String path) { return (String) this.get(path); }
    public String getString(Path path) {
        return this.getString(path.getPath());
    }

    public boolean getBoolean(String path) { return Boolean.parseBoolean(this.getString(path)); }
    public boolean getBoolean(Path path) { return this.getBoolean(path.getPath()); }

    public byte getByte(String path) {
        return Byte.parseByte(this.getString(path));
    }
    public byte getByte(Path path) { return this.getByte(path.getPath()); }

    public short getShort(String path) {
        return Short.parseShort(this.getString(path));
    }
    public short getShort(Path path) {
        return this.getShort(path.getPath());
    }

    public int getInt(String path) {
        return Integer.parseInt(this.getString(path));
    }
    public int getInt(Path path) {
        return this.getInt(path.getPath());
    }

    public long getLong(String path) {
        return Long.parseLong(this.getString(path));
    }
    public long getLong(Path path) {
        return this.getLong(path.getPath());
    }

    public float getFloat(String path) {
        return Float.parseFloat(this.getString(path));
    }
    public float getFloat(Path path) {
        return this.getFloat(path.getPath());
    }

    public double getDouble(String path) {
        return Double.parseDouble(this.getString(path));
    }
    public double getDouble(Path path) {
        return this.getDouble(path.getPath());
    }

}
