import java.io.IOException;
import java.net.*; //���� ����� ���� ���̺귯��
import java.util.ArrayList; //��������Ʈ ����� ���� ���̺귯��

//ä�ü���
//�ش� Ŭ�������� �ʿ��� ���: Ŭ���̾�Ʈ ������ ���� ���� ���ϻ���, ���� Ŭ���̾�Ʈ�� ���Ͽ� �����ϱ� ���� ��������Ʈ, ���� Ŭ���̾�Ʈ ����� ���� ������ ����
public class Server {
	ServerSocket soc = null;
	ArrayList<ConnectedClient> client = new ArrayList<ConnectedClient>(); //ConnectedClient ��ü����� ��������Ʈ
	LoginChecker lc;
	ArrayList<String> users = new ArrayList<String>(); //�α��ε� ���� ����Ʈ�� �����ϴ� ���� ����Ʈ

	public static void main(String[] args) {
		Server server = new Server();
		server.lc = new LoginChecker();
		try {
			server.soc = new ServerSocket(60000);  //60000�� ��Ʈ�� ä�ÿ� ��Ʈ�� ���
			System.out.println("����> ���������� �����Ǿ����ϴ�."+ server.soc);
			while(true) {
				Socket socket = server.soc.accept(); //������ �׻� ���⼭ ���Ͽ�û�� ����ϰ� �ִٰ� ��û�� ������ accept()������
				ConnectedClient cc = new ConnectedClient(server, socket); //�����ڰ����� ���� ���ϰ��� ������ü(Server@------)�� �ѱ�
				server.client.add(cc); //��������Ʈ�� Ŭ���̾�Ʈ �� �Ҵ�
				cc.start(); //����Ǹ� ConnectedClient�� run()�޼ҵ带 ���������� �����Ŵ
			}
		} catch (IOException e) {
			System.out.println("Server>IOEXCEPTION �߻�:"+e);
		} 
		
		
	}

}
