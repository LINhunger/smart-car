package handler;

import constant.GlobalConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import util.FileUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
        Future<Boolean> future =  executor.submit(task);
        if (future.get()) {
            if (pictureGenerator.incrementAndGet() %10 == 0)
            log.info("executor >> : execute: "+pictureGenerator.get());
        }
//        else {
//            pictureGenerator.set(0);
//            log.info("图片开始重新计数 ");
//        }
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("executor >> : shutdown");
//        executor.shutdown();
        ctx.fireChannelInactive();
    }



    private static class WriteTask implements Callable<Boolean> {

        private ByteBuf buf;
        private  AtomicInteger pictureGenerator;

        public WriteTask(ByteBuf buf, AtomicInteger pictureGenerator) {
            this.buf = buf;
            this.pictureGenerator = pictureGenerator;
        }

        @Override
        public Boolean call() throws Exception {

            File dirname = new File(GlobalConfig.PICTURE_PATH + GlobalConfig.CAR_ID );
            if (!dirname.exists()) {
                log.info("APP端连接,开始重新计数");
                pictureGenerator.set(0);
                FileUtil.createDir(GlobalConfig.PICTURE_PATH + GlobalConfig.CAR_ID);
                return false;
            }


            File tempfile = new File(GlobalConfig.PICTURE_PATH + GlobalConfig.CAR_ID + "\\" +GlobalConfig.TEMP_NAME);
            tempfile.deleteOnExit();
            tempfile.createNewFile();
            try (
                    OutputStream out = new BufferedOutputStream(
                            new FileOutputStream(tempfile))) {
                out.write(255);
                out.write(216);
                byte b;
                while ((b = buf.readByte()) == 0);
                buf.readByte();
                while (buf.isReadable()) {
                    b = buf.readByte();
                    out.write(b);
                }
                out.write(255);
                out.write(217);
                out.close();
                buf.release();
            }
            File picturefile = new File(GlobalConfig.PICTURE_PATH + GlobalConfig.CAR_ID + "\\" +String.format("%05d", pictureGenerator.get()) + ".jpg");
            picturefile.delete();
            FileUtil.renameFile(tempfile, picturefile);
            return true;
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
