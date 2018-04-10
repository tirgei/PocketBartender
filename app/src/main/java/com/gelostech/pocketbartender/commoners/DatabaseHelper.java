package com.gelostech.pocketbartender.commoners;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.gelostech.pocketbartender.models.HomeModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tirgei on 3/15/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "pocket_bartender";
    public static final String FAVES_TABLE = "fave_cocktails", HISTORY_TABLE = "search_history";
    private static final String NAME = "cocktail_name";
    private static final String ID = "cocktail_id";
    private static final String URL = "cocktail_url";
    private static final String IMAGE_THUMBNAIL = "image";

    private static final String CREATE_FAVES_TABLE = "CREATE TABLE " + FAVES_TABLE + "("
            + ID + " TEXT," + NAME + " TEXT," + URL + " TEXT,"+ IMAGE_THUMBNAIL + " BLOB);";

    private static final String CREATE_HISTORY_TABLE = "CREATE TABLE " + HISTORY_TABLE + "("
            + ID + " TEXT," + NAME + " TEXT," + URL + " TEXT);";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVES_TABLE);
        sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void insertIntoDb(int id, String name, String url, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();

        if(hasObject(String.valueOf(id), FAVES_TABLE))
            deleteRow(String.valueOf(id), FAVES_TABLE);

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(NAME, name);
            contentValues.put(URL, url);
            contentValues.put(IMAGE_THUMBNAIL, getBytes(image));
            db.insert(FAVES_TABLE, null, contentValues);
            db.close();

        } catch (SQLiteException e){
            e.printStackTrace();
        }

    }

    public void addHistory(String id, String name, String imageUrl){
        SQLiteDatabase db = this.getWritableDatabase();

        if(hasObject(id, HISTORY_TABLE))
            deleteRow(id, HISTORY_TABLE);

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(NAME, name);
            contentValues.put(URL, imageUrl);
            db.insert(HISTORY_TABLE, null, contentValues);
            db.close();

        } catch (SQLiteException e){
            e.printStackTrace();
        }

    }

    public List<HomeModel> fetchHistory(){
        List<HomeModel> models = new ArrayList<>();

        String query = "SELECT * FROM " + HISTORY_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                HomeModel model = new HomeModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setImageUrl(cursor.getString(2));
                model.setType(0);

                models.add(0, model);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return models;
    }


    public List<HomeModel> fetchFaves(){
        List<HomeModel> models = new ArrayList<>();

        String query = "SELECT * FROM " + FAVES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                HomeModel model = new HomeModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setImageUrl(cursor.getString(2));
                model.setCocktailThumb(cursor.getBlob(3));

                models.add(0, model);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return models;
    }

    public void clearDb(Boolean all){
        SQLiteDatabase db = this.getWritableDatabase();
        if(all){
            db.delete(FAVES_TABLE, null, null);
            db.delete(HISTORY_TABLE, null, null);
        } else {
            db.delete(HISTORY_TABLE, null, null);
        }
        db.close();
    }

    public boolean hasObject(String id, String table) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + table + " WHERE " + ID + " =?";

        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

        }

        cursor.close();
        return hasObject;
    }

    public void deleteRow(String id, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table + " WHERE " + ID + "='" + id + "'");
    }

    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
    }

}