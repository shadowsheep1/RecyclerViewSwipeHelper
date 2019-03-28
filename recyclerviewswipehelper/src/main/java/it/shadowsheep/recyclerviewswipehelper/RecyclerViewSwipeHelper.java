package it.shadowsheep.recyclerviewswipehelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.shadowsheep.recyclerviewswipehelper.screen.util.Units;

public class RecyclerViewSwipeHelper extends ItemTouchHelper.SimpleCallback {

    private float swipeButtonWidth; // Button width in PX

    // RecyclerView
    private RecyclerView recyclerView;

    // Swipe Menu Buttons
    private List<SwipeButton> buttons;

    // Gesture detector to check which button has been clicked
    private GestureDetector gestureDetector;

    // Active swiped position
    private int swipedPos = -1;

    // Swipe threshold: higher values -> less sensibility
    private float swipeThreshold = 0.5f;

    // Already drawn buttons
    private SparseArray<List<SwipeButton>> buttonsBuffer;

    // Already open buttons to be swiped back
    private Queue<Integer> recoverQueue;

    // This is the delegate to know what to do according to datasource
    private RecyclerViewSwipeHelperDelegate delegate;

    /**
     * RecyclerViewSwipeHelperDelegate.
     *
     * <p><b>showButton</b>: tells the helper if it has to show the button
     * <ul>
     * <li>at index buttonIndex</li>
     * <li>at row rowPosition</li>
     * </ul></p>
     */
    public interface RecyclerViewSwipeHelperDelegate {
        boolean showButton(int rowPosition, int buttonIndex);

        @DimenRes int buttonWidth();

