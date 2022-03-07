import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class CreateUserFrame extends JFrame {
	Container c  =getContentPane();
	JPanel jp = new JPanel(new FlowLayout());
	JButton enter = new JButton("생성");
	JButton cancel = new JButton("취소");
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	JLabel idf = new JLabel("I  D");
	JLabel pwf = new JLabel("P  W");
	
	Operator op = null;
	
	CreateUserFrame(Operator _o){
		op = _o;
		c.add(jp);
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		idf.setPreferredSize(new Dimension(70, 30));
		pwf.setPreferredSize(new Dimension(70, 30));
		id.setPreferredSize(new Dimension(300,30));
		pw.setPreferredSize(new Dimension(300,30));
		enter.setPreferredSize(new Dimension(180,30));
		cancel.setPreferredSize(new Dimension(180, 30));
		CA_ActionListener cli = new CA_ActionListener();
		jp.add(idf);
		jp.add(id);
		jp.add(pwf);
		jp.add(pw);
		jp.add(enter);
		jp.add(cancel);
		
		enter.addActionListener(cli);
		cancel.addActionListener(cli);
		
	}
	
	class CA_ActionListener implements ActionListener  {
		  //이벤트를 발생시킨 컴포넌트(소스)
		public void actionPerformed(ActionEvent e) {
			JButton b =  (JButton)e.getSource();
			if (b.getText().equals("생성")) {	
				String Iid = id.getText();
				String Ipw = pw.getText();
				boolean res = op.dc.sendCreateAccountInfo(Iid, Ipw);
				if (res) {
					JOptionPane.showMessageDialog(null, "계정 생성에 성공했습니다.");
					op.lf.setEnabled(true);
					setVisible(false);
				}
				else {
				JOptionPane.showMessageDialog(null, "계정 생성에 실패했습니다.이미 해당아이디가 존재합니다.");
					op.lf.setEnabled(true);
					setVisible(false);
				}
			}
			else if (b.getText().equals("취소")){
				op.lf.setEnabled(true);
				setVisible(false);
			}
		}
	}
}
