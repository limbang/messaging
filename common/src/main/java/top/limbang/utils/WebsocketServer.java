/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.utils;

import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import top.limbang.entity.UserMessage;
import top.limbang.handler.WebSocketHandler;

import static top.limbang.Messaging.logger;

public class WebsocketServer {

    public static final Gson gson = new Gson();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 用于记录和管理所有客户端的channel
     */
    private final DefaultChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void create(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // HTTP 协议解析，用于握手阶段
                        ch.pipeline().addLast(new HttpServerCodec());
                        // 对写大数据流的支持
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        // 对http消息的聚合，聚合成FullHttpRequest或FullHttpResponse
                        // 在Netty的编程中，几乎都会使用到这个handler
                        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 64));
                        // WebSocket 数据压缩扩展
                        ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                        // WebSocket 握手、控制帧处理
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/", null, true));
                        // WebSocket 消息处理
                        ch.pipeline().addLast(new WebSocketHandler(clients));
                    }
                });
        try {
            // 绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Start WebSocket Server 0.0.0.0:" + port);
            // 一直阻塞直到服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage());
            stop();
        }

    }

    /**
     * 停止服务器
     */
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    /**
     * 广播用户消息给所有客户端
     *
     * @param msg 用户消息
     */
    public void broadcast(UserMessage msg) {
        clients.writeAndFlush(new TextWebSocketFrame(gson.toJson(msg)));
    }
}



