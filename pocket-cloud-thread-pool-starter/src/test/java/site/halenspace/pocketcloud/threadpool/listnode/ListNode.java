package site.halenspace.pocketcloud.threadpool.listnode;

/**
 * 链表节点定义
 * @author Halen Leo · 2022/3/22
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {}

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
