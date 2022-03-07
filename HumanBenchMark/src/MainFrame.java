import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;

public class MainFrame extends JFrame {
	Container c = getContentPane();
	JPanel jp = new JPanel(new BorderLayout());
	JPanel jpNorth = new JPanel(new FlowLayout());
	JPanel jpCenter = new JPanel(new FlowLayout());
	JPanel jpWest = new JPanel(new FlowLayout());
	JLabel title = new JLabel("Human Benchmark java Edition");
	JButton rtb = new JButton("반응속도 벤치마크");
	JButton numb = new JButton("숫자기억력 벤치마크");
	JSplitPane splitPane = new JSplitPane();
	BtnListener BtnL = new BtnListener();
	JLabel idlabel = new JLabel("");
	RTFrame rtf = null;
	NumFrame numf = null;
	String userid = null;

	String columnNames[] = { "순위", "아이디", "점수" };
	String rankTemp[][];
	String rank[][];
	DefaultTableModel rankBoard;
	JTable table;
	JScrollPane sc;
	
	DefaultTableModel rankBoardN;
	JTable tableN;
	JScrollPane scN;
	JScrollPane scW;
	JLabel rtText = new JLabel("반응속도 벤치마크 순위");
	JLabel numText = new JLabel("숫자열 기억력 벤치마크 순위");
	
	
	
	MainFrame(Operator _o) {
		c.add(jp);
		
	    
		rtf = new RTFrame(_o);
		numf = new NumFrame(_o);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
		Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		rtb.setPreferredSize(new Dimension(230, 50));
		numb.setPreferredSize(new Dimension(230, 50));
		idlabel.setPreferredSize(new Dimension(230, 50));

		jp.add(jpNorth, BorderLayout.NORTH);
		jp.add(jpCenter, BorderLayout.CENTER);
		
		scW = new JScrollPane(jpWest);
		jp.add(scW, BorderLayout.WEST);
		
		jpNorth.add(title);
		jpCenter.add(numb);
		jpCenter.add(rtb);
		
		jpWest.setPreferredSize(new Dimension(450,600));
		
		rtb.addActionListener(BtnL);
		numb.addActionListener(BtnL);
		rtText.setPreferredSize(new Dimension(400,30));
		numText.setPreferredSize(new Dimension(400,30));
		
		
		jpWest.add(rtText);
		
		
		rankBoard = new DefaultTableModel(rank, columnNames);
		table = new JTable(rankBoard);
		sc = new JScrollPane(table);
		jpWest.add(sc);
		ReBuildTable(_o.dc.getRtRank());
		
		rankBoardN = new DefaultTableModel(rank, columnNames);
		tableN = new JTable(rankBoardN);
		scN = new JScrollPane(tableN);
		jpWest.add(numText);
		jpWest.add(scN);
		ReBuildTableN(_o.dc.getNumRank());
		scN.setPreferredSize(new Dimension(400,300));
		sc.setPreferredSize(new Dimension(400,300));
		
	}
	
	public void setIdTitle() {
		setTitle("환영합니다 %s 님!".formatted(userid));
		update(getGraphics());
	}
	
	public void ReBuildTable(ArrayList<String> tempData) {
		for (int a = 0; tempData.size() > a; a++) {
			System.out.println("되받기 된값: " + tempData.get(a));
		}
		rankBoard.setNumRows(0);
		int sizeOfStudentTable = tempData.size() / 3;
		rankTemp = new String[sizeOfStudentTable][3];
		for (int tmp = 0; sizeOfStudentTable > tmp; tmp++) {
			for (int tmps = 0; 3 > tmps; tmps++) {
				rankTemp[tmp][tmps] = tempData.get(tmp * 3 + tmps);
			}
		}
		System.out.println(Arrays.deepToString(rankTemp));
		rank = rankTemp;
		for (int tmp = 0; sizeOfStudentTable > tmp; tmp++) {
			rankBoard.addRow(new Object[] { rank[tmp][0], rank[tmp][1], rank[tmp][2] });
		}

	}

	public void ReBuildTableN(ArrayList<String> tempData) {
		for (int a = 0; tempData.size() > a; a++) {
			System.out.println("되받기 된값: " + tempData.get(a));
		}
		rankBoardN.setNumRows(0);
		int sizeOfStudentTable = tempData.size() / 3;
		rankTemp = new String[sizeOfStudentTable][3];
		for (int tmp = 0; sizeOfStudentTable > tmp; tmp++) {
			for (int tmps = 0; 3 > tmps; tmps++) {
				rankTemp[tmp][tmps] = tempData.get(tmp * 3 + tmps);
			}
		}
		System.out.println(Arrays.deepToString(rankTemp));
		rank = rankTemp;
		for (int tmp = 0; sizeOfStudentTable > tmp; tmp++) {
			rankBoardN.addRow(new Object[] { rank[tmp][0], rank[tmp][1], rank[tmp][2] });
		}

	}
	void SetIdLabel(String idv) {
		System.out.println(idv);
		userid = idv;
		idlabel.setText(idv);
		jp.update(getGraphics());
		rtf.userid = userid;
		numf.userid = userid;
	}

	public class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			if (jb.getText().equals("반응속도 벤치마크")) {
				setVisible(false);
				rtf.setVisible(true);
			} else if (jb.getText().equals("숫자기억력 벤치마크")) {
				setVisible(false);
				numf.setVisible(true);
			}

		}

	}

}
