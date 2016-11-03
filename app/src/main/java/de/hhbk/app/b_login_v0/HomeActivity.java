package de.hhbk.app.b_login_v0;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_activity);
        String username = getIntent().getStringExtra("username");

        String helloText = "Herzlich willkommen, " + username + "!";
        TextView textViewUsername = (TextView) findViewById(R.id.TextViewUsername);
        textViewUsername.setText(helloText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_login__v0, menu);
        getMenuInflater().inflate(R.menu.menu_logged_in_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                // do nothing for now
                break;
            case R.id.action_pupil_list:
                Intent intent = new Intent(this, PupilListActivity.class);
                this.startActivity(intent);
                break;
        }
        return true;
    }

}
