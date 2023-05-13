package com.example.italyword;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.italyword.databinding.ActivityMainBinding;
import com.example.italyword.model.WordModel;
import com.example.italyword.view.createword.CreateWordActivity;
import com.example.italyword.view.home.HomeFragment;
import com.example.italyword.viewmodel.WordViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String tag =this.getClass().getSimpleName();
    private WordViewModel wordViewModel ;
    private View header;
    private LinearLayout resetButtn;
    private TextView checkStatusCountText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordModel model = new WordModel();
        wordViewModel =new ViewModelProvider(this).get(WordViewModel.class);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        header = navigationView.getHeaderView(0);
        resetButtn =(LinearLayout) header.findViewById(R.id.wordRestBtn);
        checkStatusCountText=(TextView) header.findViewById(R.id.checkStatusCountText);

        resetButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordViewModel.resetAllWordStatus();
                setCheckCountStatud();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setCheckCountStatud();
    }

    public void setCheckCountStatud(){

        wordViewModel.getCheckStatusCount().observe(this,blog ->{
            checkStatusCountText.setText(blog);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){

            startActivity(new Intent(MainActivity.this, CreateWordActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}