import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언 
	JButton enter = new JButton("Login"); // Button enter 선언 
	JButton cancel = new JButton("Cancel"); // Button enter 선언
	JButton createAccount = new JButton("Create new account");
	JTextField typeId = new JTextField(); // id 받은곳  선언
	JPasswordField typePassword = new JPasswordField(); // password 받은곳 선언 받으면 ** < 처럼 나옴
	JLabel id = new JLabel("I   D"); // 라벨 type id
	JLabel password = new JLabel("Password"); // 라벨 type password
	Operator mainOperator = null;
	DBConnector dbc = null;
	String userid = null;
	
	public LoginFrame(Operator _o) {
		mainOperator = _o;
		dbc = mainOperator.dc;//db커넥터 클래스 연결용
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
		panel.add(id); //  ID 추가 
		panel.add(typeId); // 입력된 ID 추가 
		panel.add(password); // PASSWORD 추가 
		panel.add(typePassword); // 입력된 PASSWORD 추가 
		panel.add(enter);
		panel.add(cancel);
		panel.add(createAccount);
		setContentPane(panel);
		createAccount.addActionListener(ml); //createAccount 버튼에 이벤트 리스너 추가
		enter.addActionListener(ml); // Login 버튼에 이벤트 리스너 추가 
		cancel.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

		setResizable(false);
		setSize(450, 180);
		//로그인 창을 화면 중앙에 배치시키기...
  	    Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
 	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		setVisible(true);
	}
	
	class MyActionListener implements ActionListener  {
		  //이벤트를 발생시킨 컴포넌트(소스)
		public void actionPerformed(ActionEvent e) {
			JButton b =  (JButton)e.getSource();
			if (b.getText().equals("Login")) {	// 로그인버튼을 누르면...
//Password 컴포넌트에서 문자열 읽어오기
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
					JOptionPane.showMessageDialog(null, "입력한 정보가 올바르지 않습니다.");
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
