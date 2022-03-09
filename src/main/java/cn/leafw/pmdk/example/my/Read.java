package cn.leafw.pmdk.example.my;

import com.intel.pmem.llpl.MemoryPool;

import java.util.Random;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2022/3/3
 */
public class Read {

    public static void main(String[] args) {
        String path = "/mnt/pmem0/weigong";
    }

    private static void read(String path) {
        MemoryPool memoryPool = MemoryPool.openPool(path);
        Random random = new Random();
        int anInt = memoryPool.getInt(0);
    }
}

