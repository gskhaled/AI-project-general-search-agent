package generic;

import mission.Operator;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class SearchProblem {
    private static HashMap<String, String> nodesPassed = new HashMap<String, String>();
    private generic.Operator[] operators;
    public static int expandedNodes = 0;

    // returns state given grid, current node and operator to perform
    public abstract Node stateSpace(Node node, Operator operator);

    //returns true if a given node passes the goal test, otherwise false
    public abstract Boolean goalTest(Node node);

    public abstract Node getInitialState();

    public abstract Operator[] getOperators();

    public abstract String returnPath(Node n);

    /*public static String search(SearchProblem problem, QueuingFunction queuingFunction) {
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
                while (true) {
                    while (!queue.isEmpty()) {
                        Node curr = queue.removeFirst();
                        if (!(curr.getDepth() > currentDepth)) {
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
            case ORDERED_INSERT: {
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
            default:
                return "";
        }
        return res;
    }*/
    public static String search(SearchProblem problem, QueuingFunction queuingFunction) {
        String res = "";
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.addLast(problem.getInitialState());
        switch (queuingFunction) {
            case ENQUEUE_AT_END: {
                while (!queue.isEmpty()) {
                    Node curr = queue.removeFirst();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr))
                            return problem.returnPath(curr);
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null)
                                queue.addLast(node);
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT: {
                while (!queue.isEmpty()) {
                    Node curr = queue.removeFirst();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest((curr)))
                            return problem.returnPath(curr);
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null)
                                queue.addFirst(node);
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT_IDS: {
                int currentDepth = 0;
                while (true) {
                    while (!queue.isEmpty()) {
                        Node curr = queue.removeFirst();
                        if (!(curr.getDepth() > currentDepth)) {
                            String currString = curr.formulateNodeToString();
                            if (nodesPassed.get(currString) == null) {
                                nodesPassed.put(currString, currString);
                                if (problem.goalTest((curr)))
                                    return problem.returnPath(curr);
                                expandedNodes++;
                                for (int i = 0; i < problem.getOperators().length; i++) {
                                    Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                                    if (node != null)
                                        queue.addLast(node);
                                }
                            }
                        }
                    }
                    currentDepth = currentDepth + 1;
                    queue.addLast(problem.getInitialState());
                    nodesPassed.clear();
                }
            }
            case ORDERED_INSERT: {
                while (!queue.isEmpty()) {
                    Node curr = queue.removeFirst();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest((curr)))
                            return problem.returnPath(curr);
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null)
                                queue = priorityInsert(queue, node);
                        }
                    }
                }
            }
            break;
            default:
                return "";
        }
        return res;
    }


    public static LinkedList<generic.Node> priorityInsert(LinkedList<generic.Node> queue, generic.Node node) {
        boolean isInserted = false;
        if (!queue.isEmpty() && queue.getLast().getPathCost() > node.getPathCost())
            for (int i = 0; i < queue.size(); i++)
                if (queue.get(i).getPathCost() > node.getPathCost()) {
                    queue.add(i, node);
                    isInserted = true;
                    break;
                }
        if (!isInserted)
            queue.addLast(node);
        return queue;
    }

}
