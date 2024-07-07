package com.bak.libra26;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("Client[%s] is conencted", ctx.channel().remoteAddress()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        /* Do not anything */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
