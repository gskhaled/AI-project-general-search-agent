package code.generic;

import java.util.HashMap;
import java.util.PriorityQueue;

public abstract class SearchProblem {
	private static HashMap<String, String> repeatedStates = new HashMap<String, String>();
	public static int expandedNodes = 0;
	public Node initialState;

	/**
	 * This function is responsible of applying a given operator to the current
	 * Node.
	 * 
	 * @param node
	 *            a node object which the operator will be applied to.
	 * @param operator
	 *            one of pre-set operators representing the operation done to the
	 *            node.
	 * @return the resulting node from doing the operation.
	 */
	public abstract Node transitionFunction(Node node, String operator);

	/**
	 * Checks the goal condition on the given node.
	 * 
	 * @param node
	 *            a node object that is used to check if it satisfies the goal test.
	 * @return whether this node is a goal state or not.
	 */
	public abstract Boolean goalTest(Node node);

	/**
	 * @return returns the initial state of the search problem.
	 */
	public abstract Node getInitialState();

	/**
	 * @return returns a String array which represents all the operators in the
	 *         search problem.
	 */
	public abstract String[] getOperators();

	/**
	 * Produces a string that shows the path to be followed from the root (initial
	 * state) to the goal node (or the current node).
	 * 
	 * @param n
	 *            node object to figure out the path from the root to the given node
	 *            n.
	 * @return String representing the path from the initial state to that node.
	 */
	public abstract String returnPath(Node n);

	/**
	 * Takes a search problem along with the queuing strategy to follow during the
	 * searching operation.
	 * 
	 * @param problem
	 *            Instance of search the problem to search upon.
	 * @param queuingFunction
	 *            The queuing function followed in that search criteria.
	 * @return the operations done to reach the goal state.
	 */
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
				if (problem.goalTest(curr)) {
					repeatedStates.clear();
					return problem.returnPath(curr);
				}
				expandedNodes++;
				for (int i = 0; i < problem.getOperators().length; i++) {
					Node node = problem.transitionFunction(curr, problem.getOperators()[i]);

					if (node != null) {
						String nodeString = node.formulateNodeToString();
						if (repeatedStates.get(nodeString) == null) {
							repeatedStates.put(nodeString, nodeString);
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
				if (problem.goalTest(curr)) {
					repeatedStates.clear();
					return problem.returnPath(curr);
				}
				expandedNodes++;
				for (int i = problem.getOperators().length - 1; i >= 0; i--) {
					Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
					if (node != null) {
						String nodeString = node.formulateNodeToString();
						if (repeatedStates.get(nodeString) == null) {
							repeatedStates.put(nodeString, nodeString);
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
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					if (curr.getDepth() < currentDepth) {
						for (int i = problem.getOperators().length - 1; i >= 0; i--) {
							node = problem.transitionFunction(curr, problem.getOperators()[i]);

							if (node != null) {
								nodeString = node.formulateNodeToString();
								if (repeatedStates.get(nodeString) == null) {
									repeatedStates.put(nodeString, nodeString);
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
				repeatedStates.clear();
			}
		}
		case ORDERED_INSERT: {
			while (!queue.isEmpty()) {
				Node curr = queue.poll();
				String currString = curr.formulateNodeToString();
				if (repeatedStates.get(currString) == null) {
					repeatedStates.put(currString, currString);
					if (problem.goalTest(curr)) {
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.transitionFunction(curr, problem.getOperators()[i]);

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
				if (repeatedStates.get(currString) == null) {
					repeatedStates.put(currString, currString);
					if (problem.goalTest(curr)) {
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
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
				if (repeatedStates.get(currString) == null) {
					repeatedStates.put(currString, currString);
					if (problem.goalTest(curr)) {
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
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
				if (repeatedStates.get(currString) == null) {
					repeatedStates.put(currString, currString);
					if (problem.goalTest(curr)) {
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
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
				if (repeatedStates.get(currString) == null) {
					repeatedStates.put(currString, currString);
					if (problem.goalTest(curr)) {
						repeatedStates.clear();
						return problem.returnPath(curr);
					}
					expandedNodes++;
					for (int i = 0; i < problem.getOperators().length; i++) {
						Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
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

	/**
	 * Calculates the first heuristic (estimates the cost to reach the goal from the
	 * given node).
	 * 
	 * @param node
	 *            Node object to calculate the heuristic cost for (estimate the cost
	 *            to reach the goal from this node).
	 * @return heuristic cost of that node using the first heuristic function.
	 */
	public abstract int calculateFirstHeuristic(Node node);

	/**
	 * * Calculates the second heuristic (estimates the cost to reach the goal from
	 * the given node).
	 * 
	 * @param node
	 *            Node object to calculate the heuristic cost for (estimate the cost
	 *            to reach the goal from this node).
	 * @return heuristic cost of that node using the second heuristic function.
	 */
	public abstract int calculateSecondHeuristic(Node node);

}
