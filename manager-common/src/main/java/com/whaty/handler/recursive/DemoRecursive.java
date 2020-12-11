package com.whaty.handler.recursive;

/**
 * 尾递归demo
 *
 * @author weipengsen
 */
public class DemoRecursive {


    public static void main(String[] args) {
        // 尾递归无异常
        System.out.println(demo(1000000, 0).invoke());
        // 普通递归溢出
        System.out.println(normalRecursive(1000000, 0));
    }

    private static TailRecursion<Integer> demo(int count, int total) {
        if (count == 0) {
            return RecursiveInvoker.done(total);
        }
        return RecursiveInvoker.call(() -> demo(count - 1, count + total));
    }

    private static int normalRecursive(int count, int total) {
        if (count == 0) {
            return total;
        }
        return normalRecursive(count - 1, count + total);
    }

}
