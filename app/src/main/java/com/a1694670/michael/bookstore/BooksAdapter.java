package com.a1694670.michael.bookstore;

/**
 * Created by admin on 2017-10-12.
 */



        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

public class BooksAdapter extends ArrayAdapter<BookFeed> {

    public BooksAdapter(Context context, BookFeed[] objects) {
        super(context, R.layout.bookfeed, objects);
        //Context is the context we are in... For our example it will be within a listview.
        //Layout: we are going to make a layout with feed in mind.
        //Objects: This will be whatever else we need to pass to it.
    }

    //This fun
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflator = LayoutInflater.from(getContext());  //Instantiates a layout XML file into its corresponding object
        //In our test case it would our ListView object. Context will change where you use.
        //Think of it as prepare data.

        View customFeedView = myInflator.inflate(R.layout.bookfeed, parent, false);

        /***Think of the inflator part as in it converts the xml layout into a widget!*****/
        ////In our case, it is adding that feed to the listview

        //Get our widgets
        TextView myText = (TextView)customFeedView.findViewById(R.id.bookName);
        ImageView myImage = (ImageView)customFeedView.findViewById(R.id.bookimage);

        //Get our values
        String message = getItem(position).message; //GetItem gets the current item within the array.
        String pic = getItem(position).picture;        //As that item is a custom object we made.
        //We have access to message and picture.
        //Set our values
        myText.setText(message);
       //myImage.setImageResource(pic);
        new DownloadImageTask(myImage)
                .execute(pic);
        return customFeedView;  //Sending the view back, in this case as a row.
    }


}
