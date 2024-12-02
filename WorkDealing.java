import java.util.concurrent.*;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class WorkDealing {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the array: ");
        int size = scanner.nextInt();
        System.out.print("Enter the min value of the elements: ");
        int min = scanner.nextInt();
        System.out.print("Enter the max value of the elements: ");
        int max = scanner.nextInt();

        // Генерація масиву
        int[] array = generateArray(size, min, max);
        System.out.println("Array is generated: ");
        printArray(array);

        // Work Dealing
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        long startTime = System.currentTimeMillis();
        int workDealingResult = executeWorkDealing(array, executorService);
        long workDealingTime = System.currentTimeMillis() - startTime;
        System.out.println("Work Dealing result: " + workDealingResult + ", Time: " + workDealingTime + " ms");

        executorService.shutdown();
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

    // Реалізація Work Dealing через ExecutorService
    private static int executeWorkDealing(int[] array, ExecutorService executorService) {
        int numTasks = Runtime.getRuntime().availableProcessors();
        int chunkSize = array.length / numTasks;
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < numTasks; i++) {
            int start = i * chunkSize;
            int end = (i == numTasks - 1) ? array.length : start + chunkSize;

            futures.add(executorService.submit(() -> {
                int sum = 0;
                for (int j = start; j < end - 1; j += 2) {
                    sum += array[j] + array[j + 1];
                }
                return sum;
            }));
        }

        int totalSum = 0;
        for (Future<Integer> future : futures) {
            try {
                totalSum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return totalSum;
    }
}