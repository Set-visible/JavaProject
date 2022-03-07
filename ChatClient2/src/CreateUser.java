import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import CreateAccount.CA_ActionListener;



//�α��� â���� �� ���� ������ ������ ��� �����Ǵ� Ŭ����. ���⼭ ���� ������ �����
@SuppressWarnings("serial")
public class CreateUser extends JFrame {
	Container c  =getContentPane();
	JPanel jp = new JPanel(new FlowLayout());
	JButton enter = new JButton("����");
	JButton cancel = new JButton("���");
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
		  //�̺�Ʈ�� �߻���Ų ������Ʈ(�ҽ�)
		public void actionPerformed(ActionEvent e) {
			JButton b =  (JButton)e.getSource();
			if (b.getText().equals("����")) {	
				String Iid = id.getText();
				String Ipw = pw.getText();
				String Iname = name.getText();
				String Iphone = phone.getText();
				boolean res = connector.sendCreateAccountInfo(Iid, Ipw, Iname, Iphone); //���� �������� Ŀ���ͷ� ����
				if (res) {
					JOptionPane.showMessageDialog(null, "���� ������ �����߽��ϴ�.");
					op.lf.setEnabled(true);
					setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null, "���� ������ �����߽��ϴ�.");
					op.lf.setEnabled(true);
					setVisible(false);
				}
			}
			else if (b.getText().equals("���")){
				op.lf.setEnabled(true);
				setVisible(false);
			}
		}
	}
	
}

