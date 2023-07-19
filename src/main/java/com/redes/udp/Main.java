package com.redes.udp;

public class Main {
    public static void main(String[] args) {
        for (String num : "-200 / -300".replaceAll(" ", "").split("/", 0)) {
            System.out.println(num);
        }
    }
}