
public class Operator {
	//채팅클라이언트 오퍼레이터
	LoginFrame lf = null;
	ChatFrame cf = null;   //채팅프레임 내부클래스 ChatConnector 가 서버와 통신을 담당함
	CreateUser cu = null;
	public static void main(String[] args) {
		Operator operator = new Operator();
		operator.cf = new ChatFrame(operator);
		operator.lf = new LoginFrame(operator);
		operator.cu = new CreateUser(operator);

	}

}
