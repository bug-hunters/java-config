package co.bh.config;

public class ConfigNotFoundException extends Exception {

    public ConfigNotFoundException(String key) {
        super("Key `" + key + "` was not found");
    }
}
