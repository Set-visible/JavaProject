
public class Operator {
	//ä��Ŭ���̾�Ʈ ���۷�����
	LoginFrame lf = null;
	ChatFrame cf = null;   //ä�������� ����Ŭ���� ChatConnector �� ������ ����� �����
	CreateUser cu = null;
	public static void main(String[] args) {
		Operator operator = new Operator();
		operator.cf = new ChatFrame(operator);
		operator.lf = new LoginFrame(operator);
		operator.cu = new CreateUser(operator);

	}

}
