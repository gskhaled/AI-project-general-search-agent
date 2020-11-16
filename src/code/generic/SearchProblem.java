package generic;

import mission.Node;
import mission.Operator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class SearchProblem {
    private static HashMap<String, String> nodesPassed = new HashMap<String, String>();
    private generic.Operator[] operators;
    private static int expandedNodes = 0;


    // returns state given grid, current node and operator to perform
    public abstract Node stateSpace(Node node, Operator operator);

    //returns true if a given node passes the goal test, otherwise false
    public abstract Boolean goalTest(Node node);

    public abstract Node getInitialState();

    public abstract Operator[] getOperators();

    public static String returnPath(Node n) {
        if (n.getParent() == null || n.getParent().getOperator() == null)
            return n.getOperator().toString().toLowerCase() + "";
        else
            return returnPath(n.getParent()) + "," + n.getOperator().toString().toLowerCase();
    }

    public static String formulateNodeToString(Node state) {
        return state.getX() + "," + state.getY() + ";" + Arrays.toString(state.getIMFstates()) + ";" + state.getC();
    }

    public static String convertArrayToString(short[] arr) {
        String res = "";
        for (int i = 0; i < arr.length; i++) {
            if (i < arr.length - 1)
                res += arr[i] + ",";
            else
                res += arr[i];
        }
        return res;
    }

    public static String printArray(short[] arr) {
        String s = "[";
        for (int i = 0; i < arr.length; i++)
            s += arr[i] + ", ";
        s += "]";
        return s;
    }

    public static String search(SearchProblem problem, QueuingFunction queuingFunction) {

        String res = "";
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.addLast(problem.getInitialState());

        switch (queuingFunction) {
            case ENQUEUE_AT_END: {
                while (!queue.isEmpty()) {
                    Node curr = queue.removeFirst();
                    String stringCurr = formulateNodeToString(curr);
                    if (nodesPassed.get(stringCurr) == null) {
                        nodesPassed.put(stringCurr, stringCurr);
                        if (curr != null) {
                            if (problem.goalTest((curr)))
                                return returnPath(curr) + ";" + curr.getDeaths() + ";" + convertArrayToString(curr.getDamages()) + ";" + expandedNodes;
                            expandedNodes++;
                            for (int i = 0; i < problem.getOperators().length; i++) {
                                Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                                if (node != null)
                                    queue.addLast(node);
                            }
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT: {
                while (!queue.isEmpty()) {
                    Node curr = queue.removeFirst();
                    String stringCurr = formulateNodeToString(curr);
                    if (nodesPassed.get(stringCurr) == null) {
                        nodesPassed.put(stringCurr, stringCurr);
                        if (curr != null) {
                            if (problem.goalTest((curr)))
                                return returnPath(curr) + ";" + curr.getDeaths() + ";" + convertArrayToString(curr.getDamages()) + ";" + expandedNodes;
                            expandedNodes++;
                            for (int i = 0; i < problem.getOperators().length; i++) {
                                Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                                if (node != null)
                                    queue.addFirst(node);
                            }
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT_IDS: {
                int currentDepth = 0;
                while(true) {
                    while (!queue.isEmpty()) {
                        Node curr = queue.removeFirst();
                        if(!(curr.getDepth() > currentDepth)) {
                            String stringCurr = formulateNodeToString(curr);
                            if (nodesPassed.get(stringCurr) == null) {
                                nodesPassed.put(stringCurr, stringCurr);
                                if (curr != null) {
                                    if (problem.goalTest((curr)))
                                        return returnPath(curr) + ";" + curr.getDeaths() + ";" + convertArrayToString(curr.getDamages()) + ";" + expandedNodes;
                                    expandedNodes++;
                                    for (int i = 0; i < problem.getOperators().length; i++) {
                                        Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                                        if (node != null)
                                            queue.addLast(node);
                                    }
                                }
                            }
                        }
                    }
                    currentDepth = currentDepth + 1;
                    queue.addLast(problem.getInitialState());
                    nodesPassed.clear();
                }
            }
            default:
                return "";
        }


        return res;
    }
}
