package com.maomovie.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.maomovie.entity.*;

/**
 * Created by YanP on 2016/8/15.
 */
public class SqliteHelper extends SQLiteOpenHelper {


    public static SqliteHelper sqliteHelper = null;

    public static SqliteHelper getInstance(Context context) {
        if (sqliteHelper == null) {
            sqliteHelper = new SqliteHelper(context);
        }
        return sqliteHelper;
    }

    private static final int VERSION = 1;//数据库版本号

    private static final String DATABASENAME = "MAO_DB";//数据库名称

    public SqliteHelper(Context context) {
        this(context, VERSION);
    }

    public SqliteHelper(Context context, int version) {
        this(context, DATABASENAME, null, version);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //在创建数据库时调用此方法
        //一般是创建数据库需要的表
        db.execSQL(CinemaEntity.getCreateTableSql());
        db.execSQL(TodayMovieEntity.getCreateTableSql());
        db.execSQL(MovieEntity.getCreateTableSql());
        db.execSQL(MoviePlayingInfoEntity.getCreateTableSql());
        db.execSQL(SupportCityEntity.getCreateTableSql());
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当版本号不一致时调用
        if (oldVersion != newVersion) { //说明数据库版本有更新，更新字段，更新表
            for (int j = oldVersion; j <= newVersion; j++) {
                try {
                    switch (j) {
                        case 2:
                            //创建临时表
                            db.execSQL(TEMP_SQL_CREATE_TABLE_SUBSCRIBE);
                            //旧表增加新字段
                            db.execSQL(ADD_NEW_COLUMN_TO_TABLE_CINEMA);
                            //执行OnCreate方法，这个方法中放的是表的初始化操作工作，比如创建新表之类的
                            onCreate(db);
                            //将临时表中的数据放入表A
                            Cursor cursor = db.rawQuery(INSERT_SUBSCRIBE, null);
                            if (cursor.moveToFirst()) {
                                do {
                                    db.execSQL(cursor.getString(cursor.getColumnIndex("insertSQL")));
                                } while (cursor.moveToNext());

                            }
                            cursor.close();
                            //将临时表删除掉
                            db.execSQL(DROP_TEMP_SUBSCRIBE);
                            break;
                        default:
                            break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        /**说明：为什么要在方法里写for循环，主要是考虑到夸版本升级，比如有的用户一直不升级版本，数据库版本号一直是1，
         * 而客户端最新版本其实对应的数据库版本已经是4了，那么我中途可能对数据库做了很多修改，通过这个for循环，可以迭代升级，不会发生错误。
         */

    }
    //1.对数据库需要做的改动
        //1.1首先我们需要把原来的数据库表重命名一下
    private static final String TEMP_SQL_CREATE_TABLE_SUBSCRIBE = "alter table "
            + "TodayMovie" +
            " rename to temp_A";
         //1.2在旧表中增加新字段
    private static final String ADD_NEW_COLUMN_TO_TABLE_CINEMA = "ALTER TABLE "
                 + "Cinema" +
                 " ADD COLUMN distance varchar(60)";
    //2.然后把备份表temp_A中的数据copy到新创建的数据库表A中，这个表A没发生结构上的变化
    private static final String INSERT_SUBSCRIBE = "select 'insert into "
            + "TodayMovie" +
            "(id,cnms,dur,pn,preSale)" +
            " values ('''||id||''','''||cnms||''','''||dur||''',''pn'','''||preSale||''')'  as insertSQL from temp_A";
    //3.删除备份表temp_A
    private static final String DELETE_TEMP_SUBSCRIBE = "delete from temp_A ";
    private static final String DROP_TEMP_SUBSCRIBE = "drop table if exists temp_A";
    //4.然后把数据库版本号改为比之前高的版本号，在OnUpgrade方法中执行上述语句就行
}
