package in.aqel.blogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;

public class DatabaseHelper {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_BLOG_ID = "blog_id";
    public static final String KEY_BLOGGER_ID = "blogger_id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CONTENT_SHORT = "content_short";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_BLOG_NAME = "blog_name";
    public static final String KEY_TITLE = "title";
    private static final String KEY_READ_OR_NOT = "readOrNot";
    private static final String KEY_IS_FAV_OR_NOT = "isFav";
    private static final String KEY_LINK = "link";
    private static final String DATABASE_NAME = "Blogger";
    private static final String DATABASE_TABLE = "Post";
    private static final String DATABASE_BLOGGER_TABLE = "Bloggers";
    private static final String DATABASE_NOTIFICATIONS_TABLE = "Notifications";
    private static final String DATABASE_READ_LATER_TABLE = "ReadLater";



    private static final int DATABASE_VERSION = 2;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub _id INTEGER PRIMARY KEY
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ( " + KEY_ROWID + " INTEGER " +
                            " PRIMARY KEY , " + KEY_BLOG_ID + " TEXT NOT NULL UNIQUE , "
                            + KEY_BLOGGER_ID + " INTEGER NOT NULL , " + KEY_CONTENT +
                            " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, " + KEY_TITLE +
                            " TEXT NOT NULL, " +  KEY_READ_OR_NOT +
                            " INTEGER NOT NULL, "+  KEY_IS_FAV_OR_NOT +
                            " INTEGER NOT NULL, "+ KEY_CONTENT_SHORT +
                            " TEXT NOT NULL, " + KEY_LINK +
                            " TEXT NOT NULL " +" );"
            );


            db.execSQL("CREATE TABLE " + DATABASE_NOTIFICATIONS_TABLE + " ( " + KEY_ROWID + " INTEGER NOT NULL UNIQUE" +
                            " , " + KEY_TITLE + " TEXT NOT NULL , "
                            + KEY_BLOGGER_ID + " INTEGER NOT NULL , " + KEY_CATEGORY +
                            " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, " + KEY_READ_OR_NOT +
                            " INTEGER NOT NULL ,"+  "blogger_name" +
                            " TEXT NOT NULL ," + KEY_BLOG_NAME +
                            " TEXT NOT NULL "+" );"
            );