        void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                               List<SwipeButton> swipeButtons);
    }

    /**
     * This is the Swipe Helper Class.
     *
     * @param context      current context
     * @param recyclerView recycler view to be attached with
     * @param delegate     helper delegate
     */
    @SuppressLint("ClickableViewAccessibility")
    public RecyclerViewSwipeHelper(@NonNull Context context,
                                   @NonNull RecyclerView recyclerView,
                                   @NonNull RecyclerViewSwipeHelperDelegate delegate) {
        super(0, ItemTouchHelper.LEFT);

        this.delegate = delegate;

        swipeButtonWidth = getButtonWidth(context);

        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();

        // We check which button has been pressed
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector
                .SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                for (SwipeButton button : buttons) {
                    if (button.onClick(e.getX(), e.getY())) {
                        break;
                    }
                }
                return true;
            }
        };

        this.gestureDetector = new GestureDetector(context, gestureListener);

        // If we are not touching the current swiped item, we swipe the new one
        // and close the previous one.
        // Otherwise we pass through the event to the GestureDetector
        // in order to manage buttons click.
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if (swipedPos < 0) {
                    return false;
                }

                Point point = new Point((int) e.getRawX(), (int) e.getRawY());

                RecyclerView.ViewHolder swipedViewHolder
                        = RecyclerViewSwipeHelper.this
                        .recyclerView.findViewHolderForAdapterPosition(swipedPos);

                if (null == swipedViewHolder) {
                    return false;
                }

                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);

                if (e.getAction() == MotionEvent.ACTION_DOWN
                        || e.getAction() == MotionEvent.ACTION_UP
                        || e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y) {
                        gestureDetector.onTouchEvent(e);
                    } else {
                        recoverQueue.add(swipedPos);
                        swipedPos = -1;
                        recoverSwipedItem();
                    }
                }
                return false;
            }
        };

        this.recyclerView.setOnTouchListener(onTouchListener);
        buttonsBuffer = new SparseArray<>();
        recoverQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer o) {
                if (contains(o)) {
                    return false;
                } else {
                    return super.add(o);
                }
            }
        };
        attachSwipe();
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (swipedPos != pos) {
            recoverQueue.add(swipedPos);
        }

        // Here we save the current swiped position.
        // It's the first value write of mSwipePos.
        swipedPos = pos;

        if (null != buttonsBuffer.get(swipedPos)) {
            buttons = buttonsBuffer.get(swipedPos);
        } else {
            buttons.clear();
        }

        buttonsBuffer.clear();
        swipeThreshold = 0.5f * buttons.size() * swipeButtonWidth;
        recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float deltaX,
                            float deltaY,
                            int actionState,
                            boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = deltaX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            swipedPos = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (deltaX < 0) {
                List<SwipeButton> buffer = new ArrayList<>();

                if (null == buttonsBuffer.get(pos)) {
                    delegate.setupSwipeButtons(viewHolder, buffer);
                    hideHiddenButtons(pos, buffer);
                    buttonsBuffer.put(pos, buffer);
                } else {
                    if (null != buttonsBuffer.get(pos)) {
                        buffer = buttonsBuffer.get(pos);
                    }

                }

                assert buffer != null;

                translationX = deltaX * buffer.size() * swipeButtonWidth / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX, swipeButtonWidth);
            }
        }

        // We block the swipe to tranlationX. This is needed also to get the right button click
        super.onChildDraw(c, recyclerView, viewHolder, translationX, deltaY, actionState,
                isCurrentlyActive);
    }

    private float getButtonWidth(Context context) {
        if (null != delegate) {
            int buttonWidthResId = delegate.buttonWidth();
            if (0 == buttonWidthResId) {
                return Units.dp2px(context, 80.f);
            } else {
                return context.getResources().getDimension(buttonWidthResId);
            }
        } else {
            return Units.dp2px(context, 80.f);
        }
    }

    // https://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re
    // Avoid ConcurrentModificatinoException
    private void hideHiddenButtons(int rowPosition, List<SwipeButton> buffer) {
        int buttonIdx = -1;
        for (Iterator<SwipeButton> iterator = buffer.iterator(); iterator.hasNext(); ) {
            buttonIdx++;
            iterator.next(); // This must be called to avoid IllegalStateException
            // Note: Call here and not on for loop first line.
            if (null != delegate
                    && !delegate.showButton(rowPosition, buttonIdx)) {
                iterator.remove();
            }
        }
    }

    // We roll back the swipe
    private synchronized void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            int pos = recoverQueue.poll();
            if (pos > -1) {
                if (null != recyclerView.getAdapter()) {
                    recyclerView.getAdapter().notifyItemChanged(pos);
                }
            }
        }
    }

    private void drawButtons(@NonNull Canvas c,
                             @NonNull View itemView,
                             @NonNull List<SwipeButton> buffer,
                             int pos,
                             float deltaX,
                             float swipedButtonWidth) {
        float right = itemView.getRight();
        float buttonWidth = (-1) * deltaX / buffer.size();

        for (SwipeButton button : buffer) {
            float left = right - buttonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop(),
                            right,
                            itemView.getBottom()
                    ),
                    pos,
                    swipedButtonWidth
            );
            right = left;
        }
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public static class SwipeButton {
        private String text;
        private Drawable icon;
        private float iconSize;
        private float textSize;
        private int color;
        private int pos;
        private RectF clickRegion;
        private SwipeButtonClickListener clickListener;

        /**
         * SwipeButtonClickListener.
         */
        public interface SwipeButtonClickListener {
            void onClick(int pos);
        }

        /**
         * This is our SwipeButton class.
         *
         * @param context       current context
         * @param textResId     the resource string id for the button text
         * @param iconResId     the resource image id for the button icon
         * @param colorResId    the resource color id for the button color
         * @param clickListener the button click listener
         */
        public SwipeButton(@NonNull Context context,
                           @StringRes int textResId,
                           @DimenRes int textSizeId,
                           @DrawableRes int iconResId,
                           @DimenRes int iconSizeId,
                           @ColorRes int colorResId,
                           @NonNull SwipeButtonClickListener clickListener) {
            this.text = textResId != 0 ? context.getString(textResId) : "";
            this.icon = iconResId != 0 ? ContextCompat.getDrawable(context, iconResId) : null;
            this.iconSize = iconSizeId != 0 ? context.getResources().getDimension(iconSizeId) : 0;
            this.textSize = textSizeId != 0 ? context.getResources().getDimension(textSizeId)
                    : Units.dp2px(context, 14);
            this.color = ContextCompat.getColor(context, colorResId);
            this.clickListener = clickListener;
        }

        // We call our click callback only if we have a click in this button region
        boolean onClick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        void onDraw(Canvas c, RectF rect, int pos, float swipedButtonWidth) {
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);

            if (null != icon) {
                // draw icon
                int iconIntrinsicWidth = icon.getIntrinsicWidth();
                int iconIntrinsicHeight = icon.getIntrinsicHeight();

                if (0 != iconSize) {
                    iconIntrinsicWidth = (int) iconSize;
                    iconIntrinsicHeight = (int) iconSize;
                }


                int iconLeft = (int) rect.left;
                int iconRight = (int) rect.right;
                int iconTop = (int) rect.top;
                int iconBottom = (int) rect.bottom;

                if (iconRight - iconLeft > iconIntrinsicWidth) {
                    iconLeft = (int) (rect.left
                            + (rect.right - rect.left - iconIntrinsicWidth) / 2);
                    iconRight = iconLeft + iconIntrinsicWidth;
                }

                if (iconBottom - iconTop > iconIntrinsicHeight) {
                    iconTop = (int) (rect.top + (rect.bottom - rect.top - iconIntrinsicHeight) / 2);
                    iconBottom = iconTop + iconIntrinsicHeight;
                }

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(c);
            } else {
                float textSizeFactor = (rect.right - rect.left) / swipedButtonWidth;
                if (textSizeFactor > 1) {
                    textSizeFactor = 1;
                }

                // Draw Text
                p.setColor(Color.WHITE);
                p.setTextSize(this.textSize * textSizeFactor);
                p.setTextAlign(Paint.Align.LEFT);
                Rect r = new Rect(); // we populate rect values by exec the below line [-;
                p.getTextBounds(text, 0, text.length(), r);

                float rectHeight = rect.height();
                float rectWidth = rect.width();
                float x = rectWidth / 2f - r.width() / 2f - r.left;
                float y = rectHeight / 2f + r.height() / 2f - r.bottom;

                c.drawText(text, rect.left + x, rect.top + y, p);
            }

            clickRegion = rect;
            this.pos = pos;
        }
    }
}