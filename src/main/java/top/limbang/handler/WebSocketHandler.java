/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import top.limbang.Messaging;
import top.limbang.entity.UserMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.limbang.Messaging.logger;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final DefaultChannelGroup clients;

    private final Pattern pattern;

    private final TextComponentString reply;

    public WebSocketHandler(DefaultChannelGroup clients) {
        this.clients = clients;
        // 消息正则
        pattern = Pattern.compile("name\":\"(.*?)\".*message\":\"(.*)\"");
        // 设置点击回复样式和点击事件
        reply = new TextComponentString( "点击回复");
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"群 ");
        reply.getStyle().setItalic(true).setColor(TextFormatting.AQUA).setClickEvent(clickEvent);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        logger.info("客户端:{} 发来消息 {}", ctx.channel().id().asShortText(), msg.text());
        Matcher m = pattern.matcher(msg.text());
        if (m.find()) {
            UserMessage userMessage = new UserMessage(m.group(1), m.group(2));
            // 拼接要发送对消息
            ITextComponent text = new TextComponentString(TextFormatting.DARK_GREEN + "[群]")
                    .appendText(TextFormatting.GOLD + " <"+ userMessage.getName() + "> ")
                    .appendText(TextFormatting.WHITE + userMessage.getMessage() + " ")
                    .appendSibling(reply);
            // 对所有玩家发送消息
            for (EntityPlayerMP player : Messaging.instance.mcServer.getPlayerList().getPlayers()) {
                player.sendMessage(text);
            }
        } else {
            logger.error("NO MATCH");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        clients.add(ctx.channel());
        logger.info("客户端:{} 连接成功...",ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端:{} 断开连接...",ctx.channel().id().asShortText());
    }
}
