package android518.tttalenaerika;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity responsible for displaying the about page
 */

public class AboutActivity extends AppCompatActivity {
    /**
     * Overriden lifecycle method. Sets the layout.
     *
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.gameName);
        setTheme(R.style.AboutTheme);
        setContentView(R.layout.activity_about);
    }
}
