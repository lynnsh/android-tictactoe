package android518.tttalenaerika;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int opponent = 1; //0:other user, 1:computer
    private int turn = 0;
    private int playerXPts = 0;
    private int playerOPts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
