import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

public class NumFrame extends JFrame {
	Container c = getContentPane();
	JPanel jp = new JPanel(new FlowLayout());
	String userid = null;
	JButton startB = new JButton("벤치마크 시작");
	JButton ansB = new JButton("확인");
	JButton reStartB = new JButton("다시 해보기");
	JLabel resultLabel = new JLabel();
	JButton uploadB = new JButton("점수 저장하기");
	JProgressBar timer = new JProgressBar(0, 100);
	BtnActionListener BAL = new BtnActionListener();
	JLabel numL = new JLabel();
	JTextField ansField = new JTextField();
	Operator op;
	ArrayList<String> randnum = new ArrayList<String>();
	String temp;
	
	JPanel resultPanel = new JPanel(new BorderLayout());
	JPanel resultPanelCenter = new JPanel(new FlowLayout());
	
	JPanel playPanel = new JPanel(new BorderLayout());
	JPanel playPanelCenter = new JPanel(new FlowLayout());
	JPanel playPanelNorth = new JPanel(new FlowLayout());
	
	NumFrame(Operator _o) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		op = _o;
		setSize(500, 500);
		c.add(jp);
		jp.add(startB);
		numL.setPreferredSize(new Dimension(100,20));
		ansField.setPreferredSize(new Dimension(100,20));
		ansB.addActionListener(BAL);
		startB.addActionListener(BAL);
		reStartB.addActionListener(BAL);
		timer.setStringPainted(true);
		uploadB.addActionListener(BAL);
		resultPanel.add(resultPanelCenter, BorderLayout.CENTER);
		resultPanelCenter.add(reStartB);
		resultPanel.add(resultLabel, BorderLayout.NORTH);
		resultPanelCenter.add(uploadB);
		
		playPanel.add(playPanelCenter, BorderLayout.CENTER);
		playPanel.add(playPanelNorth, BorderLayout.NORTH);
		playPanelNorth.add(timer);
		playPanelCenter.add(numL);
		playPanelCenter.add(ansField);
		playPanelCenter.add(ansB);
		Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	void PlayPhase() {
		randnum.clear();
		c.removeAll();
		setSize(490,490);
		c.update(getGraphics());
		setSize(500,500);
		c.add(playPanel);
		randnum.add("1");
		c.update(getGraphics());
		StartBActionPerformed();
	}
	
	void KeepGoingPhase() {
		c.removeAll();
		setSize(490,490);
		c.update(getGraphics());
		setSize(500,500);
		c.add(playPanel);
		StartBActionPerformed();
		c.update(getGraphics());
	}
	
	boolean CheckAnswer(String userAns) {
		if(temp.equals(userAns)) {
			return true;
		}else {
			return false;
		}
	}
	void ResultPhase() {
		c.removeAll();
		setSize(490,490);
		c.update(getGraphics());
		setSize(500,500);
		c.add(resultPanel);
		c.update(getGraphics());
		resultLabel.setText("점수: %s".formatted(randnum.size()-1));
	}
	class BtnActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			if (jb.getText().equals("벤치마크 시작")) {
				PlayPhase();
			}else if(jb.getText().equals("확인")) {
				String answer = ansField.getText();
				if(CheckAnswer(answer)) {
					ansField.setText("");
					randnum.add("1");
					KeepGoingPhase();
				}else {
					ansField.setText("");
					System.out.println("종료");
					ResultPhase();
				}
				
			}else if(jb.getText().equals("다시 해보기")) {
				PlayPhase();
			}else if(jb.getText().equals("점수 저장하기")) {
				if(op.dc.sendNumScore(userid, String.valueOf(randnum.size()-1))) {
					JOptionPane.showMessageDialog(null, "성공적으로 저장을 마쳤습니다.");
				}else {
					JOptionPane.showMessageDialog(null, "점수 저장에 실패했습니다.");
				}
			}

		}

	}
	private void StartBActionPerformed() {
		Random rand = new Random();
		final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				ansField.setEnabled(false);
				numL.setVisible(true);
				ansB.setEnabled(false);
				int size = randnum.size();
				for(int a=0; size>a;a++) {
					randnum.set(a,String.valueOf(rand.nextInt(10)));
				}
				temp = randnum.toString();
				temp = temp.replace(", ", "");
				temp = temp.replace("[", "");
				temp = temp.replace("]", "");
				numL.setText(temp);
				c.update(getGraphics());
				System.out.println(temp);
				for (int i = 0; i <= 100; i++) {
					timer.setValue(i);
					try {
						Thread.sleep(20);
					} catch (InterruptedException ex) {
					}
				}
				ansField.setEnabled(true);
				ansB.setEnabled(true);
				numL.setVisible(false);
				setSize(490,490);
				c.update(getGraphics());
				setSize(500,500);
				c.update(getGraphics());
				return null;
			}
		};
		worker.execute();

	}
}
