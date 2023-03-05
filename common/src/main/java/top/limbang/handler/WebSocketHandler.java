/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.handler;

import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import top.limbang.MessagingServer;
import top.limbang.entity.UserMessage;

import static top.limbang.Messaging.logger;
import static top.limbang.utils.WebsocketServer.gson;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final DefaultChannelGroup clients;

    public WebSocketHandler(DefaultChannelGroup clients) {
        this.clients = clients;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        logger.info("客户端:{}" + ctx.channel().id().asShortText() + "发来消息 " + msg.text());
        try {
            UserMessage userMessage = gson.fromJson(msg.text(), UserMessage.class);
            if (userMessage.getName() != null && userMessage.getMessage() != null)
                MessagingServer.sendMessageToAllPlayer(userMessage);
        } catch (JsonSyntaxException e) {
            logger.error("Json语法错误:" + msg.text());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        clients.add(ctx.channel());
        logger.info("客户端:" + ctx.channel().id().asShortText() + " 连接成功...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端:" + ctx.channel().id().asShortText() + " 断开连接...");
    }
}
