package jv.concurrent;

/**
 *
 */
public class LockTest {
    public static void main(String[] args) {
        final Outputter1 output = new Outputter1();
        new Thread() {
            public void run() {
                output.output("zhangsan");
            };
        }.start();
        new Thread() {
            public void run() {
                output.output("lisi");
            };
        }.start();
    }
}
