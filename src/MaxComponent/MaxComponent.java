package MaxComponent;

import java.util.*;

public class MaxComponent {
    private static Vertex[] vertices;
    private static Edge[] edges;

    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        int vertex_count, edge_count;
        vertex_count = sn.nextInt();
        edge_count = sn.nextInt();
        vertices = new Vertex[vertex_count];
        for (int i = 0; i < vertex_count; ++i)
            vertices[i] = new Vertex(i);
        edges = new Edge[edge_count];
        for (int i = 0; i < edge_count; ++i) {
            Vertex v = vertices[sn.nextInt()], u = vertices[sn.nextInt()];
            Edge e = new Edge(v, u);
            v.add(e);
            if (v != u) u.add(e);
            edges[i] = e;
        }
        Component max = null;
        for (Vertex v : vertices) {
            if (!v.is_visited()) {
                Component cmp = calculateComponent(v);
                if (null == max)
                    max = cmp;
                else
                    max = max.gt(cmp);
            }
        }
        max.check();
        printGraph(vertices, edges);
    }

    private static Component calculateComponent(Vertex in) {
        Stack<Vertex> vSt = new Stack<Vertex>();
        List<Vertex> vLst = new LinkedList<Vertex>();
        List<Edge> eLst = new LinkedList<Edge>();
        Vertex v = in;
        vSt.push(v);
        v.pushed = true;
        while (!vSt.empty()) {
            v = vSt.pop();
            v.pushed = false;
            v.visit();
            vLst.add(v);
            for (Edge e : v) {
                Vertex tmp = (e.v == v ? e.u : e.v);
                if (tmp.is_visited()) {
                    if (tmp == v) eLst.add(e);
                    continue;
                }
                if (!tmp.pushed) {
                    vSt.push(tmp);
                    tmp.pushed = true;
                }
                eLst.add(e);
            }
        }
        return new Component(in, vLst, eLst);
    }

    private static void printGraph(Vertex[] vertices, Edge[] edges) {
        System.out.println("graph {");
        for (Vertex v : vertices) {
            System.out.print("    " + v.getId());
            if (v.is_max())
                System.out.print(" [color = red]");
            System.out.print("\n");
        }
        for (Edge e : edges) {
            System.out.print("    " + e.v.getId() + " -- " + e.u.getId());
            if (e.is_max())
                System.out.print(" [color = red]");
            System.out.print("\n");
        }
        System.out.print("}");
    }
}

class Edge {
    final Vertex v;
    final Vertex u;

    Edge(Vertex v, Vertex u) {
        this.v = v;
        this.u = u;
        this.max = false;
    }

    void check() {
        this.max = true;
    }

    boolean is_max() {
        return max;
    }

    private boolean max;
}

class Vertex implements Iterable<Edge> {
    boolean pushed;

    Vertex(int id) {
        this.id = id;
        this.adjacentList = new ArrayList<Edge>();
        this.visited = false;
        this.max = false;
        this.pushed = false;
    }

    int getId() {
        return id;
    }

    public void add(Edge e) {
        adjacentList.add(e);
    }

    void visit() {
        visited = true;
    }

    void check() {
        this.max = true;
    }

    boolean is_visited() {
        return visited;
    }

    boolean is_max() {
        return max;
    }

    public Iterator<Edge> iterator() {
        return adjacentList.iterator();
    }

    private int id;
    private List<Edge> adjacentList;
    private boolean visited;
    private boolean max;
}

class Component {
    Component(Vertex from, List<Vertex> vertexLst, List<Edge> edgeLst) {
        this.from = from;
        this.vertexLst = vertexLst;
        this.edgeLst = edgeLst;
    }

    Component gt(Component obj) {
        if (obj.vertexLst.size() != this.vertexLst.size())
            return (obj.vertexLst.size() > this.vertexLst.size() ? obj : this);
        if (obj.edgeLst.size() != this.edgeLst.size())
            return (obj.edgeLst.size() > this.edgeLst.size() ? obj : this);
        return (obj.from.getId() < this.from.getId() ? obj : this);
    }

    void check() {
        for (Vertex v : vertexLst)
            v.check();
        for (Edge e : edgeLst)
            e.check();
    }

    private Vertex from;
    private List<Vertex> vertexLst;
    private List<Edge> edgeLst;
}