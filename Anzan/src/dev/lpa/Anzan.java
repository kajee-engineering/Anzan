package dev.lpa;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Anzan {

    // みとり暗算3級 3桁5口
    private static final int digit = 3;
    private static final int count = 5; // 口数
    private static final Scanner scanner = new Scanner(System.in);
    private static Logger logger;

    public static void main(String[] args) {
        mitoriAnzan();
    }

    static void mitoriAnzan() {
        while(true) {
            System.out.println("Are you ready??(y/n)");
            String input = scanner.nextLine();
            if (Objects.equals(input, "y")) {
                int[] question = generateThreeDigitNumber();
                printDigitNumber(question);
                TimerThread timerThread = new TimerThread();
                timerThread.start();

                scanner.nextLine();
                timerThread.interrupt();

                System.out.println("Enter your answer");
                int expect = Integer.parseInt(scanner.nextLine());
                int actual = 0;
                for (int num : question) {actual += num;}
                setLogger();
                logger.info(expect + ", " + actual + ", " + Arrays.toString(question));
                printAnswerAndProcess(question, expect, actual);

            } else if (Objects.equals(input, "n")) {
                System.out.println("bye");
                break;
            }
        }
    }

    static class TimerThread extends Thread {

        @Override
        public void run() {
            try {
                int second = 25;
                for (int i = second; 0 < i; i--) {
                    System.out.print("\r" + i + " ");
                    Thread.sleep(1000);
                }
                System.out.print("\r"+0);
                System.out.println("\nTime is up! (please enter Key)");
            } catch (InterruptedException e) {
                System.out.println("Timer interrupted!");
            }
        }
    }

    static void printAnswerAndProcess(int[] numbers, int expected, int actual) {
        System.out.printf("%-20s : %d%n", "Your answer", expected);
        System.out.printf("%-20s : %d%n", "Actual answer", actual);

        // 途中経過
        int step = 0;
        for (int num : numbers) {
            step += num;
            System.out.println(step + " ");
        }
    }

    static void printDigitNumber(int[] array) {
        System.out.println("-".repeat(20));
        for (int num : array) {
            System.out.println(num);
        }
        System.out.println("-".repeat(20));
    }

    static int[] generateThreeDigitNumber() {
        Random random = new Random();
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            if (digit == 3) {
                numbers[i] = 100 + random.nextInt(900);
            } else if (digit == 2) {
                numbers[i] = 10 + random.nextInt(90);
            } else {
                numbers[i] = random.nextInt(9);
            }
        }

        return numbers;
    }

    static void setLogger() {
        LogManager.getLogManager().reset(); // no output in console
        logger = java.util.logging.Logger.getLogger(Anzan.class.getName());
        String fileName = "calculation.log";
        try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
