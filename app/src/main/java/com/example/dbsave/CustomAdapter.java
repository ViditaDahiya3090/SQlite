package com.example.dbsave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {
    private List<Contact> lst;
    private List<Contact> contacts;
    private Context mcx;
    private DatabaseHandler db1;

    CustomAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.lst = contacts;
        this.mcx = context;
        db1 = new DatabaseHandler ( context );
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder ( LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.recycler, parent, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView1.setText ( contacts.get ( i ).getName () );
        viewHolder.textView2.setText ( contacts.get ( i ).getPhoneNumber () );
        TextDrawable drawable1 = TextDrawable.builder ().buildRound ( String.valueOf ( contacts.get ( i ).getName ().toUpperCase ().charAt ( 0 ) ),getc ());
        viewHolder.im1.setImageDrawable ( drawable1 );

    }

    private int getc() {
        Random rnd = new Random ();
        return Color.argb ( 255, rnd.nextInt ( 256 ), rnd.nextInt ( 256 ), rnd.nextInt ( 256 ) );
    }

    @Override
    public int getItemCount() {
        return contacts.size ();
    }


    private void popup(final int i, final View itemview) {
        final PopupMenu popupMenu = new PopupMenu ( itemview.getContext (), itemview );
        popupMenu.getMenuInflater ().inflate ( R.menu.popup_menu, popupMenu.getMenu () );
        popupMenu.setOnMenuItemClickListener ( new PopupMenu.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.editp:
                        Intent intent = new Intent ( itemview.getContext (), MainActivity.class );
                        mcx.startActivity ( intent );
                    case R.id.deletep:
                        db1.delete ( contacts.get ( i ).get_id () );
                        contacts.remove ( contacts.get ( i ) );
                        Log.e ( "onmenuclick", "onMenuItemClick:12 " + contacts.get ( i ) );
                }
                return true;
            }
        } );
        popupMenu.show ();
    }

    @Override
    public Filter getFilter() {
        return new Filter () {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charS = constraint.toString ();
                lst.clear ();
                if (charS.isEmpty ()) {
                    lst.addAll ( db1.getcontact () );
                    Log.e ( "FilterResults", "performFiltering: char empty" + lst + " | " + charS );
                } else {
                    for (Contact row : db1.getcontact ()) {
                        if (row.getName ().toLowerCase ().contains ( charS.toLowerCase () ) || row.getPhoneNumber ().contains ( constraint )) {
                            lst.add ( row );
                            Log.e ( "FilterResults", "performFiltering: else  name  = " + row.getName () );
                        }
                    }
                }
                FilterResults filterResults = new FilterResults ();
                filterResults.values = lst;
                Log.e ( "FilterResults", "performFiltering: else  name  = " + filterResults.values.toString () );
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.values != null) {
                    contacts = (ArrayList<Contact>) results.values;
                }
                notifyDataSetChanged ();
            }
        };
    }

    private void mcall(final int i) {
        try {
            Intent callIntent = new Intent ( Intent.ACTION_CALL );//Uri.fromParts("tel",  contacts.get ( i ).getPhoneNumber (), null));
            //  callIntent.setPackage("com.android.phone");
            callIntent.setData ( Uri.parse ( "tel:" + contacts.get ( i ).getPhoneNumber () ) );
            Log.e ( "mcall", "mcall: " + contacts.get ( i ).getPhoneNumber () );
            mcx.startActivity ( callIntent );

        } catch (Exception e) {
            Log.e ( "mcall", "mcall: exceptions " + e.getMessage () );
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView im1;
        RelativeLayout relativeLayout;

        ViewHolder(final View itemView) {
            super ( itemView );

            textView1 = itemView.findViewById ( R.id.namer );
            textView2 = itemView.findViewById ( R.id.phoner );
            im1 = itemView.findViewById ( R.id.image_view );


            textView2.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    mcall ( getAdapterPosition () );
                }
            } );
            relativeLayout = itemView.findViewById ( R.id.relative );
            itemView.setOnLongClickListener ( new View.OnLongClickListener () {
                @Override
                public boolean onLongClick(View v) {
                    popup ( getAdapterPosition (), itemView );
                    return true;
                }
            } );
        }
    }
}
