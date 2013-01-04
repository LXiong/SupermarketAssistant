package abz.chand.supermarketassistant;

import abz.chand.supermarketassistant.splash.DrawIt;
import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;

public class SupermarketAssistant extends Activity {
    private DrawIt draw;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView(R.layout.draw);
           
    }

}