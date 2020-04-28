package com.example.dbsave;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHandler db1;
    CustomAdapter customAdapter;
    List<Contact> list = new ArrayList<> ();
    private SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main2 );
        db1 = new DatabaseHandler ( this );
        recyclerView = findViewById ( R.id.recyclerView );
        swipeToRefresh = findViewById ( R.id.swipeToRefresh );
        list = db1.getcontact ();
        customAdapter = new CustomAdapter ( list, Main2Activity.this );
        swipeToRefresh.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                swipeToRefresh.setRefreshing ( false );
                list.clear ();
                list.addAll ( db1.getcontact () );
                customAdapter.notifyDataSetChanged ();
                Toast.makeText ( Main2Activity.this, "Contacts updated", Toast.LENGTH_LONG ).show ();
            }
        } );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
        recyclerView.setAdapter ( customAdapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.menu, menu );
        final MenuItem searchViewItem = menu.findItem ( R.id.app_bar_search );
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView ( searchViewItem );
        SearchManager searchManager = (SearchManager) getSystemService ( SEARCH_SERVICE );
        searchView.setSearchableInfo ( searchManager.getSearchableInfo ( getComponentName () ) );
        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus ();
                customAdapter.getFilter ().filter ( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter ().filter ( newText );
                return false;
            }
        } );

        searchView.setOnCloseListener ( new SearchView.OnCloseListener () {
            @Override
            public boolean onClose() {
                list.clear ();
                list.addAll ( db1.getcontact () );
                customAdapter.notifyDataSetChanged ();
                return false;
            }
        } );
        return super.onCreateOptionsMenu ( menu );
    }
}
