package com.kartersanamo;

import java.util.Scanner;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);

    static void main() throws InterruptedException {
        System.out.println("Welcome to the game of War");
        System.out.println("You will be playing against the computer, good luck!");
        System.out.println();
        System.out.println("Please select an option:");
        System.out.println("p) Play Against Computer");
        System.out.println("q) Quit");
        System.out.println("s) Simulate the game between 2 computers");

        String input = scanner.nextLine().toLowerCase();

        if (input.equals("p")) {
            System.out.println("Starting the Game...");
            System.out.println();
            Game game = new Game();
            game.startGame(false);
        }
        else if (input.equals("s")) {
            System.out.println("Simulating Game...");
            System.out.println();
            Game game = new Game();
            game.startGame(true);
        }
    }
}
