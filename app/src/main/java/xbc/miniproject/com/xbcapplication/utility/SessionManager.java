package xbc.miniproject.com.xbcapplication.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    protected static SharedPreferences retrieveSharedReferences(Context context){
        return context.getSharedPreferences( Constanta.SHARED_PREFERENCE_NAME, context.MODE_PRIVATE);
    }

    protected static SharedPreferences.Editor retieveSharedPreferenceEditor (Context context){
        return retrieveSharedReferences(context).edit();
    }

    //simpan data login
    public static void saveLoginData(Context context,
                                            String username,
                                            String token,
                                            boolean login
    ){
        SharedPreferences.Editor editor = retieveSharedPreferenceEditor(context);
        editor.putString("username", username);
        editor.putString("token", token);
        editor.putBoolean("isLogin", login);
        editor.commit();
    }

    public static boolean isLogin(Context context){
        return retrieveSharedReferences(context).getBoolean("isLogin", false);
    }

    public static String getUsername(Context context){
        return retrieveSharedReferences(context).getString("username", "User");
    }
}