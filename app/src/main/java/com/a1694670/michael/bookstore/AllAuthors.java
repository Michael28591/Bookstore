package com.a1694670.michael.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllAuthors extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    String autrid="";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_authors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AllAuthors.this);




        DatabaseReference myRef = database.getReference("authors");

        // Toast.makeText(this,currentCountry, Toast.LENGTH_SHORT).show();
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                int count = (int) dataSnapshot.getChildrenCount();
               // Toast.makeText(AllAuthors.this, String.valueOf(count), Toast.LENGTH_SHORT).show();

                AuthorFeed[] myFeeds = new AuthorFeed[3];
                int i=0;
                Boolean avail=false;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    autrid= ds.getKey();
                  //  Toast.makeText(AllAuthors.this, autrid, Toast.LENGTH_SHORT).show();
                    myFeeds[i] = new AuthorFeed(ds.child("name").getValue().toString(), ds.child("image").getValue().toString(),ds.getKey());
                    i = i + 1;
                }

                ListView feedListView = (ListView)findViewById(R.id.allauthors);
                AuthorAdapter feedAdapter = new AuthorAdapter(AllAuthors.this, myFeeds);
                feedListView.setAdapter(feedAdapter);   //Set the data for this ListView

                feedListView.setOnItemClickListener(    //Creating these on the fly.
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AuthorFeed myFeed = (AuthorFeed)parent.getItemAtPosition(position);
                                gotClicked(myFeed);
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,Search.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(this,AllBooks.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this,AllAuthors.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(this,Search.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotClicked(AuthorFeed feed)
    {

            Intent i = new Intent(this,AllBooks.class);
            i.putExtra("authorid",feed.authorid);
            startActivity(i);

    }
}


