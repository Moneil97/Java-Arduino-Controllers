package arduino;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.zu.ardulink.Link;
import org.zu.ardulink.RawDataListener;
import org.zu.ardulink.event.AnalogReadChangeEvent;
import org.zu.ardulink.event.AnalogReadChangeListener;
import org.zu.ardulink.event.ConnectionEvent;
import org.zu.ardulink.event.ConnectionListener;
import org.zu.ardulink.event.DigitalReadChangeEvent;
import org.zu.ardulink.event.DigitalReadChangeListener;
import org.zu.ardulink.event.DisconnectionEvent;
import org.zu.ardulink.protocol.IProtocol;
import org.zu.ardulink.protocol.MessageInfo;
import org.zu.ardulink.protocol.ReplyMessageCallback;

public class RGBSlider extends JFrame{

	public static void main(String[] args) {
		new RGBSlider();
	}
	
	int red, green, blue;
	boolean update = false;
	
	public RGBSlider(){
		
		//GUI
		
		JSlider redSlider = new JSlider();
		redSlider.setValue(0);
		redSlider.setMaximum(255);
		redSlider.setMinimum(0);
		redSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				red = redSlider.getValue();
				update = true;
				say(red + "  " + green + "  " + blue);
			}
		});
		
		JSlider greenSlider = new JSlider();
		greenSlider.setValue(0);
		greenSlider.setMaximum(255);
		greenSlider.setMinimum(0);
		greenSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				green = greenSlider.getValue();
				update = true;
				say(red + "  " + green + "  " + blue);
			}
		});
		
		JSlider blueSlider = new JSlider();
		blueSlider.setValue(0);
		blueSlider.setMaximum(255);
		blueSlider.setMinimum(0);
		blueSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				blue = blueSlider.getValue();
				update = true;
				say(red + "  " + green + "  " + blue);
			}
		});
		
		JColorChooser picker = new JColorChooser();
		picker.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Color color = picker.getColor();
				red = color.getRed();
				blue = color.getBlue();
				green = color.getGreen();
				update = true;
				redSlider.setValue(red);
				greenSlider.setValue(green);
				blueSlider.setValue(blue);
			}
		});
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(3,0));
		sliderPanel.add(redSlider);
		sliderPanel.add(greenSlider);
		sliderPanel.add(blueSlider);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(sliderPanel, BorderLayout.CENTER);
		mainPanel.add(picker, BorderLayout.SOUTH);
		
		add(mainPanel);
		setSize(600,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		//Link
		
		Link link = Link.getDefaultInstance();
		say(link.getName());
		say(link.getPortList());
		say(link.getProtocolName());
		say(link.getLoggerCallback());
		
		link.addAnalogReadChangeListener(new AnalogReadChangeListener() {
			
			@Override
			public void stateChanged(AnalogReadChangeEvent e) {
				say("State Changed: " + e.getIncomingMessage());
			}
			
			@Override
			public int getPinListening() {
				return 0;
			}
		});
		
		link.addConnectionListener(new ConnectionListener() {
			
			@Override
			public void disconnected(DisconnectionEvent e) {
				say("Link is now disconnected: " + e.getConnectionId());
			}
			
			@Override
			public void connected(ConnectionEvent e) {
				say("Link is now connected: " + e.getConnectionId());
			}
		});
		
		link.addDigitalReadChangeListener(new DigitalReadChangeListener() {
			
			@Override
			public void stateChanged(DigitalReadChangeEvent arg0) {
				say ("State Changed: " + arg0.getIncomingMessage());
			}
			
			@Override
			public int getPinListening() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		link.addRawDataListener(new RawDataListener() {
			
			@Override
			public void parseInput(String arg0, int arg1, int[] arg2) {
				say("Raw Data Parse Input: " + arg0 + " " + arg1 + " " + Arrays.toString(arg2));
			}
		});
		
		link.connect("COM3");
		
		ReplyMessageCallback callBack = new ReplyMessageCallback() {
			
			@Override
			public void replyInfo(MessageInfo arg0) {
				say ("Call Back: " + arg0.getMessageReceived());
			}
		};
		
		
		//2 Second Delay
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		int redPin = 3, greenPin = 5, bluePin = 6;

		
		while (true){
			
			if (update){
				
				link.sendPowerPinIntensity(redPin, red, callBack);
				link.sendPowerPinIntensity(greenPin, green, callBack);
				link.sendPowerPinIntensity(bluePin, blue, callBack);
				update = false;
			}
			else{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

//		
//		link.disconnect();
		
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
