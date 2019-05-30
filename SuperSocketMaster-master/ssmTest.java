import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ssmTest extends JFrame implements ActionListener{
	
	private SuperSocketMaster ssm;
	private String user = "paraujo", pass = "1101795";
	private int sessionId = 0;
	private long time;
	private int questions = 0;
	
	private JPanel pnl;
	private JTextField userField, passField;
	private JButton beginBtn;
	
	public static void main(String[] args){
		new ssmTest();
	}
	
	public ssmTest(){
		super("SSM Test");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		pnl = new JPanel();
		pnl.setPreferredSize(new Dimension(300, 300));
		pnl.setLayout(null);
		
		userField = new JTextField();
		userField.setSize(200, 50);
		userField.setLocation(50, 50);
		pnl.add(userField);
		
		passField = new JTextField();
		passField.setSize(200, 50);
		passField.setLocation(50, 100);
		passField.addActionListener(this);
		pnl.add(passField);
		
		beginBtn = new JButton("BEGIN TEST");
		beginBtn.setSize(200, 50);
		beginBtn.setLocation(50, 150);
		beginBtn.addActionListener(this);
		pnl.add(beginBtn);
		
		this.add(pnl);
		this.pack();
		this.show();
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(ssm)){
			String[] input = ssm.readText().split(",");
			
			int sessionId = Integer.parseInt(input[1]);
			if(this.sessionId == 0 && input[0].equals(user)){
				this.sessionId = sessionId;
			}
			
			if(input.length >= 3 && this.sessionId == sessionId){
				//System.out.println(ssm.readText());
				
				int question = Integer.parseInt(input[2]);
				String response = this.answerQuestion(question,
							input.length > 3 ?
							Arrays.copyOfRange(input, 3, input.length) : 
							new String[]{});
				
				String responseText = user + "," + pass + "," + sessionId + ","
							+ question + "," + response;
				//System.out.println(responseText);
				ssm.sendText(responseText);
				
				questions++;
				if(questions >= 12){
					JOptionPane.showMessageDialog(null,
							"ANSWERED ALL QUESTIONS CORRECTLY IN " +
									(System.currentTimeMillis() - time) + " MS!",
							"SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}else if(e.getSource().equals(beginBtn) || e.getSource().equals(passField)){
			this.user = userField.getText();
			this.pass = passField.getText();
			
			ssm = new SuperSocketMaster("10.8.12.210", 1337, this);
			ssm.connect();
			time = System.currentTimeMillis();
			ssm.sendText(user + "," + pass + "," + sessionId + ",1,hello");
		}
	}
	
	private String answerQuestion(final int question, String[] inputParams){
		switch(question){
			case 2:
				int num1 = Integer.parseInt(inputParams[0]);
				int num2 = Integer.parseInt(inputParams[1]);
				return "" + (num1 + num2);
			case 3:
				int num3 = Integer.parseInt(inputParams[0]);
				int num4 = Integer.parseInt(inputParams[1]);
				return "" + (num3 % num4);
			case 4:
				return "BufferedReader";
			case 5:
				return "github";
			case 6:
				return "getMyAddress";
			case 7:
				return "actionPerformed";
			case 8:
				return "ip,port";
			case 9:
				return "port";
			case 10:
				return "connect";
			case 11:
				return "text";
			case 12:
				return "sendText";
			case 13:
				return "readText";
			default:
				return "";
		}
	}
	
}
