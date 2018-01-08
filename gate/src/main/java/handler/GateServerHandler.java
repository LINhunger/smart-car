package handler;

import constant.GlobalConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import util.ActiveMQReceiverUtil;
import util.FileUtil;

import java.io.*;
import java.util.ArrayList;



/**
 * Created by Dell on 2016/2/1.
 */
@Slf4j
public class GateServerHandler extends ChannelInboundHandlerAdapter {

//    InheritableThreadLocal<Boolean> flag = new InheritableThreadLocal<Boolean>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client has connect !");
        //        sendInstruction(ctx);
        //唯一标识判断//todo
        String carId = "1";
        //开始接受并转发消息队列的指令
//        flag.set(Boolean.TRUE);
        deliverCommand(carId, ctx);
        //TODO
        FileUtil.deleteAllFiles(new File(GlobalConfig.PICTURE_PATH+GlobalConfig.CAR_ID));
        FileUtil.createDir(GlobalConfig.PICTURE_PATH+GlobalConfig.CAR_ID);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        ctx.fireChannelRead(message);
    }


//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//4
//                .addListener(ChannelFutureListener.CLOSE);
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("client has disconnect !");
        FileUtil.deleteAllFiles(new File(GlobalConfig.PICTURE_PATH+GlobalConfig.CAR_ID));
//        flag.set(Boolean.FALSE);
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
//        flag.set(Boolean.FALSE);
        System.out.println(cause.getMessage());
        ctx.fireExceptionCaught(cause);

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
}
