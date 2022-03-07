import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;

//로그인 GUI
@SuppressWarnings("serial")
public class LoginFrame extends JFrame{
	
	Container c = getContentPane();
	JPanel panel = new JPanel(new FlowLayout());
	JButton enter = new JButton("ENTER");
	JTextField idField = new JTextField();
	JPasswordField pwField = new JPasswordField();
	String idvalue;
	JButton cancel = new JButton("CANCEL");
	JButton create = new JButton("새 계정 생성");
	JLabel idLable = new JLabel("I      D");
	JLabel pwLable = new JLabel("PassWord");
	
	Operator operator;
	ChatFrame.ChatConnector connector; //소켓 통신을 위해 ChatFrame의 내부클래스 ChatConnector을 받음
	
	
	
	LoginFrame(Operator _o){
		Listener li = new Listener();
		operator = _o;				//메인으로 실행되는 오퍼레이터 클래스를 받음
		connector = operator.cf.cc; //소켓 통신을 위해 ChatFrame의 내부클래스 ChatConnector을 받음
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
		
		Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
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
					idvalue = id;  //각 클라이언트마다 idvalue값 따로 저장(서버에서 받아오지않음)
					System.out.println(a);
					dispose();
					operator.cf.setVisible(true);
					connector.interrupt(); //커넥터 스레드 종료
					operator.cf.cc.start();
				}
				else {
					JOptionPane.showMessageDialog(null, "사용자 정보가 일치하지 않거나, 이미 로그인된 계정입니다.");
				}
				
			}
			else if(btnListener.getText().equals("새 계정 생성")) {
				operator.cu.setVisible(true);
				setEnabled(false);
			}
		}
		
	}
	
}
