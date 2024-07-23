package com.example.projekt;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {
    private Semaphore queueSem;
    private Semaphore seatsSem;
    private SimulationController controller;
    private Settings settings;
    private boolean seatsAv[];
    private final ReentrantLock protectSeats = new ReentrantLock();
    private final ReentrantLock protectWorkers = new ReentrantLock();
    private Semaphore workers[];
    ArrayList pValues;
    private boolean workersAv[][];

    public Queue(SimulationController controller, Settings settings){
        this.settings = settings;
        this.controller = controller;

        queueSem = new Semaphore(settings.getM());

        seatsSem = new Semaphore(settings.getL());
        seatsAv = new boolean[settings.getL()];
        for (int i = 0; i < settings.getL(); i++) {
            seatsAv[i] = true;
        }

        workers = new Semaphore[settings.getN()];
        pValues = settings.getPArray();
        for (int i = 0; i < settings.getN(); i++) {
            workers[i] = new Semaphore((int)pValues.get(i));
        }

        workersAv = new boolean[settings.getN()][];
        for (int i = 0; i < settings.getN(); i++) {
            workersAv[i] = new boolean[(int)pValues.get(i)];
            for (int j = 0; j < (int)pValues.get(i); j++) {
                workersAv[i][j] = true;
            }
        }
    }

    public int[] enter(int color, long delay) throws InterruptedException {
        queueSem.acquire();
        addNewCircle(color);
        Thread.sleep(1500);

        workers[color].acquire();
        protectWorkers.lock();
        int workerNo = -1;
        for (int i = 0; i < (int)pValues.get(color); i++) {
            if(workersAv[color][i]){
                workersAv[color][i] = false;
                workerNo = i;
                break;
            }
        }
        protectWorkers.unlock();

        seatsSem.acquire();
        int seatId = 0;
        protectSeats.lock();
        for (int i = 0; i < settings.getL(); i++) {
            if(seatsAv[i]){
                seatsAv[i] = false;
                seatId = i;
                break;
            }
        }
        moveToSeat(seatId, color, delay);
        moveWorker(color, workerNo, seatId);
        Thread.sleep(2000);
        protectSeats.unlock();
        queueSem.release();
        return new int[]{seatId, workerNo};
    }

    public void exit(int seatId, int workerNo, int color) throws InterruptedException {
        protectSeats.lock();
        protectWorkers.lock();
        exitFromSeat(seatId);
        exitWorker(color, workerNo);
        Thread.sleep(1300);
        seatsAv[seatId] = true;
        workersAv[color][workerNo] = true;
        protectWorkers.unlock();
        protectSeats.unlock();
        workers[color].release();
        seatsSem.release();
    }


    private void addNewCircle(int color){
        controller.addCircle(color);
    }

    private void moveToSeat(int seatId, int color, long delay){
        controller.moveCircleToSeat(seatId, color, delay);
    }

    private void exitFromSeat(int seatNo){
        controller.exitCircleFromSeat(seatNo);
    }

    private void moveWorker(int color, int workerNo, int seatId){
        controller.moveWorker(color, workerNo, seatId);
    }
    private void exitWorker(int color, int workerNo){
        controller.exitWorker(color, workerNo);
    }

}
