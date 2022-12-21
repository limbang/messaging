/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.forge.handler;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.limbang.Messaging;
import top.limbang.entity.UserMessage;
import top.limbang.MessagingServer;

@Mod.EventBusSubscriber
public class MessagingEventHandler {

    /**
     * 监听服务器聊天消息事件
     */
    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event) {
        // 判断前缀是否带群
        if (event.getMessage().startsWith("群")) {
            UserMessage msg = new UserMessage(event.getUsername(), event.getMessage().substring(1).trim());
            Messaging.logger.info("监听到要转发的消息:{}", msg);
            // 广播消息给客户端
            MessagingServer.broadcast(msg);
        }
    }

    /**
     * 监听服务器启动完成事件
     */
    @SubscribeEvent
    public static void onServerStartedEvent(ServerStartedEvent event) {
        MessagingServer.create(event.getServer());
    }

    /**
     * 监听服务器停止事件
     */
    @SubscribeEvent
    public static void onServerStoppedEvent(ServerStoppedEvent event) {
        MessagingServer.stop();
    }
}