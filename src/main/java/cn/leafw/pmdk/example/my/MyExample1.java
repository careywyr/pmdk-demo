package cn.leafw.pmdk.example.my;

import com.intel.pmem.llpl.Heap;
import com.intel.pmem.llpl.MemoryBlock;
import com.intel.pmem.llpl.MemoryPool;
import com.intel.pmem.llpl.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2022/2/26
 */
public class MyExample1 {

    private static final Long KB = 1024L;
    private static final Long MB = 1024L * 1024;
    private static final Long GB = 1024L * 1024 * 1024;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String path = "/home/wyr/temp/nvm";
        nvmWrite(path);
        long time1 = System.currentTimeMillis();
        System.out.println("write normal: {}" + String.valueOf(time1 - start));

        path = "/mnt/pmem0/weigong";
        nvmWrite(path);
        long start2 = System.currentTimeMillis();
        System.out.println("write dax: {}" + String.valueOf(start2 - time1));

        // mmap
        mmapWrite();
        System.out.println("mmap write: " + String.valueOf(System.currentTimeMillis() - start2));
    }

    private static void nvmWrite(String path) {
        boolean exists = MemoryPool.exists(path);
        if (!exists) {
            long start = System.currentTimeMillis();
            MemoryPool pool = MemoryPool.createPool(path, GB);
            long offset = 0;
            long size = GB;
            while (size != 0) {
                pool.setInt(offset, 1);
                size -= Integer.BYTES;
                offset += Integer.BYTES;
            }
        }else {
            System.out.println("exist " );
        }
    }

    private static void mmapWrite() {
        FileChannel fileChannel = null;
//        String path = "/Users/carey/Documents/paper/temp/mmap";
        String path = "/home/wyr/temp/pmem/mmap";
        try {
            byte[] data = new byte[4];
            RandomAccessFile file = new RandomAccessFile(path, "rw");
            FileChannel channel = file.getChannel();
            long size = GB;
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, GB);
            while (size != 0) {
                map.put(data);
                size -= data.length;
            }
            map.force();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

