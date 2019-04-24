package ejb;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
@Local(UserService.class)
public class UserServiceBean implements UserService {

    @EJB
    private AuthData authChecker;
    private boolean userLoginStatus = false;
    private boolean wasReset = false;
    int msgCounter = 0;

    private void reset() {
        msgCounter = 0;
        userLoginStatus = false;
    }

    public UserServiceBean() {
    }

    @Override
    public int getMsgCounter() {
        return msgCounter;
    }

    @Override
    public boolean getLoginStatus() {
        return userLoginStatus;
    }

    @Override
    public boolean login(String login, String psw) {

        reset();
        if (authChecker.checkAuth(login, psw)) {

            userLoginStatus = true;
        } else {
            userLoginStatus = false;
        }

        return userLoginStatus;
    }

    @Override
    public String getMessage() {

        String msg = "";

        if (userLoginStatus) {
            msgCounter++;
            msg = " Hello " + msgCounter + " from EJB " + this.hashCode() + "!";
            if (msgCounter == 3) {
                reset();
                wasReset = true;
            }

        } else {
            if (wasReset) {
                wasReset = false;
                msg = "Message limit was reached for this session!";
            } else {
                msg = "Incorrect login or password, try once more!";
            }

        }
        return msg;
    }


    @PostConstruct
    void init() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PostConstruct --> " + date + " : UserServiceBean created, hash: " + this.hashCode());

    }

    @PreDestroy
    void preDestroy() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PreDestroy --> " + date + " : hash: " + this.hashCode());
    }

    @PostActivate
    void postActivate() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PostActivate --> " + date + " : hash: " + this.hashCode());
    }

    @PrePassivate
    void prePassivate() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PrePassivate --> " + date + " : hash: " + this.hashCode());
    }

    @Remove
    @Override
    public void remove() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @Remove --> " + date + " : hash: " + this.hashCode());

    }

}
