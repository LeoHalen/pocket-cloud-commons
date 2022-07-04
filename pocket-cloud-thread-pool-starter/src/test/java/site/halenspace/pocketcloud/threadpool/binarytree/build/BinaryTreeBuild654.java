package site.halenspace.pocketcloud.threadpool.binarytree.build;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * @author Halen Leo · 2022/6/13
 */
public class BinaryTreeBuild654 {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2, 1, 6, 0, 5};
        new BinaryTreeBuild654().constructMaximumBinaryTree(nums);
    }

    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return build(nums, 0, nums.length - 1);
    }

    public TreeNode build(int[] nums, int low, int high) {
        // 判断low索引位置小于high索引位置
        if (low > high) return null;

        // 寻找数组索引区间中最大的值和索引位置
        int maxIdx = 0, maxVal = Integer.MIN_VALUE;
        for (int i = low; i <= high; i++) {
            if (nums[i] > maxVal) {
                maxIdx = i;
                maxVal = nums[i];
            }
        }

        // 构造根节点
        TreeNode root = new TreeNode(maxVal);
        // 递归构建左右子树
        root.left = build(nums, low, maxIdx - 1);
        root.right = build(nums, maxIdx + 1, high);

        return root;
    }

}
