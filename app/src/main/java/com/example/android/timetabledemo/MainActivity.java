package com.example.android.timetabledemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.example.android.timetabledemo.pojo.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    User authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAuthUser();

        setupUIViews();
        initToolbar();
        setupListView();
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarMain);
        listView = (ListView)findViewById(R.id.lvMain);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Smart Routine");
    }

    private void setupListView(){

        String[] title = getResources().getStringArray(R.array.Main);
        String[] description = getResources().getStringArray(R.array.Description);

        int permissionCount = 0;
        for (int i=0; i<title.length; i++) {
            if (authUser.getType().equalsIgnoreCase("Student")) {
                if (title[i].equalsIgnoreCase("My Schedule")) {
                    continue;
                }
            }
            permissionCount++;
        }

        String[] titles = new String[permissionCount];
        String[] descriptions = new String[permissionCount];

        permissionCount = 0;
        for (int i=0; i<title.length; i++) {
            if (authUser.getType().equalsIgnoreCase("Student")) {
                if (title[i].equalsIgnoreCase("My Schedule")) {
                    continue;
                }
            }
            titles[permissionCount] = title[i];
            descriptions[permissionCount] = description[i];
            permissionCount++;
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, titles, descriptions);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch(position){
                case 0: {
                    Intent intent = new Intent(MainActivity.this, MyScheduleActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1: {
                    Intent intent = new Intent(MainActivity.this, WeekActivity.class);
                    startActivity(intent);
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
                    startActivity(intent);
                    break;
                }
                case 4: {
                    break;
                }
                default:
                    break;
            }
        });
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView title, description;
        private String[] titleArray;
        private String[] descriptionArray;
        private ImageView imageView;

        public SimpleAdapter(Context context, String[] title, String[] description){
            mContext = context;
            titleArray = title;
            descriptionArray = description;
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public Object getItem(int position) {
            return titleArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.main_activity_single_item, null);
            }

            title = (TextView)convertView.findViewById(R.id.tvMain);
            description = (TextView)convertView.findViewById(R.id.tvDescription);
            imageView = (ImageView)convertView.findViewById(R.id.ivMain);

            title.setText(titleArray[position]);
            description.setText(descriptionArray[position]);

            if(titleArray[position].equalsIgnoreCase("My Schedule")){
                imageView.setImageResource(R.drawable.calender);
            }else if(titleArray[position].equalsIgnoreCase("Schedules")){
                imageView.setImageResource(R.drawable.timetable);
            }else if(titleArray[position].equalsIgnoreCase("Subjects")){
                imageView.setImageResource(R.drawable.book);
            }else if(titleArray[position].equalsIgnoreCase("Faculties")){
                imageView.setImageResource(R.drawable.contact);
            }
            else{
                imageView.setImageResource(R.drawable.settings);
            }

            return convertView;

        }
    }

    private void setAuthUser () {
        TextView authFullNameTextView = (TextView) findViewById(R.id.authFullName);
        TextView authUsernameTextView = (TextView) findViewById(R.id.authUsername);

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        authUser = sessionManagement.getSession();

        authFullNameTextView.setText("NAME: " + authUser.getFullName());
        authUsernameTextView.setText("USERNAME: " + authUser.getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.credits:
                Toast.makeText(MainActivity.this, "Under Construction.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
