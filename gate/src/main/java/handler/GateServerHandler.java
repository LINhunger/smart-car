package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ActiveMQReceiverUtil;
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
    private static final String  PICTURE_PATH = "J:\\JAVA\\repo\\com\\qg\\smart-car\\picture\\";
    private AtomicInteger pictureGenerator = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client has connect !");
        //        sendInstruction(ctx);
        //唯一标识判断//todo
        String carId = "1";
        //开始接受并转发消息队列的指令
        deliverCommand(carId, ctx);
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
            byte b =buf.readByte();
            while(b == 0);
            while(buf.isReadable()){
                out.write(b);
                b =buf.readByte();
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

    /**
     * 指令测试
     * @param ctx
     * @throws InterruptedException
     */
    private void sendInstruction(ChannelHandlerContext ctx) throws InterruptedException {
        String ahead = "ahead#";//前
        String back = "back#";//后
        String left = "left#";//左
        String right = "right#";//右

        String low = "low#";//低速
        String middle = "middle#";//中速
        String high = "high#";//高速

        String open = "open#";//开启摄像头
        String close = "close#";//关闭摄像头

        String stop = "stop#";//停止

        ArrayList<String> list = new ArrayList();
        list.add(ahead);list.add(back);list.add(left);list.add(right);list.add(stop);
        list.add(low);list.add(middle);list.add(high);
        list.add(open);list.add(close);



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try{
                        Thread.sleep((long)( Math.random()*10000));
                    }catch (Exception e) {}
                    ctx.writeAndFlush(Unpooled.copiedBuffer(list.get((int) (Math.random()*list.size())), CharsetUtil.UTF_8));
                    System.out.println("command has send ");
                }
            }
        });
//        t.start();

    }


    private void deliverCommand(String carId, ChannelHandlerContext ctx)  {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                    try{
                        //从队列拉去指令并转发
                        System.out.println("队列创建 >> : "+carId);
                        ActiveMQReceiverUtil.queueReceiver(carId, ctx);
                    }catch (Exception e) {
                        System.err.println("deliverCommand method has been failed ");
                    }
                    System.out.println("队列销毁 >> : "+carId);
            }
        });
        t.start();
    }
}
