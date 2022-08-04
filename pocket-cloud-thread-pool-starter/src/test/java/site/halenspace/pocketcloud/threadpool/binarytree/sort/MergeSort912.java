package site.halenspace.pocketcloud.threadpool.binarytree.sort;

import java.util.Random;

/**
 * Leecode.912 排序数组
 * 思路: 归并排序和快速排序等都能实现
 *
 * @author Halen Leo · 2022/6/15
 */
public class MergeSort912 {
    /* merge时的辅助数组 */
    private static int[] temp;

    /**
     * 归并排序解题
     * @param nums
     * @return
     */
    public int[] sortArray(int[] nums) {
        if (nums == null || nums.length < 1) return null;
        temp = new int[nums.length];
        sort(nums, 0, nums.length - 1);
        return nums;
    }

    public void sort(int[] nums, int left, int right) {
        // 一个元素的时候不需要排序
        if (left >= right) return;

        // 二分
        int mid = left + (right - left) / 2;

        sort(nums, left, mid);
        sort(nums, mid + 1, right);

        // 后序位置合并
        merge(nums, left, mid, right);
    }

    public void merge(int[] nums, int left, int mid, int right) {
        // 合并的元素先放到temp数组中
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }

        // 双指针推动左右部分数组索引移动合并
        int leftIdx = left, rightIdx = mid + 1;
        for (int j = left; j <= right; j++) {
            if (leftIdx == mid + 1) {
                // 左半部分全部合并完成
                nums[j] = temp[rightIdx++];
            } else if (rightIdx == right + 1) {
                nums[j] = temp[leftIdx++];
            } else if (temp[rightIdx] > temp[leftIdx]) {
                nums[j] = temp[leftIdx++];
            } else {
                nums[j] = temp[rightIdx++];
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{5, 2, 3, 1};
//        new MergeSort912().sortArray(nums);
        new MergeSort912.Quick2().sortArray(nums);
    }

    /* 快速排序解题 */
    static class Quick2 {
        public int[] sortArray(int[] nums) {

            sort(nums, 0, nums.length - 1);
            return nums;
        }

        public void sort(int[] nums, int left, int right) {
            if (left >= right) return;

            // 前序位置分片
            int p = partition(nums, left, right);

            sort(nums, left, p - 1);
            sort(nums, p + 1, right);
        }

        public Integer partition(int[] nums, int left, int right) {
            // 选择基准元素
            int base = nums[left];

            int l = left + 1, r = right;
            for (int i = l; i <= r; i++) {
                while (l < right && nums[l] <= base) {
                    l++;
                }

                while (r > left && nums[r] > base) {
                    r--;
                }

                if (l >= r) break;

                swap(nums, l, r);
            }

            swap(nums, left, r);
            return r;
        }

        public void swap(int[] nums, int l, int r) {
            if (l == r) return;
            nums[l] += nums[r];
            nums[r] = nums[l] - nums[r];
            nums[l] = nums[l] - nums[r];
        }
    }

    /* 快速排序带随机打乱的解题方式（因为快速排序是不稳定的排序算法） */
    static class Quick {

        public static void sort(int[] nums) {
            // 为了避免出现耗时的极端情况，先随机打乱
            shuffle(nums);
            // 排序整个数组（原地修改）
            sort(nums, 0, nums.length - 1);
        }

        private static void sort(int[] nums, int lo, int hi) {
            if (lo >= hi) {
                return;
            }
            // 对 nums[lo..hi] 进行切分
            // 使得 nums[lo..p-1] <= nums[p] < nums[p+1..hi]
            int p = partition(nums, lo, hi);

            sort(nums, lo, p - 1);
            sort(nums, p + 1, hi);
        }

        // 对 nums[lo..hi] 进行切分
        private static int partition(int[] nums, int lo, int hi) {
            int pivot = nums[lo];
            // 关于区间的边界控制需格外小心，稍有不慎就会出错
            // 我这里把 i, j 定义为开区间，同时定义：
            // [lo, i) <= pivot；(j, hi] > pivot
            // 之后都要正确维护这个边界区间的定义
            int i = lo + 1, j = hi;
            // 当 i > j 时结束循环，以保证区间 [lo, hi] 都被覆盖
            while (i <= j) {
                while (i < hi && nums[i] <= pivot) {
                    i++;
                    // 此 while 结束时恰好 nums[i] > pivot
                }
                while (j > lo && nums[j] > pivot) {
                    j--;
                    // 此 while 结束时恰好 nums[j] <= pivot
                }
                // 此时 [lo, i) <= pivot && (j, hi] > pivot

                if (i >= j) {
                    break;
                }
                swap(nums, i, j);
            }
            // 将 pivot 放到合适的位置，即 pivot 左边元素较小，右边元素较大
            swap(nums, lo, j);
            return j;
        }

        // 洗牌算法，将输入的数组随机打乱
        private static void shuffle(int[] nums) {
            Random rand = new Random();
            int n = nums.length;
            for (int i = 0 ; i < n; i++) {
                // 生成 [i, n - 1] 的随机数
                int r = i + rand.nextInt(n - i);
                swap(nums, i, r);
            }
        }

        // 原地交换数组中的两个元素
        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }
}
