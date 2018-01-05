package au.id.martinstrauss.martinstrauss.feature;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CallingCardActivity extends AppCompatActivity {

  /**
   * The number of milliseconds to wait after user interaction before hiding the system UI.
   */
  private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

  /**
   * Some older devices needs a small delay between UI widget updates
   * and a change of the status and navigation bar.
   */
  private static final int UI_ANIMATION_DELAY = 300;

  /**
   * Whether or not we're showing the back of the card (otherwise showing the front).
   */
  private boolean mShowingBack = false;

  private final Handler mHideHandler = new Handler();
  private View mContentView;
  private final Runnable mHidePart2Runnable = new Runnable() {
    @SuppressLint("InlinedApi")
    @Override
    public void run() {
      // Delayed removal of status and navigation bar

      // Note that some of these constants are new as of API 16 (Jelly Bean)
      // and API 19 (KitKat). It is safe to use them, as they are inlined
      // at compile-time and do nothing on earlier devices.
      mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
          | View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
  };
  private final Runnable mHideRunnable = new Runnable() {
    @Override
    public void run() {
      hide();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_calling_card);

    mContentView = findViewById(R.id.container);

    if (savedInstanceState == null) {
      getFragmentManager()
          .beginTransaction()
          .add(R.id.container, new CardFrontFragment())
          .commit();
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Trigger the initial hide() shortly after the activity has been
    // created, to briefly hint to the user that UI controls
    // are available.
    delayedHide(100);
  }

  private void hide() {
    // Hide UI first
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }

    // Schedule a runnable to remove the status and navigation bar after a delay
    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
  }

  /**
   * Schedules a call to hide() in delay milliseconds, canceling any
   * previously scheduled calls.
   */
  private void delayedHide(int delayMillis) {
    mHideHandler.removeCallbacks(mHideRunnable);
    mHideHandler.postDelayed(mHideRunnable, delayMillis);
    mHideHandler.postDelayed(mHideRunnable, delayMillis);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    flipCard();
    return super.onTouchEvent(event);
  }

  private void flipCard() {
    if (mShowingBack) {
      getFragmentManager().popBackStack();
      return;
    }

    mShowingBack = true;

    getFragmentManager()
        .beginTransaction()
        .setCustomAnimations(
            R.animator.card_flip_right_in,
            R.animator.card_flip_right_out,
            R.animator.card_flip_left_in,
            R.animator.card_flip_left_out)
        .replace(R.id.container, new CardBackFragment())
        .addToBackStack(null)
        .commit();
  }

  /**
   * A fragment representing the front of the card.
   */
  public static class CardFrontFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_card_front, container, false);
    }
  }

  /**
   * A fragment representing the back of the card.
   */
  public static class CardBackFragment extends Fragment {
    private View mCardBackView;
    private View mContactDetailsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      mCardBackView = inflater.inflate(R.layout.fragment_card_back, container, false);

      mContactDetailsView = mCardBackView.findViewById(R.id.contact_details);

      addContactMethod(inflater, R.drawable.email, R.string.email);
      addContactMethod(inflater, R.drawable.phone, R.string.phone);
      addContactMethod(inflater, R.drawable.facebook, R.string.facebook);
      addContactMethod(inflater, R.drawable.youtube, R.string.youtube);
      addContactMethod(inflater, R.drawable.twitter, R.string.twitter);
      addContactMethod(inflater, R.drawable.linkedin, R.string.linkedin);

      return mCardBackView;
    }

    private void addContactMethod(LayoutInflater inflater, int imageResource, int textResource) {
      View contactMethodView = inflater.inflate(R.layout.contact_method, (ViewGroup) mCardBackView,false);

      ImageView icon = (ImageView) contactMethodView.findViewById(R.id.icon);
      icon.setImageResource(imageResource);

      TextView text = (TextView) contactMethodView.findViewById(R.id.text);
      text.setText(textResource);
      text.setMovementMethod(LinkMovementMethod.getInstance());

      ViewGroup contactDetailsViewGroup = (ViewGroup) mContactDetailsView;
      int heightInPixels = (int) (36 * getResources().getDisplayMetrics().scaledDensity);

      contactDetailsViewGroup.addView(contactMethodView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, heightInPixels));
    }  }
}
