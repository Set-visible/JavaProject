import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;


//로그인 체커 클래스의 기본 기능: user 어레이 리스트 생성
//ChatServer 프로젝트 파일의 유저 데이터 텍스트 파일 열람
//로그인 허용, 반려 여부 결정
//아이디 생성
public class LoginChecker {
	ArrayList<User> userInfo = new ArrayList<>();
	File dataFile = new File("users.txt");
	String readData = null;
	StringTokenizer st;
	ArrayList<String> connectedUserList = new ArrayList<>(); //연결에 성공한 유저아이디 값을 담는 리스트
	
	LoginChecker(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			//아래 구문을 통해 서버는 유저목록 텍스트 파일을 읽어와 User 객체에 id pw 값을 저장후 userInfo 배열 리스트에 저장함
			//텍스트 파일에는 이름과 전화번호도 있지만, 실제로 로그인 할때는 아이디와 패스워드값만 가지고 운용됌
			while((readData = br.readLine()) != null) {
				if(readData.equals("")) {break;} // StringTokenizer가 마지막 빈줄값을 널로 인식하는게 아니라 empty String값으로 인식해서 오류메세지가 나오는걸 방지
				st = new StringTokenizer(readData, "//");
				String userId = st.nextToken();
				String userPw = st.nextToken();
				String userName = st.nextToken();
				String userPhone = st.nextToken();
				User user = new User(userId, userPw);
				userInfo.add(user);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Server>유저 리스트값을 불러오는데실패했습니다."+e);
			
		}
	}
	
	void reWriteUserList() {
		//해당 메서드는 새로운 계정이 추가되었을때 사용됌.
		userInfo = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			//아래 구문을 통해 서버는 유저목록 텍스트 파일을 읽어와 User 객체에 id pw 값을 저장후 userInfo 배열 리스트에 저장함
			while((readData = br.readLine()) != null) {
				if(readData.equals("")) {break;} // StringTokenizer가 마지막 빈줄값을 널로 인식하는게 아니라 empty String값으로 인식해서 오류메세지가 나오는걸 방지
				st = new StringTokenizer(readData, "//");
				String userId = st.nextToken();
				String userPw = st.nextToken();
				String userName = st.nextToken();
				String userPhone = st.nextToken();
				User user = new User(userId, userPw);
				userInfo.add(user);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Server>유저 리스트값을 불러오는데실패했습니다."+e);
			
		}
	}
	
	String WriteNewUser(String id, String pw, String name, String phone) {
		//해당 메서드는 입력된게 중복된 아이디인지 검사하고, 이상이 없다면 users.txt 에 계정 정보를 넣고 저장함
		if(preventUserDuplicate(id)) {
			System.out.println("userDuplicationBlocked");
			return "CREATE_FAIL";
		}
		else {
			try {
				BufferedWriter wr = new BufferedWriter(new FileWriter(dataFile, true));
				//true없으면 덮어쓰기 되버림
				wr.write(id+"//"+pw+"//"+name+"//"+phone+"\r\n");
				wr.close();
				reWriteUserList();
				return "CREATE_OK";
			} catch (IOException e) {
				System.out.println(e);
				return "CREATE_FAIL";
			}
		}
	}
	
	boolean preventUserDuplicate(String _id) {
		boolean dupl = false;
		//아이디 값을 저장한 userInfo 배열의 갯수만큼 반복
		for(int i =0; i<userInfo.size();i++) {
			if (userInfo.get(i).id.equals(_id)) {
				dupl = true;
				System.out.println("server>중복아이디 생성시도 감지"+_id);
			}
		}
		return dupl;
	}
	boolean check(String _id, String _pw) {
		//해당 메서드는 로그인 할때 사용됌.
		boolean stat = false;  //현재 리스트에 해당계정이 있는지 조사
		boolean dupl = false;  //중복된 접속 여부 조사
		//아이디 값을 저장한 userInfo 배열의 갯수만큼 반복
		for(int i =0; i<connectedUserList.size();i++) {
			if (connectedUserList.get(i).equals(_id)) {
				dupl = true;
				System.out.println("server>중복로그인 감지"+_id);
			}
		}
		
		for(int i =0 ; i<userInfo.size();i++) {
			if(userInfo.get(i).id.equals(_id) && userInfo.get(i).pw.equals(_pw)){
				stat = true;
			}
		}
		if (dupl == false && stat ==true) {
			connectedUserList.add(_id);  //로그인 성공시 일치했던 아이디 값을 담는다.
			
			return true;					//연결된 유저 목록에 해당 아이디가 없고(미접속상태), 기록된 유저 리스트에 존재하면 접근허가
		}else {
			return false;					//하나라도 충족하지 않으면 전부 접근 거부
		}	
	}
	
	String updateConnectedUserList() {
		String userStr="";
		for(int i =0 ;connectedUserList.size()>i; i++) {
			userStr = userStr + connectedUserList.get(i)+ "//";
		}
		return userStr;
	}
	
	void removeExitUser(String connectedUser) {
		if(connectedUserList.contains(connectedUser)) {
			connectedUserList.remove(connectedUser);
		}
	}
	
}
