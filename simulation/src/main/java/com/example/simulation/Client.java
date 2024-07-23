package com.example.projekt;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Client extends Thread{
    Random random = new Random();
    private Queue queue;
    private Semaphore semaphore;
    private int color;
    public Client(Queue queue, Semaphore semaphore, int colors){
        this.queue = queue;
        this.semaphore = semaphore;
        this.color = random.nextInt(colors);
    }
    @Override
    public void run() {
        try {
            long delay = getRandomDelay();
            int data[] = queue.enter(color, delay - 1600);
            Thread.sleep(delay);
            queue.exit(data[0], data[1], color);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private long getRandomDelay() {
        return 5000 + random.nextInt(5000);
    }
}
