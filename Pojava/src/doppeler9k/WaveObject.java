package doppeler9k;

public class WaveObject {
	double x = 0;
	double y = 0;
	double radius = 0;
	double velocity = 0;
	//
	public WaveObject(double X, double Y, double R, double V) {
		x = X;
		y = Y;
		radius = R;
		velocity = V;
	}
	//
	/*public WaveObject() {
		x = 0;
		y = 0;
		radius = 0;
		velocity = 0;
	}*/
	public void setV(double V) {
		velocity = V;
	}
	public void setX(double X) {
		x = X;
	}
	public void setY(double Y) {
		y = Y;
	}
	public void setXY(double X, double Y) {
		x = X;
		y = Y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double calculateRad(double dt, double maxRad) {
		radius = velocity * (dt % maxRad) * 0.002;
		return radius;
	}
}