package com.a1694670.michael.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Locale;

public class AllBooks extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Already on homepage", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //Toast.makeText(this, "Intent Haiga", Toast.LENGTH_SHORT).show();
                if (extras.containsKey("authorid")) {
                    String idOfAuthor = extras.getString("authorid");
                    fetchBookByAuthorId(idOfAuthor);
                }
                if (extras.containsKey("searchby")) {
                    String searchBy = extras.getString("searchby");
                    String searchTerm = extras.getString("searchterm");
                    if (searchBy.equals("Book Name")) searcgByBookName(searchTerm);
                    if (searchBy.equals("Author"))     searcgByAuthorName(searchTerm);
                    if (searchBy.equals("Category"))   searcgByCatName(searchTerm);

                }

            }


        else {
           // Toast.makeText(this, "Nahi haiga all books", Toast.LENGTH_SHORT).show();
            fetchallbooks();
        }






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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotClicked(BookFeed feed)
    {
       // Toast.makeText(AllBooks.this, feed.message, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,SingleBook.class);
        i.putExtra("BookName",feed.message);
        startActivity(i);
    }

    public void fetchallbooks(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        // Toast.makeText(this,currentCountry, Toast.LENGTH_SHORT).show();
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String currentCountry = AllBooks.this.getResources().getConfiguration().locale.getCountry();

                String loc = "shipToUSA";

                if(currentCountry.equals("CA")) {
                    loc = "shipToCanada";
                }
                int count = (int) dataSnapshot.getChildrenCount();
                // Toast.makeText(AllBooks.this, count, Toast.LENGTH_SHORT).show();

                BookFeed[] myFeeds = new BookFeed[count-1];
                int i=0;
                Boolean avail=false;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    avail =  (Boolean) ds.child(loc).getValue();

                    if (avail) {
                        myFeeds[i] = new BookFeed(ds.child("title").getValue().toString(), ds.child("cover").getValue().toString());
                        // Toast.makeText(AllBooks.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                        i = i + 1;
                    }
                    //Toast.makeText(AllBooks.this,ds.child("title").getValue().toString(),Toast.LENGTH_LONG).show();
                    // Log.d("Titles",ds.child("title").getValue().toString());
                }

                ListView feedListView = (ListView)findViewById(R.id.allbooks);
                BooksAdapter feedAdapter = new BooksAdapter(AllBooks.this, myFeeds);
                feedListView.setAdapter(feedAdapter);   //Set the data for this ListView

                feedListView.setOnItemClickListener(    //Creating these on the fly.
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BookFeed myFeed = (BookFeed)parent.getItemAtPosition(position);
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

    public void fetchBookByAuthorId(String id) {
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        final String authoriftoftech = id;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        // Toast.makeText(this,currentCountry, Toast.LENGTH_SHORT).show();
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String currentCountry = AllBooks.this.getResources().getConfiguration().locale.getCountry();

                String loc = "shipToUSA";

                if(currentCountry.equals("CA")) {
                    loc = "shipToCanada";
                }
                int count = (int) dataSnapshot.getChildrenCount();
                // Toast.makeText(AllBooks.this, count, Toast.LENGTH_SHORT).show();
                int countNum=0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                     if (ds.child("author_id").getValue().toString().equals(authoriftoftech)) {
                         countNum++;

                     }
                }
                if (countNum == 0){
                    return;
                }
                BookFeed[] myFeeds = new BookFeed[countNum];
                int i=0;
                Boolean avail=false;

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    avail =  (Boolean) ds.child(loc).getValue();

                    if (avail) {
                       // Toast.makeText(AllBooks.this, "Loca theek aw", Toast.LENGTH_SHORT).show();
                        if (ds.child("author_id").getValue().toString().equals(authoriftoftech)) {
                            myFeeds[i] = new BookFeed(ds.child("title").getValue().toString(), ds.child("cover").getValue().toString());
                            // Toast.makeText(AllBooks.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                            i = i + 1;
                           // Toast.makeText(AllBooks.this, "Id a vv gai", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(AllBooks.this,ds.child("title").getValue().toString(),Toast.LENGTH_LONG).show();
                    // Log.d("Titles",ds.child("title").getValue().toString());
                }
                if (i==0) {
                    myFeeds[0] = new BookFeed("He has No Book", "https://images-na.ssl-images k-amazon.com/images/I/51Rmbpdgh3L._SY200_.gif");
                }
                ListView feedListView = (ListView)findViewById(R.id.allbooks);
                BooksAdapter feedAdapter = new BooksAdapter(AllBooks.this, myFeeds);
                feedListView.setAdapter(feedAdapter);   //Set the data for this ListView

                feedListView.setOnItemClickListener(    //Creating these on the fly.
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BookFeed myFeed = (BookFeed)parent.getItemAtPosition(position);
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

    public void  searcgByBookName(final String TermToSearch) {
       // Toast.makeText(this, "Book Name ", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
           String name = TermToSearch;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int adapterLength=0;
                for(DataSnapshot dsl: dataSnapshot.getChildren())
                {
                    String titleBook=dsl.child("title").getValue().toString();
                    if (titleBook.toLowerCase().equals(name.toLowerCase()) || titleBook.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                        adapterLength++;
                    }
                }
                String currentCountry = AllBooks.this.getResources().getConfiguration().locale.getCountry();

                String loc = "shipToUSA";

                if(currentCountry.equals("CA")) {
                    loc = "shipToCanada";
                }
                if (adapterLength == 0) adapterLength=1;
                BookFeed[] myFeeds = new BookFeed[adapterLength];
                int i=0;
                Boolean avail=false;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //Toast.makeText(SingleBook.this, "test", Toast.LENGTH_SHORT).show();
                    String title=ds.child("title").getValue().toString();

                    avail =  (Boolean) ds.child(loc).getValue();

                    if (avail) {
                        if (title.toLowerCase().equals(name.toLowerCase()) || title.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                            myFeeds[i] = new BookFeed(ds.child("title").getValue().toString(), ds.child("cover").getValue().toString());
                            // Toast.makeText(AllBooks.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                            i = i + 1;

                            adapterLength++;
                        }
                    }
                    // Toast.makeText(SingleBook.this,ds.child("title").getValue().toString(),Toast.LENGTH_LONG).show();
                    // Log.d("Titles",ds.child("title").getValue().toString());
                }
                if (i==0) {
                    myFeeds[0] = new BookFeed("No Book found", "https://images-na.ssl-images k-amazon.com/images/I/51Rmbpdgh3L._SY200_.gif");
                }
                ListView feedListView = (ListView)findViewById(R.id.allbooks);
                BooksAdapter feedAdapter = new BooksAdapter(AllBooks.this, myFeeds);
                feedListView.setAdapter(feedAdapter);   //Set the data for this ListView

                feedListView.setOnItemClickListener(    //Creating these on the fly.
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BookFeed myFeed = (BookFeed)parent.getItemAtPosition(position);
                                gotClicked(myFeed);
                            }
                        }
                );
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    public void  searcgByAuthorName(final String TermToSearch) {
       // Toast.makeText(this, "AUTHOR NAME", Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("authors");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot dsauthor: dataSnapshot.getChildren())
                {
                    if(dsauthor.child("name").getValue().toString().toLowerCase().equals(TermToSearch.toLowerCase()) ||  dsauthor.child("name").getValue().toString().toLowerCase().indexOf(TermToSearch.toLowerCase()) != -1) {
                        fetchBookByAuthorId(dsauthor.getKey());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
    public void  searcgByCatName(final String TermToSearch) {
        //Toast.makeText(this, "Cat name", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            String name = TermToSearch;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int adapterLength=0;
                for(DataSnapshot dsl: dataSnapshot.getChildren())
                {
                    String categories = dsl.child("category").getValue().toString();
                    String[] separated = categories.split("\\[");
                    separated[1] = separated[1].substring(0, separated[1].length() - 1);
                    if (separated[1].toLowerCase().equals(name.toLowerCase()) || separated[1].toLowerCase().indexOf(name.toLowerCase()) != -1) {
                        //Toast.makeText(AllBooks.this, separated[1], Toast.LENGTH_SHORT).show();
                        adapterLength++;
                    }
                }
                String currentCountry = AllBooks.this.getResources().getConfiguration().locale.getCountry();

                String loc = "shipToUSA";

                if(currentCountry.equals("CA")) {
                    loc = "shipToCanada";
                }
                if (adapterLength == 0) adapterLength=1;
                BookFeed[] myFeeds = new BookFeed[adapterLength];
              //  Toast.makeText(AllBooks.this, String.valueOf(adapterLength), Toast.LENGTH_SHORT).show();
                int i=0;
                Boolean avail=false;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String category = ds.child("category").getValue().toString();


                    //Toast.makeText(AllBooks.this, category, Toast.LENGTH_SHORT).show();
                    String[] separatedCat = category.split("\\[");
                    separatedCat[1] = separatedCat[1].substring(0, separatedCat[1].length() - 1);
                    avail =  (Boolean) ds.child(loc).getValue();
//
                    if (avail) {
                        if (separatedCat[1].toLowerCase().equals(name.toLowerCase()) || separatedCat[1].toLowerCase().indexOf(name.toLowerCase()) != -1) {
                            myFeeds[i] = new BookFeed(ds.child("title").getValue().toString(), ds.child("cover").getValue().toString());

                            i = i + 1;

                        }
                    }
                }
                if (i==0) {
                    myFeeds[0] = new BookFeed("No Book found", "https://images-na.ssl-images k-amazon.com/images/I/51Rmbpdgh3L._SY200_.gif");
                }

                ListView feedListView = (ListView)findViewById(R.id.allbooks);
                BooksAdapter feedAdapter = new BooksAdapter(AllBooks.this, myFeeds);
                feedListView.setAdapter(feedAdapter);   //Set the data for this ListView

                feedListView.setOnItemClickListener(    //Creating these on the fly.
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BookFeed myFeed = (BookFeed)parent.getItemAtPosition(position);
                                gotClicked(myFeed);
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