            db.execSQL("CREATE TABLE " + DATABASE_BLOGGER_TABLE + " ( " + KEY_ROWID + " INTEGER " +
                            " NOT NULL , " + "blogger_name_en" + " TEXT NOT NULL , "
                            + "blogger_name_mal" + " TEXT NOT NULL, "
                            + "blog_name_en" + " TEXT NOT NULL, "
                            + "blog_name_mal" + " TEXT NOT NULL, "
                            + "description" + " TEXT NOT NULL, "
                            + "phone_number" + " TEXT NOT NULL, "
                            + "email" + " TEXT NOT NULL, "
                            + "google_plus" + " TEXT NOT NULL, "
                            + "facebook" + " TEXT NOT NULL, "
                            + "weight" + " INTEGER NOT NULL, "
                            + "loadedOrNot" + " INTEGER NOT NULL, "
                            + "blog_url" + " TEXT NOT NULL UNIQUE, isFav INTEGER NOT NULL  "
                            +"  );"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            //db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_BLOGGER_TABLE  );
            /*db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_TABLE  );
            onCreate(db);*/
            // Version 2 - added a column isFav - dropped table readlater - Notificationtable changed
            if (oldVersion ==  1) {
                db.execSQL("ALTER TABLE "+ DATABASE_BLOGGER_TABLE + " ADD COLUMN " + "isFav" + " INTEGER DEFAULT 0");
                //db.execSQL("ALTER TABLE "+ DATABASE_TABLE + " ADD COLUMN " + KEY_CATEGORY + " INTEGER DEFAULT 0");
                db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_READ_LATER_TABLE  );
                db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_NOTIFICATIONS_TABLE  );
                db.execSQL("CREATE TABLE " + DATABASE_NOTIFICATIONS_TABLE + " ( " + KEY_ROWID + " INTEGER NOT NULL UNIQUE" +
                                "  , " + KEY_TITLE + " TEXT NOT NULL  , "
                                + KEY_BLOGGER_ID + " INTEGER NOT NULL , " + KEY_CATEGORY +
                                " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, " + KEY_READ_OR_NOT +
                                " INTEGER NOT NULL ,"+  "blogger_name" +
                                " TEXT NOT NULL ," + KEY_BLOG_NAME +
                                " TEXT NOT NULL "+" );"
                );
            }
         /*   if (oldVersion ==  3) {
                db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_READ_LATER_TABLE  );
                db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_NOTIFICATIONS_TABLE  );
                db.execSQL("CREATE TABLE " + DATABASE_NOTIFICATIONS_TABLE + " ( " + KEY_ROWID + " INTEGER NOT NULL UNIQUE" +
                                " , " + KEY_TITLE + " TEXT NOT NULL UNIQUE , "
                                + KEY_BLOGGER_ID + " INTEGER NOT NULL , " + KEY_CATEGORY +
                                " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, " + KEY_READ_OR_NOT +
                                " INTEGER NOT NULL ,"+  "blogger_name" +
                                " TEXT NOT NULL ," + KEY_BLOG_NAME +
                                " TEXT NOT NULL "+" );"
                );
            }
            */
        }

    }

    public DatabaseHelper(Context c){
        ourContext = c;
    }

    public DatabaseHelper open(){
        ourHelper = new DbHelper (ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long addBlogger(int id, String blogger_name_en, String blogger_name_mal, String blog_name_en ,
                           String blog_name_mal, String description, String phone_number, String email,
                           String google_plus, String facebook, int weight, int loadedOrNot, String blog_url
    ){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ROWID, id);
        cv.put("blogger_name_en", blogger_name_en);
        cv.put("blogger_name_mal", blogger_name_mal);
        cv.put("blog_name_en",blog_name_en );
        cv.put("blog_name_mal", blog_name_mal);
        cv.put("description", description);
        cv.put("phone_number", phone_number);
        cv.put("email", email );
        cv.put("google_plus", google_plus );
        cv.put("facebook", facebook );
        cv.put("weight", weight);
        cv.put("loadedOrNot", loadedOrNot);
        cv.put("blog_url", blog_url);
        cv.put("isFav", 0);
        return ourDatabase.insert(DATABASE_BLOGGER_TABLE, null, cv);

    }

    public long addNewBlogger(int id, String blogger_name_en, String blog_name_en, String blog_url
    ){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ROWID, id);
        cv.put("blogger_name_en", blogger_name_en);
        cv.put("blogger_name_mal", blogger_name_en);
        cv.put("blog_name_en",blog_name_en );
        cv.put("blog_name_mal", blog_name_en);
        cv.put("description", "null");
        cv.put("phone_number", "null");
        cv.put("email", "null" );
        cv.put("google_plus", "null" );
        cv.put("facebook", "null" );
        cv.put("weight", "null");
        cv.put("loadedOrNot", 1);
        cv.put("blog_url", blog_url);
        cv.put("isFav", 5);
        return ourDatabase.insert(DATABASE_BLOGGER_TABLE, null, cv);

    }

    public Void deleteMyBlogger(int id){
        ourDatabase.delete(DATABASE_BLOGGER_TABLE, KEY_ROWID + " = " + id, null);
        return null;
    }


    public Void addPost(String blogId, int bloggerId, String content, String date , String title, String link, int categ){
        String str = content;
        str = str.replaceAll("<a href(.*?)\\>","");
        str = str.replaceAll("<img(.*?)\\>","");

        str = Html.fromHtml(str).toString();
        if (str.length() > 400) {
            str =  str.substring(0, Math.min(str.length(), 400)) + "...";

        }

        ContentValues cv = new ContentValues();
        cv.put(KEY_BLOG_ID, blogId);
        cv.put(KEY_BLOGGER_ID, bloggerId);
        cv.put(KEY_CONTENT, content);
        cv.put(KEY_CONTENT_SHORT, str);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TITLE, title);
        cv.put(KEY_READ_OR_NOT,0);
        cv.put(KEY_IS_FAV_OR_NOT,0);
        cv.put(KEY_LINK,link);
       ourDatabase.insert(DATABASE_TABLE, null, cv);
