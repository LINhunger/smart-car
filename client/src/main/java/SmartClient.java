import handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by hunger on 2017/8/4.
 */
public class SmartClient {


    public static final int CLIENT_NUN = 1;
    private final String host;
    private final int port;

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartClient.class);

    public SmartClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ByteBuf delimiter  = Unpooled.copiedBuffer("#".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(64, delimiter));
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f1 = b.connect();
            ChannelFuture f2 = b.connect();

            //关闭连接
            f1.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        final String host = "127.0.0.1";  //10.21.48.11
        final int port = 10000;
        new SmartClient(host, port).start();
    }



    private static void startConnection(Bootstrap b,final int index) throws InterruptedException{
        ChannelFuture f = b.connect().sync()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            LOGGER.info("Client[{}] connected Gate Successed...", index);
                        } else {
                            LOGGER.error("Client[{}] connected Gate Failed", index);
                        }
                    }
                });
        f.channel().closeFuture().sync();
    }
}
