package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import util.FileUtil;

import java.io.*;


import java.io.*;



/**
 * Created by hunger on 2017/8/4.
 */
public class ClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DefaultClientHandler channelActive");
        testUpload(ctx);
        //关闭channel
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoClientHandler  channelInactive!!!");
        ctx.fireChannelInactive();
    }

    private static void  testUpload(ChannelHandlerContext ctx)  {
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //"C:\\Users\\Administrator\\Desktop\\smart-car-client\\temp"
                    File[] files  = FileUtil.getFilesByPathAndSuffix("J:\\project_smartCar\\temp",".jpg");
                    for (int i =0 ;i <files.length;i++) {
                        if(i%10 == 0)
                        System.out.println(files[i].getName());
                        ByteBuf buf = Unpooled.buffer();
                        InputStream in = new BufferedInputStream(new FileInputStream(files[i]));
                        int temp;
                        while ((temp = in.read()) != -1) {
                            buf.writeByte(temp);
                        }
                        in.close();
                        Thread.sleep(300L);
                        if (ctx.isRemoved()) {
                            break;
                        }
                        ctx.writeAndFlush(buf);
                        if ( i == files.length - 1) {
                            i = 0;
                        }
                    }
                }catch (Exception e) {
                    System.err.println("testUpload method has been failed ");
                }
                System.out.println("testUpload >> :  finish：耗时："+(System.currentTimeMillis()-startTime));
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        });
        t.start();
    }
}




