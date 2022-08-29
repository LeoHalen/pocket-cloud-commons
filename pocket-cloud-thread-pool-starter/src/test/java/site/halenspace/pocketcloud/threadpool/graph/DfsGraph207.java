package site.halenspace.pocketcloud.threadpool.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Leetcode.207 题「课程表」
 *
 * @author Zg.Li · 2022/6/27
 */
public class DfsGraph207 {

    /* 标记遍历过的课程 */
    private boolean[] visited;

    /* 标记已经在路径上课程 */
    private boolean[] onPath;

    /* 标记是否有环，默认为无环 */
    private boolean hasCycle = false;

    /**
     * DFS解法
     * @param numCourses 这个学期必须选修 numCourses 门课程
     * @param prerequisites 先修课程按数组 prerequisites
     * @return 是否可能完成所有课程的学习
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = buildGraph(numCourses, prerequisites);

        visited = new boolean[numCourses];
        onPath = new boolean[numCourses];
        for (int i = 0; i < numCourses; i++) {
            traverse(graph, i);
        }

        // 无环表示可以完成课程，否则不可以
        return !hasCycle;
    }

    /* 判断图中是否有环 */
    public void traverse(List<List<Integer>> graph, int s) {

        if (onPath[s]) {
            // 重复出现在路径上的课程证明是存在环的
            hasCycle = true;
            return;
        }

        if (visited[s]) {
            // 课程s已经作为过起点则return
            return;
        }

        // 前序位置
        // 标记s已经在遍历过程的路径上了
        onPath[s] = true;
        // 标记课程s做为起点位置
        visited[s] = true;

        for (Integer t : graph.get(s)) {
            traverse(graph, t);
        }

        // 后序位置
        // 类似回溯过程，移出路径上的课程s
        onPath[s] = false;
    }

    /* 构建临界表的图 */
    public List<List<Integer>> buildGraph(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < numCourses; i++) {
            graph.add(new LinkedList<>());
        }

        for (int[] preRequisite : prerequisites) {
            // 表示from 指向 to的一条有向边
            int from = preRequisite[1], to = preRequisite[0];

            graph.get(from).add(to);
        }

        return graph;
    }
}
