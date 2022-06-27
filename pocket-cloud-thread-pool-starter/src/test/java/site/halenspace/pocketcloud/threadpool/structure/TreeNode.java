package site.halenspace.pocketcloud.threadpool.structure;

/**
 * Definition for a binary tree node.
 *
 * @author Halen Leo · 2022/6/6
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
