package com.example.demo;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Target target = new Target();
        while (true) {
            String value = target.test();
            System.out.println(value);
            Thread.sleep(2000);
        }
    }
}