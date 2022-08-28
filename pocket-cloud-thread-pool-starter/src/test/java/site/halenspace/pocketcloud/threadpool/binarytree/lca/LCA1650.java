package site.halenspace.pocketcloud.threadpool.binarytree.lca;

/**
 * @author Zg.Li · 2022/6/17
 */
public class LCA1650 {

    public Node lowestCommonAncestor(Node p, Node q) {
        Node node1 = p, node2 = q;
        // 可以看似两条相交链表，两个指针分辨遍历两个链表直到走到头再去遍历对方的链表直到两个指针相遇，相遇位置就为相交节点
        while (node1 != node2) {
            if (node1 == null) {
                // 走到头换到q节点开始走
                node1 = q;
            } else {
                // 继续向下遍历
                node1 = node1.parent;
            }

            if (node2 == null) {
                // 走到头换到p节点开始走
                node2 = p;
            } else {
                // 继续向下遍历
                node2 = node2.parent;
            }
        }

        // 相交后node1或者node2都可以返回
        return node1;
    }

    static class Node {
        int val;
        Node left;
        Node right;
        Node parent;
    };
}
