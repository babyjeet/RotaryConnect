package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class RotaryLibraryActivity extends AppCompatActivity {
    SessionManager session;
    private GridView gridView;
    AlertDialogManager alert = new AlertDialogManager();
    private TextView txtName;
    String[] web = {"Tree Plantation", "Blood Donation ", "Rakshabandhan",
            "Tree Plantation", "Citizen Forum","Canvas Painting",
            "Burn & Fire Safety",  "Swacchata", "Green Bhatti"} ;
    int[] imageId = {
            R.drawable.tree_plantation, R.drawable.blood_donation,
            R.drawable.suraksha_rakshabandhan, R.drawable.mega_tree_plantation,
            R.drawable.citizen_forum, R.drawable.canvas_painting,
            R.drawable.burn_fire_safety, R.drawable.swacchata,
            R.drawable.green_bhatti
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_library);

        session = new SessionManager(getApplicationContext());

        ImageAdapter adapter = new ImageAdapter(RotaryLibraryActivity.this, web, imageId);
        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(DashboardActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                //Toast.makeText(DashboardActivity.this, "You Clicked On " +position+"th Grid", Toast.LENGTH_SHORT).show();

                switch (position){
                    case 0:
                        /*Intent intent = new Intent(getApplicationContext(),DirectoryActivity.class);
                        startActivity(intent);*/
                        break;
                    case 1:
                        /*Intent intent1 = new Intent(getApplicationContext(), EventActivity.class);
                        startActivity(intent1);*/
                        break;
                    case 2:
                        /*Intent intent2 = new Intent(getApplicationContext(), AnnouncementsActivity.class);
                        startActivity(intent2);*/
                        break;
                    case 3:
                        /*Intent intent3 = new Intent(getApplicationContext(),NewsletterActivity.class);
                        startActivity(intent3);*/
                        break;
                    case 4:
                        /*Intent intent4 = new Intent(getApplicationContext(),FindUsActivity.class);
                        startActivity(intent4);*/
                        break;
                    case 5:
                        /*Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.app.clubfinder");
                        startActivity(LaunchIntent);*/
                        break;
                    case 6:
                        /*Intent intent6 = new Intent(getApplicationContext(),GalleryActivity.class);
                        startActivity(intent6);*/
                        break;
                    case 7:
                       /* Intent intent7 = new Intent(getApplicationContext(),RotaryLibraryActivity.class);
                        startActivity(intent7);*/
                        break;
                    case 8:
                        /*Intent intent8 = new Intent(getApplicationContext(),DashProfileActivity.class);
                        startActivity(intent8);*/
                        break;
                    case 9:
                        /*Intent intent9 = new Intent(getApplicationContext(),PastPresidentsActivity.class);
                        startActivity(intent9);*/
                        break;
                    case 10:
                        /*Intent intent10 = new Intent(getApplicationContext(),BoardOfDirectorsActivity.class);
                        startActivity(intent10);*/
                        break;
                    case 11:
                        /*Uri uri = Uri.parse("https://www.rotary.org/"); // missing 'http://' will cause crashed
                        Intent intent11 = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent11);*/
                        break;
                    default:
                        break;
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Rotary Library");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                session.logoutUser();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
