package in.aqel.blogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_BLOG_ID = "blog_id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_TITLE = "title";
	

	
	private static final String DATABASE_NAME = "Blogger";
	private static final String DATABASE_TABLE = "Post";
	private static final int DATABASE_VERSION = 1;
	
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
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ( " + KEY_ROWID + " INTEGER PRIMARAY" +
					" KEY AUTO INCREMENT, " + KEY_BLOG_ID + " TEXT NOT NULL UNIQUE , " + KEY_CONTENT +
					" TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, " + KEY_TITLE +
					" TEXT NOT NULL );"
					);

			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_TABLE  );
			onCreate(db);
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
	
	public long addPost(String blogId, String content, String date , String title){
		ContentValues cv = new ContentValues();
		cv.put(KEY_BLOG_ID, blogId);
		cv.put(KEY_CONTENT, content);
		cv.put(KEY_DATE, date);
		cv.put(KEY_TITLE, title);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
		
	}
	
	/*
	public void updateEntry(String entry, String Date, String Category , int order){
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, entry);
		cv.put(KEY_AMOUNT, order);
		cv.put(KEY_CATEGORY, Category);
		cv.put(KEY_DATE, Date);
	 ourDatabase.update(DATABASE_TABLE_MONTH, cv, KEY_AMOUNT + " = " + order, null);
	}
	
	public String getData(int i) {
		// TODO Auto-generated method stub
		String[] columns = new String[] {KEY_ROWID, KEY_DATE, KEY_AMOUNT, KEY_CATEGORY, KEY_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		int iROw = c.getColumnIndex(KEY_ROWID);
		int iDate = c.getColumnIndex(KEY_DATE);
		int iAmount = c.getColumnIndex(KEY_AMOUNT);
		int iCategory = c.getColumnIndex(KEY_CATEGORY);
		int iName = c.getColumnIndex(KEY_NAME);
		for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()){
			result = result + i + ". " + c.getString(iDate) + " "  + c.getString(iAmount) + " "
					 + c.getString(iCategory) + " "  + c.getString(iName) + "\n";
			i++;
		}
		return result;
	}
	*/
	public String getMonthData(int i) {
		// TODO Auto-generated method stub
		String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		int iROw = c.getColumnIndex(KEY_ROWID);
		int iDate = c.getColumnIndex(KEY_DATE);
		int iAmount = c.getColumnIndex(KEY_TITLE);
		int iCategory = c.getColumnIndex(KEY_BLOG_ID);
		int iName = c.getColumnIndex(KEY_CONTENT);
		for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()){
			result = result +  c.getString(iDate) + "\n \n"  + c.getString(iCategory) + "\n"  + c.getString(iName) + "\n \n";
			i++;
		}
		return result;
	}
	
	
    public Cursor getAllData () {

    	String[] columns = new String[] {KEY_ROWID, KEY_BLOG_ID, KEY_CONTENT, KEY_DATE, KEY_TITLE};
    	Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null,  KEY_ROWID +" ASC");
    	return c;
    }




	
	
}
