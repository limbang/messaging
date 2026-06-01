/*
 * Copyright 2026 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 配置文件
 */
public class MessagingConfig {

    public int websocketPort = 2333;

    public static final String FILE_NAME = "messaging.properties";

    private static final Logger LOGGER = LogManager.getLogger("MessagingConfig");

    /**
     * 加载配置文件
     *
     * @return 配置文件
     */
    public static MessagingConfig load(Path configDir) {
        Path file = configDir.resolve(FILE_NAME);
        Properties props = new Properties();
        MessagingConfig config = new MessagingConfig();

        try {
            // 1. 文件不存在 → 创建默认
            if (Files.notExists(file)) {
                LOGGER.info("Config not found, creating default: {}", file);
                save(file,config);
                return config;
            }

            // 2. 文件存在 → 加载
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                props.load(reader);
            }

            // 3. 配置项不存在 → 创建
            String portStr = props.getProperty("websocket.port", "2333");
            try {
                config.websocketPort = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                config.websocketPort = 2333;
                LOGGER.error("Invalid port '{}', fallback to 2333", portStr);
            }

            LOGGER.info("Config loaded successfully port={}", config.websocketPort);

        } catch (IOException e) {
            LOGGER.error("Failed to load config file: {}", file, e);
        }

        return config;
    }

    /**
     * 保存配置文件
     *
     * @param config 配置文件
     */
    public static void save(Path file,MessagingConfig config) {
        Properties props = new Properties();

        // 写入配置项
        props.setProperty("websocket.port", String.valueOf(config.websocketPort));

        try {
            Files.createDirectories(file.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {

                // 手动写注释
                writer.write("# =========================================\n");
                writer.write("# Messaging Mod Configuration\n");
                writer.write("# =========================================\n\n");

                writer.write("# WebSocket 监听端口\n");
                writer.write("websocket.port=" + config.websocketPort + "\n");
            }

            LOGGER.info("Config saved: {}", file);

        } catch (IOException e) {
            LOGGER.error("Failed to save config file: {}", file, e);
        }
    }
}
