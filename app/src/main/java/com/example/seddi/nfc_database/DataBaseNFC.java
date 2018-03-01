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


/**
 * Created by seddi on 01/02/2018.
 */

public class DataBaseNFC extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "dbPresence.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "emargement";
    private static final String _ID = "_ID";
    public  static final String Id_NFC="Id_NFC";
    public  static final String Num_etudiant="Num_etudiant";
    public  static final String Nom="Nom";
    public  static final String Prenom="Prenom";
    public  static final String Date_arrive="Date_arrive";
    public static final String present="present";


    public  DataBaseNFC( Context context,SQLiteDatabase.CursorFactory factory){
        super(context,DATABASE_NAME,factory,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1 = "CREATE TABLE " + TABLE_NAME + " (" + Id_NFC + " text not null, " + Num_etudiant + " integer not null, "+ Nom + " text not null, "+ Prenom + " text not null,  "+Date_arrive+" text not null, "+present+" integer);";
      db.execSQL(sql_1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void Createtable(){
        SQLiteDatabase db = super.getWritableDatabase();
        String sql_1 = "CREATE TABLE " + TABLE_NAME + " (" + Id_NFC + " text not null, " + Num_etudiant + " integer not null, "+ Nom + " text not null, "+ Prenom + " text not null, "+Date_arrive+" text not null, "+present+" integer);";
        db.execSQL(sql_1);
    }
    public void dropTable() {
        SQLiteDatabase db = super.getWritableDatabase();
        String sql = "drop table " + TABLE_NAME;
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
        }
    }
    public void insertItem(String Id_NFC,Long Num_etudiant ,String Nom, String Prenom) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        SQLiteDatabase db = super.getWritableDatabase();
        String sql1 = "INSERT INTO " + TABLE_NAME + " (Id_NFC , Num_etudiant , Nom , Prenom , Date_arrive , present ) values('"+ Id_NFC + "', " + Num_etudiant + ", '" + Nom + "', '"+ Prenom + "', '"+timeStamp+"', "+1+");";
        Log.i("sql1=", sql1);
        try {
            Log.i("sql1=", sql1);
            db.execSQL(sql1);
            Log.e("info","Element inseré");
        } catch (SQLException e) {
            e.getMessage();
        }
    }
    private void deleteItem() {
        try {
            SQLiteDatabase db = super.getWritableDatabase();
            db.delete(TABLE_NAME, " title = 'haiyang'", null);
        } catch (SQLException e) {

        }

    }

    public String showItems() {

        SQLiteDatabase db = super.getReadableDatabase();
        String col[] = { Id_NFC, Num_etudiant,Nom,Prenom };
        int p=1;
        String whereClause ="present=?";
        String[] whereArgs=new String[]{p+""};
        Cursor cur = db.query(TABLE_NAME, col, whereClause, whereArgs, null, null, null);
        Integer num = cur.getCount();
        Log.e("nbr elements",num+"");
        cur.moveToFirst();
        String items="";
        if(cur.getCount()!=0){
            for(int i=0;i<cur.getCount();i++){
                items+=(cur.getString(3)+" "+cur.getString(2)+" "+cur.getString(1)+"/");
                cur.moveToNext();
            }
            return  items;
        }else{
            Log.e("table emargement","aucun element present/ ");
            return items;
        }
    }
    public void update(int presence){
        SQLiteDatabase db = super.getReadableDatabase();
        String sql="UPDATE "+TABLE_NAME+" SET "+ present+" = "+presence+" WHERE "+present+" != "+presence+" ;";
        db.execSQL(sql);
    }
    public void updatePresence(String NFC){
        SQLiteDatabase db = super.getReadableDatabase();
        String sql=" UPDATE "+TABLE_NAME+" SET "+present+"  = 1 WHERE "+Id_NFC+" = '"+NFC+"' ;";
        db.execSQL(sql);
    }

    public String showItemByNFC(String NFC) {

        SQLiteDatabase db = super.getReadableDatabase();
        String col[] = { Id_NFC, Num_etudiant,Nom,Prenom };

        String whereClause ="Id_NFC=?";
        String[] whereArgs=new String[]{NFC};
        Cursor cur = db.query(TABLE_NAME, col, whereClause, whereArgs, null, null, null);
        String s="";
        if(cur.getCount()!= 0){
            cur.moveToFirst();
            s="Nom : "+cur.getString(2)+"\n Prenom : "+cur.getString(3)+"\nNum etudiant: "+cur.getString(1);
        }else{
            s="";
        }

        return  s;
    }
}
