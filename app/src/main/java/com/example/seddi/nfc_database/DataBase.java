package com.example.seddi.nfc_database;
        import android.app.Activity;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;

public class DataBase extends Activity {
    DatabaseHelper mOpenHelper;

    private static final String DATABASE_NAME = "dbPresence.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "emargement";
    private static final String _ID = "_ID";
    public  static final String Id_NFC="Id_NFC";
    public  static final String Num_etudiant="Num_etudiant";
    public  static final String Nom="Nom";
    public  static final String Prenom="Prenom";
    public  static final String Date_arrive="Date_arrive";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql_1 = "CREATE TABLE " + TABLE_NAME + " (" + Id_NFC + " text not null, " + Num_etudiant + " integer not null, "+ Nom + " text not null, "+ Prenom + " text not null );";
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mOpenHelper = new DatabaseHelper(this);

    }

    public void CreateTable() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String sql = "CREATE TABLE " + TABLE_NAME + " ("+ Id_NFC + " text not null, " + Num_etudiant + " integer not null, "+ Nom + " text not null, "+ Prenom + " text not null );";
        //   Log.i("createDB=", sql);
        try {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            db.execSQL(sql);
            setTitle("drop");
        } catch (SQLException e) {
            setTitle("exception");
        }
    }
    private void dropTable() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String sql = "drop table " + TABLE_NAME;
        try {
            db.execSQL(sql);
            setTitle(sql);
        } catch (SQLException e) {
            setTitle("exception");
        }
    }
    public void insertItem(String Id_NFC,Long Num_etudiant ,String Nom, String Prenom) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String sql1 = "insert into " + TABLE_NAME + " (" + Id_NFC + ", " + Num_etudiant + ", " + Nom + ", "+ Prenom + ", "+Date_arrive+" ) values('"+Id_NFC+"',"+Num_etudiant+",'"+Nom+"','"+Prenom+"');";
        String sql2 = "insert into " + TABLE_NAME + " (" + Id_NFC + ", " + Num_etudiant + ", " + Nom + ", "+ Prenom + " ) values('80 3f 5f 0a 80 6b04',20163875,'KADI','Seddik');";
        try {
            Log.i("sql1=", sql1);
            Log.i("sql2=", sql2);
            db.execSQL(sql1);
            db.execSQL(sql2);
            setTitle("done");
        } catch (SQLException e) {
            setTitle("exception");
        }
    }

    private void deleteItem() {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.delete(TABLE_NAME, " title = 'haiyang'", null);
            setTitle("title");
        } catch (SQLException e) {

        }

    }
    public String showItems() {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String col[] = { Id_NFC, Num_etudiant,Nom,Prenom };
        Cursor cur = db.query(TABLE_NAME, col, null, null, null, null, null);
        Integer num = cur.getCount();
        setTitle(Integer.toString(num));
        cur.moveToLast();
        return  cur.getString(3);
    }
    public String showItemByNFC(String NFC) {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String col[] = { Id_NFC, Num_etudiant,Nom,Prenom };

        String whereClause ="Id_NFC=?";
        String[] whereArgs=new String[]{NFC};
        Cursor cur = db.query(TABLE_NAME, col, whereClause, whereArgs, null, null, null);
        Integer num = cur.getCount();
        setTitle(Integer.toString(num));
        TextView text=findViewById(R.id.data);
        cur.moveToFirst();
        text.setText(cur.getString(3)+" "+cur.getString(2));
        return  cur.getString(3)+" "+cur.getString(2);
    }
}