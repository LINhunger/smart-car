package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import util.FileUtil;

import java.io.*;


/**
 * Created by hunger on 2017/8/4.
 */
public class DefaultHandler extends
        SimpleChannelInboundHandler<ByteBuf> {



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println("DefaultClientHandler channelActive");
        File[] files  = FileUtil.getFilesByPathAndSuffix("F:\\壮哉我大QG\\智能小车\\代码\\ffmpeg-20170724-03a9e6f-win64-static\\bin\\temp",".jpg");
        for (int i =0 ;i <files.length;i++) {
            System.out.println(files[i].getName());
            ByteBuf buf = Unpooled.buffer();
            InputStream in = new BufferedInputStream(new FileInputStream(files[i]));
            int temp;
            while ((temp = in.read()) != -1) {
                buf.writeByte(temp);
            }
            in.close();
            ctx.writeAndFlush(buf);
        }
        //关闭channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);

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
}
