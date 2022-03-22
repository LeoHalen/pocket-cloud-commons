package site.halenspace.pocketcloud.threadpool.listnode.reverse;

import site.halenspace.pocketcloud.threadpool.listnode.ListNode;

/**
 * 链表翻转
 * @author Halen Leo · 2022/3/22
 */
public class ListNodeReverse {

    private static ListNode successor;

    /**
     * 反转前n个节点
     * @param head
     * @param n
     * @return
     */
    public ListNode reverseN(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        if (n == 1) {
            successor = head.next;
            return head;
        }

        ListNode lastNode = reverseN(head.next, n - 1);

        // 反转指针
        head.next.next = head;
        // 将反转的节点与后边节点连接起来
        head.next = successor;
        return lastNode;
    }

    /**
     *
     * @param head
     * @param left
     * @param right
     * @return
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {

        return null;
    }
}
