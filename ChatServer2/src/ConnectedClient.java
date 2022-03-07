import java.io.*; //ioStream�� ���� ���̺귯��
import java.net.*; //���� ���̺귯�� ���
import java.util.StringTokenizer; //�α��ν� �����ڸ� ���� �����͸� �м��ϱ� ������ �ʿ��� ���ڿ� �и���

public class ConnectedClient extends Thread{
	Socket socket;
	Server server;
	
	OutputStream outStream;
	DataOutputStream dataOutStream; //DOS,DIS�� ���������� ������ �а� ���� ����. �׷��� OutPutStream�� ������ ����
	InputStream inStream;
	DataInputStream dataInStream;
	String msg;	//��� ������ ����� msg ���ڿ��� ���� ����
	String ActiveUser;
	final String loginTag = "LOGIN";  //Ŭ���̾�Ʈ ������ �ѱ� �����Ͱ� �α������� �ƴϸ� ���� ���� �����ϱ����� ������
	final String queryTag = "QUERY";  //Ŭ���̾�Ʈ ������ �ѱ� �����Ͱ� �α������� �ƴϸ� ���� ���� �����ϱ����� ������
	final String broadcast = "BROAD_CAST"; //���� ������ ��ü���� �Ѹ��� ���� ������
	final String createTag = "CREATE";		//���� ���� ������
	final String userAsk = "CON_USR_ASK";   //���� ������ ���� ����Ʈ���� ������
	
	ConnectedClient(Server server, Socket socket){
		//������ ȣ��� �Ѱܹ��� ���� ConnectedClient �ʵ忡 ����
		this.server = server;
		this.socket = socket;
	}
	
	//run()�� �׻� public void ������,������ �ʿ�
	public void run() {
		try {
			System.out.println("Server>"+ this.socket.getInetAddress()+"���������� �����.");
			outStream = this.socket.getOutputStream();  //�����Ͱ� ������ ���� ä���� ����
			dataOutStream = new DataOutputStream(outStream);  //������ ä���� ��ü�� �޾� ���ϴ� �����͸� �ѱ�� ����
			inStream = this.socket.getInputStream();
			dataInStream = new DataInputStream(inStream);
			//�Ʒ� �ش� ������ �������ν� �ѹ� Ȱ���� Ŭ���̾�Ʈ���� ������ ������ �ʴ� �̻� ���������� ������ ���� �޾ƿ��� �Ѱ��ִ� ����� ������
		while(true) {	
			msg = dataInStream.readUTF();
			System.out.println(msg);
			StringTokenizer stk = new StringTokenizer(msg, "//");  //�޾ƿ� msg ������ �α��� ������ �Է½� ���
			String condition = stk.nextToken();
			if(condition.equals(loginTag)) {
				String id = stk.nextToken();
				String pw = stk.nextToken();
				//�α����� ���� ���̵�� ��ȣȮ�� �۾��ʿ�..
				if(server.lc.check(id,pw)){
					ActiveUser = id;
					dataOutStream.writeUTF("LOGIN_OK");
					dataOutStream.writeUTF("CONN_UPDATE"); //�α��� ������ ������������Ʈ ������Ʈ ��ȣ����
					}	
				else{
					dataOutStream.writeUTF("LOGIN_FAIL");
					}
				}
			else if(condition.equals(queryTag)) {
				String talk = stk.nextToken();
				for(int i = 0 ; server.client.size()>i ; i++){
					server.client.get(i).dataOutStream.writeUTF(broadcast+"//"+talk); //������ �ڽ��� ������ �ް� �ٽ� ��µǴ°� �����ϱ� ���� broadcast��� �����ڸ� ����
				}
				
				System.out.println("��ε�ĳ���õ�");
			}
			else if(condition.equals(createTag)) {
				String id = stk.nextToken();
				String pw = stk.nextToken();
				String name = stk.nextToken();
				String phone = stk.nextToken();
				dataOutStream.writeUTF(server.lc.WriteNewUser(id, pw, name, phone));
			}
			else if(condition.equals(userAsk)) {
				String response = server.lc.updateConnectedUserList();
				for(int i = 0 ; server.client.size()>i ; i++){
					server.client.get(i).dataOutStream.writeUTF("USR_RESPONSE"+"//"+response);
				}
				
			}
		
		}
		}catch(Exception e) {
			// ����ڰ� ���� �Ұ��, ������ ������� �̸����� �����ϰ�, �������� ����Ʈ�� ������ Ŭ���̾�Ʈ ���� ����
			System.out.println("�������� ����");
			server.lc.removeExitUser(ActiveUser);
			
			for(int x = 0; server.client.size()>x ; x++) {
				if(ActiveUser == null) {
					break;
				}
				if(server.client.get(x).ActiveUser.equals(ActiveUser)) {
					server.client.remove(x);
				}
			}
			
			try {
				String response = server.lc.updateConnectedUserList();
				for(int i = 0 ; server.client.size()>i ; i++){
					server.client.get(i).dataOutStream.writeUTF("USR_RESPONSE"+"//"+response);
				}
				
			} catch (Exception ee) {
				System.out.println(server.client.size());
				System.out.println("server>connected user update Error: "+ee);
			}
			System.out.println("Server>Ŭ���̾�Ʈ ������ �����������߽��ϴ�.: "+ ActiveUser);
		}
	}
}
