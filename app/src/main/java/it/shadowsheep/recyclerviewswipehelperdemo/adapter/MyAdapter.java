package it.shadowsheep.recyclerviewswipehelperdemo.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import it.shadowsheep.recyclerviewswipehelperdemo.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;
        View parentView;

        MyViewHolder(ConstraintLayout parent, TextView tv) {
            super(parent);
            textView = tv;
            parentView = parent;

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "Main item clicked!",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_recycler_view_item, parent, false);

        TextView tv = v.findViewById(R.id.textView);
        return new MyViewHolder(v, tv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(position + ". " + dataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
