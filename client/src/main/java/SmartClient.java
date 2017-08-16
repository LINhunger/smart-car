import handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hunger on 2017/8/4.
 */
public class SmartClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "10000"));
    public static final int CLIENT_NUN = 1;
    public static final int FREQUENCY = 100;  //ms

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartClient.class);

    public static void main(String[] args) throws Exception {
        beginPressTest();
    }

    public static void beginPressTest() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();

                        p.addLast(new ClientHandler());
                    }
                });

        // Start the client
        for(int i = 1; i <= CLIENT_NUN; i++) {
            startConnection(b, i);
        }
        group.shutdownGracefully().sync();
    }

    private static void startConnection(Bootstrap b,final int index) throws InterruptedException{
        ChannelFuture f = b.connect(HOST, PORT)
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
