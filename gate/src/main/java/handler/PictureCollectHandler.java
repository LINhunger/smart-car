package handler;

import constant.GlobalConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by 小排骨 on 2017/9/29.
 */
@Slf4j
public class PictureCollectHandler extends ChannelInboundHandlerAdapter {


    private  AtomicInteger pictureGenerator = new AtomicInteger(0);
    private static ExecutorService executor = Executors.newFixedThreadPool(1);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        ByteBuf buf = (ByteBuf) message;
        WriteTask task = new WriteTask(buf, pictureGenerator);
        executor.submit(task);
        log.info("executor >> : execute: "+pictureGenerator.get());
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("executor >> : shutdown");
        executor.shutdown();
        ctx.fireChannelInactive();
    }



    private static class WriteTask implements Callable<Boolean> {

        private ByteBuf buf;
        private AtomicInteger pictureGenerator;

        public WriteTask(ByteBuf buf, AtomicInteger pictureGenerator) {
            this.buf = buf;
            this.pictureGenerator = pictureGenerator;
        }

        @Override
        public Boolean call() throws Exception {
            File filename = new File(GlobalConfig.PICTURE_PATH + GlobalConfig.CAR_ID + "\\" + String.format("%05d", pictureGenerator.incrementAndGet()) + ".jpg");
            filename.createNewFile();
            try (
                    OutputStream out = new BufferedOutputStream(
                            new FileOutputStream(filename))) {
                byte b;
                while ((b = buf.readByte()) == 0);
                out.write(b);
                while (buf.isReadable()) {
                    b = buf.readByte();
                    out.write(b);
                }
                buf.release();
            }
            return true;
        }
    }





}
