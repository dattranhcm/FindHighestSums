/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenderratetest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author datth
 */
public class LenderrateTest {

   public void commonInput(int n) {
        int[][] input = new int[][]{
            {1},
            {5, 4, 3, 2, 1},
            {12, 10, 4, 1},
            {19, 15, 0,}

        };
        printResult(input, n);
    }

    public static void main(String[] args) {
        LenderrateTest test = new LenderrateTest();
        test.commonInput(1); // when n require one result, result is one highest sum
        test.commonInput(10); // when n require 10 result, result are 10 highest sum decrease
        test.commonInput(1000); // when n require very much result, return as many sums as possible

    }

    List findHighestSums(int[][] lists, int n) {
        /**
         * We need a place to store each result when a highest candidate is
         * found, in this case, an object like ElementResult is good choice We
         * need a place to store highest results will be found, that must make
         * sure these are unique, a HasSet is reasonable We need a place to
         * store all candidate will be found, that can be auto sort (for
         * convenience reason), use PriorityQueue is feasibility
         */
        List<Integer> result = new ArrayList<Integer>(); // to store result of sums
        HashSet<ElementResult> uniqueResult = new HashSet<>(); // to ensure the array element when proccess is uniq
        PriorityQueue<ElementResult> queue = new PriorityQueue<ElementResult>(Comparator.reverseOrder()); // store results that have sum lower priority 
        //Get first highest sum result from highest number from each list
        ElementResult firstResult = new ElementResult(lists.length);
        for (int i = 0; i < lists.length; i++) {
            firstResult.elementPositionArray[i] = 0;
            firstResult.sum = firstResult.sum + lists[i][0];
        }
        queue.add(firstResult);
        while (result.size() < n) { // start and still proccess when n result is not enought
            ElementResult highestInQueue = queue.poll(); // put current highest result to find next lower highest from left elements
            if (highestInQueue == null) {
                break;  //  when queue is empty, proccessing is done!!
            }
            if (uniqueResult.add(highestInQueue)) { //if this last highest result have not yet found before, continue proccess
                result.add(highestInQueue.sum);
                for (int i = 0; i < lists.length; i++) { //with this last highest result, continue found the next one best candidate in each left list
                    int[] tempArray = highestInQueue.elementPositionArray.clone();
                    if (tempArray[i] + 1 < lists[i].length) {  // to make sure this candidate number still have next to move
                        tempArray[i] = tempArray[i] + 1;
                        int closestValue = lists[i][highestInQueue.elementPositionArray[i]] - lists[i][tempArray[i]];// this is the value between current highest and next highest candidate number
                        int nextHighValue = highestInQueue.sum - closestValue; // value of next highest candidate
                        queue.add(new ElementResult(tempArray, nextHighValue)); // add this candidate to queue (defaul sort sum by desc)
                    }
                }
            }
        }
        return result;
    }

    class ElementResult implements Comparable<ElementResult> {

        int[] elementPositionArray;
        int sum;

        public ElementResult(int length) {
            elementPositionArray = new int[length];
            sum = 0;
        }

        public ElementResult(int[] array, int sum) {
            elementPositionArray = array;
            this.sum = sum;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ElementResult)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return Arrays.equals(this.elementPositionArray, ((ElementResult) obj).elementPositionArray);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(elementPositionArray);
        }

        @Override
        public int compareTo(ElementResult obj) {
            if (obj.sum > sum) {
                return -1;
            } else if (obj.sum < sum) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void printResult(int[][] input, int n) {
        LenderrateTest test = new LenderrateTest();
        List<Integer> result = test.findHighestSums(input, n);
        System.out.println(String.format("n= %s", n));
        for (int i : result) {
            System.out.print(String.format("%s ", i));
        }
        System.out.println();
    }

}
