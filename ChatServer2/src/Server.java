import java.io.IOException;
import java.net.*; //서버 운용을 위한 라이브러리
import java.util.ArrayList; //동적리스트 운용을 위한 라이브러리

//채팅서버
//해당 클래스에서 필요한 기능: 클라이언트 연결을 위한 서버 소켓생성, 여러 클라이언트를 소켓에 연결하기 위한 동적리스트, 여러 클라이언트 통신을 위한 쓰레드 실행
public class Server {
	ServerSocket soc = null;
	ArrayList<ConnectedClient> client = new ArrayList<ConnectedClient>(); //ConnectedClient 객체를담는 동적리스트
	LoginChecker lc;
	ArrayList<String> users = new ArrayList<String>(); //로그인된 유저 리스트를 저장하는 동적 리스트

	public static void main(String[] args) {
		Server server = new Server();
		server.lc = new LoginChecker();
		try {
			server.soc = new ServerSocket(60000);  //60000번 포트를 채팅용 포트로 사용
			System.out.println("서버> 서버소켓이 생성되었습니다."+ server.soc);
			while(true) {
				Socket socket = server.soc.accept(); //서버는 항상 여기서 소켓요청을 대기하고 있다가 요청이 들어오면 accept()를실행
				ConnectedClient cc = new ConnectedClient(server, socket); //생성자값으로 서버 소켓값과 서버객체(Server@------)를 넘김
				server.client.add(cc); //동적리스트에 클라이언트 값 할당
				cc.start(); //실행되면 ConnectedClient의 run()메소드를 병렬적으로 실행시킴
			}
		} catch (IOException e) {
			System.out.println("Server>IOEXCEPTION 발생:"+e);
		} 
		
		
	}

}
