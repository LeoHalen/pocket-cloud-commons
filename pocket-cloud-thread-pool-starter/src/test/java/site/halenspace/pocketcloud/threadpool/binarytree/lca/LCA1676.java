package site.halenspace.pocketcloud.threadpool.binarytree.lca;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

import java.util.HashSet;

/**
 * @author Zg.Li · 2022/6/20
 */
public class LCA1676 {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {

        HashSet<Integer> nodeValues = new HashSet<>();
        // 转换为HashSet
        for (TreeNode node : nodes) {
            nodeValues.add(node.val);
        }

        // 相交后node1或者node2都可以返回
        return find(root, nodeValues);
    }

    public TreeNode find(TreeNode root, HashSet<Integer> nodeValues) {
        if (root == null) return null;

        // 前序位置
        if (nodeValues.contains(root.val)) {
            return root;
        }

        TreeNode left = find(root, nodeValues);
        TreeNode right = find(root, nodeValues);

        if (left != null && right != null) {
            // 找到LCA节点
            return root;
        }

        // 返回找到的左子树或右子树
        return left != null ? left : right;
    }

    public int countNodes(TreeNode root) {
        TreeNode l = root, r = root;
        // 记录左、右子树的高度
        int hl = 0, hr = 0;
        while (l != null) {
            l = l.left;
            hl++;
        }
        while (r != null) {
            r = r.right;
            hr++;
        }
        // 如果左右子树的高度相同，则是一棵满二叉树
        if (hl == hr) {
            return (int)Math.pow(2, hl) - 1;
        }
        // 如果左右高度不同，则按照普通二叉树的逻辑计算
        return 1 + countNodes(root.left) + countNodes(root.right);
    }
}
