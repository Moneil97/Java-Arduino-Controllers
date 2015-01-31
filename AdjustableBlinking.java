package arduino;

import java.util.Arrays;

import javax.swing.JFrame;
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

public class AdjustableBlinking extends JFrame{

	public static void main(String[] args) {
		new AdjustableBlinking();
	}
	
	int delay = 50;
	
	public AdjustableBlinking(){
		
		setSize(200,200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		JSlider slider = new JSlider();
		slider.setValue(delay);
		
		add(slider);
		
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				delay = slider.getValue();
				say(delay);
				
			}
		});
		
		
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

		
		while (true){
			
			link.sendPowerPinSwitch(13, IProtocol.POWER_HIGH, callBack);
			
			try {
				Thread.sleep(delay*10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			link.sendPowerPinSwitch(13, IProtocol.POWER_LOW, callBack);
			
			try {
				Thread.sleep(delay*10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

//		
//		link.disconnect();
		
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
