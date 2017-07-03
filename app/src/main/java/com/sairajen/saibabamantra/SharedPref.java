package com.sairajen.saibabamantra;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context ctx;
    private SharedPreferences custom_prefence;

    public SharedPref(Context context) {
        this.ctx = context;
        custom_prefence = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE);
    }

    public void setFirstTime(boolean value){
        custom_prefence.edit().putBoolean("FIRST_TIME", value).apply();
    }

    public boolean isFirstTime(){
        return custom_prefence.getBoolean("FIRST_TIME", true);
    }

}
