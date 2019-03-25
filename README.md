# RecyclerViewSwipeHelper

[ ![Download](https://api.bintray.com/packages/shadowsheep/RecyclerViewSwipeHelper/RecyclerViewSwipeHelper/images/download.svg) ](https://bintray.com/shadowsheep/RecyclerViewSwipeHelper/RecyclerViewSwipeHelper/_latestVersion)
[![Build Status](https://travis-ci.org/shadowsheep1/RecyclerViewSwipeHelper.svg?branch=master)](https://travis-ci.org/shadowsheep1/RecyclerViewSwipeHelper)

**RecyclerViewSwipeHelper**

This helper class could be used to add *Swipe Action Buttons* to a `RecyclerView` by extending `ItemTouchHelper.SimpleCallback`.

![RecyclerViewSwipeHelper](http://www.shadowsheep.it/github/recyclerviewswipehelper_1.1.0-rc1.gif)

Add library to your `build.gradle` file

```
implementation 'it.shadowsheep.recyclerviewswipehelper:recyclerviewswipehelper:{latest_version}'
```

Add the helper to your recycler view

**JAVA**

```java
    private void setupSwipeMenu() {
        new RecyclerViewSwipeHelper(this, recyclerView, this);
    }
```

And then implement delegate methods

```java
    @Override
    public boolean showButton(int rowPosition, int buttonIndex) {
        if (0 == rowPosition % 2) {
            return 1 != buttonIndex;
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
                    }
                }
        ));
    }
```

Or provide the interface directly on constructor

**KOTLIN**

```kotlin
    private fun setupSwipeMenu() {
        RecyclerViewSwipeHelper(
            baseContext,
            recyclerView,
            object : RecyclerViewSwipeHelperDelegate {
                override fun showButton(rowPosition: Int, buttonIndex: Int): Boolean {
                    return if (0 == rowPosition % 2) {
                        1 != buttonIndex
                    } else {
                        true
                    }
                }

                override fun buttonWidth(): Float {
                    return Units.dp2px(baseContext, 80f)
                }

                override fun setupSwipeButtons(
                    viewHolder: RecyclerView.ViewHolder,
                    swipeButtons: MutableList<RecyclerViewSwipeHelper.SwipeButton>
                ) {
                    swipeButtons.add(RecyclerViewSwipeHelper.SwipeButton(
                        baseContext,
                        0,
                        0,
                        R.drawable.ic_delete_24dp,
                        R.dimen.ic_delete_size,
                        R.color.colorAccent,
                        RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener { pos ->
                            Log.d(
                                TAG,
                                "pos: $pos"
                            )
                        }
                    ))

                    swipeButtons.add(RecyclerViewSwipeHelper.SwipeButton(
                        baseContext,
                        0,
                        0,
                        R.drawable.ic_edit_24dp,
                        R.dimen.ic_delete_size,
                        R.color.colorPrimary,
                        RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener { pos ->
                            Log.d(
                                TAG,
                                "pos: $pos"
                            )
                        }
                    ))

                    swipeButtons.add(RecyclerViewSwipeHelper.SwipeButton(
                        baseContext,
                        R.string.yay,
                        R.dimen.swipe_button_text_size,
                        0,
                        0,
                        R.color.colorPrimary,
                        RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener { pos ->
                            Log.d(
                                TAG,
                                "Yay! pos: $pos"
                            )
                        }
                    ))
                }
            }
        )
    }
```

This is a vary basic implementation still useful.
