package Sortowanie;

import java.util.Arrays;
import java.util.Random;

import static java.lang.StrictMath.random;
import static java.util.Arrays.sort;

class ThretSort implements Runnable {
    private int[] a;
    private int threadCount;

    public ThretSort(int[] a, int threadCount) {
        this.a = a;
        this.threadCount = threadCount;
    }

    @Override
    public void run() {
        Main.parallelMergeSort(a, threadCount);
    }
}

public class Main {

    private static Random Rand = new Random();

    public static void main(String[] args) throws Throwable {


        int length = 30;   // inicjalizacja tablicy
        int runs = 2;   // ile razy ma podwoić rozmiar tablicy

        for (int i = 1; i <= runs; i++) {
            int[] a = createRandomArray(length);
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[j] + " ");
            }


            long startTime1 = System.currentTimeMillis();
            parallelMergeSort(a);

            long endTime1 = System.currentTimeMillis();


            if (!isSorted(a)) {
                throw new RuntimeException("źle posortowana");
            }

            System.out.printf("%10d elementów  =>  %6d ms \n", length, endTime1 - startTime1);
            length *= 2;
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[j] + " ");
            }


        }
    }

    public static void parallelMergeSort(int[] a) {
        // int cores = Runtime.getRuntime().availableProcessors();
        int levelthreads = 5;
        parallelMergeSort(a, levelthreads);
    }

    public static void parallelMergeSort(int[] a, int threadCount) {
        if (threadCount <= 1) {
            insertionSort(a);
        } else if (a.length >= 2) {
            // split array in half
            int[] left = Arrays.copyOfRange(a, 0, a.length / 2);
            int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);

            Thread lThread = new Thread(new ThretSort(left, threadCount - 1));
            Thread rThread = new Thread(new ThretSort(right, threadCount - 1));
            lThread.start();
            rThread.start();

            try {
                lThread.join();
                rThread.join();
            } catch (InterruptedException ie) {
            }

            // łączy tablice z powrotem
            merge(left, right, a);
        }


    }

    // dzieli i łączy tablice
    public static void mergeSort(int[] a) {
        if (a.length >= 2) {
            // dzieli na pół tablice
            int[] left = Arrays.copyOfRange(a, 0, a.length / 2);
            int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);

            // dzieli i sortuje połówki, rekurencja
            mergeSort(left);
            mergeSort(right);

            // łączy połówki
            merge(left, right, a);

        }
    }

    // łączy połówki tablic posortowanych
    public static void merge(int[] left, int[] right, int[] a) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < a.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                a[i] = left[i1];
                i1++;
            } else {
                a[i] = right[i2];
                i2++;
            }
        }
    }

    public static void insertionSort(int arr[]) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }

    }

    public static boolean isSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }


    // generuje tablice losową
    public static int[] createRandomArray(int length) {
        int[] a = new int[length];
        for (int i = 0; i < a.length; i++) {
            a[i] = Rand.nextInt(90) + 10;

        }
        return a;
    }
}