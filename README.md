# RecyclerViewSwipeHelper

[![Download](https://api.bintray.com/packages/shadowsheep/RecyclerViewSwipeHelper/RecyclerViewSwipeHelper/images/download.svg)](https://bintray.com/shadowsheep/RecyclerViewSwipeHelper/RecyclerViewSwipeHelper/_latestVersion)
[![Build Status](https://travis-ci.org/shadowsheep1/RecyclerViewSwipeHelper.svg?branch=master)](https://travis-ci.org/shadowsheep1/RecyclerViewSwipeHelper)
[![License](https://img.shields.io/static/v1.svg?label=License&message=Apache-2.0&color=blue)](https://shields.io)
[![API level compat](https://img.shields.io/badge/api-19+-yellogreen.svg)](https://shields.io)

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

                @DimenRes
                override fun buttonWidth(): Int {
                    return R.dimen.swipe_button_width
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

If you want a more fancy swipe back behavior you could make your `ViewHolder` extend `SwipeViewHolder`.

This is a very basic implementation still useful.
