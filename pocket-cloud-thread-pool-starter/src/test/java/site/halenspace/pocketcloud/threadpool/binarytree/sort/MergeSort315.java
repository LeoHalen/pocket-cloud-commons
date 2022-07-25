package site.halenspace.pocketcloud.threadpool.binarytree.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Halen Leo · 2022/6/15
 */
public class MergeSort315 {
    private static class Pair {
        int val;

        int index;

        public Pair(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }

    /* 合并使用的临时数组 */
    private Pair[] temp;
    /* 记录元素位置右侧小于该元素值的元素个数 */
    private int[] count;

    public List<Integer> countSmaller(int[] nums) {

        temp = new Pair[nums.length];
        count = new int[nums.length];

        Pair[] pairs = new Pair[nums.length];
        for (int i = 0; i < nums.length; i++) {
            pairs[i] = new Pair(nums[i], i);
        }

        sort(pairs, 0, nums.length - 1);

        List<Integer> result = new ArrayList<>();
        for (int c : count) {
            result.add(c);
        }

        return result;
    }

    public void sort(Pair[] pairs, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;

        // 递归遍历
        sort(pairs, left, mid);
        sort(pairs, mid + 1, right);

        // 后序位置merge
        merge(pairs, left, mid, right);
    }

    public void merge(Pair[] pairs, int left, int mid, int right) {
        // 合并的元素拷贝一份到temp数组中
        for (int i = left; i <= right; i++) {
            temp[i] = pairs[i];
        }

        // 使用双指针技巧合并左右部分的数组元素
        int leftIdx = left, rightIdx = mid + 1;
        for (int j = left; j <= right; j++) {
            if (leftIdx == mid + 1) {
                // 左半部分全部合并完成
                pairs[j] = temp[rightIdx++];
                continue;
            }

            if (rightIdx == right + 1) {
                // 右半部分全部合并完成
                pairs[j] = temp[leftIdx++];
                // 在左半部分元素合并的时候计算小于元素在原数组位置右侧的元素个数
                count[pairs[j].index] += rightIdx - mid - 1;
                continue;
            }

            if (temp[leftIdx].val > temp[rightIdx].val) {
                pairs[j] = temp[rightIdx++];
                continue;
            }

            // else nums[rightIdx] >= nums[leftIdx]的情况
            pairs[j] = temp[leftIdx++];
            // 在左半部分元素合并的时候计算小于元素在原数组位置右侧的元素个数
            count[pairs[j].index] += rightIdx - mid - 1;
        }

    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 9, 7, 8, 5};
        new MergeSort315().countSmaller(nums);
    }
}
