import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.event.*;


//채팅 프레임
@SuppressWarnings("serial")
public class ChatFrame extends JFrame{
	
	Container c = getContentPane();
	JPanel jp = new JPanel(new FlowLayout());
	JTextArea textArea = new JTextArea();
	JTextArea msgViewPort = new JTextArea();
	JButton  jbtn = new JButton("SEND");
	Operator operator;
	DefaultListModel<String> model;
	JList<String> userList;
	ChatConnector cc = new ChatConnector();
	
	ChatFrame(Operator _o){
		operator = _o;
		Listener li = new Listener();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		model = new DefaultListModel<>();
		userList = new JList<>(model);
		
		c.add(jp);
		jp.add(msgViewPort);
		jp.add(userList);
		jp.add(textArea);
		jp.add(jbtn);
		msgViewPort.setPreferredSize(new Dimension(280,600));
		userList.setPreferredSize(new Dimension(100, 600));
		
		textArea.setPreferredSize(new Dimension(280, 100));
		jbtn.setPreferredSize(new Dimension(100,100));
		msgViewPort.setEditable(false); //채팅한게 보이는 창은 직접 값을 입력하거나 지울수 없음
		
		jbtn.addActionListener(li);
		setSize(500,850);
		Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
	 	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	    
	}
	class Listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton btnListener = (JButton)e.getSource();
			if (btnListener.getText().equals("SEND")) {
				String text = textArea.getText();
				if (text != null) {
					cc.sendText(text, operator.lf.idvalue);
					textArea.setText("");
				}
				else {
					System.out.println("널값임");
				}
			}
		}
		
	}
	
	//서버소켓 통신을 위한 내부 클래스 ChatConnector, 해당 클래스에서 모든 서버 통신을 처리
	public class ChatConnector extends Thread{
		Socket soc = null;
		OutputStream ops = null;
		DataOutputStream dops = null;
		InputStream ips = null;
		DataInputStream dips =null;
		
		//통신 처리용 구분자
		final String loginTag = "LOGIN";
		final String queryTag = "QUERY";
		final String broadcast = "BROAD_CAST";
		final String userResponse = "USR_RESPONSE";
		final String createOK ="CREATE_OK";
		
		ChatConnector(){
			
			try {
				soc = new Socket("localhost",60000);
				System.out.println("CLIENT LOG> 서버로 연결되었습니다.");
				ops = soc.getOutputStream();
				dops = new DataOutputStream(ops);
				ips = soc.getInputStream();
				dips =  new DataInputStream(ips);
			
			}catch(Exception e) {
				System.out.println("클라이언트>서버에 연결할 수 없습니다. " + e);
				
			}
		}
		
		public void run() {
			while(true) {
				String received = "";
				String statment = "";
				try {
					System.out.println("인풋 스트림 대기중..");
					received = dips.readUTF();
					System.out.println(received);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringTokenizer stk = new StringTokenizer(received, "//");
				statment = stk.nextToken();
				
				switch(statment) {
				case (broadcast):
					msgViewPort.append(stk.nextToken()+"\n");
					break;
				case userResponse:
					model.removeAllElements(); //새로운클라이언트가 들어오면 리스트를 초기화하고 새로 받아온 값으로 업데이트
					while(stk.hasMoreTokens()) {
						model.addElement(stk.nextToken());
					}
					break;
				case createOK:
					break;
				default:
					System.out.println(statment);
					break;
					
				}
				
				
			}
		}
		boolean sendCreateAccountInfo(String Iid, String Ipw, String Iname, String Iphone) {
			try {
				dops.writeUTF("CREATE"+"//"+Iid+"//"+Ipw+"//"+Iname+"//"+Iphone);
				if(dips.readUTF().equals("CREATE_OK")) {
					return true;
				}
				else {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		void askConnectedUserList(){
			try {
				dops.writeUTF("CON_USR_ASK");
			}catch(Exception e) {
				
			}
		}
		
		boolean sendLoginInformation(String uid, String upass){
			String msg = null;
			try {
				dops.writeUTF(loginTag+ "//" + uid + "//"+upass);
				msg = dips.readUTF();
			if(msg.equals("LOGIN_OK")) {
				System.out.println("sendLI에서 받은 값"+msg);
				dops.writeUTF("CON_USR_ASK");
				return true;
			}else {
				return false;
			}
			}catch(Exception e) {
				System.out.println("???");
				return false;
			}
		}
		
		void sendText(String msgSender, String id) {
			try {
				dops.writeUTF(queryTag+"//"+ "%s>%s".formatted(id, msgSender));
			}catch(Exception e) {
				System.out.println("client>"+e);
			}
		}
	}
}
