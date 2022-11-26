/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Logger;
import top.limbang.utils.WebsocketServer;

@Mod(
        modid = Messaging.MOD_ID,
        name = Messaging.MOD_NAME,
        version = Messaging.VERSION,
        acceptableRemoteVersions = "*"
)
public class Messaging {
    public static final String MOD_ID = "messaging";
    public static final String MOD_NAME = "Messaging";
    public static final String VERSION = "1.0.0-SNAPSHOT";


    @Mod.Instance(MOD_ID)
    public static Messaging instance;

    public static Logger logger;

    public MinecraftServer mcServer;
    public WebsocketServer websocketServer = new WebsocketServer();

    /**
     * 读取配置文件，注册物品，方块等需要调用GameRegistry的行为
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    /**
     * 监听服务器启动事件
     */
    @Mod.EventHandler
    public void onServerStartingEvent(FMLServerStartingEvent event) {
        mcServer = event.getServer();
    }

    /**
     * 监听服务器启动完成事件
     */
    @Mod.EventHandler
    public void onServerStartedEvent(FMLServerStartedEvent event) {
        new Thread(()-> websocketServer.create(2333),"WebSocketServer").start();

    }

    /**
     * 监听服务器停止事件
     */
    @Mod.EventHandler
    public void onServerStoppedEvent(FMLServerStoppedEvent event) {
        websocketServer.stop();
    }
}
