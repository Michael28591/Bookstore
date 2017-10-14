package com.a1694670.michael.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleBook extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            Intent i = getIntent();

            String name = i.getStringExtra("BookName");
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView SingleBookName = (TextView) findViewById(R.id.SingleBookName);


                TextView categories = (TextView) findViewById(R.id.category);
                TextView date =(TextView) findViewById(R.id.date);
                TextView publisher = (TextView) findViewById(R.id.publisher);
                TextView quantity = (TextView) findViewById(R.id.quantity);
                ImageView img = (ImageView) findViewById(R.id.SingleBookImage);
                Button btn = (Button) findViewById(R.id.RentBtn);
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //Toast.makeText(SingleBook.this, "test", Toast.LENGTH_SHORT).show();
                    String title=ds.child("title").getValue().toString();
                    if (name.equals(title)) {

                        String srcImg = ds.child("cover").getValue().toString();
                        SingleBookName.setText(title);

                        final String id =  ds.child("author_id").getValue().toString();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("authors");
                        myRef.addValueEventListener(new ValueEventListener() {

                            TextView author = (TextView) findViewById(R.id.authorName);
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                for(DataSnapshot dsauthor: dataSnapshot.getChildren())
                                {
                                      //  Toast.makeText(SingleBook.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(SingleBook.this,dsauthor.getKey(), Toast.LENGTH_SHORT).show();
                                    //int authorid=(int) dsauthor.getKey();
                                    if (dsauthor.getKey().equals(id)) {
                                        author.setText(dsauthor.child("name").getValue().toString());
                                       // Toast.makeText(SingleBook.this, dsauthor.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(AllBooks.this,ds.child("title").getValue().toString(),Toast.LENGTH_LONG).show();
                                    // Log.d("Titles",ds.child("title").getValue().toString());
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                              //  Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                        categories.setText(ds.child("category").getValue().toString());
                        date.setText(ds.child("date").getValue().toString());
                        publisher.setText(ds.child("publisher").getValue().toString());
                        //Toast.makeText(SingleBook.this, ds.child("date").getValue().toString(), Toast.LENGTH_SHORT).show();
                        quantity.setText(ds.child("quantity").getValue().toString());

                        new DownloadImageTask(img)
                                .execute(srcImg);

                        Long quantitya = (Long) ds.child("quantity").getValue();
                        if (quantitya > 0 ) {
                            btn.setVisibility(View.VISIBLE);
                        }
                   }
                   // Toast.makeText(SingleBook.this,ds.child("title").getValue().toString(),Toast.LENGTH_LONG).show();
                    // Log.d("Titles",ds.child("title").getValue().toString());
                }
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
    public void rent(View V) {

        Toast.makeText(this, "Book Rented", Toast.LENGTH_SHORT).show();


    }
}
