package mission;

public class Node extends generic.Node {
	private Node parent;
	private String operator;
	private int depth;
	private int pathCost;
	private String state;

	public Node(short x, short y, short c, short deaths, short[] xPoss, short[] yPoss, short[] damages,
			short[] IMFstates, Node parent, String operator, int depth, int pathCost) {
		this.parent = parent;
		// this.state = "";
		// this.state += x + "," + y + "," + c + "," + deaths + ";";
		StringBuilder xPositions = new StringBuilder();
		StringBuilder yPositions = new StringBuilder();
		StringBuilder damageString = new StringBuilder();
		StringBuilder IMFStatesString = new StringBuilder();
		xPositions.append(x).append(',').append(y).append(',').append(c).append(',').append(deaths).append(';');
		int loopCounter = xPoss.length - 1;
		for (int i = 0; i < loopCounter; i++) {
			xPositions.append(xPoss[i]).append(',');
			yPositions.append(yPoss[i]).append(',');
			damageString.append(damages[i]).append(',');
			IMFStatesString.append(IMFstates[i]).append(',');

		}
		xPositions.append(xPoss[loopCounter]).append(';');
		yPositions.append(yPoss[loopCounter]).append(';');
		damageString.append(damages[loopCounter]).append(';');
		IMFStatesString.append(IMFstates[loopCounter]);
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
		xPositions.append(yPositions).append(damageString).append(IMFStatesString);
		this.state = xPositions.toString();
	}

	// public Node(short x, short y, short c, short deaths, short[] xPoss, short[]
	// yPoss, short[] damages, short[] IMFstates, Node parent, Operator operator,
	// int depth, int pathCost) {
	// this.x = x;
	// this.y = y;
	// this.c = c;
	// this.deaths = deaths;
	// this.xPoss = xPoss;
	// this.yPoss = yPoss;
	// this.damages = damages;
	// this.IMFstates = IMFstates;
	// this.parent = parent;
	// this.operator = operator;
	// this.depth = depth;
	// this.pathCost = pathCost;
	// }
	@Override
	public String formulateNodeToString() {
		String[] currentStates = this.state.split(";");
		String[] ethanPosCarryDamages = currentStates[0].split(",");
		// short x = Short.valueOf(ethanPosCarryDamages[0]);
		// short y = Short.valueOf(ethanPosCarryDamages[1]);
		// short c = Short.valueOf(ethanPosCarryDamages[2]);
		// String IMFstates = currentStates[4];
		return ethanPosCarryDamages[0] + "," + ethanPosCarryDamages[1] + ";"
				+ currentStates[4]/*
									 * + ";" + ethanPosCarryDamages[2]
									 */;
	}

	@Override
	public Node getParent() {
		return this.parent;
	}

	@Override
	public String getOperator() {
		return this.operator;
	}

	@Override
	public int getDepth() {
		return this.depth;
	}

	@Override
	public int getPathCost() {
		return this.pathCost;
	}

	@Override
	public String getState() {
		return this.state;
	}

}
