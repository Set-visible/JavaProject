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
	JButton startB = new JButton("��ġ��ũ ����");
	JButton checkB = new JButton("�ʷϻ��� �Ǹ� ��������!");
	JButton reStartB = new JButton("�ٽ� �غ���");
	JButton uploadB = new JButton("���� ����");
	JLabel resLable = new JLabel("���");
	JLabel reStartLable = new JLabel("�ʹ� ���� �������ϴ�!");
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
		Dimension frameSize = this.getSize();   //������ ����� ��������
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	class RTThread extends Thread{
		boolean startstate = false;
		public void run() {
			while(true) {
				System.out.print(""); //�̰� ������ while���� startstate�� �˻����� ����..��??
										//������ ���ڸ� while(true)�϶� ���� �����Ұ� �ƹ��͵� ������ jvm���� ����ȭ ���鿡�� �Ⱦ��°Ͱ���
				if(startstate == true) {
				try {
						checkB.setText("�ʷϻ��� �Ǹ� ��������!");
						checkB.setBackground(new Color(69,177,255));
						sleep(rand.nextInt(6000)+1000);
						checkB.setText("��������!");
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
		resLable.setText("���: %s ms".formatted(resultScore));
		setSize(400,400);
		jp.update(getGraphics());
	}
	
	
	class BtnL implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton)e.getSource();
			if(jb.getText().equals("��ġ��ũ ����")) {
				PlayPhase();
			}else if (jb.getText().equals("�ʷϻ��� �Ǹ� ��������!")) {
				System.out.println("�ʹ� ���� �������ϴ�.");
				RestartPhase();
			}else if (jb.getText().equals("��������!")) {
				double timea = System.nanoTime();
				resultScore = Double.toString(((timea-timeb)/1000000)-40);  //���α׷� ������ 40ms ����(����ġ)
				ResultPhase();
			}else if(jb.getText().equals("�ٽ� �غ���")) {
				PlayPhase();
			}else if(jb.getText().equals("���� ����")){
				op.dc.sendRTScore(userid, resultScore);
				JOptionPane.showMessageDialog(null, "���������� ������ ���ƽ��ϴ�.");
			}
			
			
		}
		
	}
	
}
