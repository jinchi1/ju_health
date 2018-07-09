package globesoft.ju_heath;
import android.app.Application;

/**
 * Created by system777 on 2018-01-04.
 */

public class Ju_healthApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private String userValue = "guest";
    private String userUid;
    private String userName;
    private String FCMtoken;
    private boolean connect;

    public void setuserValue(String value){
        this.userValue = value;
    }

    public String getuserValue() {
        return userValue;
    }

    public void setFCMtoken(String token){
        this.FCMtoken = token;
    }

    public String getFCMtoken() {
        return FCMtoken;
    }

    public void setUserName(String name) {
        this.userName = name;
    }
    public String getuserName(){
        return userName;
    }
    public void setUserUid(String uid) {
        this.userUid = uid;
    }
    public String getUserUid(){
        return userUid;
    }
    public void setuserConnect(boolean connect){
        this.connect = connect;
    }

    public boolean getuserConnect() {
        return connect;
    }

}
