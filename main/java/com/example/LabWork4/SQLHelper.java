import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PhoneRepairDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "repairs";
    private static final String COL_ID = "_id";
    private static final String COL_CLIENT = "client";
    private static final String COL_PHONE = "phone";
    private static final String COL_ISSUE = "issue";
    private static final String COL_PRICE = "price";
    private static final String COL_STATUS = "status";

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLIENT + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_ISSUE + " TEXT, " +
                COL_PRICE + " INTEGER, " +
                COL_STATUS + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addRepair(String client, String phone, String issue, int price, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CLIENT, client);
        cv.put(COL_PHONE, phone);
        cv.put(COL_ISSUE, issue);
        cv.put(COL_PRICE, price);
        cv.put(COL_STATUS, status ? 1 : 0);
        return db.insert(TABLE_NAME, null, cv);
    }

    public Cursor getAllRepairs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public int updateRepairStatus(int id, boolean newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, newStatus ? 1 : 0);
        return db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteRepair(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}