import java.io.*; //ioStream을 위한 라이브러리
import java.net.*; //서버 라이브러리 사용
import java.util.StringTokenizer; //로그인시 구분자를 통해 데이터를 분석하기 때문에 필요한 문자열 분리기

public class ConnectedClient extends Thread{
	Socket socket;
	Server server;
	
	OutputStream outStream;
	DataOutputStream dataOutStream; //DOS,DIS는 직접적으로 파일을 읽고 쓸수 없음. 그래서 OutPutStream의 도움을 받음
	InputStream inStream;
	DataInputStream dataInStream;
	String msg;	//모든 데이터 통신은 msg 문자열로 들어가고 나감
	String ActiveUser;
	final String loginTag = "LOGIN";  //클라이언트 측에서 넘긴 데이터가 로그인인지 아니면 질의 인지 구분하기위한 구분자
	final String queryTag = "QUERY";  //클라이언트 측에서 넘긴 데이터가 로그인인지 아니면 질의 인지 구분하기위한 구분자
	final String broadcast = "BROAD_CAST"; //받은 내용을 전체에게 뿌릴때 쓰는 구분자
	final String createTag = "CREATE";		//계정 생성 구분자
	final String userAsk = "CON_USR_ASK";   //현재 접속한 유저 리스트질의 구분자
	
	ConnectedClient(Server server, Socket socket){
		//생성자 호출시 넘겨받은 값을 ConnectedClient 필드에 저장
		this.server = server;
		this.socket = socket;
	}
	
	//run()은 항상 public void 한정자,리턴이 필요
	public void run() {
		try {
			System.out.println("Server>"+ this.socket.getInetAddress()+"에서접속이 연결됌.");
			outStream = this.socket.getOutputStream();  //데이터가 나가기 위한 채널을 생성
			dataOutStream = new DataOutputStream(outStream);  //생성된 채널을 객체로 받아 원하는 데이터를 넘기는 변수
			inStream = this.socket.getInputStream();
			dataInStream = new DataInputStream(inStream);
			//아래 해당 구문을 넣음으로써 한번 활성된 클라이언트에서 연결이 끊기지 않는 이상 지속적으로 데이터 값을 받아오고 넘겨주는 기능을 수행함
		while(true) {	
			msg = dataInStream.readUTF();
			System.out.println(msg);
			StringTokenizer stk = new StringTokenizer(msg, "//");  //받아온 msg 값에서 로그인 데이터 입력시 사용
			String condition = stk.nextToken();
			if(condition.equals(loginTag)) {
				String id = stk.nextToken();
				String pw = stk.nextToken();
				//로그인을 위한 아이디와 암호확인 작업필요..
				if(server.lc.check(id,pw)){
					ActiveUser = id;
					dataOutStream.writeUTF("LOGIN_OK");
					dataOutStream.writeUTF("CONN_UPDATE"); //로그인 성공후 연결유저리스트 업데이트 신호송출
					}	
				else{
					dataOutStream.writeUTF("LOGIN_FAIL");
					}
				}
			else if(condition.equals(queryTag)) {
				String talk = stk.nextToken();
				for(int i = 0 ; server.client.size()>i ; i++){
					server.client.get(i).dataOutStream.writeUTF(broadcast+"//"+talk); //서버가 자신의 쿼리를 받고 다시 출력되는걸 방지하기 위해 broadcast라는 구분자를 붙임
				}
				
				System.out.println("브로드캐스팅됨");
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
			// 사용자가 퇴장 할경우, 퇴장한 사용자의 이름값을 검출하고, 서버에서 리스트로 생성한 클라이언트 값을 지움
			System.out.println("유저퇴장 감지");
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
			System.out.println("Server>클라이언트 측에서 연결을종료했습니다.: "+ ActiveUser);
		}
	}
}
