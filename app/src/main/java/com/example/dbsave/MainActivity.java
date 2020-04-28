package com.example.dbsave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler db1;
    private EditText name, mobile;
    private String Name, Mobile;
    private Boolean empty;
    private Button save,get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        db1  = new DatabaseHandler ( this );
        name = findViewById ( R.id.name );
        mobile = findViewById ( R.id.mobile );
        save = findViewById ( R.id.save );
        get = findViewById ( R.id.get );
        save.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                add ();
            }
        } );
        get.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                startActivity(myIntent);
            }
        } );
    }

    public void add() {
        Name = name.getText ().toString ();
        Log.e ( "Add", "add: " + Name );
        Mobile = mobile.getText ().toString ();
        Log.e ( "Add", "add: " + Mobile );
        checkempty ( Name, Mobile );
        if (empty) {
            Log.e ( "Add", "add: " + empty );
            db1.addContacts ( new Contact ( Name, Mobile ) );
            Toast.makeText ( MainActivity.this, "data saved into DB", Toast.LENGTH_LONG ).show ();
            clearaftersave ();
        }
    }

    public void checkempty(String name, String mobile) {
        if (TextUtils.isEmpty ( name ) || TextUtils.isEmpty ( mobile )) {
            empty = false;
            Toast.makeText ( MainActivity.this, "Enter data to store ", Toast.LENGTH_LONG ).show ();
        } else {
            empty = true;
        }
    }

    public void clearaftersave() {
        name.getText ().clear ();
        mobile.getText ().clear ();
    }
}
