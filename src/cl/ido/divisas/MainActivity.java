package cl.ido.divisas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cl.ido.divisas.db.DataBaseOperations;
import cl.ido.divisas.fragment.DivisasFragment;
import cl.ido.divisas.fragment.IndicadoresFragment;
import cl.ido.divisas.util.Constants;
import cl.ido.divisas.util.JSONParser;

import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	ViewPager pager;
	
	Bundle bundle;

	MyFragmentPagerAdapter adapter;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(TAG, "onCreate()");

		pager = (ViewPager) findViewById(R.id.pager);


        adapter = new MyFragmentPagerAdapter( getSupportFragmentManager() );

		//Por problemas con JSON
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		//Bundle bundle;
        new JSONDownloadTask().execute();
        
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	 
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	if (position == DivisasFragment.FRAGMENT_POSITION)
	    		return DivisasFragment.FRAGMENT_TITLE;
	    	else if (position == IndicadoresFragment.FRAGMENT_POSITION)
	    		return IndicadoresFragment.FRAGMENT_TITLE;
	        else
	        	return "ERROR";
	    }

	    List<Fragment> fragments;
	 
	    public MyFragmentPagerAdapter(FragmentManager fm) {
	        super(fm);
	        this.fragments = new ArrayList<Fragment>();
	    }
	 
	    public void addFragment(Fragment fragment) {
	        this.fragments.add(fragment);
	    }
	 
	    @Override
	    public Fragment getItem(int arg0) {
	        return this.fragments.get(arg0);
	    }
	 
	    @Override
	    public int getCount() {
	        return this.fragments.size();
	    }
	}

	/**
	 * Clase que maneja la transición del ViewPager
	 * http://developer.android.com/training/animation/screen-slide.html
	 *
	 */
	@SuppressLint("NewApi")
	public class DepthPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.75f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if (position < -1) {
	            view.setAlpha(0);

	        } else if (position <= 0) {
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) {
	            view.setAlpha(1 - position);

	            view.setTranslationX(pageWidth * -position);


	            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else {
	            view.setAlpha(0);
	        }
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_about) {
			Intent i = new Intent(this, AboutMeActivity.class);
			startActivity(i);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}


	private class JSONDownloadTask extends AsyncTask<Object, Void, Bundle>{

		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute(){
			Log.d(TAG, "onPreExecute()");
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.msgBuscando));
			progressDialog.show();
		}
		
		@Override
		protected Bundle doInBackground(Object... param) {
			Log.d(TAG, "doInBackground()");
			bundle = new Bundle();
			DataBaseOperations dbo = new DataBaseOperations(MainActivity.this);
			if ( hasActiveInternetConnection() ) {
				try {
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.getJSONFromUrl(Constants.JSON_URL);
					JSONObject jsonIndicador = json.getJSONObject(Constants.INDICADOR_EXTRA_INDICADOR);
					
					bundle.putString(Constants.INDICADOR_EXTRA_UF, jsonIndicador.getString(Constants.INDICADOR_EXTRA_UF));
					bundle.putString(Constants.INDICADOR_EXTRA_UTM, jsonIndicador.getString(Constants.INDICADOR_EXTRA_UTM));
					
					jsonIndicador = json.getJSONObject(Constants.INDICADOR_EXTRA_MONEDA);
					bundle.putString(Constants.INDICADOR_EXTRA_USD, jsonIndicador.getString(Constants.INDICADOR_EXTRA_USD));
					bundle.putString(Constants.INDICADOR_EXTRA_EUR, jsonIndicador.getString(Constants.INDICADOR_EXTRA_EUR));
					
					dbo.insertIndicadores(bundle);

				} catch (Exception e) {
					Log.e(TAG, "Error al obtener info desde el JSON: " + e.getMessage());
				}
			} else {
				Log.e(TAG, "No hay conexión disponible. Se rescatarásn los datos desde la Base de datos");
				//TODO Rescatar el Bundle desde la BDD
				bundle = dbo.getAllIndicadores();
				showToast(getResources().getString(R.string.msgValoresAntiguos)  + bundle.getString(Constants.INDICADOR_EXTRA_FECHA));
			}

			return bundle;
		}

		@Override
		protected void onPostExecute(Bundle result) {
			Log.d(TAG, "onPostExecute()");
			super.onPostExecute(result);
			
			adapter.addFragment(DivisasFragment.newInstance( bundle ));
			adapter.addFragment(IndicadoresFragment.newInstance( bundle ));

	        pager.setAdapter(adapter);
	        pager.setPageTransformer(true, new DepthPageTransformer());

	        // Bind the title indicator to the adapter
	        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
	        titleIndicator.setViewPager(pager);
	        
			progressDialog.dismiss();
		}

	}

	public boolean hasActiveInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
	
	public void showToast(final String toast){
	    runOnUiThread(new Runnable() {
	        public void run() {
	            Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show();
	        }
	    });
	}
}