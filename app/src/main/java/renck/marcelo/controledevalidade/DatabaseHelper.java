package renck.marcelo.controledevalidade;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mr Robot on 09/04/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="controle_validade.db";
    public static final String TABLE_NAME="produtos";

    public static final String COL_1="_id";
    public static final String COL_2="cod_barras";
    public static final String COL_3="descricao";
    public static final String COL_4="quantidade";
    public static final String COL_5="preco";
    public static final String COL_6="local";
    public static final String COL_7="validade";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, cod_barras TEXT NOT NULL, descricao TEXT NOT NULL, quantidade INTEGER NOT NULL" +
                ", preco TEXT NOT NULL, local INTEGER, validade TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //retorna os produtos
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}

