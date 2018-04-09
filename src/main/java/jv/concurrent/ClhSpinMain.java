package jv.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class ClhSpinMain {
    static int count = 0;

    public static void testLock(Lock lock) {
        try {
            lock.lock();
            for (int i = 0; i < 10000000; i++) ++count;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        final ClhSpinLock clh = new ClhSpinLock();
        final CyclicBarrier cb = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println(count);
            }
        });

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testLock(clh);
                    // 这段代码是非lock比较使用
//                    for (int i = 0; i < 10000000; i++)
//                        count++;
                    
                    try {
                        cb.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}