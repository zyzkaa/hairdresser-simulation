package com.example.projekt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Settings {
    private int m; // waiting room capacity
    private int l; // seats
    private int p; // workers amount
    private int n; // services amount

    ArrayList<Integer> pArray = new ArrayList<Integer>();

    public Settings(){
        try {
            readDataFromFile();
        } catch (IOException e) {

        }
    }

    public void readDataFromFile() throws IOException {
        Properties properties = new Properties();
        InputStream file = getClass().getResourceAsStream("/data.properties");
        if(file == null){
            System.out.println("file not found");
        }
        properties.load(file);
        file.close();

        m = Integer.parseInt(properties.getProperty("m"));
        l = Integer.parseInt(properties.getProperty("l"));
        p = Integer.parseInt(properties.getProperty("p"));
        n = Integer.parseInt(properties.getProperty("n"));
        pArray = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            pArray.add(Integer.parseInt(properties.getProperty("p"+(i+1))));
        }
    }

    public int[] exportAsArray () {
        return new int[]{m, l, p, n};
    }

    public ArrayList<Integer> getPArray(){
        return pArray;
    }

    public void setPArray(ArrayList<Integer> newArray) {
        pArray = newArray;
    }

    public void reassignValues() {
        for (int i = 0; i < pArray.size(); i++) {
            pArray.set(i, 0);
        }
        for (int i = 0; i < p; i++) {
            int index = i%n;
            pArray.set(index, pArray.get(index) + 1);
        }
    }

    public void changeArraySize(){
        int newSize = n;
        int currentSize = pArray.size();

        if(newSize == currentSize){
            return;
        }

        if(newSize > currentSize) {
            for (int i = 0; i < newSize - currentSize; i++) {
                pArray.add(0);
            }
        } else if (newSize < currentSize){
            int roznica = currentSize - newSize;
            for (int i = currentSize - newSize; i > 0; i--) {
                pArray.remove(i);
            }
        }
        reassignValues();
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }
}
