import handler.GateServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TransUtil;

import java.net.InetSocketAddress;

/**
 * Created by hunger on 2017/8/3.
 */
public class GateServer {

    private static final Logger logger = LoggerFactory.getLogger(GateServer.class);

    /**
     * 配置服务器
     * @param port 端口号
     */
    public static void startGateServer(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel)
                            throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();

                        ByteBuf delimiter  = Unpooled.copiedBuffer(TransUtil.toByteArray("FFD9"));
                        pipeline.addLast("DelimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(100*1024, delimiter));
                        pipeline.addLast("ClientMessageHandler", new GateServerHandler());
                    }
                });
        //配置option
        bindConnectionOptions(bootstrap);
        //绑定端口
        bootstrap.bind(new InetSocketAddress(port)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    logger.info("[GateServer] Started Successed, registry is complete, waiting for client connect...");
                } else {
                    logger.error("[GateServer] Started Failed, registry is incomplete");
                }
            }
        });
    }

    protected static void bindConnectionOptions(ServerBootstrap bootstrap) {

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_LINGER, 0);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //心跳机制暂时使用TCP选项，之后再自己实现

    }

    public static void main(String[] args) {
        startGateServer(10000);
    }
}
