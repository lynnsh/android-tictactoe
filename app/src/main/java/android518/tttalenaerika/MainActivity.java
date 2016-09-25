package android518.tttalenaerika;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.Random;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int opponent = 1; //0:other user, 1:computer
    private ImageView[] views = new ImageView[10];
    private int turn = 0;
    private int playerXPts = 0;
    private int playerOPts = 0;
    private int compPts = 0;
    private int tiePts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        prepareBoard();
    }


    /**
     * Performs tasks connected to user's choice of the button, i.e.
     * verifies whether the button is available, whether there is a winning
     * sequence for the player as a result of this choice, and indicates
     * player whose turn is the next one.
     */
    public void btnClick(View view) {
        String currentPlayer = findPlayer(turn);

        markSpace(view, currentPlayer);

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
            continueGame();
        }

    }

    public void play(View view) {
        opponent = opponent == 1? 0: 1;
        prepareBoard();
    }

    public void reset(View view) {
        prepareBoard();
    }

    /**
     * Examines whether there is a winning position.
     */
    private boolean checkWin() {
        return  //check horizontally
                checkImages(views[1], views[2], views[3]) ||
                checkImages(views[4], views[5], views[6]) ||
                checkImages(views[7], views[8], views[9])
                //check vertically
                || checkImages(views[1], views[4], views[7])
                || checkImages(views[2], views[5], views[8])
                || checkImages(views[3], views[6], views[9])
                //check diagonally
                || checkImages(views[1], views[5], views[9]) || checkImages(views[3], views[5], views[7]);
    }

    private boolean checkImages(View view1, View view2, View view3) {
        Object obj = view1.getTag();
        String val1 = obj == null? "" : obj.toString();
        obj = view2.getTag();
        String val2 = obj == null? "" : obj.toString();
        obj = view3.getTag();
        String val3 = obj == null? "" : obj.toString();
        if (val1.equals(val2) && val1.equals(val3) && !val1.isEmpty()) {
            //changeColor(val1, val2, val3);
            return true;
        }
        return false;
    }

    /**
     * Sets and displays computer's choice according to the chosen level
     * of difficulty. Checks whether this choice led to a win.
     */
    private void computerTurn()
    {
        int choice = compEasy();
        ImageView iv = views[choice];
        iv.setImageResource(R.drawable.o);
        iv.setOnClickListener(null);
        iv.setTag("O");

        if (checkWin())  {
            Toast.makeText(this, getResources().getString(R.string.computer), Toast.LENGTH_LONG).show();
            compPts++;
            disableBoard();
        }
        else {}
            //enableBoard();
    }

    /**
     * Randomly assigns computer's choice.
     *
     * @return computer's choice.
     */
    private int compEasy()
    {
        Random random = new Random();
        int choice = 0;
        do {
            choice = random.nextInt(9)+1;
        } while (isTaken(choice));

        return choice;
    }

    private void continueGame() {
        if (turn > 8) {
            tiePts++;
            Toast.makeText(this, R.string.draw, Toast.LENGTH_LONG).show();
            disableBoard();
        }
        else if (opponent == 0) {
            String player = findPlayer(turn);
            Toast.makeText(this, String.format(getResources().getString(R.string.turn), player),
                                 Toast.LENGTH_SHORT).show();
        }
        else {
            computerTurn();
            turn++;
        }
    }

    private void disableBoard() {
        for(int i = 1; i < views.length; i++)
            views[i].setOnClickListener(null);
    }

    /**
     * Finds current player according to the turn value.
     *
     * @param turn Current turn value.
     *
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
     * Verifies whether given button is already taken.
     *
     * @param num The integer number of the button.
     *
     * @return true if button is already used;
     *         false otherwise.
     */
    private boolean isTaken(int num)  {

        View view = views[num];
        return view.getTag() != null;
    }

    private void getViews() {
        for(int i = 1; i < views.length; i++) {
            int id = getResources().getIdentifier("img"+i, "id", getPackageName());
            views[i] = (ImageView)findViewById(id);
        }
    }

    private void markSpace(View view, String player) {
        view.setTag(player);
        view.setOnClickListener(null);

        if(player.equals("X"))
            ((ImageView)view).setImageResource(R.drawable.x);
        else
            ((ImageView)view).setImageResource(R.drawable.o);
    }

    private void prepareBoard() {
        for(int i = 1; i < views.length; i++) {
            views[i].setImageResource(R.drawable.back);
            views[i].setTag(null);
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClick(v);
                }});
        }
        TextView tv = (TextView) findViewById(R.id.info);
        if(opponent == 1)
            tv.setText(R.string.info_comp);
        else
            tv.setText(R.string.info_user);

        turn = 0;
    }
}
