package site.halenspace.pocketcloud.threadpool.binarytree;


import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Halen Leo · 2022/6/6
 */
public class BinaryTree {

    // 一定要用 array 存储，因为要用索引随机访问
    ArrayList<Integer> res = new ArrayList<>();

    public List<Integer> largestValues(TreeNode root) {
        if (root == null) {
            return res;
        }
        traverse(root, 0);
        return res;
    }

    // 遍历二叉树
    void traverse(TreeNode root, int depth) {
        if (root == null) {
            return;
        }
        if (res.size() <= depth) {
            res.add(root.val);
        } else {
            // 记录当前行的最大值
            res.set(depth, Math.max(res.get(depth), root.val));
        }
        traverse(root.left, depth + 1);
        traverse(root.right, depth + 1);
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(3);
        treeNode.right = new TreeNode(2);
        treeNode.left.left = new TreeNode(5);
        treeNode.left.right = new TreeNode(3);
        treeNode.right.right = new TreeNode(9);
        new BinaryTree().largestValues(treeNode);
    }
}
