package generic;

import java.util.Comparator;

public class PathCost implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        return a.getPathCost() < ((mission.Node) b).getPathCost() ? -1 : ((mission.Node) b).getPathCost() == a.getPathCost() ? 0 : 1;
    }
}
