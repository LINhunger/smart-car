package handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;



public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {

        // 流拷贝
//        try(
//        InputStream in = new BufferedInputStream(new FileInputStream("D:\\JAVA\\IDEA\\IDEA\\smart-car\\temp\\00001.jpg"))){
//            int temp;
//            while ((temp = in.read()) != -1) {
//                ctx.writeAndFlush(temp);
//            }
            ctx.writeAndFlush("aaaaaaaaaaaaadddddds");
            LOGGER.info(this+"   channelActive runs");
//        }
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOGGER.info(this+"channelReadComplete runs");
        ctx.flush();
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        LOGGER.info("received message: {}",message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        LOGGER.error(this+"exceptionCaught runs");
        cause.printStackTrace();
        ctx.close();
    }
}
