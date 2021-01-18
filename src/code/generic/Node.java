package code.generic;

public abstract class Node implements Comparable<Node> {
	String state;
	Node parent;
	String operator;
	int depth;
	int pathCost;
	int heuristicCost;
	int priority;

	/**
	 * @return the parent node of the current node in the tree.
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * @return the operator that was applied to reach this node.
	 */
	public String getOperator() {
		return this.operator;
	}

	/**
	 * @return the depth of this node in the tree.
	 */
	public int getDepth() {
		return this.depth;
	}

	/**
	 * @return the path cost so far to reach this node.
	 */
	public int getPathCost() {
		return this.pathCost;
	}

	/**
	 * @return the complete state string representing this node.
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * @return the heuristic cost of this node.
	 */
	public int getHeuristicCost() {
		return this.heuristicCost;
	}

	/**
	 * @param the
	 *            cost to set the heuristic cost variable with.
	 */
	public void setHeuristicCost(int cost) {
		this.heuristicCost = cost;
	}

	/**
	 * @return the priority of this node
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * @param priority
	 *            the value to set the priority of the node.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Method to return the node state as a string, in a specific representation.
	 * 
	 * @return String representing the node state.
	 */
	public abstract String formulateNodeToString();

	@Override
	public int compareTo(code.generic.Node o) {
		return getPriority() < o.getPriority() ? -1 : o.getPriority() == getPriority() ? 0 : 1;
	}
}
