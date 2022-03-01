package cn.leafw.pmdk.example.mmap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2022/2/24
 */
public class MExample {

    private static final int KB4 = 4 * 1024;


    public static void main(String[] args) throws IOException {
        File file = new File("/Users/carey/Documents/paper/temp/testFile");
        // 方法一: 4kb 刷盘
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(KB4);
//        for (int i = 0; i < KB4; i++) {
//            byteBuffer.put((byte)0);
//        }
//        for (int i = 0; i < 1024 * 1024 * 1024; i += KB4) {
//            byteBuffer.position(0);
//            byteBuffer.limit(KB4);
//            fileChannel.write(byteBuffer);
//        }

        byteBuffer.put((byte)0);
        for (int i = 0; i < 1024 * 1024 * 1024; i += KB4) {
            byteBuffer.position(0);
            byteBuffer.limit(1);
            fileChannel.write(byteBuffer);
        }
    }
}

