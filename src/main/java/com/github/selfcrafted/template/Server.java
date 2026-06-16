package com.github.selfcrafted.template;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Server {
    private static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    public static Logger logger() {
        return serverLogger;
    }

    static void main(String[] args) {
        serverLogger.info("====== VERSIONS ======");
        serverLogger.info("Java: {}", Runtime.version());
        serverLogger.info("{}: {}", Versions.implementation(), Versions.version());
        serverLogger.info("Minestom: {}", Versions.minestom());
        serverLogger.info("Supported protocol: {} ({})", MinecraftServer.PROTOCOL_VERSION, MinecraftServer.VERSION_NAME);
        serverLogger.info("======================");
        if (args.length > 0 && args[0].equalsIgnoreCase("-v")) System.exit(0);

        Settings.read();

        /*
         * Pre server init
         * Settings already read
         */

        // Initialise server
        Auth auth = null;
        switch (Settings.getMode()) {
            case OFFLINE -> auth = new Auth.Offline();
            case ONLINE -> auth = new Auth.Online();
            case BUNGEECORD -> {
                if (!Settings.hasSecret()) auth = new Auth.Bungee();
                else auth = new Auth.Bungee(Set.of(Settings.getSecret()));
            }
            case VELOCITY -> auth = new Auth.Velocity(Settings.getSecret());
        }
        MinecraftServer server = MinecraftServer.init(auth);
        serverLogger.info("Running in {} mode.", Settings.getMode());

        /*
         * Pre server start
         */

        // Start server
        server.start(Settings.getServerIp(), Settings.getServerPort());
        serverLogger.info("Listening on {}:{}", Settings.getServerIp(), Settings.getServerPort());

        /*
         * Post server start
         */
    }
}