package com.example.balbir.myinvoices;

/**
 * Created by Parsania Hardik on 15/01/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 11/01/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_title = "title";
    private static final String TABLE_date= "date";
    private static final String TABLE_invoice = "invoice";

    private static final String TABLE_shop= "shop";
    private static final String TABLE_loc = "loc";
    private static final String TABLE_comment = "comment";

    private static final String TABLE_Image = "table_image";
    private static final String KEY_ID = "id";
    private static final String KEY_title = "title";
    private static final String KEY_date= "date";
    private static final String KEY_invoice= "invoice";

    private static final String KEY_shop= "shop";
    private static final String KEY_loc= "loc";
    private static final String KEY_comment= "comment";

    private static final String KEY_image = "image_name";

    /*CREATE TABLE students ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT......);*/

    private static final String CREATE_title = "CREATE TABLE "
            + TABLE_title+ "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_title + " TEXT );";

    private static final String CREATE_date= "CREATE TABLE "
            + TABLE_date + "(" + KEY_ID + " INTEGER,"+ KEY_date + " TEXT );";

    private static final String CREATE_invoice = "CREATE TABLE "
            + TABLE_invoice + "(" + KEY_ID + " INTEGER,"+ KEY_invoice+ " TEXT );";

    private static final String CREATE_shop = "CREATE TABLE "
            + TABLE_shop + "(" + KEY_ID + " INTEGER,"+ KEY_shop+ " TEXT );";
    private static final String CREATE_loc = "CREATE TABLE "
            + TABLE_loc + "(" + KEY_ID + " INTEGER,"+ KEY_loc+ " TEXT );";
    private static final String CREATE_comment= "CREATE TABLE "
            + TABLE_comment + "(" + KEY_ID + " INTEGER,"+ KEY_comment+ " TEXT );";

    private static final String CREATE_image= "CREATE TABLE "
            + TABLE_Image + "(" + KEY_ID + " INTEGER,"+ KEY_image+ " BLOB );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d("table", CREATE_title);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_title);
        db.execSQL(CREATE_date);
        db.execSQL(CREATE_invoice);
        db.execSQL(CREATE_shop);
        db.execSQL(CREATE_loc);
        db.execSQL(CREATE_comment);

        db.execSQL(CREATE_image);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_title + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_date + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_invoice + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_shop + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_loc + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_comment + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_Image + "'");

        onCreate(db);
    }

    public void addUser(String title, String date, String invoice,String shop, String loc, String comment,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        //adding user name in users table
        ContentValues values = new ContentValues();
        values.put(KEY_title, title);
       // db.insert(TABLE_USER, null, values);
        long id = db.insertWithOnConflict(TABLE_title, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //adding user hobby in users_hobby table
        ContentValues valuesdate = new ContentValues();
        valuesdate.put(KEY_ID, id);
        valuesdate.put(KEY_date, date);
        db.insert(TABLE_date, null, valuesdate );

        ContentValues valuesinvoice = new ContentValues();
        valuesinvoice.put(KEY_ID, id);
        valuesinvoice.put(KEY_invoice,invoice);
        db.insert(TABLE_invoice, null, valuesinvoice);

        ContentValues valuesshop = new ContentValues();
        valuesshop .put(KEY_ID, id);
        valuesshop .put(KEY_shop, shop);
        db.insert(TABLE_shop, null, valuesshop );

        ContentValues valuesloc = new ContentValues();
        valuesloc.put(KEY_ID, id);
        valuesloc.put(KEY_loc, loc);
        db.insert(TABLE_loc, null, valuesloc);
        //adding user city in users_city table
        ContentValues valuescomment = new ContentValues();
        valuescomment.put(KEY_ID, id);
        valuescomment.put(KEY_comment, comment);
        db.insert(TABLE_comment, null, valuescomment);


        ContentValues valuesimage = new ContentValues();
        valuesimage.put(KEY_ID, id);
        valuesimage.put(KEY_image, image);
        db.insert(TABLE_Image, null, valuesimage );

    }

    public ArrayList<UserModel> getAllUsers() {
        ArrayList<UserModel> userModelArrayList = new ArrayList<UserModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_title;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                userModel.setTitle(c.getString(c.getColumnIndex(KEY_title)));

                            //getting user hobby where id = id from user_hobby table
                            String selectdateQuery = "SELECT  * FROM " + TABLE_date +" WHERE "+KEY_ID+" = "+ userModel.getId();
                            Log.d("oppp",selectdateQuery);
                            //SQLiteDatabase dbhobby = this.getReadableDatabase();
                            Cursor cdate= db.rawQuery(selectdateQuery, null);

                                        if (cdate.moveToFirst()) {
                                            do {
                                                userModel.setdate(cdate.getString(cdate.getColumnIndex(KEY_date)));
                                            } while (cdate.moveToNext());
                                        }

                            //getting user city where id = id from user_city table
                            String selectinvoiceQuery = "SELECT  * FROM " + TABLE_invoice+" WHERE "+KEY_ID+" = "+ userModel.getId();;
                            //SQLiteDatabase dbCity = this.getReadableDatabase();
                            Cursor cinvoice = db.rawQuery(selectinvoiceQuery, null);

                            if (cinvoice.moveToFirst()) {
                                do {
                                    userModel.setinvoice(cinvoice.getString(cinvoice.getColumnIndex(KEY_invoice)));
                                } while (cinvoice.moveToNext());
                            }

                String selectshopQuery = "SELECT  * FROM " + TABLE_shop+" WHERE "+KEY_ID+" = "+ userModel.getId();;
                //SQLiteDatabase dbCity = this.getReadableDatabase();
                Cursor cshop= db.rawQuery( selectshopQuery , null);

                if (cshop.moveToFirst()) {
                    do {
                        userModel.setshop(cshop.getString(cshop.getColumnIndex(KEY_shop)));
                    } while (cshop.moveToNext());
                }

                String selectimageQuery = "SELECT  * FROM " + TABLE_Image+" WHERE "+KEY_ID+" = "+ userModel.getId();;
                //SQLiteDatabase dbCity = this.getReadableDatabase();
                Cursor cimage= db.rawQuery( selectimageQuery , null);

                if (cimage.moveToFirst()) {
                    do {
                        userModel.setImage(cimage.getBlob(cimage.getColumnIndex(KEY_image)));
                    } while (cimage.moveToNext());
                }

                String selectlocQuery = "SELECT  * FROM " + TABLE_loc+" WHERE "+KEY_ID+" = "+ userModel.getId();;
                //SQLiteDatabase dbCity = this.getReadableDatabase();
                Cursor cloc = db.rawQuery(selectlocQuery, null);

                if (cloc .moveToFirst()) {
                    do {
                        userModel.setloc(cloc .getString(cloc .getColumnIndex(KEY_loc)));
                    } while (cloc .moveToNext());
                }
                String selectcommentQuery = "SELECT  * FROM " + TABLE_comment+" WHERE "+KEY_ID+" = "+ userModel.getId();;
                //SQLiteDatabase dbCity = this.getReadableDatabase();
                Cursor comment = db.rawQuery(selectcommentQuery, null);

                if (comment .moveToFirst()) {
                    do {
                        userModel.setcomment(comment .getString(comment .getColumnIndex(KEY_comment)));
                    } while (comment .moveToNext());
                }
                    userModelArrayList.add(userModel);
                } while (c.moveToNext());
         }
        return userModelArrayList;
    }

    public void updateUser(int id, String title, String date, String invoice,String shop, String loc, String comment,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        // updating name in users table
        ContentValues values1 = new ContentValues();
        values1.put(KEY_title, title);
        db.update(TABLE_title, values1, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        ContentValues values2 = new ContentValues();
        values2.put(KEY_date, date);
        db.update(TABLE_date, values2, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        ContentValues values3= new ContentValues();
        values3.put(KEY_invoice, invoice);
        db.update(TABLE_invoice, values3, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        // updating hobby in users_hobby table
        ContentValues values4 = new ContentValues();
        values4.put(KEY_shop, shop);
        db.update(TABLE_shop, values4, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        // updating city in users_city table
        ContentValues values5 = new ContentValues();
        values5.put(KEY_loc, loc);
        db.update(TABLE_loc, values5, KEY_ID + " = ?", new String[]{String.valueOf(id)});


        ContentValues values6= new ContentValues();
        values6.put(KEY_comment, comment);
        db.update(TABLE_comment, values6, KEY_ID + " = ?", new String[]{String.valueOf(id)});



        ContentValues values7= new ContentValues();
        values7.put(KEY_image, image);
        db.update(TABLE_Image, values7, KEY_ID + " = ?", new String[]{String.valueOf(id)});



    }

    public void deleteUSer(int id) {

        // delete row in students table based on id
        SQLiteDatabase db = this.getWritableDatabase();

        //deleting from users table
        db.delete(TABLE_title, KEY_ID + " = ?",new String[]{String.valueOf(id)});

        //deleting from users_hobby table
        db.delete(TABLE_date, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        //deleting from users_city table
        db.delete(TABLE_invoice, KEY_ID + " = ?",new String[]{String.valueOf(id)});

        db.delete(TABLE_shop, KEY_ID + " = ?",new String[]{String.valueOf(id)});
        db.delete(TABLE_loc, KEY_ID + " = ?",new String[]{String.valueOf(id)});
        db.delete(TABLE_comment, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }

}

