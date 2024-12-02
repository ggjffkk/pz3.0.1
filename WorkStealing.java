import java.util.concurrent.*;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class WorkStealing {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8")); // Налаштування кодування

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        // Користувацьке введення
        int size;
        int min;
        int max;

        System.out.print("Enter the size of the array: ");
        size = scanner.nextInt();

        do {
            System.out.print("Enter the min value of the elements: ");
            min = scanner.nextInt();
            System.out.print("Enter the max value of the elements: ");
            max = scanner.nextInt();

            if (max <= min) {
                System.out.println("The maximum value must be greater than the minimum. Try again ;)");
            }
        } while (max <= min);

        // Генерація масиву
        int[] array = generateArray(size, min, max);
        System.out.println("Array is generated: ");
        printArray(array);

        // Work Stealing
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        int forkJoinResult = forkJoinPool.invoke(new PairSumTask(array, 0, array.length));
        long forkJoinTime = System.currentTimeMillis() - startTime;
        System.out.println("Work Stealing result: " + forkJoinResult + ", Time: " + forkJoinTime + " ms");
    }

    // Генерація масиву
    private static int[] generateArray(int size, int min, int max) {
        Random random = new Random();
        return random.ints(size, min, max + 1).toArray();
    }

    // Друк масиву
    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if ((i + 1) % 10 == 0)
                System.out.println();
        }
        System.out.println();
    }

    // Реалізація Work Stealing через Fork/Join
    static class PairSumTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10000; // Граничний розмір задачі
        private int[] array;
        private int start, end;

        public PairSumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                int sum = 0;
                for (int i = start; i < end - 1; i += 2) {
                    sum += array[i] + array[i + 1];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                PairSumTask leftTask = new PairSumTask(array, start, mid);
                PairSumTask rightTask = new PairSumTask(array, mid, end);
                leftTask.fork();
                return rightTask.compute() + leftTask.join();
            }
        }
    }
}