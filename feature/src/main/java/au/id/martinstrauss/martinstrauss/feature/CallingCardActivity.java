package au.id.martinstrauss.martinstrauss.feature;

import android.annotation.SuppressLint;
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
  private final Handler mHideHandler = new Handler();
  private View mContentView;
  private View mContactDetailsView;
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

    mContentView = findViewById(R.id.fullscreen_content);

    mContactDetailsView = findViewById(R.id.contact_details);

    addContactMethod(R.drawable.email, R.string.email);
    addContactMethod(R.drawable.phone, R.string.phone);
    addContactMethod(R.drawable.facebook, R.string.facebook);
    addContactMethod(R.drawable.youtube, R.string.youtube);
    addContactMethod(R.drawable.twitter, R.string.twitter);
    addContactMethod(R.drawable.linkedin, R.string.linkedin);
  }

  private void addContactMethod(int imageResource, int textResource) {
    LayoutInflater inflater = LayoutInflater.from(this);
    View contactMethodView = inflater.inflate(R.layout.contact_method, null, false);

    ImageView icon = (ImageView) contactMethodView.findViewById(R.id.icon);
    icon.setImageResource(imageResource);

    TextView text = (TextView) contactMethodView.findViewById(R.id.text);
    text.setText(textResource);
    text.setMovementMethod(LinkMovementMethod.getInstance());

    ViewGroup contactDetailsViewGroup = (ViewGroup) mContactDetailsView;
    int heightInPixels = (int) (36 * getResources().getDisplayMetrics().scaledDensity);

    contactDetailsViewGroup.addView(contactMethodView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, heightInPixels));
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
}
