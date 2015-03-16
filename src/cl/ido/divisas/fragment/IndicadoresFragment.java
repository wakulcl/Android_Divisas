package cl.ido.divisas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import cl.ido.divisas.R;
import cl.ido.divisas.util.Constants;
import cl.ido.divisas.util.Utils;

public class IndicadoresFragment extends Fragment {

	private static final String TAG = IndicadoresFragment.class.getSimpleName();
	
	Spinner spnIndicadores;

	TextView txtIndicador;
	TextView txtTituloInputIndicador;
	TextView txtTituloOutputIndicador;
	
	EditText editInputIndicador;
	EditText editInputCLP;
	EditText editOutputIndicador;
	EditText editOutputCLP;
	
	float indicador;
	
	public final static int FRAGMENT_POSITION = 1;
    public final static String FRAGMENT_TITLE = "Indicadores";

    public static IndicadoresFragment newInstance(Bundle bundle) {
    	IndicadoresFragment fragment = new IndicadoresFragment();

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

    	//Para rescatar los argunmentos
        //title = getArguments().getInt(EXTRA_TITLE);
        //message = getArguments().getString(EXTRA_MESSAGE);
    }


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View view = inflater.inflate(R.layout.fragment_indicadores, null);
		
		final String txtUF = getArguments().getString(Constants.INDICADOR_EXTRA_UF);
		final String txtUTM = getArguments().getString(Constants.INDICADOR_EXTRA_UTM);
        
		//TextView que mostrará el valor en pesos (CLP) del Indicador seleccionado (UF/UTM)
		txtIndicador = (TextView) view.findViewById(R.id.txtIndicador);
		
		txtTituloInputIndicador = (TextView) view.findViewById(R.id.txtTituloInputIndicador);
		txtTituloOutputIndicador = (TextView) view.findViewById(R.id.txtTituloOutputIndicador);

		spnIndicadores = (Spinner) view.findViewById(R.id.spnIndicadores);
		spnIndicadores.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				cleanAllEditText();
				if (pos == Constants.POSICION_UF) {
					txtIndicador.setText(txtUF);
					txtTituloInputIndicador.setText( getResources().getString(R.string.txtUF) );
					txtTituloOutputIndicador.setText( getResources().getString(R.string.txtUF) );
				} else {
					txtIndicador.setText(txtUTM);
					txtTituloInputIndicador.setText( getResources().getString(R.string.txtUTM) );
					txtTituloOutputIndicador.setText( getResources().getString(R.string.txtUTM) );
				}
				indicador = Utils.getNumberFromString( txtIndicador.getText().toString() );
			}

			public void onNothingSelected(AdapterView<?> parent) {}

		});
		
		
		
		editOutputCLP = (EditText) view.findViewById(R.id.editOutputCLP);
		editOutputIndicador = (EditText) view.findViewById(R.id.editOutputIndicador);
		editOutputCLP.setFocusable(false);
		editOutputIndicador.setFocusable(false);

		editInputIndicador = (EditText) view.findViewById(R.id.editInputIndicador);
		editInputIndicador.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//if ( (event.getAction() == KeyEvent.ACTION_DOWN)  && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					int cantidad = 0;
					try {
						cantidad = Integer.parseInt( editInputIndicador.getText().toString() );
					} catch (NumberFormatException e) {}
					float monto = indicador * cantidad;

					editOutputCLP.setText( Utils.roundNumber(monto, 2) );
				//}
				return false;
			}
		});

		
		editInputCLP = (EditText) view.findViewById(R.id.editInputIndicadorCLP);
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
						monto = cantidad / indicador;
					} catch (Exception e) {
						Log.d(TAG, e.getMessage());
					}

					editOutputIndicador.setText( Utils.roundNumber(monto, 2) );
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
    	editInputIndicador.setText("");
    	editInputCLP.setText("");
    	editOutputIndicador.setText("");
    	editOutputCLP.setText("");
    }
}