return null;
    }

    public long addNotifications(int id, int bloggerId,  String date , String title, String blogger_name, String categ, String blogName){

        ContentValues cv = new ContentValues();
        cv.put(KEY_ROWID, id);
        cv.put(KEY_BLOGGER_ID, bloggerId);
        cv.put(KEY_CATEGORY, categ);
        cv.put(KEY_DATE, date);
        cv.put(KEY_BLOG_NAME, blogName);
        cv.put(KEY_TITLE, title);
        cv.put("blogger_name",blogger_name);
        cv.put(KEY_READ_OR_NOT, 0);

        return ourDatabase.insert(DATABASE_NOTIFICATIONS_TABLE, null, cv);

    }

    public long addToReadLater(String blogId, int bloggerId, String content, String date , String title, String link){
        String str = content;
        str = str.replaceAll("<a href(.*?)\\>","");
        str = str.replaceAll("<img(.*?)\\>","");

        str = Html.fromHtml(str).toString();
        if (str.length() > 400) {
            str =  str.substring(0, Math.min(str.length(), 400)) + "...";

        }
        ContentValues cv = new ContentValues();
        cv.put(KEY_BLOG_ID, blogId);
        cv.put(KEY_BLOGGER_ID, bloggerId);
        cv.put(KEY_CONTENT, content);
        cv.put(KEY_CONTENT_SHORT, str);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TITLE, title);
        cv.put(KEY_READ_OR_NOT,0);
        cv.put(KEY_IS_FAV_OR_NOT,0);
        cv.put(KEY_LINK,link);
        return ourDatabase.insert(DATABASE_READ_LATER_TABLE, null, cv);

    }

    public Cursor getAllData (int bloggerId) {

        String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_BLOGGER_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, KEY_IS_FAV_OR_NOT, KEY_CONTENT_SHORT, KEY_LINK};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_BLOGGER_ID + " = " + bloggerId, null, null, null,  KEY_DATE + " DESC");

        return c;
    }


    public Cursor getAllReadLater () {

        String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_BLOGGER_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, KEY_IS_FAV_OR_NOT, KEY_CONTENT_SHORT, KEY_LINK};
        Cursor c = ourDatabase.query(DATABASE_READ_LATER_TABLE, columns, null, null, null, null,  null);

        return c;
    }

    public Cursor getAllFavourites () {

        String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_BLOGGER_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, KEY_IS_FAV_OR_NOT,KEY_CONTENT_SHORT, KEY_LINK};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_IS_FAV_OR_NOT + " = " + 1, null, null, null,  KEY_DATE + " DESC");

        return c;
    }

    public Cursor getAllNotifications () {
        String[] columns = new String[] {KEY_ROWID, KEY_BLOGGER_ID, KEY_BLOG_NAME, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, "blogger_name",KEY_CATEGORY};
        Cursor c = ourDatabase.query(DATABASE_NOTIFICATIONS_TABLE, columns, null, null, null, null,  KEY_ROWID + " DESC");
        return c;
    }

    public Cursor getAllBloggers () {

        String[] columns = new String[] {KEY_ROWID, "blogger_name_en", "blogger_name_mal", "blog_name_en" ,
                "blog_name_mal", "description", "phone_number", "email",
                "google_plus", "facebook" , "weight", "loadedOrNot", "blog_url", "isFav"};
        Cursor c = ourDatabase.query(DATABASE_BLOGGER_TABLE, columns, "isFav = 0 OR isFav = 1 " , null, null, null,  "weight " +" ASC");
        return c;
    }

    public Cursor getAllMyBloggers () {

        String[] columns = new String[] {KEY_ROWID, "blogger_name_en", "blogger_name_mal", "blog_name_en" ,
                "blog_name_mal", "description", "phone_number", "email",
                "google_plus", "facebook" , "weight", "loadedOrNot", "blog_url", "isFav"};
        Cursor c = ourDatabase.query(DATABASE_BLOGGER_TABLE, columns, "isFav = 1 OR isFav = 5 " , null, null, null,  "weight " +" ASC");
        return c;
    }

    public Void updateAsLoaded(int id){
        // New value for one column
        ContentValues contentValues = new ContentValues();
        contentValues.put("loadedOrNot", 1);
        // Which row to update, based on the ID
        String selection = KEY_ROWID + " LIKE ?" ;
        String[] selectionArgs = { String.valueOf(id) };
        int count = ourDatabase.update(
                DATABASE_BLOGGER_TABLE,
                contentValues,
                selection,
                selectionArgs);
        Log.d("int count", Integer.toString(count));
        return null;
    }

    public int checkIsFav(String blogId){
        String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_BLOGGER_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, KEY_IS_FAV_OR_NOT};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_BLOG_ID + " = '" + blogId+"'", null, null, null,  KEY_DATE + " DESC");
        int isFav = 0;
        while (c.moveToNext()) {
            String a = Integer.toString(c.getInt(c.getColumnIndex(KEY_IS_FAV_OR_NOT)));
            isFav = c.getInt(c.getColumnIndex(KEY_IS_FAV_OR_NOT));
            Log.d("isFav On database", a + " " + blogId);
        }
        return isFav;
    }

    public int getId(String postId){
        int idOfPost = 0;
        String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_BLOGGER_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, KEY_IS_FAV_OR_NOT};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_BLOG_ID + " = '" +postId+"'", null, null, null,  null);
        while (c.moveToNext()) {
            String a = Integer.toString(c.getInt(c.getColumnIndex(KEY_IS_FAV_OR_NOT)));
            idOfPost = c.getInt(c.getColumnIndex(KEY_ROWID));
            Log.d("isFav On database", a);
        }
        Log.d("id", Integer.toString(idOfPost));
        return idOfPost;

    }

    public String[] getBlogger(int blogId){
        String[] columns = new String[] {KEY_ROWID, "blogger_name_en", "blogger_name_mal", "blog_name_en" ,
                "blog_name_mal", "blog_url", "loadedOrNot", "isFav" };
        int id = 0,isFav=0,loadedOrNot=0;
        String blogger_name_en = null;
        String blogger_name_mal = null;
        String blog_name_en = null;
        String blog_name_mal = null;
        String blog_url = null;
        Cursor c = ourDatabase.query(DATABASE_BLOGGER_TABLE, columns, KEY_ROWID + " = " + blogId, null, null, null,  null);

        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(KEY_ROWID));
            loadedOrNot = c.getInt(c.getColumnIndex("loadedOrNot"));
            isFav = c.getInt(c.getColumnIndex("isFav"));
            blogger_name_en = c.getString(c.getColumnIndex("blogger_name_en"));
            blogger_name_mal = c.getString(c.getColumnIndex("blogger_name_mal"));
            blog_name_en = c.getString(c.getColumnIndex("blog_name_en"));
            blog_name_mal = c.getString(c.getColumnIndex("blog_name_mal"));
            blog_url = c.getString(c.getColumnIndex("blog_url"));
            Log.d("LogAq In database id", Integer.toString(id));
        }
        String[] toBeReturned = {blogger_name_en, blogger_name_mal, blog_name_en, blog_name_mal, blog_url
            , Integer.toString(id), Integer.toString(isFav), Integer.toString(loadedOrNot)};
        return toBeReturned;
    }


    public String[] getThePost(String title){
        String[] columns = new String[] {KEY_ROWID, KEY_CONTENT, KEY_BLOG_ID, KEY_DATE , KEY_BLOGGER_ID,
                KEY_IS_FAV_OR_NOT, KEY_LINK, KEY_TITLE};
        String content = null;
        int id = 0;
        int bloggerId=0 , isFav=0;
        String blogId = null;
        String date = null;
        String link = null;
        String strTitle = null;
        Log.d("LogAq title", title);
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_TITLE + " = '" + title + " '", null, null, null,  null);

        while (c.moveToNext()) {
            content = c.getString(c.getColumnIndex(c.getColumnName(1)));
            Log.d("LogAq content", content);
            id = c.getInt(c.getColumnIndex(c.getColumnName(0)));
            bloggerId = c.getInt(c.getColumnIndex(c.getColumnName(4)));
            isFav = c.getInt(c.getColumnIndex(c.getColumnName(5)));
            blogId = c.getString(c.getColumnIndex(c.getColumnName(2)));
            date = c.getString(c.getColumnIndex(c.getColumnName(3)));
            link = c.getString(c.getColumnIndex(c.getColumnName(6)));
            title = c.getString(c.getColumnIndex(c.getColumnName(7)));
        }
        String[] toBeReturned = {content, Integer.toString(id), blogId, date, Integer.toString(bloggerId)
            , Integer.toString(isFav), link, strTitle};
        return toBeReturned;
    }

   public Void addToFavourites(String blogId, int value){
        // New value for one column
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IS_FAV_OR_NOT, value);
        int count =ourDatabase.update(DATABASE_TABLE, contentValues, KEY_BLOG_ID+"='"+ blogId+"'", null);
        Log.d("isFav", Integer.toString(count));
        checkIsFav(blogId);
        Log.d("isFav","reached here" + blogId);

        return null;
    }

    public Void markNotifAsRead(int id){
        // New value for one column
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_READ_OR_NOT, 1);
        int count =ourDatabase.update(DATABASE_NOTIFICATIONS_TABLE, contentValues, KEY_ROWID+" = "+ id, null);
        Log.d("isFav", Integer.toString(count));
        return null;
    }

    public Void addToMyBloggers(int blogId, int value){
        // New value for one column
        ContentValues contentValues = new ContentValues();
        contentValues.put("isFav", value);
        int count =ourDatabase.update(DATABASE_BLOGGER_TABLE, contentValues, KEY_ROWID + " = " + blogId , null);
        Log.d("isFav", Integer.toString(count));
        return null;
    }

    public Void updatePostAsRead(String blogId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_READ_OR_NOT, 1);
        int count =ourDatabase.update(DATABASE_TABLE, contentValues, KEY_BLOG_ID+"='"+ blogId+"'", null);
        Log.d("isFav", Integer.toString(count));
        return null;
    }






}
