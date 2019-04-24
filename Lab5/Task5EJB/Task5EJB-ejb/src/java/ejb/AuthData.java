package ejb;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

@Singleton
@Startup
@LocalBean
public class AuthData {

    private static HashMap<String, String> db;

    public boolean checkAuth(String login, String psw) {

        if (db.containsKey(login)) {
            String dbPsw = db.get(login);
            if (dbPsw == null) {
                return false;
            }
            if (dbPsw.equals(psw)) {
                return true;
            }
        }

        return false;
    }

    public AuthData() {

        db = new HashMap<>();
        String res1 = db.put("evgen", "1111");
        String res2 = db.put("admin", "0000");
        String res3 = db.put("java", "java");
    }

    @PostConstruct
    public void init() {
        System.out.println("----> AuthData bean was created");
    }

}
