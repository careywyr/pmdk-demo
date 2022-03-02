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

        String path = "/mnt/pmem0/weigong";
        long start2 = System.currentTimeMillis();
        nvmWrite(path);
        System.out.println("write dax: {}" + String.valueOf(System.currentTimeMillis() - start2));

        path = "/home/wyr/temp/nvm";
        long start = System.currentTimeMillis();
        nvmWrite(path);
        System.out.println("write normal: {}" + String.valueOf(System.currentTimeMillis() - start));


        // mmap
        long time3 = System.currentTimeMillis();
        mmapWrite();
        System.out.println("mmap write: " + String.valueOf(System.currentTimeMillis() - time3));
    }

    private static void nvmWrite(String path) {
//        boolean exists = MemoryPool.exists(path);
//        if (!exists) {
//
//        }else {
//            System.out.println("exist " );
//        }
        long start = System.currentTimeMillis();
        MemoryPool pool = MemoryPool.createPool(path, GB);
        System.out.println("创建内存池消耗时间: " + String.valueOf(System.currentTimeMillis() - start));
        long offset = 0;
        long size = GB;
        while (size != 0) {
            pool.setInt(offset, 1);
            size -= Integer.BYTES;
            offset += Integer.BYTES;
        }
        pool.flush(0, GB);
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

