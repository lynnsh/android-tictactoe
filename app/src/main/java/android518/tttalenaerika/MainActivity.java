package android518.tttalenaerika;

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

    private int opponent = 1; //0:other user, 1:computer
    private ImageView[] views = new ImageView[10];
    private int turn = 0;
    private int playerXPts = 0;
    private int playerOPts = 0;
    private int compPts = 0;
    private int tiePts = 0;

    /**
     * Lifecycle method. Initiates the board and ImageViews with default values.
     * @param savedInstanceState Bundle object to restore saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        prepareBoard();
    }


    /**
     * Invoked when user clicks on the ImageView. Checks for the winner
     * and either initiates computer's turn or, in case of two humans playing,
     * indicates whose turn is next.
     * @param view The ImageView that triggered this method.
     */
    public void btnClick(View view) {
        String currentPlayer = findPlayer(turn);
        reserveView(view, currentPlayer);
        turn++;

        if (checkWin()) {
            Toast.makeText(this, String.format(getResources().getString(R.string.win), currentPlayer), Toast.LENGTH_LONG).show();
            disableBoard();
            if (currentPlayer.equals("X"))
                playerXPts++;
            else
                playerOPts++;
        }
        else {
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
            Toast.makeText(this, R.string.draw, Toast.LENGTH_LONG).show();
            disableBoard();
        }
        //two humans playing
        else if (opponent == 0) {
            String player = findPlayer(turn);
            Toast.makeText(this, String.format(getResources().getString(R.string.turn), player),
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
     * @param view The view that triggered this method.
     */
    public void play(View view) {
        opponent = opponent == 1? 0: 1;
        prepareBoard();
    }

    /**
     * Triggered when the user clicks on Reset button.
     * Clears the board for the next game.
     * @param view The view that triggered this method.
     */
    public void reset(View view) {
        prepareBoard();
    }

    /**
     * Examines whether there is a winning position.
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
     * @param view1 The first view to compare to.
     * @param view2 The second view to compare to.
     * @param view3 The third view to compare to.
     * @return true if views form a winning position; false otherwise.
     */
    private boolean formWinPosition(View view1, View view2, View view3) {
        Object obj = view1.getTag();
        String val1 = obj == null? "" : obj.toString();
        obj = view2.getTag();
        String val2 = obj == null? "" : obj.toString();
        obj = view3.getTag();
        String val3 = obj == null? "" : obj.toString();
        return val1.equals(val2) && val1.equals(val3) && !val1.isEmpty();
    }

    /**
     * Determines computer's choice and its consequences,
     * that is it displays the choice on the board and
     * checks whether the computer has won the game
     * (computer points are increased if this is the case).
     */
    private void computerTurn()
    {
        int choice = compEasy();
        ImageView iv = views[choice];
        iv.setImageResource(R.drawable.o);
        iv.setClickable(false);
        iv.setTag("O");

        if (checkWin())  {
            Toast.makeText(this, getResources().getString(R.string.computer), Toast.LENGTH_LONG).show();
            compPts++;
            disableBoard();
        }
    }

    /**
     * Randomly assigns computer's choice.
     * @return computer's choice.
     */
    private int compEasy()
    {
        Random random = new Random();
        int choice = 0;
        do {
            choice = random.nextInt(9)+1;
        } while (isViewReserved(choice));

        return choice;
    }

    /**
     * Makes buttons not clickable.
     */
    private void disableBoard() {
        for(int i = 1; i < views.length; i++)
            views[i].setClickable(false);
    }

    /**
     * Finds current player according to the turn value.
     * @param turn Current turn value.
     * @return current player, which could be X or O.
     */
    private String findPlayer(int turn)
    {
        if (turn % 2 == 0)
            return "X";
        else
            return "O";
    }

    /**
     * Verifies whether the given view is already reserved by a player.
     * @param num The integer number of the view.
     * @return true if view is reserved; false otherwise.
     */
    private boolean isViewReserved(int num)  {
        View view = views[num];
        return view.getTag() != null;
    }

    /**
     * Populates ImageView array to have a reference to all images
     * representing 1-9 buttons.
     */
    private void getViews() {
        for(int i = 1; i < views.length; i++) {
            int id = getResources().getIdentifier("img"+i, "id", getPackageName());
            views[i] = (ImageView)findViewById(id);
        }
    }

    /**
     * Reserves view for the provided player.
     * @param view The ImageView to ne reserved.
     * @param player The player that reserves the view.
     */
    private void reserveView(View view, String player) {
        view.setTag(player);
        view.setClickable(false);

        if(player.equals("X"))
            ((ImageView)view).setImageResource(R.drawable.x);
        else
            ((ImageView)view).setImageResource(R.drawable.o);
    }

    /**
     * Clears the board and sets all values to their defaults.
     */
    private void prepareBoard() {
        for(int i = 1; i < views.length; i++) {
            views[i].setImageResource(R.drawable.back);
            views[i].setTag(null);
            views[i].setClickable(true);
        }
        TextView tv = (TextView) findViewById(R.id.info);
        if(opponent == 1)
            tv.setText(R.string.info_comp);
        else
            tv.setText(R.string.info_user);

        turn = 0;
    }
}
