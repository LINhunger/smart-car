package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dell on 2016/2/1.
 */
@ChannelHandler.Sharable
public class GateServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateServerHandler.class);
    private static final String  PICTURE_PATH = "D:\\JAVA\\IDEA\\IDEA\\smart-car\\picture\\";
    private AtomicInteger pictureGenerator = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("妈卖批，菜狗", CharsetUtil.UTF_8));
        System.out.println("client has connect !");
        FileUtil.createDir(PICTURE_PATH+this);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        ByteBuf buf = (ByteBuf) message;
        File filename = new File(PICTURE_PATH+this+"\\"+String.format("%05d",pictureGenerator.incrementAndGet())+".jpg");
        filename.createNewFile();
        try(
            OutputStream out = new BufferedOutputStream(
                    new FileOutputStream(filename))) {
            while(buf.isReadable()){
                out.write(buf.readByte());
            }
        }
        buf.release();
        LOGGER.info("byte stream write success!!!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("client has disconnect !");
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println(cause.getMessage());
//        ctx.fireExceptionCaught(cause);

    }
}
