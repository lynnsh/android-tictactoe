package android518.tttalenaerika;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity responsible for displaying the MainActivity's scores.
 */

public class DisplayScoresActivity extends Activity {

    private int playerXPts;
    private int playerOPts;
    private int tiePts;
    private int compPts;

    /**
     * Overriden lifecycle method. Calls the methods to get the data from the intent and update
     * the display with the values for the scores.
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayscores);

        // Retrieving data from Intent
        setData();

        // Retrieve data from SavedInstanceState
        if (savedInstanceState != null)
        {
            // Ints cannot be null, thus do not need to check
            playerXPts = savedInstanceState.getInt("playerXPts");
            playerOPts = savedInstanceState.getInt("playerOPts");
            tiePts = savedInstanceState.getInt("tiePts");
            compPts = savedInstanceState.getInt("compPts");
        }

        // Update the display
        updateDisplay();
    }

    /**
     * Sets the data given through the intent.
     */
    private void setData()
    {
        playerXPts = getIntent().getExtras().getInt("playerXPts", 0);
        playerOPts = getIntent().getExtras().getInt("playerOPts", 0);
        tiePts = getIntent().getExtras().getInt("tiePts", 0);
        compPts = getIntent().getExtras().getInt("compPts", 0);
    }

    /**
     * Updates the display to append the scores to the strings
     */
    private void updateDisplay()
    {
        // Getting all the appropriate text views
        TextView txtViewPlayerX = (TextView) findViewById(R.id.txtViewPlayerX);
        TextView txtViewPlayerO = (TextView) findViewById(R.id.txtViewPlayerO);
        TextView txtViewTies = (TextView) findViewById(R.id.txtViewTies);
        TextView txtViewComp = (TextView) findViewById(R.id.txtViewComp);

        // Setting the text to append the score
        txtViewPlayerX.setText(getResources().getString(R.string.scoresPlayerX) + playerXPts);
        txtViewPlayerO.setText(getResources().getString(R.string.scoresPlayerO) + playerOPts);
        txtViewTies.setText(getResources().getString(R.string.scoresTies) + tiePts);
        txtViewComp.setText(getResources().getString(R.string.scoresComp) + compPts);
    }

    /**
     * Overriden method.  Saves score values in the bundle.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // Call the super
        super.onSaveInstanceState(outState);

        // Save scores to instance state
        outState.putInt("playerXPts", playerXPts);
        outState.putInt("playerOPts", playerOPts);
        outState.putInt("tiePts", tiePts);
        outState.putInt("compPts", compPts);
    }
}
