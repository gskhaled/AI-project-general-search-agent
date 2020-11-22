package generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public abstract class SearchProblem {
	private static HashMap<String, String> nodesPassed = new HashMap<String, String>();
	private String[] operators;
	public static int expandedNodes = 0;

	// returns state given grid, current node and operator to perform
	public abstract Node stateSpace(Node node, String operator);

	// returns true if a given node passes the goal test, otherwise false
	public abstract Boolean goalTest(Node node);

	public abstract Node getInitialState();

	public abstract String[] getOperators();

	public abstract String returnPath(Node n);

	public static String search(SearchProblem problem, QueuingFunction queuingFunction) {
		String res = "";
		PriorityQueue<Node> queue = new PriorityQueue<>();
		Node initialState = problem.getInitialState();
		queue.add(initialState);
		switch (queuingFunction) {
		case ENQUEUE_AT_END: {
			int priority = 0;
			while (!queue.isEmpty()) {
				Node curr = queue.remove();
				String currString = curr.formulateNodeToString();
				if (nodesPassed.get(currString) == null) {
					nodesPassed.put(currString, currString);
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.stateSpace(curr, problem.getOperators()[i]);
						if (node != null) {
							node.setPriority(priority);
							queue.add(node);
							priority += 1;
						}
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
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = problem.getOperators().length - 1; i >= 0; i--) {
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
			break;
		case ENQUEUE_AT_FRONT_IDS: {
			int priority;
			int currentDepth = 0;
			Node node;
			Node curr;
			String nodeString;
			while (true) {
				priority = Integer.MAX_VALUE;
				while (!queue.isEmpty()) {
					curr = queue.poll();
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					if (curr.getDepth() < currentDepth) {
						for (int i = problem.getOperators().length - 1; i >= 0; i--) {
							node = problem.stateSpace(curr, problem.getOperators()[i]);

							if (node != null) {
								nodeString = node.formulateNodeToString();
								if (nodesPassed.get(nodeString) == null) {
									nodesPassed.put(nodeString, nodeString);
									node.setPriority(priority);
									queue.add(node);
									priority -= 1;
								}
							}
						}
					}

				}
				currentDepth += 10;
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
					if (problem.goalTest(curr)) {
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
			int heuristic = problem.calculateFirstHeuristic(initialState);
			initialState.setHeuristicCost(heuristic);
			while (!queue.isEmpty()) {
				Node curr = queue.poll();
				String currString = curr.formulateNodeToString();
				if (nodesPassed.get(currString) == null) {
					nodesPassed.put(currString, currString);
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.stateSpace(curr, problem.getOperators()[i]);
						if (node != null) {
							heuristic = problem.calculateFirstHeuristic(node);
							node.setHeuristicCost(heuristic);
							node.setPriority(node.getHeuristicCost());
							queue.add(node);
						}
					}
				}
			}
		}
			break;
		case HEURISTIC_FN2: {
			int heuristic = problem.calculateSecondHeuristic(initialState);
			initialState.setHeuristicCost(heuristic);
			while (!queue.isEmpty()) {
				Node curr = queue.poll();
				String currString = curr.formulateNodeToString();
				if (nodesPassed.get(currString) == null) {
					nodesPassed.put(currString, currString);
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.stateSpace(curr, problem.getOperators()[i]);
						if (node != null) {
							heuristic = problem.calculateSecondHeuristic(node);
							node.setHeuristicCost(heuristic);
							node.setPriority(node.getHeuristicCost());
							queue.add(node);
						}
					}

				}
			}
		}
			break;
		case EVALUATION_FN1: {
			int heuristic = problem.calculateFirstHeuristic(initialState);
			initialState.setHeuristicCost(heuristic);
			while (!queue.isEmpty()) {
				Node curr = queue.poll();
				String currString = curr.formulateNodeToString();
				if (nodesPassed.get(currString) == null) {
					nodesPassed.put(currString, currString);
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.stateSpace(curr, problem.getOperators()[i]);
						if (node != null) {
							heuristic = problem.calculateFirstHeuristic(node);
							node.setHeuristicCost(heuristic);
							node.setPriority(node.getPathCost() + node.getHeuristicCost());
							queue.add(node);
						}
					}
				}
			}
		}
			break;
		case EVALUATION_FN2: {
			int heuristic = problem.calculateSecondHeuristic(initialState);
			initialState.setHeuristicCost(heuristic);
			while (!queue.isEmpty()) {
				Node curr = queue.poll();
				String currString = curr.formulateNodeToString();
				if (nodesPassed.get(currString) == null) {
					nodesPassed.put(currString, currString);
					if (problem.goalTest(curr)) {
						nodesPassed.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.stateSpace(curr, problem.getOperators()[i]);
						if (node != null) {
							heuristic = problem.calculateSecondHeuristic(node);
							node.setHeuristicCost(heuristic);
							node.setPriority(node.getPathCost() + node.getHeuristicCost());
							queue.add(node);
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
	}

	public static ArrayList<generic.Node> priorityInsert(ArrayList<generic.Node> queue, generic.Node node) {
		boolean isInserted = false;
		if (!queue.isEmpty() && queue.get(queue.size()).getPathCost() > node.getPathCost()) {
			for (int i = 0; i < queue.size(); i++)
				if (queue.get(i).getPathCost() > node.getPathCost()) {
					queue.add(i, node);
					isInserted = true;
					break;
				}
		}
		if (!isInserted) {
			queue.add(queue.size(), node);
		}
		return queue;
	}

	public abstract int calculateFirstHeuristicOld(Node node);

	public abstract int calculateFirstHeuristic(Node node);

	public abstract int calculateSecondHeuristic(Node node);

	public abstract int calculateSecondHeuristicOld(Node node, boolean print);

}
