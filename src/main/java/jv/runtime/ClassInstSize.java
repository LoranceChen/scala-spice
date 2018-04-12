package jv.runtime;

/**
 * https://stackoverflow.com/questions/22110151/cost-of-each-class-in-java-application-fewer-huge-classes-or-several-smaller-o
 */
public class ClassInstSize {
    public static void main(String[] args) {
        Object[] array = new Object[10_000_000];
        Runtime rt = Runtime.getRuntime();
        long usedBefore = rt.totalMemory() - rt.freeMemory();
        for (int i = 0; i < array.length; i++ ) {
            array[i] = new Object();
        }
        long usedAfter = rt.totalMemory() - rt.freeMemory();
        System.out.println(usedBefore);
        System.out.println(usedAfter);
        System.out.println((double)(usedAfter - usedBefore) / array.length);
    }
}