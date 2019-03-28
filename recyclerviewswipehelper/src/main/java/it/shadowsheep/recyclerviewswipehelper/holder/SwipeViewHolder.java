package it.shadowsheep.recyclerviewswipehelper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class SwipeViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<Float> animationStep;
    private int stepIndex;
    private boolean isSwiped;

    /**
     * Default constructor.
     *
     * @param itemView itemView of view holder
     */

    public SwipeViewHolder(@NonNull View itemView) {
        super(itemView);
        animationStep = new SparseArray<>();
        isSwiped = false;
        stepIndex = 0;
    }

    public void setSwiped() {
        isSwiped = true;
    }

    public boolean isSwiped() {
        return isSwiped;
    }

    /**
     * Set a deltaX translation point.
     *
     * @param deltaX deltaX
     */
    public void setTranslationX(float deltaX) {
        animationStep.append(stepIndex++, deltaX);
    }

    /**
     * Get a deltaX translation point.
     *
     * @return deltaX
     */
    public float getTranslationX() {
        Float translationX = animationStep.get(--stepIndex);
        float deltaX = 0;
        if (null != translationX) {
            deltaX = translationX;
        }
        return deltaX;
    }

    /**
     * Clear view holder status.
     */
    public void clear() {
        animationStep.clear();
        isSwiped = false;
        stepIndex = 0;
    }
}
