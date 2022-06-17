package site.halenspace.pocketcloud.threadpool.decision;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Leetcode.870 优势洗牌
 * @author Halen Leo · 2022/6/6
 */
public class AdvantageShuffling {

    /**
     * 870. 优势洗牌
     * @param nums1 数组1
     * @param nums2 数组2
     * @return 数组1的最终排列
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {
        // 排序nums1
        Arrays.sort(nums1);
        // 构建nums2的优先级队列
        Queue<int[]> queue = new PriorityQueue<>((arr1, arr2) -> {
            return arr2[1] - arr1[1];
        });

        for (int i = 0; i < nums2.length; i++) {
            queue.offer(new int[]{i, nums2[i]});
        }

        int[] res = new int[nums1.length];
        int left = 0, right = nums1.length - 1;
        while (!queue.isEmpty()) {
            int[] pair = queue.poll();
            int idx = pair[0], value = pair[1];
            if (nums1[right] > value) {
                // 有优势就自己上
                res[idx] = nums1[right--];
            } else {
                res[idx] = nums1[left++];
            }
        }

        return res;
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{ 12, 24, 8, 32 };
        int[] nums2 = new int[]{ 13, 25, 32, 11 };
        new AdvantageShuffling().advantageCount(nums1, nums2);
    }


}
