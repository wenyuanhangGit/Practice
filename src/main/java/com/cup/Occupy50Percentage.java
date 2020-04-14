package com.cup;

public class Occupy50Percentage {

    public static void main(String[] args) throws InterruptedException {

        Long gap = 100L;

        while(true) {
            Long now = System.currentTimeMillis();
            while (System.currentTimeMillis() <= now + gap)
                ;
            Thread.sleep(gap);
        }
    }

}
