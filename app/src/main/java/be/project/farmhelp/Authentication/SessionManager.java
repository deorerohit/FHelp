package be.project.farmhelp.Authentication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGGED_IN = "Is logged in";
    public static final String KEY_NAME = "name";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context context) {
        this.context = context;
        userSession = context.getSharedPreferences("userLogInSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String _name, String _mobile, String _password) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, _name);
        editor.putString(KEY_MOBILE, _mobile);
        editor.putString(KEY_PASSWORD, _password);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailsFromSession() {
        HashMap<String, String> usersData = new HashMap<>();

        usersData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        usersData.put(KEY_MOBILE, userSession.getString(KEY_MOBILE, null));
        usersData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));

        return usersData;
    }

    public boolean isLoggedIn() {
        if (userSession.getBoolean(IS_LOGGED_IN, false))
            return true;
        else
            return false;
    }

    public void logOutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
