package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
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
        System.out.println("client has connect !");
        sendInstruction(ctx);
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

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//4
//                .addListener(ChannelFutureListener.CLOSE);
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("client has disconnect !");
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println(cause.getMessage());
        ctx.fireExceptionCaught(cause);

    }

    private void sendInstruction(ChannelHandlerContext ctx) throws InterruptedException {
        String ahead = "#ahead#";//前
        String back = "#back#";//后
        String left = "#left#";//左
        String right = "#leftright#";//右

        String low = "&low&";//低速
        String middle = "&low&";//中速
        String high = "&low&";//高速

        String take = "@take@";//开启摄像头
        String close = "@close@";//关闭摄像头

        ArrayList<String> list = new ArrayList();
        list.add(ahead);list.add(back);list.add(left);list.add(right);
        list.add(low);list.add(middle);list.add(high);
        list.add(take);list.add(close);


        int i =0;
        while (i++<1000) {
            Thread.sleep((long) Math.random()*1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer(list.get((int) (Math.random()*list.size())), CharsetUtil.UTF_8));
        }

    }
}
