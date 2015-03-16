package cl.ido.divisas.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import cl.ido.divisas.util.Constants;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class DataBaseOperations {
	private final static String TAG = DataBaseOperations.class.getSimpleName();
	private DataBaseHelper dbHelper;
	
	public DataBaseOperations(Context context) {
		dbHelper = new DataBaseHelper(context);
	}


	/**
	 * Inserta todos los valores (Indicadores) obtenidos desde el JSON
	 * @param bundle
	 */
	@SuppressLint("SimpleDateFormat")
	public void insertIndicadores(Bundle bundle) {
		Log.d(TAG, "insertIndicadores()");
		SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
		
		dataBase.delete(DataBaseHelper.TABLA, null, null);
		ContentValues values;
		for (String key : bundle.keySet()) {
			values = new ContentValues();
			values.put(DataBaseHelper.C_NOMBRE, key);
			values.put(DataBaseHelper.C_VALOR, bundle.get(key).toString());
			dataBase.insert(DataBaseHelper.TABLA, null, values);
		}
		
		//También se inserta la fecha de cuando se insertaron los datos
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	Date now = new Date();
		values = new ContentValues();
		values.put(DataBaseHelper.C_NOMBRE, Constants.INDICADOR_EXTRA_FECHA);
		values.put(DataBaseHelper.C_VALOR, sdf.format(now) );
		dataBase.insert(DataBaseHelper.TABLA, null, values);
		dataBase.close();
	}


	/**
	 * Devuelve un Bundle con todos los indicadores
	 * @return
	 */
	public Bundle getAllIndicadores() {
		Log.d(TAG, "getAllIndicadores()");
		Bundle bundle = new Bundle();
		SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
		Cursor cursor = dataBase.query(DataBaseHelper.TABLA, null, null, null, null, null, null);

		if ( cursor.moveToFirst() ) {
			while ( !cursor.isAfterLast() ) {
				bundle.putString(
						cursor.getString( DataBaseHelper.C_NOMBRE_INDEX ),
						cursor.getString( DataBaseHelper.C_VALOR_INDEX )
				);
				cursor.moveToNext();
			}
		}
		cursor.close();
		dataBase.close();
		return bundle;
	}
}