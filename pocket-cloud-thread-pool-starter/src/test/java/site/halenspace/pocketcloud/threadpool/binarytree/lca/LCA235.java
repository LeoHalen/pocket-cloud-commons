package site.halenspace.pocketcloud.threadpool.binarytree.lca;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * @author Zg.Li · 2022/6/17
 */
public class LCA235 {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        int minVal = Math.min(p.val, q.val);
        int maxVal = Math.max(p.val, q.val);
        return find(root, minVal, maxVal);
    }

    public TreeNode find(TreeNode root, int pVal, int qVal) {
        if (root == null) return null;

        if (root.val == pVal || root.val == qVal) {
            return root;
        }


        if (pVal > root.val) {
            // 检索右子树
            return find(root.right, pVal, qVal);
        }

        if (qVal < root.val ) {
            // 检索左子树
            return find(root.left, pVal, qVal);
        }

        // pVal <= root.val <= qVal 表示当前节点就是最近公共祖先节点
        return root;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);
        root.left.right.left = new TreeNode(3);
        root.left.right.right = new TreeNode(5);

        TreeNode p = new TreeNode(0);
        TreeNode q = new TreeNode(5);
        new LCA235().lowestCommonAncestor(root, p, q);
    }
}
