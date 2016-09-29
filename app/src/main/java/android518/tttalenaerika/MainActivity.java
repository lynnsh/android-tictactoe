package android518.tttalenaerika;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class responsible for Tic Tac Toe game logic.
 */
public class MainActivity extends AppCompatActivity {
    // CHECK: make toast text centered

    private boolean isPlayerTwoHuman = false;
    private ImageView[] views = new ImageView[10];
    private int turn = 0;
    private int playerXPts = 0;
    private int playerOPts = 0;
    private int compPts = 0;
    private int tiePts = 0;
    private int resetCount = 0;

    /**
     * Lifecycle method. Initiates the board and ImageViews with default values.
     *
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.gameName);
        setContentView(R.layout.activity_main);
        getViews();
        prepareBoard();

        // Retrieving Shared Preferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        // Retrieving the points if they exist, else keeping them as 0
        playerXPts = prefs.getInt("playerXPts", 0);
        playerOPts = prefs.getInt("playerOPts", 0);
        tiePts = prefs.getInt("tiePts", 0);
        compPts = prefs.getInt("compPts", 0);
        resetCount = prefs.getInt("resetCount", 0);

        // Retrieving the state from Bundle
        if (savedInstanceState != null) {
            // Getting isPlayerTwoHuman and turn values
            isPlayerTwoHuman = savedInstanceState.getBoolean("isPlayerTwoHuman");
            turn = savedInstanceState.getInt("turn");

            // Updating the string for game mode
            TextView txtViewMode = (TextView) findViewById(R.id.info);
            if (isPlayerTwoHuman) {
                txtViewMode.setText(R.string.modePlayer);
            } else {
                txtViewMode.setText(R.string.modeComp);
            }

            // Make sure tags is not empty array
            if (savedInstanceState.getStringArray("tags").length != 0) {
                String[] temp = savedInstanceState.getStringArray("tags");
                for (int i = 1; i < temp.length; i++) {
                    // Reserve appropriate view if it is not null (un-clicked)
                    if (temp[i] != null) {
                        reserveView(views[i], temp[i]);
                    }
                }
            }
        }
    }

    /**
     * Invoked when user clicks on the ImageView. Checks for the winner
     * and either initiates computer's turn or, in case of two humans playing,
     * indicates whose turn is next.
     *
     * @param view The ImageView that triggered this method.
     */
    public void btnClick(View view) {
        String currentPlayer = findPlayer(turn);
        reserveView(view, currentPlayer);
        turn++;

        if (checkWin()) {
            Toast.makeText(this, String.format(getResources().getString(R.string.toastPlayerWin), currentPlayer), Toast.LENGTH_LONG).show();
            disableBoard();
            if (currentPlayer.equals("X"))
                playerXPts++;
            else
                playerOPts++;
        } else {
            initiateNextPlayer();
        }

    }

    /**
     * If game is not tied, indicates which player's turn is next.
     * Initiates computer turn if droid is an opponent.
     */
    private void initiateNextPlayer() {
        //check for a draw
        if (turn > 8) {
            tiePts++;
            Toast.makeText(this, R.string.toastDraw, Toast.LENGTH_LONG).show();
            disableBoard();
        }
        //two humans playing
        else if (isPlayerTwoHuman) {
            String player = findPlayer(turn);
            Toast.makeText(this, String.format(getResources().getString(R.string.toastTurn), player),
                    Toast.LENGTH_SHORT).show();
        }
        //droid playing
        else {
            computerTurn();
            turn++;
        }
    }

    /**
     * Triggered when the user clicks on Play button.
     * Clears the board for the next game and changes the opponent.
     *
     * @param view The view that triggered this method.
     */
    public void play(View view) {
        isPlayerTwoHuman = !isPlayerTwoHuman;
        prepareBoard();
    }

    /**
     * Triggered when the user clicks on Reset button.
     * Clears the board for the next game.
     *
     * @param view The view that triggered this method.
     */
    public void reset(View view) {
        prepareBoard();
        resetCount++;
    }

    /**
     * Examines whether there is a winning position.
     *
     * @return true if someone has won; false otherwise.
     */
    private boolean checkWin() {
        return  //check horizontally
                formWinPosition(views[1], views[2], views[3]) ||
                        formWinPosition(views[4], views[5], views[6]) ||
                        formWinPosition(views[7], views[8], views[9])
                        //check vertically
                        || formWinPosition(views[1], views[4], views[7])
                        || formWinPosition(views[2], views[5], views[8])
                        || formWinPosition(views[3], views[6], views[9])
                        //check diagonally
                        || formWinPosition(views[1], views[5], views[9]) ||
                        formWinPosition(views[3], views[5], views[7]);
    }

    /**
     * Determines whether views values are the same and not empty, hence leading
     * to the winning position.
     *
     * @param view1 The first view to compare to.
     * @param view2 The second view to compare to.
     * @param view3 The third view to compare to.
     * @return true if views form a winning position; false otherwise.
     */
    private boolean formWinPosition(View view1, View view2, View view3) {
        Object obj = view1.getTag();
        String val1 = obj == null ? "" : obj.toString();
        obj = view2.getTag();
        String val2 = obj == null ? "" : obj.toString();
        obj = view3.getTag();
        String val3 = obj == null ? "" : obj.toString();
        return val1.equals(val2) && val1.equals(val3) && !val1.isEmpty();
    }

