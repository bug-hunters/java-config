package com.java.config;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class ConfigurationFactory {
    static final String DEFAULT_PACKAGE = "io.katmai.config";
    static Configuration config = null;
    public static Configuration getConfiguration() {
        if(config != null) {
            return config;
        }
        // look for explicit configuration loaders
        String loader = System.getProperty("configLoader");
        if(loader != null) {
            try {
                Class<?> cls = ConfigurationFactory.class.getClassLoader().loadClass(loader);
                if(cls != null) {
                    ConfigurationFactory.config = (Configuration)cls.newInstance();
                    return ConfigurationFactory.config;
                }
            }
            catch (Exception e) {
                System.out.println("couldn't load `" + loader + "`: " + e.getMessage());
                // try with package...
            }
            loader = DEFAULT_PACKAGE + "." + loader;
            try {
                Class<?> cls = ConfigurationFactory.class.getClassLoader().loadClass(loader);
                if(cls != null) {
                    ConfigurationFactory.config = (Configuration)cls.newInstance();
                    return ConfigurationFactory.config;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("unable to load configuration `" + loader + "`", e);
            }
        }
        String location = System.getProperty("configuration");
        if(location == null) {
            // try classpath
            InputStream stream = Configuration.class.getClassLoader().getResourceAsStream("configuration.yaml");

            if(stream != null) {
                try {
                    System.out.println("found configuration in resources");
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(stream, writer, "UTF-8");
                    ConfigurationFactory.config = new ConfigurationImpl().fromContents(writer.toString());
                    return ConfigurationFactory.config;
                }
                catch (Exception e) {
                    // not found
                }
            }

            // try cwd
            File file = new File("configuration.yaml");
            if(file.exists()) {
                try {
                    System.out.println("found configuration in cwd (" + file.getAbsolutePath() + ")");
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(new FileInputStream(file), writer, "UTF-8");
                    ConfigurationFactory.config = new ConfigurationImpl().fromContents(writer.toString());
                    return ConfigurationFactory.config;
                }
                catch (Exception e) {
                    // not found
                }
            }

            // try src/main/config
            file = new File("src/main/config/configuration.yaml");
            if(file.exists()) {
                try {
                    System.out.println("found configuration in " + file.getAbsolutePath());
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(new FileInputStream(file), writer, "UTF-8");
                    ConfigurationFactory.config = new ConfigurationImpl().fromContents(writer.toString());
                    return ConfigurationFactory.config;
                }
                catch (Exception e) {
                    // not found
                }
            }

            // try src/test/config
            file = new File("src/test/config/configuration.yaml");
            if(file.exists()) {
                try {
                    System.out.println("found configuration in " + file.getAbsolutePath());
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(new FileInputStream(file), writer, "UTF-8");
                    ConfigurationFactory.config = new ConfigurationImpl().fromContents(writer.toString());
                    return ConfigurationFactory.config;
                }
                catch (Exception e) {
                    // not found
                }
            }
            return new SystemPropertyConfig();
        }
        System.out.println("loading app configuration from " + location);
        ConfigurationFactory.config = new ConfigurationImpl().fromLocation(location);
        return ConfigurationFactory.config;
    }

    public static void reload() {
        ConfigurationFactory.config = null;
    }
}


