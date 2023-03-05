/*
 * Copyright 2023 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.events;

import me.shedaniel.architectury.event.events.ChatEvent;
import me.shedaniel.architectury.event.events.LifecycleEvent;
import net.minecraft.world.InteractionResultHolder;
import top.limbang.Messaging;
import top.limbang.MessagingServer;
import top.limbang.entity.UserMessage;


public class MessagingEvents {

    public static void register() {
        // 注册服务器启动完成事件监听
        LifecycleEvent.SERVER_STARTED.register(MessagingServer::create);

        // 注册服务器停止事件监听
        LifecycleEvent.SERVER_STOPPING.register(server -> MessagingServer.stop());

        // 注册客户端发给服务器聊天消息事件监听
        ChatEvent.SERVER.register((player, message, component) -> {
            // 判断前缀是否带群
            if (message.startsWith("群")) {
                UserMessage msg = new UserMessage(player.getName().getString(), message.substring(1).trim());
                Messaging.logger.info("监听到要转发的消息:" + msg);
                // 广播消息给客户端
                MessagingServer.broadcast(msg);
            }
            return InteractionResultHolder.pass(component);
        });
    }
}
