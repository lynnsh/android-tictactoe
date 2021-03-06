package android518.tttalenaerika;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Activity responsible for displaying the MainActivity's scores.
 */
public class DisplayScoresActivity extends AppCompatActivity {

    private int playerXPts;
    private int playerOPts;
    private int tiePts;
    private int compPts;
    private int resetCount;

    /**
     * Overriden lifecycle method. Calls the methods to get the data from the intent and update
     * the display with the values for the scores.
     *
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.gameName);
        setTheme(R.style.ScoresTheme);
        setContentView(R.layout.activity_displayscores);

        // Retrieving data from Intent
        setData();

        // Retrieve data from SavedInstanceState
        if (savedInstanceState != null) {
            // Ints cannot be null, thus do not need to check
            playerXPts = savedInstanceState.getInt("playerXPts");
            playerOPts = savedInstanceState.getInt("playerOPts");
            tiePts = savedInstanceState.getInt("tiePts");
            compPts = savedInstanceState.getInt("compPts");
            resetCount = savedInstanceState.getInt("resetCount");
        }

        // Update the display
        updateDisplay();
    }

    /**
     * Sets the data given through the intent.
     */
    private void setData() {
        playerXPts = getIntent().getExtras().getInt("playerXPts", 0);
        playerOPts = getIntent().getExtras().getInt("playerOPts", 0);
        tiePts = getIntent().getExtras().getInt("tiePts", 0);
        compPts = getIntent().getExtras().getInt("compPts", 0);
        resetCount = getIntent().getExtras().getInt("resetCount", 0);
    }

    /**
     * Updates the display to append the scores to the strings
     */
    private void updateDisplay() {
        // Getting all the appropriate text views
        TextView txtViewPlayerX = (TextView) findViewById(R.id.txtViewPlayerXScore);
        TextView txtViewPlayerO = (TextView) findViewById(R.id.txtViewPlayerOScore);
        TextView txtViewTies = (TextView) findViewById(R.id.txtViewTiesScore);
        TextView txtViewComp = (TextView) findViewById(R.id.txtViewCompScore);
        TextView txtViewReset = (TextView) findViewById(R.id.txtViewResetScore);

        // Setting the text to append the score
        txtViewPlayerX.setText("" + playerXPts);
        txtViewPlayerO.setText("" + playerOPts);
        txtViewTies.setText("" + tiePts);
        txtViewComp.setText("" + compPts);
        txtViewReset.setText("" + resetCount);
    }

    /**
     * Overriden method.  Saves score values in the bundle.
     *
     * @param outState Bundle object to save data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Call the super
        super.onSaveInstanceState(outState);

        // Save scores to instance state
        outState.putInt("playerXPts", playerXPts);
        outState.putInt("playerOPts", playerOPts);
        outState.putInt("tiePts", tiePts);
        outState.putInt("compPts", compPts);
        outState.putInt("resetCount", resetCount);
    }
}
