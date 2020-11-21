package generic;
import java.util.*;

public abstract class SearchProblem {
    private static HashMap<String, String> nodesPassed = new HashMap<String, String>();
    private String[] operators;
    public static int expandedNodes = 0;

    // returns state given grid, current node and operator to perform
    public abstract Node stateSpace(Node node, String operator);

    //returns true if a given node passes the goal test, otherwise false
    public abstract Boolean goalTest(Node node);

    public abstract Node getInitialState();

    public abstract String[] getOperators();

    public abstract String returnPath(Node n);

    public static String search(SearchProblem problem, QueuingFunction queuingFunction) {
        String res = "";
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node initialState = problem.getInitialState();
        int heuristic = problem.calculateFirstHeuristic(initialState);
        initialState.setHeuristicCost(heuristic);
        queue.add(initialState);
        switch (queuingFunction) {
            case ENQUEUE_AT_END: {
                while (!queue.isEmpty()) {
                    Node curr = queue.remove();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null)
                                queue.add(node);
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT: {
                int priority = Integer.MAX_VALUE;
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i =problem.getOperators().length-1 ; i >=0; i--) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null) {
                                node.setPriority(priority);
                                queue.add(node);
                                priority -=1;
                            }
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT_IDS: {
                int priority = Integer.MAX_VALUE;
                int currentDepth = 0;
                while (true) {

                    while (!queue.isEmpty()) {
                        Node curr = queue.poll();
                        if (curr.getDepth() <= currentDepth) {
                            String currString = curr.formulateNodeToString();
                            if (nodesPassed.get(currString) == null) {
                                nodesPassed.put(currString, currString);
                                if (problem.goalTest(curr)){
                                    nodesPassed.clear();
                                    return problem.returnPath(curr);
                                }
                                expandedNodes++;
                                for (int i = problem.getOperators().length-1; i>=0; i--) {
                                    Node node = problem.stateSpace(curr, problem.getOperators()[i]);

                                    if (node != null) {
                                        node.setPriority(priority);
                                        queue.add(node);
                                        priority -= 1;
                                    }
                                }
                            }
                        }
                    }
                    currentDepth += 1;
                    queue.add(problem.getInitialState());
                    nodesPassed.clear();
                }
            }
            case ORDERED_INSERT: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);

                            if (node != null) {
                                node.setPriority(node.getPathCost());
                                queue.add(node);
                            }
                        }
                    }
                }
            }
            break;
            case HEURISTIC_FN1: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null){
                                heuristic = problem.calculateFirstHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getHeuristicCost());
                                queue.add(node);
                            }
                        }
                    }
                }
            }
            case HEURISTIC_FN2: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null){
                                heuristic = problem.calculateSecondHeuristic(node ,false);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getHeuristicCost());
                                queue.add(node);
                            }
                        }

                    }
                }
            }
            case EVALUATION_FN1: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null){
                                heuristic = problem.calculateFirstHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getPathCost() + node.getHeuristicCost());
                                queue.add(node);
                            }
                        }
                    }
                }
            }
            case EVALUATION_FN2: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (nodesPassed.get(currString) == null) {
                        nodesPassed.put(currString, currString);
                        if (problem.goalTest(curr)){
                            nodesPassed.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.stateSpace(curr, problem.getOperators()[i]);
                            if (node != null){
                                heuristic = problem.calculateSecondHeuristic(node ,false);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getPathCost() + node.getHeuristicCost());
                                queue.add(node);
                            }
                        }
                    }
                }
            }
            default:
                return "";
        }
        return res;
    }
    public static ArrayList<generic.Node> priorityInsert(ArrayList<generic.Node> queue, generic.Node node) {
        boolean isInserted = false;
        if (!queue.isEmpty() && queue.get(queue.size()).getPathCost() > node.getPathCost())
            for (int i = 0; i < queue.size(); i++)
                if (queue.get(i).getPathCost() > node.getPathCost()) {
                    queue.add(i, node);
                    isInserted = true;
                    break;
                }
        if (!isInserted)
            queue.add(queue.size(), node);
        return queue;
    }

    public abstract int calculateFirstHeuristic(Node node);

    public abstract int calculateSecondHeuristic(Node node, boolean print);

}
