package com.example.dbsave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DNAME = "storeContacts";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHNO = "phone_number";

    DatabaseHandler(Context context) {
        super ( context, DNAME, null, VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHNO + " TEXT" + ")";
        db.execSQL ( CREATE_TABLE_CONTACTS );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_CONTACTS );
        onCreate ( db );
    }

    void addContacts(Contact contact) {
        SQLiteDatabase db1 = this.getWritableDatabase ();
        ContentValues values = new ContentValues ();
        values.put ( KEY_NAME, contact.getName () );
        values.put ( KEY_PHNO, contact.getPhoneNumber () );
        long i = db1.insert ( TABLE_CONTACTS, null, values );
        if (i != -1) {
            Log.e ( "DatabaseH", "addContacts: " );
        }
        db1.close ();
    }

    List<Contact> getcontact() {
        List<Contact> contacts = new ArrayList<> ();
        SQLiteDatabase db1 = this.getReadableDatabase ();
        Cursor res = db1.rawQuery ( "select * from " + TABLE_CONTACTS, null );
        if (res != null) {
            res.moveToFirst ();
            do {
                String name = res.getString ( res.getColumnIndex ( KEY_NAME ) );
                String mobile = res.getString ( res.getColumnIndex ( KEY_PHNO ) );
                int id = res.getInt ( res.getColumnIndex ( KEY_ID ) );
                Contact contact = new Contact ();
                contact.setName ( name );
                contact.setPhoneNumber ( mobile );
                contact.set_id ( id );
                contacts.add ( contact );
            } while (res.moveToNext ());
        }
        res.close ();
        db1.close ();
        return contacts;
    }

    void delete(int id) {
        SQLiteDatabase db1 = this.getWritableDatabase ();
        db1.delete ( TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf ( id )} );
        db1.close ();
    }
}