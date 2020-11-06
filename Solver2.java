package com.example.lib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;



public class Solver2 {
    int numVar;
    public Stack<Integer> S = new Stack<>();
    public boolean [] visited = new boolean[numVar];
    public boolean [] visitedRev = new boolean[numVar];
    public HashMap<Integer, List<Integer>> adj = new HashMap<>();
    public HashMap<Integer, List<Integer>> adjRev = new HashMap<>();
    public int max = 15000;
    public int [] scc = new int[max];
    public int counter = 1;

    Solver2(int n){
        this.numVar = n;
        this.visited = new boolean[max];
        this.visitedRev = new boolean[max];
    }

    public void addEdge(int a, int b){
        if(adj.get(a)==null){
            adj.put(a,new ArrayList<Integer>());
            adj.get(a).add(b);
        }else{
            adj.get(a).add(b);
        }

    }

    public void addEdgeRev(int a,int b){
        if(adjRev.get(b)==null){
            adjRev.put(b,new ArrayList<Integer>());
            adjRev.get(b).add(a);
        }else{
            adjRev.get(b).add(a);
        }

    }

    public void dfs1(int u){

        if(visited[u] || adj.get(u)==null){ return;}
        visited[u] = true;
        for(int i=0;i<adj.get(u).size();i++){
            dfs1(adj.get(u).get(i));
        }
        S.push(u);
    }

    public void dfs2(int u){
        if(visitedRev[u] || adjRev.get(u)==null) return;
        visitedRev[u] = true;
        for(int i=0;i<adjRev.get(u).size();i++){
            dfs2(adjRev.get(u).get(i));

        }
        scc[u] = counter;

    }

    //this is void, but you can access the graph by calling the public adj and adjRev maps
    public void solve(int n, int m, List<Integer> literalA, List<Integer> literalB){
        for(int i=0;i<m;i++){
            if(literalA.get(i)>0 && literalB.get(i)>0){
                addEdge(literalA.get(i)+n,literalB.get(i));
                addEdgeRev(literalA.get(i)+n,literalB.get(i));
                addEdge(literalB.get(i)+n,literalA.get(i));
                addEdgeRev(literalB.get(i)+n,literalA.get(i));
            }
            else if(literalA.get(i)>0 && literalB.get(i)<0){
                addEdge(literalA.get(i)+n,n-literalB.get(i));
                addEdgeRev(literalA.get(i)+n,n-literalB.get(i));
                addEdge(-literalB.get(i),literalA.get(i));
                addEdgeRev(-literalB.get(i),literalA.get(i));
            }
            else if(literalA.get(i)<0 && literalB.get(i)>0){
                addEdge(-literalA.get(i),literalB.get(i));
                addEdgeRev(-literalA.get(i),literalB.get(i));
                addEdge(literalB.get(i)+n,n-literalA.get(i));
                addEdgeRev(literalB.get(i)+n,n-literalA.get(i));
            }
            else{
                addEdge(-literalA.get(i),n-literalB.get(i));
                addEdgeRev(-literalA.get(i),n-literalB.get(i));
                addEdge(-literalB.get(i),n-literalA.get(i));
                addEdgeRev(-literalB.get(i),n-literalA.get(i));
            }
        }
        /**
         n is number of variables
         so 2n is number of nodes.
         You want to do dfs on nodes, hence 2n
         **/
        //later can try i=0
        for(int i=1;i<=2*n;i++){

            if(!visited[i]){
                dfs1(i);
            }
        }

        while(!S.isEmpty()){
            int node = S.pop();
            if(!visitedRev[node]){

                dfs2(node);
                counter++;
            }
        }

        for(int i=1;i<=n;i++){
            if(scc[i] == scc[i+n] && scc[i] != 0){
                System.out.println("unsat");
                System.out.println(adj);
                return;
            }

        }
        System.out.println("sat");
        return;


    }



}
