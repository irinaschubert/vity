package ch.ffhs.vity.vity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by irina on 10.02.2018.
 */

public class CreateNewActivityActivity extends Activity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.new_activity);
    }

    /** Schlüssel für den Namen der neuen Aktivität. */
    public static final String NAME_ACTIVITY = "name";

    /** Schlüssel für die Kategorie der neuen Aktivität. */
    public static final String CATEGORY = "cat";

    /**public void onClickAddPicture(View button) {

     }**/

    /**public void onClickAddLocation(View button) {

     }**/

    /**public void onClickSaveNewActivity(View button) {

     }**/
}
