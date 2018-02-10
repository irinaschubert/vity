package ch.ffhs.vity.vity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickCreateNewActivity(View button) {
        // View "Neue Aktivität Erstellen" öffnen
        final Intent intent = new Intent(this, CreateNewActivityActivity.class);
        startActivity(intent);
    }
}