package com.petwhistle;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class Config {
    public static int range = 32, cooldownTicks = 200;
    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("petwhistle.properties");
        Properties p = new Properties();
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) { try (InputStream in = Files.newInputStream(path)) { p.load(in); } }
            else { p.setProperty("range", "32"); p.setProperty("cooldownTicks", "200"); try (OutputStream out = Files.newOutputStream(path)) { p.store(out, "Pet Whistle settings; restart after editing"); } }
            range = Math.clamp(Integer.parseInt(p.getProperty("range", "32")), 1, 128);
            cooldownTicks = Math.clamp(Integer.parseInt(p.getProperty("cooldownTicks", "200")), 0, 72000);
        } catch (Exception e) { System.err.println("Pet Whistle: invalid config, using defaults: " + e); }
    }
}
