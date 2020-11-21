package generic;

import java.util.Comparator;

public class Heuristic implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        return a.getHeuristicCost() < b.getHeuristicCost() ? -1 : a.getHeuristicCost() == b.getHeuristicCost() ? 0 : 1;
    }
}