package s3.ai;


import java.util.List;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;
import java.util.ArrayList;
import static s3.ai.Node.removeLowestF;


public class AStar {

	private Node start;
	private Node goal;
	private S3 game;
	private S3PhysicalEntity pe;
	
	
	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;
	}

	public AStar(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {

		this.start = new Node(start_x, start_y);
		this.goal = new Node(goal_x, goal_y);
		this.game = the_game;
		this.pe = (S3PhysicalEntity) i_entity.clone();

	}

	public Node getStart(){
		return this.start;
	}

	public Node getGoal(){
		return this.goal;
	}

	public S3PhysicalEntity getPe() {
		return this.pe;
	}

	public S3 getGame() {
		return this.game;
	}

	public boolean isGoal(Node n) {
		if((n.getX() == this.getGoal().getX()) && (n.getY() == this.getGoal().getY())) return true;
		else return false;
	}

	public boolean isStart(Node n) {
		if((n.getX() == this.getStart().getX()) && (n.getY() == this.getStart().getY())) return true;
		else return false;
	}

	public double heuristic(Node n) {
		double x = this.getGoal().getX() - n.getX();
		double y = this.getGoal().getY() - n.getY();
		return Math.abs(x) + Math.abs(y);
	}

	public boolean collisionCheck(Node n){
		this.getPe().setX((int)n.getX());
		this.getPe().setY((int)n.getY());
		if (n.getX() < 0 || n.getY() < 0 || n.getX() >= game.getMap().getWidth() || n.getY() >= game.getMap().getHeight()) {
			return false;
		}
		else return this.getGame().anyLevelCollision(this.getPe()) == null;
	}

	public List<Pair<Double, Double>> getPath(Node N){
		List<Pair<Double, Double>> path = new ArrayList<>();
		while (!this.isStart(N)) {
			Pair<Double, Double> pair = new Pair<>(N.getX(), N.getY());
			path.add(0, pair);
			N = N.getParent();
		}
		return path;
	}

	public List<Node> getChildren(Node N){
		List<Node> c = new ArrayList<>();
		Node child1 = new Node(N.getX() - 1, N.getY());
		Node child2 = new Node(N.getX() + 1, N.getY());
		Node child3 = new Node(N.getX(), N.getY() - 1);
		Node child4 = new Node(N.getX(), N.getY() + 1);
		c.add(child1);
		c.add(child2);
		c.add(child3);
		c.add(child4);
		return c;
	}

	public List<Pair<Double, Double>> computePath() {
		this.getStart().setG(0);
		this.getStart().setH(heuristic(this.getStart()));
		ArrayList<Node> open = new ArrayList<>();
		ArrayList<Node> closed = new ArrayList<>();
		open.add(this.getStart());
		while (!open.isEmpty()) {
			Node N = removeLowestF(open);
			if (this.isGoal(N)) {
				return this.getPath(N);
			}
			closed.add(N);
			List<Node> children = this.getChildren(N);
			for (Node child : children) {
				if (!open.contains(child) && !closed.contains(child) && collisionCheck(child)) {
					child.setParent(N);
					child.setG(N.getG() + 1);
					child.setH(heuristic(child));
					open.add(child);
				}
			}
		}
		return null;
	}
}
