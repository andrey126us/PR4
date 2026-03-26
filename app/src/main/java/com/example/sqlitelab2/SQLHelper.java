package com.example.sqlitelab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "vet.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "animals";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPECIES = "species";
    public static final String COLUMN_BREED = "breed";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_OWNER = "owner";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_SPECIES + " TEXT NOT NULL, "
            + COLUMN_BREED + " TEXT, "
            + COLUMN_AGE + " INTEGER, "
            + COLUMN_OWNER + " TEXT NOT NULL);";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление
    public long addAnimal(Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, animal.getName());
        values.put(COLUMN_SPECIES, animal.getSpecies());
        values.put(COLUMN_BREED, animal.getBreed());
        values.put(COLUMN_AGE, animal.getAge());
        values.put(COLUMN_OWNER, animal.getOwner());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Получить всех
    public ArrayList<Animal> getAllAnimals() {
        ArrayList<Animal> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String species = cursor.getString(cursor.getColumnIndex(COLUMN_SPECIES));
                String breed = cursor.getString(cursor.getColumnIndex(COLUMN_BREED));
                int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                String owner = cursor.getString(cursor.getColumnIndex(COLUMN_OWNER));
                list.add(new Animal(id, name, species, breed, age, owner));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Обновление
    public int updateAnimal(Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, animal.getName());
        values.put(COLUMN_SPECIES, animal.getSpecies());
        values.put(COLUMN_BREED, animal.getBreed());
        values.put(COLUMN_AGE, animal.getAge());
        values.put(COLUMN_OWNER, animal.getOwner());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(animal.getId())});
    }

    // Удаление
    public void deleteAnimal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}