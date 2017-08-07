package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.After;
import org.junit.Test;

import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Created by hunger on 2017/8/4.
 */

public class GateServerHandlerTest {

    private AtomicInteger pictureGenerator = new AtomicInteger(0);
    @Test
    public void test01() {
        System.out.println(pictureGenerator.incrementAndGet());
        ByteBuf buf = Unpooled.copiedBuffer("a".getBytes());
        System.out.println(buf.getByte(0));
    }
}