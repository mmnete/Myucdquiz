package com.companywesbite.myucdquiz.utils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

    public class MySqliteHelper extends SQLiteOpenHelper {
    public MySqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySqliteHelper(Context context){
        super(context,Constant.DATABASE_NAME,null,Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists "+Constant.TABLE_NAME+"(" +
                ""+Constant.ID+" integer primary key autoincrement ,"+
                ""+Constant.NAME+"text not null ," +
                ""+Constant.DESCRIPTION+" text ," +
                ""+Constant.Num_OF_QUES+"integer )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
