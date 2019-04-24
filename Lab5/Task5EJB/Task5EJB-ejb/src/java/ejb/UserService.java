package ejb;

public interface UserService {

    boolean login(String login, String psw);

    boolean getLoginStatus();

    String getMessage();

    void remove();

    int getMsgCounter();
}
