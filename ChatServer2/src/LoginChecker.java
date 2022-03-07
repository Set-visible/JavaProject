import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;


//�α��� üĿ Ŭ������ �⺻ ���: user ��� ����Ʈ ����
//ChatServer ������Ʈ ������ ���� ������ �ؽ�Ʈ ���� ����
//�α��� ���, �ݷ� ���� ����
//���̵� ����
public class LoginChecker {
	ArrayList<User> userInfo = new ArrayList<>();
	File dataFile = new File("users.txt");
	String readData = null;
	StringTokenizer st;
	ArrayList<String> connectedUserList = new ArrayList<>(); //���ῡ ������ �������̵� ���� ��� ����Ʈ
	
	LoginChecker(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			//�Ʒ� ������ ���� ������ ������� �ؽ�Ʈ ������ �о�� User ��ü�� id pw ���� ������ userInfo �迭 ����Ʈ�� ������
			//�ؽ�Ʈ ���Ͽ��� �̸��� ��ȭ��ȣ�� ������, ������ �α��� �Ҷ��� ���̵�� �н����尪�� ������ ����
			while((readData = br.readLine()) != null) {
				if(readData.equals("")) {break;} // StringTokenizer�� ������ ���ٰ��� �η� �ν��ϴ°� �ƴ϶� empty String������ �ν��ؼ� �����޼����� �����°� ����
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
			System.out.println("Server>���� ����Ʈ���� �ҷ����µ������߽��ϴ�."+e);
			
		}
	}
	
	void reWriteUserList() {
		//�ش� �޼���� ���ο� ������ �߰��Ǿ����� ����.
		userInfo = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			//�Ʒ� ������ ���� ������ ������� �ؽ�Ʈ ������ �о�� User ��ü�� id pw ���� ������ userInfo �迭 ����Ʈ�� ������
			while((readData = br.readLine()) != null) {
				if(readData.equals("")) {break;} // StringTokenizer�� ������ ���ٰ��� �η� �ν��ϴ°� �ƴ϶� empty String������ �ν��ؼ� �����޼����� �����°� ����
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
			System.out.println("Server>���� ����Ʈ���� �ҷ����µ������߽��ϴ�."+e);
			
		}
	}
	
	String WriteNewUser(String id, String pw, String name, String phone) {
		//�ش� �޼���� �ԷµȰ� �ߺ��� ���̵����� �˻��ϰ�, �̻��� ���ٸ� users.txt �� ���� ������ �ְ� ������
		if(preventUserDuplicate(id)) {
			System.out.println("userDuplicationBlocked");
			return "CREATE_FAIL";
		}
		else {
			try {
				BufferedWriter wr = new BufferedWriter(new FileWriter(dataFile, true));
				//true������ ����� �ǹ���
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
		//���̵� ���� ������ userInfo �迭�� ������ŭ �ݺ�
		for(int i =0; i<userInfo.size();i++) {
			if (userInfo.get(i).id.equals(_id)) {
				dupl = true;
				System.out.println("server>�ߺ����̵� �����õ� ����"+_id);
			}
		}
		return dupl;
	}
	boolean check(String _id, String _pw) {
		//�ش� �޼���� �α��� �Ҷ� ����.
		boolean stat = false;  //���� ����Ʈ�� �ش������ �ִ��� ����
		boolean dupl = false;  //�ߺ��� ���� ���� ����
		//���̵� ���� ������ userInfo �迭�� ������ŭ �ݺ�
		for(int i =0; i<connectedUserList.size();i++) {
			if (connectedUserList.get(i).equals(_id)) {
				dupl = true;
				System.out.println("server>�ߺ��α��� ����"+_id);
			}
		}
		
		for(int i =0 ; i<userInfo.size();i++) {
			if(userInfo.get(i).id.equals(_id) && userInfo.get(i).pw.equals(_pw)){
				stat = true;
			}
		}
		if (dupl == false && stat ==true) {
			connectedUserList.add(_id);  //�α��� ������ ��ġ�ߴ� ���̵� ���� ��´�.
			
			return true;					//����� ���� ��Ͽ� �ش� ���̵� ����(�����ӻ���), ��ϵ� ���� ����Ʈ�� �����ϸ� �����㰡
		}else {
			return false;					//�ϳ��� �������� ������ ���� ���� �ź�
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
