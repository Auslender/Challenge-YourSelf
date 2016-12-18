package v.challengeyourself.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import v.challengeyourself.Challenge;

import static v.challengeyourself.storage.DBContract.Columns.*;

/**
 * Created by maria on 16.12.16.
 */
public class ChallengeStorage {
    private static final String TAG = "CHALLENGEDB";
    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public ChallengeStorage(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public void put(Challenge newch) {
        db.beginTransaction();
        Log.d(TAG, "Transaction started");
        SQLiteStatement insert = null;
        try {
            String statement = "INSERT INTO " + TABLE_NAME + " ("
                    + START + ", "
                    + DEADDATE + ", "
                    + DEADTIME + ", "
                    + CHALLENGE + ", "
                    + DETAILS + ", "
                    + DEADLINE + ", "
                    + CLOSED;
            String request = ") VALUES (?, ?, ?, ?, ?, ?, ?)";
            insert = db.compileStatement(statement + request);
            int pos = 0;
            insert.bindString(++pos, newch.start);
            insert.bindString(++pos, newch.deadDate);
            insert.bindString(++pos, newch.deadTime);
            insert.bindString(++pos, newch.challenge);
            insert.bindString(++pos, newch.details);
            insert.bindLong(++pos, newch.deadLine);
            insert.bindLong(++pos, newch.closed);

            insert.executeInsert();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        //db.close();
    }

    public void showStorage() {
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        watchTableByCursor(c);
    }

    public Cursor getCursorToSortedTable() {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DEADLINE + " ASC", null);
        return c;
    }

    public void sortByDeadLines() {
        Cursor c = getCursorToSortedTable();
        watchTableByCursor(c);
    }

    private void watchTableByCursor(Cursor c) {
        Log.d(TAG, "--- Rows in mytable: ---");
        if (c.moveToFirst()) {
            int idi = c.getColumnIndex(ID);
            int si = c.getColumnIndex(START);
            int ddi = c.getColumnIndex(DEADDATE);
            int dti = c.getColumnIndex(DEADTIME);
            int ci = c.getColumnIndex(CHALLENGE);
            int di = c.getColumnIndex(DETAILS);
            int timei = c.getColumnIndex(DEADLINE);
            int closei = c.getColumnIndex(CLOSED);

            do {
                Log.d(TAG, "id = " + c.getInt(idi) + "start = " + c.getString(si)
                        + ", deadDate=" + c.getString(ddi)
                        + ", deadTime=" + c.getString(dti)
                        + ", challenge=" + c.getString(ci)
                        + ", details=" + c.getString(di)
                        + ", deadline=" + c.getLong(timei)
                        + ", closed=" + c.getLong(closei));
            } while (c.moveToNext());
        } else {
            Log.d(TAG, "0 rows");
        }
        c.close();
    }
}
