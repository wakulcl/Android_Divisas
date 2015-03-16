package cl.ido.divisas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cl.ido.divisas.R;
import cl.ido.divisas.util.Constants;
import cl.ido.divisas.util.Utils;

public class DivisasFragment extends Fragment {

	private static final String TAG = DivisasFragment.class.getSimpleName();

	EditText editInputDivisa;
	EditText editInputCLP;
	EditText editOutputDivisa;
	EditText editOutputCLP;

	TextView txtDivisa;
	TextView txtTituloInputDivisa;
	TextView txtTituloOutputDivisa;

	Spinner spnDivisas;

	String cantidadDivisa;
	
	float divisa;

    public final static int FRAGMENT_POSITION = 0;
    public final static String FRAGMENT_TITLE = "Divisas";

    public static DivisasFragment newInstance(Bundle bundle) {
    	DivisasFragment fragment = new DivisasFragment();

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
    
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View view = inflater.inflate(R.layout.fragment_divisas, null);

		//Obtiene el valor del Dolar y Euro
		final String txtDolar = getArguments().getString(Constants.INDICADOR_EXTRA_USD);
		final String txtEuro = getArguments().getString(Constants.INDICADOR_EXTRA_EUR);

		txtTituloInputDivisa = (TextView) view.findViewById(R.id.txtTituloInputDivisa);
		txtTituloOutputDivisa = (TextView) view.findViewById(R.id.txtTituloOutputDivisa);

		//TextView que mostrará el valor en pesos (CLP) de la divisa (Dolar o Euro)
		txtDivisa = (TextView) view.findViewById(R.id.txtDivisa);

		spnDivisas = (Spinner) view.findViewById(R.id.spnDivisas);
		spnDivisas.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				cleanAllEditText();
				if (pos == Constants.POSICION_DOLAR) {
					txtDivisa.setText(txtDolar);
					txtTituloInputDivisa.setText( getResources().getString(R.string.txtUSD) );
					txtTituloOutputDivisa.setText( getResources().getString(R.string.txtUSD) );
				} else {
					txtDivisa.setText(txtEuro);
					txtTituloInputDivisa.setText( getResources().getString(R.string.txtEUR) );
					txtTituloOutputDivisa.setText( getResources().getString(R.string.txtEUR) );
				}
				divisa = Utils.getNumberFromString( txtDivisa.getText().toString() );
			}

			public void onNothingSelected(AdapterView<?> parent) {}

		});
		
		
		
		editOutputCLP = (EditText) view.findViewById(R.id.editOutputDivisaCLP);
		editOutputDivisa = (EditText) view.findViewById(R.id.editOutputDivisa);
		editOutputCLP.setFocusable(false);
		editOutputDivisa.setFocusable(false);

		editInputDivisa = (EditText) view.findViewById(R.id.editInputUSD);
		editInputDivisa.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//if ( (event.getAction() == KeyEvent.ACTION_DOWN)  && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					int cantidad = 0;
					try {
						cantidad = Integer.parseInt( editInputDivisa.getText().toString() );
					} catch (NumberFormatException e) {}
					float monto = divisa * cantidad;

					editOutputCLP.setText( Utils.roundNumber(monto, 2) );
				//}
				return false;
			}
		});



		editInputCLP = (EditText) view.findViewById(R.id.editInputDivisaCLP);
		editInputCLP.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//if ( (event.getAction() == KeyEvent.ACTION_DOWN)  && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					int cantidad = 0;
					try {
						cantidad = Integer.parseInt( editInputCLP.getText().toString() );
					} catch (NumberFormatException e) {}
					
					float monto = 0;
					try {
						monto = cantidad / divisa;
					} catch (Exception e) {
						Log.d(TAG, e.getMessage());
					}

					editOutputDivisa.setText( Utils.roundNumber(monto, 2) );
				//}
				return false;
			}
		});

		
		return view;
	}

    /**
     * Limpia todos los inputs del fragmento
     */
    public void cleanAllEditText() {
    	editInputDivisa.setText("");
    	editInputCLP.setText("");
    	editOutputDivisa.setText("");
    	editOutputCLP.setText("");
    }
}