package mc.compendium.utils.resources.configuration;

import mc.compendium.utils.Lists;
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

    private static String parseWithToken(String str, String token_key, Object token_value) {
        return str.replaceAll("%" + token_key.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}") + "%", String.valueOf(token_value));
    }

    //

    public enum NativeToken {
        DataFolder("datafolder");

        //

        public final String tokenKey;

        public final Map<ConfigParser, String> token_value_by_parser = new HashMap<>();

        //

        NativeToken(String token_key) {
            this.tokenKey = token_key;
        }

        //

        public void setValue(ConfigParser parser, String token_value) {
            this.removeValue(parser);
            this.token_value_by_parser.put(parser, token_value);
        }

        public void removeValue(ConfigParser parser) {
            this.token_value_by_parser.remove(parser);
        }

        public String parse(String str, ConfigParser parser) {
            String token_value = this.token_value_by_parser.get(parser);
            return this.parse(str, token_value == null ? "" : token_value);
        }

        public String parse(String str, Object token_value) {
            return ConfigParser.parseWithToken(str, this.tokenKey, token_value);
        }

    }

    //

    public final Plugin pluginInstance;

    private YamlConfiguration config;
    private ConfigurationSection tokenSection = null;

    private final Map<String, Object> tokenMap = new HashMap<>();

    //

    public ConfigParser(Plugin plugin_instance) {
        this.pluginInstance = plugin_instance;

        //

        NativeToken.DataFolder.setValue(this, this.pluginInstance.getDataFolder().getPath());
    }

    //

    public YamlConfiguration config() { return this.config; }
    public void setConfig(YamlConfiguration config) {
        this.config = config;

        List<String> keys = Lists.toList(this.tokenMap.keySet());
        for(String key : keys)
            this.tokenMap.remove(key);

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

        List<String> token_key_list = Lists.toList(tokenSection.getKeys(false));

        for(String token_key : token_key_list)
            this.tokenMap.put(token_key, tokenSection.getString(token_key));

        for(String config_key : this.config().getKeys(true)) {
            this.tokenMap.put("#" + config_key, this.config().get(config_key));
            if(config_key.startsWith("."))
                this.tokenMap.put("#" + config_key.substring(1), this.config().get(config_key));
        }
    }

    //

    public String parse(String message) {
        return this.parse(message, this.tokenMap);
    }

    public String parse(String message, Map<String, Object> tokens) {
        List<Map.Entry<String, Object>> token_list = Lists.toList(tokens.entrySet());

        for(NativeToken native_token : NativeToken.values())
            message = native_token.parse(message, this);

        for(Map.Entry<String, Object> token : token_list)
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
