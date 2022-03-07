import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	JPanel panel = new JPanel(new FlowLayout()); // ���̾ƿ� ���� 
	JButton enter = new JButton("Login"); // Button enter ���� 
	JButton cancel = new JButton("Cancel"); // Button enter ����
	JButton createAccount = new JButton("Create new account");
	JTextField typeId = new JTextField(); // id ������  ����
	JPasswordField typePassword = new JPasswordField(); // password ������ ���� ������ ** < ó�� ����
	JLabel id = new JLabel("I   D"); // �� type id
	JLabel password = new JLabel("Password"); // �� type password
	Operator mainOperator = null;
	DBConnector dbc = null;
	String userid = null;
	
	public LoginFrame(Operator _o) {
		mainOperator = _o;
		dbc = mainOperator.dc;//dbĿ���� Ŭ���� �����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyActionListener ml = new MyActionListener();
		setTitle("HumanBenchMark");
		id.setPreferredSize(new Dimension(70, 30));
		typeId.setPreferredSize(new Dimension(300, 30));
		password.setPreferredSize(new Dimension(70, 30));
		typePassword.setPreferredSize(new Dimension(300, 30));
		enter.setPreferredSize(new Dimension(185, 30));
		cancel.setPreferredSize(new Dimension(185, 30));
		createAccount.setPreferredSize(new Dimension(360,30));
		panel.add(id); //  ID �߰� 
		panel.add(typeId); // �Էµ� ID �߰� 
		panel.add(password); // PASSWORD �߰� 
		panel.add(typePassword); // �Էµ� PASSWORD �߰� 
		panel.add(enter);
		panel.add(cancel);
		panel.add(createAccount);
		setContentPane(panel);
		createAccount.addActionListener(ml); //createAccount ��ư�� �̺�Ʈ ������ �߰�
		enter.addActionListener(ml); // Login ��ư�� �̺�Ʈ ������ �߰� 
		cancel.addActionListener(ml); // Cancel ��ư�� �̺�Ʈ ������ �߰�

		setResizable(false);
		setSize(450, 180);
		//�α��� â�� ȭ�� �߾ӿ� ��ġ��Ű��...
  	    Dimension frameSize = this.getSize();   //������ ����� ��������
 	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		setVisible(true);
	}
	
	class MyActionListener implements ActionListener  {
		  //�̺�Ʈ�� �߻���Ų ������Ʈ(�ҽ�)
		public void actionPerformed(ActionEvent e) {
			JButton b =  (JButton)e.getSource();
			if (b.getText().equals("Login")) {	// �α��ι�ư�� ������...
//Password ������Ʈ���� ���ڿ� �о����
				String pw = "";
				for(int i=0; i<typePassword.getPassword().length; i++) {
					pw =  pw+ typePassword.getPassword()[i];
				}
				if(dbc.sendLoginInformation(typeId.getText(), pw)) {
					mainOperator.mf.setVisible(true);
					userid = typeId.getText();
					mainOperator.mf.SetIdLabel(userid);
					mainOperator.mf.setIdTitle();
					
					dispose();
				}else {
					JOptionPane.showMessageDialog(null, "�Է��� ������ �ùٸ��� �ʽ��ϴ�.");
				}
								
				
			}else if (b.getText().equals("Cancel")) {
				typeId.setText("");
				typePassword.setText("");
			}else if(b.getText().equals("Create new account")) {
				mainOperator.ca.setVisible(true);
				setEnabled(false);
			}
		}
	}
	
	
}
