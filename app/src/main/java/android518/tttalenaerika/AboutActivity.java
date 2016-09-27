package android518.tttalenaerika;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity responsible for displaying the about page
 */

public class AboutActivity extends Activity {
    /**
     * Overriden lifecycle method. Sets the layout.
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
