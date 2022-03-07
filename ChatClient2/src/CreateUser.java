import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import CreateAccount.CA_ActionListener;



//로그인 창에서 새 계정 생성을 선택할 경우 가동되는 클래스. 여기서 계정 생성을 담당함
@SuppressWarnings("serial")
public class CreateUser extends JFrame {
	Container c  =getContentPane();
	JPanel jp = new JPanel(new FlowLayout());
	JButton enter = new JButton("생성");
	JButton cancel = new JButton("취소");
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	JTextField name = new JTextField();
	JTextField phone = new JTextField();
	
	JLabel idf = new JLabel("I  D");
	JLabel pwf = new JLabel("P  W");
	JLabel namef = new JLabel("NAME");
	JLabel phonef = new JLabel("PHONE");
	
	Operator op = null;
	ChatFrame.ChatConnector connector;
	
	CreateUser(Operator _o){
		op = _o;
		connector = op.cf.cc;  
		c.add(jp);
		setSize(400, 300);
		idf.setPreferredSize(new Dimension(70, 30));
		pwf.setPreferredSize(new Dimension(70, 30));
		namef.setPreferredSize(new Dimension(70, 30));
		phonef.setPreferredSize(new Dimension(70, 30));
		
		name.setPreferredSize(new Dimension(300,30));
		phone.setPreferredSize(new Dimension(300,30));
		id.setPreferredSize(new Dimension(300,30));
		pw.setPreferredSize(new Dimension(300,30));
		enter.setPreferredSize(new Dimension(180,30));
		cancel.setPreferredSize(new Dimension(180,30));
		CA_ActionListener cli = new CA_ActionListener();
		jp.add(idf);
		jp.add(id);
		jp.add(pwf);
		jp.add(pw);
		jp.add(namef);
		jp.add(name);
		jp.add(phonef);
		jp.add(phone);
		
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
				String Iname = name.getText();
				String Iphone = phone.getText();
				boolean res = connector.sendCreateAccountInfo(Iid, Ipw, Iname, Iphone); //받은 계정값을 커넥터로 보냄
				if (res) {
					JOptionPane.showMessageDialog(null, "계정 생성에 성공했습니다.");
					op.lf.setEnabled(true);
					setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null, "계정 생성에 실패했습니다.");
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

