package cl.ido.divisas;

import cl.ido.divisas.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutMeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_me);
		
		ImageView imgMail = (ImageView)findViewById(R.id.imgMail);
		imgMail.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		    	Intent i = new Intent(Intent.ACTION_SEND);
		    	i.setType("message/rfc822");
		    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Constants.AUTHOR_MAIL});
		    	i.putExtra(Intent.EXTRA_SUBJECT, "Contacto via DIVISAS CL");
		    	startActivity(Intent.createChooser(i, "Enviar con..."));
		    }
		});


		ImageView imgLinkedin = (ImageView)findViewById(R.id.imgLinkedin);
		imgLinkedin.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse(Constants.AUTHOR_LINKEDIN));
		        startActivity(intent);
		    }
		});		
		
	}

}
