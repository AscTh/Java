package GraphBase;

import java.util.*;

public class GraphBase {

    private static int count = 0, time = 1;
    private static Stack<Vertex> S = new Stack<>();

    private static class Vertex {
        int time = 0, comp = -1, low, n;
        List<Vertex> lis_t;

        Vertex(int n) {
            this.n = n;
            lis_t = new ArrayList<>();
        }
    }

    private static void Tarjan(List<Vertex> G) {
        for (Vertex u : G)
            if (u.time == 0)
                VisitVertexTarjan(G, u, S);
    }

    private static void VisitVertexTarjan(List<Vertex> G, Vertex v, Stack<Vertex> S) {
        v.time = v.low = time++;
        S.push(v);
        for (Vertex u : v.lis_t) {
            if (u.time == 0)
                VisitVertexTarjan(G, u, S);
            if (u.comp == -1 && v.low > u.low)
                v.low = u.low;
        }
        if (v.time == v.low) {
            Vertex u;
            do {
                u = S.pop();
                u.comp = count;
            } while (u != v);
            count++;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(), M = in.nextInt();
        List<Vertex> G = new ArrayList<>(N);
        List<Vertex> base = new ArrayList<>();
        for (int i = 0; i < N; G.add(new Vertex(i++))) ;
        for (int i = 0; i < M; i++) G.get(in.nextInt()).lis_t.add(G.get(in.nextInt()));
        Tarjan(G);
        int incidence_matrix[][] = new int[count][count];
        for (Vertex v : G)
            for (Vertex u : v.lis_t)
                if (v.comp != u.comp)
                    incidence_matrix[v.comp][u.comp] = 1;
        loop:
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++)
                if (incidence_matrix[j][i] == 1)
                    continue loop;
            final int c = i;
            G.stream().filter(v -> v.comp == c).min(Comparator.comparingInt(v -> v.n)).map(base::add);
        }
        base.sort(Comparator.comparingInt(v -> v.n));
        base.forEach((x) -> System.out.print(x.n + " "));
    }
}