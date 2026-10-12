package com.example.app.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class EngineProperties {
    
    private final int HTTP_PORT = 8080;

    public void run() throws Exception {
        EventLoopGroup connectionGroup = new NioEventLoopGroup(); // соединения
        EventLoopGroup trafficGroup = new NioEventLoopGroup(); // трафик

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                .group(connectionGroup, trafficGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer()) // Поменять на свой иницализатор
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channel = bootstrap.bind(HTTP_PORT).sync();
            
            channel.channel().closeFuture().sync();
        } finally {
            trafficGroup.shutdownGracefully();
            connectionGroup.shutdownGracefully();
        }
    }

}
