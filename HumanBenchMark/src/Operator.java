
public class Operator {
	LoginFrame lf = null;
	DBConnector dc = null;
	MainFrame mf = null;
	CreateUserFrame ca = null;
	public static void main(String[] args) {
		Operator operator = new Operator();
		operator.dc = new DBConnector();
		operator.lf = new LoginFrame(operator);
		operator.mf = new MainFrame(operator);
		operator.ca = new CreateUserFrame(operator);
	}

}
