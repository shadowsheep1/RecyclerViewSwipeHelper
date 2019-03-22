package it.shadowsheep.recyclerviewswipehelperdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;
import it.shadowsheep.recyclerviewswipehelper.screen.util.Units;
import it.shadowsheep.recyclerviewswipehelperdemo.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate {

    private static final String TAG = MainActivity.class.getName();

    private RecyclerView recyclerView;

    private String[] myDataset = {"Foo", "Bar", "Beer", "Bear", "Geek", "Wizard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter myAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(myAdapter);

        setupSwipeMenu();
    }

    private void setupSwipeMenu() {
        new RecyclerViewSwipeHelper(this, recyclerView, this);
    }

    @Override
    public boolean showButton(int rowPosition, int buttonIndex) {
        if (0 == rowPosition % 2) {
            if (1 == buttonIndex) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public float buttonWidth() {
        return Units.dp2px(this, 80.f);
    }

    @Override
    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                R.string.delete,
                //*
                R.drawable.ic_delete_24dp,
                /*/
                0,
                //*/
                /*
                0,
                /*/
                R.dimen.ic_delete_size,
                //*/
                R.color.red,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Log.d(TAG, "pos: " + pos);
                    }
                }
        ));

        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                R.string.edit,
                //*
                R.drawable.ic_edit_24dp,
                /*/
                0,
                //*/
                /*
                0,
                /*/
                R.dimen.ic_delete_size,
                //*/
                R.color.green,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Log.d(TAG, "pos: " + pos);
                    }
                }
        ));
    }
}
