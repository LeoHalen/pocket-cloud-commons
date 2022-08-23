package site.halenspace.pocketcloud.threadpool.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zg.Li · 2022/6/28
 */
public class Graph210 {

    /* 记录遍历过的节点 */
    private boolean[] visited;

    /* 记录路径中遍历的节点 */
    private boolean[] onPath;

    /* 是否成环 */
    private boolean hasCycle = false;

    /* 图递归遍历时后序遍历结果 */
    private List<Integer> path;

    /**
     * DFS解法
     * @param numCourses 这个学期必须选修 numCourses 门课程
     * @param prerequisites 先修课程按数组 prerequisites
     * @return 是否可能完成所有课程的学习
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 使用邻接表构建图
        List<List<Integer>> graph = buildGraph(numCourses, prerequisites);

        // 初始化
        visited = new boolean[numCourses];
        onPath = new boolean[numCourses];
        path = new ArrayList<>();

        // 遍历图是否成环
        for (int i = 0; i < numCourses; i++) {
            traverse(graph, i);
        }

        // 如果成环肯定无法完成全部课程
        if (hasCycle) {
            return new int[]{};
        }

        // 反转的后序结果就是拓扑排序结果
        Collections.reverse(path);

        int[] res = new int[path.size()];
        for (int j = 0; j < path.size(); j++) {
            res[j] = path.get(j);
        }

        return res;
    }

    public void traverse(List<List<Integer>> graph, int s) {
        if (onPath[s]) {
            // 重复出现在一条路径上则表示存在环
            hasCycle = true;
            return;
        }

        if (visited[s]) {
            // 如果遍历过return
            return;
        }

        // 前序位置
        visited[s] = true;
        onPath[s] = true;

        for (Integer t : graph.get(s)) {
            traverse(graph, t);
        }

        // 后序位置
        onPath[s] = false;
        // 记录走过的路径
        path.add(s);
    }

    public List<List<Integer>> buildGraph(int numCourses, int[][] prerequisites) {

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new LinkedList<>());
        }

        for (int[] edge : prerequisites) {
            int from = edge[1], to = edge[0];
            graph.get(from).add(to);
        }

        return graph;
    }

}
