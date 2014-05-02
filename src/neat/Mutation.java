package neat;

public class Mutation {

	private MutationType type;
	private double a;
	private double b;
	
	public Mutation(MutationType type, double a, double b) {
		super();
		this.type = type;
		this.a = a;
		this.b = b;
	}
	public MutationType getType() {
		return type;
	}
	public void setType(MutationType type) {
		this.type = type;
	}
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	
}
