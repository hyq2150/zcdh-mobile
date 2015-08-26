package com.zcdh.mobile.utils;

 import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
	
	
    //用户数据库文件的版本
    private static final int DB_VERSION    = 1;

    private SQLiteDatabase myDataBase    = null;

    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     * @param context    上下文对象
     * @param name        数据库名称
     * @param factory    一般都是null
     * @param version    当前数据库的版本，值必须是整数并且是递增的状态
     */
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        //必须通过super调用父类当中的构造函数
        super(context, name, null, version);
    }
    
    public DBHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DBHelper(Context context, String name){
        this(context,name,DB_VERSION);
    }
    
    public DBHelper (Context context) {
        this(context, "");
    }
    
    
    
    @Override
    public synchronized void close() {
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }
    
    /**
     * 该函数是在第一次创建的时候执行，
     * 实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    }
    
    /**
     * 数据库表结构有变化时采用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    }

}