    /**
     * Determines computer's choice and its consequences,
     * that is it displays the choice on the board and
     * checks whether the computer has won the game
     * (computer points are increased if this is the case).
     */
    private void computerTurn() {
        int choice = compEasy();
        ImageView iv = views[choice];
        iv.setImageResource(R.drawable.o);
        iv.setClickable(false);
        iv.setTag("O");

        if (checkWin()) {
            Toast.makeText(this, getResources().getString(R.string.toastCompWin), Toast.LENGTH_LONG).show();
            compPts++;
            disableBoard();
        }
    }

    /**
     * Randomly assigns computer's choice.
     *
     * @return computer's choice.
     */
    private int compEasy() {
        Random random = new Random();
        int choice = 0;
        do {
            choice = random.nextInt(9) + 1;
        } while (isViewReserved(choice));

        return choice;
    }

    /**
     * Makes buttons not clickable.
     */
    private void disableBoard() {
        for (int i = 1; i < views.length; i++)
            views[i].setClickable(false);
    }

    /**
     * Finds current player according to the turn value.
     *
     * @param turn Current turn value.
     * @return current player, which could be X or O.
     */
    private String findPlayer(int turn) {
        if (turn % 2 == 0)
            return "X";
        else
            return "O";
    }

    /**
     * Verifies whether the given view is already reserved by a player.
     *
     * @param num The integer number of the view.
     * @return true if view is reserved; false otherwise.
     */
    private boolean isViewReserved(int num) {
        View view = views[num];
        return view.getTag() != null;
    }

    /**
     * Populates ImageView array to have a reference to all images
     * representing 1-9 buttons.
     */
    private void getViews() {
        for (int i = 1; i < views.length; i++) {
            int id = getResources().getIdentifier("img" + i, "id", getPackageName());
            views[i] = (ImageView) findViewById(id);
        }
    }

    /**
     * Reserves view for the provided player.
     *
     * @param view   The ImageView to be reserved.
     * @param player The player that reserves the view.
     */
    private void reserveView(View view, String player) {
        view.setTag(player);
        view.setClickable(false);

        if (player.equals("X"))
            ((ImageView) view).setImageResource(R.drawable.x);
        else
            ((ImageView) view).setImageResource(R.drawable.o);
    }

    /**
     * Clears the board and sets all values to their defaults.
     */
    private void prepareBoard() {
        for (int i = 1; i < views.length; i++) {
            views[i].setImageResource(R.drawable.back);
            views[i].setTag(null);
            views[i].setClickable(true);
        }
        TextView tv = (TextView) findViewById(R.id.info);
        if (isPlayerTwoHuman) {
            tv.setText(R.string.modePlayer);
        } else {
            tv.setText(R.string.modeComp);
        }

        turn = 0;
    }

    /**
     * This method resets the score counters all to zero, and saves
     * the counters to shared preferences.
     *
     * @param view The button that triggered the method
     */
    public void zero(View view) {
        // Resetting the points
        playerXPts = 0;
        playerOPts = 0;
        tiePts = 0;
        compPts = 0;
        resetCount = 0;

        // Save the scores
        savePrefs();

        // Toast to give result
        Toast.makeText(this, getResources().getString(R.string.toastZero), Toast.LENGTH_SHORT).show();
    }

    /**
     * This method launches the DisplayScoresActivity activity,
     * which is used to display the current scores.
     *
     * @param view The button that triggered the method
     */
    public void scores(View view) {
        // Create intent
        Intent intent = new Intent(this, DisplayScoresActivity.class);

        // Adding the score values to the intent for transfer
        intent.putExtra("playerXPts", playerXPts);
        intent.putExtra("playerOPts", playerOPts);
        intent.putExtra("tiePts", tiePts);
        intent.putExtra("compPts", compPts);
        intent.putExtra("resetCount", resetCount);

        // Start the activity using the intent
        startActivity(intent);
    }

    /**
     * This method launches the AboutActivity activity,
     * which is used to display information about how to play and the creators of the app.
     *
     * @param view The button that triggered the method
     */
    public void about(View view) {
        // Create intent
        Intent intent = new Intent(this, AboutActivity.class);

        // Start the activity using the intent
        startActivity(intent);
    }

    /**
     * Overriden lifecycle method.  Saves the score values to
     * persistent data.
     */
    @Override
    protected void onPause() {
        // Calling super class
        super.onPause();

        // Save the Scores
        savePrefs();
    }

    /**
     * Overriden method.  Saves game data in the bundle.
     *
     * @param outState Bundle object to save data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String[] tags = new String[10];

        // Call the super
        super.onSaveInstanceState(outState);

        // Save game turn number and mode
        outState.putInt("turn", turn);
        outState.putBoolean("isPlayerTwoHuman", isPlayerTwoHuman);

        // Save image data (which letter if applies)
        for (int i = 1; i < views.length; i++) {
            // If tag is not null, add it, else keep the array value as null
            if (views[i].getTag() != null) {
                tags[i] = views[i].getTag().toString();
            }
        }
        outState.putStringArray("tags", tags);
    }

    /**
     * This method saves the scores to the Shared Preferences.
     */
    private void savePrefs() {
        // Getting the Shared Preferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Adding scores to Shared Preferences
        editor.putInt("playerXPts", playerXPts);
        editor.putInt("playerOPts", playerOPts);
        editor.putInt("tiePts", tiePts);
        editor.putInt("compPts", compPts);
        editor.putInt("resetCount", resetCount);

        // Saving Shared Preferences
        editor.commit();
    }
}
