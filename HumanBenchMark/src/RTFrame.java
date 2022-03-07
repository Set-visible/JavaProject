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


public class RTFrame extends JFrame{
	static double timeb;
	Container c = getContentPane();
	JButton startB = new JButton("벤치마크 시작");
	JButton checkB = new JButton("초록색이 되면 누르세요!");
	JButton reStartB = new JButton("다시 해보기");
	JButton uploadB = new JButton("점수 저장");
	JLabel resLable = new JLabel("결과");
	JLabel reStartLable = new JLabel("너무 일찍 눌렀습니다!");
	JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	Random rand = new Random();
	BtnL btnL = new BtnL();
	RTThread rtthread = new RTThread();
	String resultScore = null;
	String userid = null;
	Operator op;
	RTFrame(Operator _o){
		op = _o;
		rtthread.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		startB.setPreferredSize(new Dimension(400,400));
		startB.addActionListener(btnL);
		checkB.addActionListener(btnL);
		checkB.setPreferredSize(new Dimension(400,400));
		
		reStartB.addActionListener(btnL);
		uploadB.addActionListener(btnL);
		c.add(jp);
		jp.add(startB);
		jp1.add(checkB);
		Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	class RTThread extends Thread{
		boolean startstate = false;
		public void run() {
			while(true) {
				System.out.print(""); //이게 없으면 while문이 startstate를 검사하지 않음..왜??
										//추측해 보자면 while(true)일때 내부 실행할게 아무것도 없으면 jvm에서 최적화 측면에서 안쓰는것같음
				if(startstate == true) {
				try {
						checkB.setText("초록색이 되면 누르세요!");
						checkB.setBackground(new Color(69,177,255));
						sleep(rand.nextInt(6000)+1000);
						checkB.setText("누르세요!");
						checkB.setBackground(Color.GREEN);
						timeb = System.nanoTime();
						startstate = false;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		}
	}
	
	void RestartPhase() {
		jp.removeAll();
		setSize(500,500);
		c.update(getGraphics());
		jp.add(reStartB);
		jp.add(reStartLable);
		
	}
	
	void PlayPhase() {
		jp.removeAll();
		jp.add(checkB);
		setSize(450,450);
		rtthread.startstate=true;
		jp.update(getGraphics());
	}
	void ResultPhase() {
		jp.removeAll();
		jp.add(resLable);
		jp.add(uploadB);
		jp.add(reStartB);
		resLable.setText("결과: %s ms".formatted(resultScore));
		setSize(400,400);
		jp.update(getGraphics());
	}
	
	
	class BtnL implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton)e.getSource();
			if(jb.getText().equals("벤치마크 시작")) {
				PlayPhase();
			}else if (jb.getText().equals("초록색이 되면 누르세요!")) {
				System.out.println("너무 일찍 눌렀습니다.");
				RestartPhase();
			}else if (jb.getText().equals("누르세요!")) {
				double timea = System.nanoTime();
				resultScore = Double.toString(((timea-timeb)/1000000)-40);  //프로그램 딜레이 40ms 감산(추정치)
				ResultPhase();
			}else if(jb.getText().equals("다시 해보기")) {
				PlayPhase();
			}else if(jb.getText().equals("점수 저장")){
				op.dc.sendRTScore(userid, resultScore);
				JOptionPane.showMessageDialog(null, "성공적으로 저장을 마쳤습니다.");
			}
			
			
		}
		
	}
	
}
