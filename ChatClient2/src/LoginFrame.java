import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;

//�α��� GUI
@SuppressWarnings("serial")
public class LoginFrame extends JFrame{
	
	Container c = getContentPane();
	JPanel panel = new JPanel(new FlowLayout());
	JButton enter = new JButton("ENTER");
	JTextField idField = new JTextField();
	JPasswordField pwField = new JPasswordField();
	String idvalue;
	JButton cancel = new JButton("CANCEL");
	JButton create = new JButton("�� ���� ����");
	JLabel idLable = new JLabel("I      D");
	JLabel pwLable = new JLabel("PassWord");
	
	Operator operator;
	ChatFrame.ChatConnector connector; //���� ����� ���� ChatFrame�� ����Ŭ���� ChatConnector�� ����
	
	
	
	LoginFrame(Operator _o){
		Listener li = new Listener();
		operator = _o;				//�������� ����Ǵ� ���۷����� Ŭ������ ����
		connector = operator.cf.cc; //���� ����� ���� ChatFrame�� ����Ŭ���� ChatConnector�� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		idField.setPreferredSize(new Dimension(300,30));
		pwField.setPreferredSize(new Dimension(300,30));
		idLable.setPreferredSize(new Dimension(70,30));
	 	pwLable.setPreferredSize(new Dimension(70,30));
		create.setPreferredSize(new Dimension(150,30));
		enter.setPreferredSize(new Dimension(150,30));
		cancel.setPreferredSize(new Dimension(150,30));
		c.add(panel);
		panel.add(idLable);
		panel.add(idField);
		panel.add(pwLable);
		panel.add(pwField);
		panel.add(enter);
		panel.add(cancel);
		panel.add(create);
		
		setResizable(false);
		
		setVisible(true);
		setSize(400,180);
		
		enter.addActionListener(li);
		cancel.addActionListener(li);
		create.addActionListener(li);
		
		Dimension frameSize = this.getSize();   //������ ����� ��������
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	

	class Listener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JButton btnListener = (JButton)e.getSource();
			if(btnListener.getText().equals("ENTER")) {
				String id = idField.getText();
				String pw = "";
				for(int i=0; i<pwField.getPassword().length; i++) {
					pw =  pw + pwField.getPassword()[i];
					}
				boolean a = connector.sendLoginInformation(id, pw);
				if(a) {
					idvalue = id;  //�� Ŭ���̾�Ʈ���� idvalue�� ���� ����(�������� �޾ƿ�������)
					System.out.println(a);
					dispose();
					operator.cf.setVisible(true);
					connector.interrupt(); //Ŀ���� ������ ����
					operator.cf.cc.start();
				}
				else {
					JOptionPane.showMessageDialog(null, "����� ������ ��ġ���� �ʰų�, �̹� �α��ε� �����Դϴ�.");
				}
				
			}
			else if(btnListener.getText().equals("�� ���� ����")) {
				operator.cu.setVisible(true);
				setEnabled(false);
			}
		}
		
	}
	
}
