package it.shadowsheep.recyclerviewswipehelperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;
import it.shadowsheep.recyclerviewswipehelperdemo.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate {

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
            return 1 != buttonIndex;
        } else {
            return true;
        }
    }

    @Override
    public @DimenRes
    int buttonWidth() {
        return R.dimen.swipe_button_width;
    }

    @Override
    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_delete_24dp,
                R.dimen.ic_delete_size,
                R.color.colorAccent,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Log.d(TAG, "pos: " + pos);
                    }
                }
        ));

        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_edit_24dp,
                R.dimen.ic_delete_size,
                R.color.colorPrimary,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Log.d(TAG, "pos: " + pos);
                    }
                }
        ));

        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                R.string.yay,
                R.dimen.swipe_button_text_size,
                0,
                0,
                R.color.colorPrimaryDark,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Log.d(TAG, "pos: " + pos);
                        Intent i = new Intent(getBaseContext(), Main2Activity.class);
                        startActivity(i);
                    }
                }
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
