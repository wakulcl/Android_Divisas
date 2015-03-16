package cl.ido.divisas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String TAG = DataBaseHelper.class.getSimpleName();

	public static final String DB_NOMBRE = "indicadores.db";
	public static final int DB_VERSION = 1;

	public static final String TABLA = "indicador";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_NOMBRE = "NOMBRE";
	public static final String C_VALOR = "VALOR";

	public static final int C_ID_INDEX = 0;
	public static final int C_NOMBRE_INDEX = 1;
	public static final int C_VALOR_INDEX = 2;

	public DataBaseHelper(Context context) {
		super(context, DB_NOMBRE, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate - Creando la base de datos.");
		String query = "CREATE TABLE " + TABLA + "( "
						+ C_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT, "
						+ C_NOMBRE + " TEXT, "
						+ C_VALOR + " TEXT)";
		db.execSQL(query);
	
		Log.d(TAG, "onCreate - Cargando la base de datos.");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLA;
		db.execSQL(sql);
		Log.d(TAG, "onUpgrade()");
		onCreate(db);
	}

}