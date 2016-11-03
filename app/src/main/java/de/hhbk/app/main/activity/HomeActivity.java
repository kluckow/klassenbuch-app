package de.hhbk.app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.hhbk.app.b_login_v0.R;


public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        String username = getIntent().getStringExtra("username");

        String helloText = "Herzlich willkommen, " + username + "!";
        TextView textViewUsername = (TextView) findViewById(R.id.TextViewUsername);
        textViewUsername.setText(helloText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_close_app:
                finish();
                break;
            case R.id.action_pupil_list:
                Intent intent = new Intent(this, SchuelerlisteActivity.class);
                this.startActivity(intent);
                break;
        }
        return true;
    }

}
