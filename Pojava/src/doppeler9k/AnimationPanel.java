/**
 * @author Robert
 * CTRL + CLICK ME-> https://github.com/rassch/Doppler-9000
 */
package doppeler9k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationPanel extends JPanel implements ActionListener {
	double soundVelocity = 340;
	double freq;
	int step = 15;
	int counter = 0;
	int ncounter = 0;
	double waveLife = 1000;
	double waveLength = (double)(20);
	int waveNumber = (int)(waveLife/waveLength);
	public SimulationObject source;
	public SimulationObject observer;
	WaveObject[] wave = new WaveObject[waveNumber];
	public PrintWriter outFile;
	public PrintWriter operator;
	//
	public void setSoundVel(float sv) {
		soundVelocity = sv;
	}
	public int getCounter() {
		return counter;
	}
	public void setFrequency(double f) {
		freq = f;
	}
	//
	public AnimationPanel() throws FileNotFoundException {
		for(int n =0; n < waveNumber; n++) {
			wave[n] = new WaveObject();
		}
		outFile = new PrintWriter(new FileOutputStream("factorfile.txt"));
	}
	//
	Timer tm = new Timer(step,this);
	//
	public void sizeCompare(SimulationObject obj) {
		if((obj.getX()>(this.getWidth()-5))||(obj.getX()<0)) {
			obj.setAngle(180-obj.getAngle());
		}
		if((obj.getY()>(this.getHeight()-5))||(obj.getY()<0)) {
			obj.setAngle(360-obj.getAngle());
		}
	}
	//
	public void propCalc() {
		observer.calculateCoords(step);
		source.calculateCoords(step);
		sizeCompare(observer);
		sizeCompare(source);
	}
	//
	public void drawSoundWaves(Graphics g) {
		double[] r = new double[waveNumber];
		//
		for(int n = 0; n < waveNumber; n++) {
			if((counter+waveLength*n)%waveLife==0) {
				wave[n].setXY(source.getX(), source.getY());
			}
		}
		for(int n = 0; n < waveNumber; n++) {
			wave[n].setV(soundVelocity);
			r[n] = wave[n].calculateDia(counter+waveLength*n, waveLife,step);
			g.drawOval((int)(wave[n].getX() - r[n]/2 + 2.5),
					(int)(wave[n].getY() - r[n]/2 + 2.5),
					(int)(r[n]),
					(int)(r[n]));
		}
	}
	//
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLUE);
		g.fillOval((int)source.getX(), (int)source.getY(), 5, 5);
		g.fillOval((int)observer.getX(), (int)observer.getY(), 5, 5);
		drawSoundWaves(g);
		tm.start();
	}
	//
	public double getFactor() throws FileNotFoundException {
		double value;
		double rx = observer.getX()-source.getX();
		double ry = observer.getY()-source.getY();
		double cosObs = 1;
		double cosSou = 1;
		if (source.getV() != 0) {
			cosSou = ((rx*source.vx()) + (ry*source.vy())) / ((Math.sqrt(rx*rx+ry*ry) * source.getV())); 
		}
		if(observer.getV() != 0) {
			cosObs = ((rx*observer.vx()) + (ry*observer.vy())) / ((Math.sqrt(rx*rx+ry*ry) * observer.getV()));
		}
		value = (soundVelocity - (observer.getV()* cosObs)) / (soundVelocity - (source.getV() * cosSou));
		return value;
	}
	//
	@Override
	public void actionPerformed(ActionEvent e) {
		propCalc();
		repaint();
		try {
			for(int n=0; n<step; n++) {
				outFile.println(getFactor());
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		counter++;
	}
}
