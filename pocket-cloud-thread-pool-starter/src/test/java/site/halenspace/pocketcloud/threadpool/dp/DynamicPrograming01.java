package site.halenspace.pocketcloud.threadpool.dp;

import java.util.Arrays;

/**
 * @Author Zg.Li · 2022/3/7
 */
public class DynamicPrograming01 {

    public static boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if (sum % 2 != 0) {
            return false;
        }
        int w = sum / 2;
        int n = nums.length;
        boolean[][] dp = new boolean[n + 1][w + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }


        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= w; j++) {
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j - nums[i - 1]] || dp[i - 1][j];
                }
            }
        }

        for (int i = 0; i <= n; i++){
            for (int j = 0; j <= w; j++){
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }

        return dp[n][w];
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 5, 11, 5};
        canPartition(nums);
    }

    /**
     *
     * @param W
     * @param N
     * @param wt
     * @param val
     * @return
     */
    public static int[][] knapsack(int W, int N, int[] wt, int[] val) {

        // base case 初始化, dp[0][0...W] = 0, dp[0...N][0] = 0
        int[][] dp = new int[N + 1][W + 1];

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= W; j++) {
                if (j < wt[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - wt[i - 1]] + val[i - 1]);
                }
            }
        }
        return dp;
    }

    public static void main2(String[] args) {
//        int N = 3, W = 4;
//        int[] wt = new int[]{2, 1, 3};
//        int[] val = new int[]{4, 2, 3};
        int N = 3, W = 4;
        int[] wt = new int[]{1, 3, 4};
        int[] val = new int[]{15, 20, 30};

        int[][] dp = knapsack(W, N, wt, val);
        for (int i = 0; i <= N; i++){
            for (int j = 0; j <= W; j++){
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
    }

}
