package com.govibs.viva.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.govibs.viva.storage.bean.NotificationBean;

/**
 * Viva DB helper
 * Created by Vibhor on 3/2/16.
 */
public class VivaDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VivaDB";
    private static final String COLUMN_TYPE_INTEGER = "INTEGER";
    private static final String COLUMN_TYPE_TEXT = "TEXT";

    /**
     * Notification table
     */
    private final class NotificationTable implements BaseColumns {
        public static final String TABLE_NAME = "NotificationTable";
        public static final String COLUMN_NAME_ID = "NotificationID";
        public static final String COLUMN_NAME_MESSAGE = "NotificationMessage";
        public static final String COLUMN_NAME_PACKAGE = "NotificationPackage";
        public static final String COLUMN_NAME_APP = "NotificationApp";
        public static final String COLUMN_NOTIFICATION_COUNT = "NotificationCount";
    }

    /**
     * Create notification table
     */
    private static final String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NotificationTable.TABLE_NAME + "(" + NotificationTable.COLUMN_NAME_ID + " "
            + COLUMN_TYPE_INTEGER + " PRIMARY KEY, " + NotificationTable.COLUMN_NAME_MESSAGE + " "
            + COLUMN_TYPE_TEXT + ", " + NotificationTable.COLUMN_NAME_PACKAGE + " " + COLUMN_TYPE_TEXT
            + ", " + NotificationTable.COLUMN_NAME_APP + " " + COLUMN_TYPE_TEXT + ", "
            + NotificationTable.COLUMN_NOTIFICATION_COUNT + " " + COLUMN_TYPE_INTEGER + ");";

    private static final String SELECT_NOTIFICATION_COUNT = "SELECT "
            + NotificationTable.COLUMN_NOTIFICATION_COUNT + " FROM " + NotificationTable.TABLE_NAME
            + " WHERE " + NotificationTable.COLUMN_NAME_ID + "='?'";

    /**
     * Delete notification table
     */
    private static final String DROP_NOTIFICATION_TABLE = "DROP TABLE IF EXISTS " + NotificationTable.TABLE_NAME;



    /**
     * DB Helper singleton instance class
     */
    private static VivaDBHelper mVivaDBHelper;

    /**
     * Default Constructor
     * @param context - the calling application context.
     */
    private VivaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Return singleton instance of the DB helper class
     * @param context - the calling application context.
     * @return DB Helper class
     */
    public static VivaDBHelper getInstance(Context context) {
        if (mVivaDBHelper == null) {
            mVivaDBHelper = new VivaDBHelper(context);
        }
        return mVivaDBHelper;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNotification(NotificationBean notificationBean) {
        ContentValues contentValues = getContentValuesFromNotificationBean(notificationBean);
        getWritableDatabase().insert(NotificationTable.TABLE_NAME, null, contentValues);
    }

    /**
     * Get content values for inserting notification details in the table.
     * @param notificationBean - the notification bean to be converted.
     * @return ContentValues
     */
    private ContentValues getContentValuesFromNotificationBean(NotificationBean notificationBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationTable.COLUMN_NAME_ID, notificationBean.getNotificationID());
        contentValues.put(NotificationTable.COLUMN_NAME_MESSAGE, notificationBean.getNotificationText());
        contentValues.put(NotificationTable.COLUMN_NAME_APP, notificationBean.getNotificationApp());
        contentValues.put(NotificationTable.COLUMN_NAME_PACKAGE, notificationBean.getNotificationPackage());
        int count = getNotificationCount(notificationBean) + 1;
        contentValues.put(NotificationTable.COLUMN_NOTIFICATION_COUNT, count);
        return contentValues;
    }

    private int getNotificationCount(NotificationBean notificationBean) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(SELECT_NOTIFICATION_COUNT, new String[]{ notificationBean.getNotificationID() + "" });
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_NOTIFICATION_COUNT));
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

}
