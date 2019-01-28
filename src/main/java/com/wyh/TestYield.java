package com.wyh;

public class TestYield {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("t1");
        MyThread t2 = new MyThread("t2");
        t1.start();
        t2.start();
    }
}

class MyThread extends Thread {
    MyThread(String s) {
        super(s);
    }

    public void run() {
        for (int i = 0; i <= 5; i++) {
            System.out.println(getName() + ":" + i);
            if (("t1").equals(getName())) {
                if (i == 0) {
                    yield();
                }
            }
        }
    }

}
