/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import top.limbang.entity.UserMessage;
import top.limbang.utils.WebsocketServer;

public class MessagingServer {

    private static final WebsocketServer websocketServer = new WebsocketServer();

    private static MinecraftServer minecraftServer;

    private static final MutableComponent reply = Component.literal("点击回复");

    static {
        // 设置点击回复样式和点击事件
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "群 ");
        reply.setStyle(Style.EMPTY
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(clickEvent)
                .withItalic(true));
    }

    /**
     * 向所有玩家发送消息
     * @param msg 消息
     */
    public static void sendMessageToAllPlayer(UserMessage msg){
        // 拼接要发送对消息
        MutableComponent text = Component.literal (ChatFormatting.DARK_GREEN + "[群]")
                .append(ChatFormatting.GOLD + " <" + msg.getName() + "> ")
                .append(ChatFormatting.WHITE + msg.getMessage() + " ")
                .append(reply);
        // 对所有玩家发送消息
        minecraftServer.getPlayerList().broadcastSystemMessage(text,true);
    }

    public static void create(MinecraftServer server){
        minecraftServer = server;
        new Thread(() -> websocketServer.create(2333), "WebSocketServer").start();
    }

    public static void stop(){
        websocketServer.stop();
    }

    public static void broadcast(UserMessage msg){
        websocketServer.broadcast(msg);
    }

}
