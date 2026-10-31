package com.daaw.avee;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.daaw.avee.vendor.legacy.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager.widget.ViewPager;
import com.daaw.avee.Common.Action;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Boast;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Events.WeakEventR2;
import com.daaw.avee.Common.Events.WeakEventR3;
import com.daaw.avee.Common.SystemUiHider;
import com.daaw.avee.Common.SystemUiHiderHoneycomb;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.EventsGlobal.EventsGlobalApp;
import com.daaw.avee.EventsGlobal.EventsGlobalTextNotifier;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest;
import com.daaw.avee.comp.AlbumArt.ImageLoadedListener;
import com.daaw.avee.comp.AlbumArt.ImageResult;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.ISearchEntry;
import com.daaw.avee.comp.LibraryQueueUI.Fragment0;
import com.daaw.avee.comp.LibraryQueueUI.Fragment1;
import com.daaw.avee.comp.SleepTimer.SleepTimerConfig;
import com.daaw.avee.comp.VisualUI.AspectRatioHelper;
import com.daaw.avee.comp.VisualUI.Fragment2;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.material.navigation.NavigationView;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends BaseActivity implements DialogInterface.OnClickListener {
    public static final int LIBRARY_PAGE_INDEX = 0;
    private static final int MSG_HIDE = 2;
    private static final int MSG_TICK1 = 4;
    private static final int MSG_TICK10 = 3;
    private static final String PREF_KEY_COLD_START_SHOWN = "cold_start_welcome_shown";
    public static final int QUEUE_PAGE_INDEX = 1;
    public static final int VISUAL_PAGE_INDEX = 2;
    private DrawerLayout drawerLayout;
    private Handler handler;
    PlayerCore playerCore;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private Toast slowClosingToast;
    private SwitchCompat switchLockOrient;
    private SwitchCompat switchSleepTimer;
    private SystemUiHiderHoneycomb systemUiHider;
    private Toolbar toolbar;
    private CustomViewPager viewPager;
    ValueAnimator vizMenuButtonAnimator;
    public static WeakEvent1<Activity> onCreate = new WeakEvent1<>();
    public static WeakEvent1<Activity> onStart = new WeakEvent1<>();
    public static WeakEvent1<Activity> onResume = new WeakEvent1<>();
    public static WeakEvent1<Activity> onPause = new WeakEvent1<>();
    public static WeakEvent1<Activity> onStop = new WeakEvent1<>();
    public static WeakEvent1<ContextData> onDestroy = new WeakEvent1<>();
    public static WeakEvent onExit = new WeakEvent();
    public static WeakEvent2<Integer, Activity> onViewPagerPageSelected = new WeakEvent2<>();
    public static WeakEvent1<Context> onCreateEarly = new WeakEvent1<>();
    public static WeakEvent2<Integer, ContextData> onMainUIAction = new WeakEvent2<>();
    public static WeakEvent4<AlbumArtRequest, ImageLoadedListener, Integer, Integer> onRequestAlbumArtLarge = new WeakEvent4<>();
    public static WeakEventR<SleepTimerConfig> onMainUIRequestSleepTimerConfig = new WeakEventR<>();
    public static WeakEventR<Boolean> onRequestLockOrientState = new WeakEventR<>();
    public static WeakEvent2<Integer, String> onUISearchQueryTextChange = new WeakEvent2<>();
    public static WeakEvent1<Boolean> onUISearchQueryStateChange = new WeakEvent1<>();
    public static WeakEvent1<Integer> onSetCurrentSearchIndex = new WeakEvent1<>();
    public static WeakEventR<ISearchEntry> onRequestCurrentSearchEntry = new WeakEventR<>();
    public static WeakEvent3<View, View, View> onCreateView = new WeakEvent3<>();
    public static WeakEvent2<List<PlaylistSong>, Integer> onPreviewOpen = new WeakEvent2<>();
    public static WeakEvent1<Boolean> onFullscreenChanged = new WeakEvent1<>();
    public static WeakEvent1<Context> onViewPagerSwipeOutAtStart = new WeakEvent1<>();
    public static WeakEvent2<Float, Context> onViewPagerSwipeProgressUpdate = new WeakEvent2<>();
    public static WeakEvent2<Activity, Integer> onRequestPermissionsResult = new WeakEvent2<>();
    public static WeakEventR3<Integer, Integer, Intent, Boolean> onActivityResult0 = new WeakEventR3<>();
    public static WeakEvent2<Integer, Intent> onActivityResult = new WeakEvent2<>();
    public static WeakEventR<PlaylistSong.Data> onRequestTrackInfo = new WeakEventR<>();
    public static WeakEventR<Boolean> onRequestShouldShowControls = new WeakEventR<>();
    public static WeakEventR1<Activity, Boolean> onBackPressed = new WeakEventR1<>();
    public static WeakEventR2<ContextData, Intent, Boolean> onLaunchIntent = new WeakEventR2<>();
    public static WeakEventR2<ContextData, Integer, Boolean> onToolbarAction = new WeakEventR2<>();
    static volatile WeakReference<MainActivity> instanceWeak = new WeakReference<>(null);
    public int currentFragmentPage = -1;
    private boolean slowClosingInProgress = false;
    private Timer slowClosingTimer = null;
    private MenuItem searchMenuItem = null;
    private MenuItem sleepTimerIndicatorMenuItem = null;
    private MenuItem lockOrientIndicatorMenuItem = null;
    private MenuItem vizBtn0MenuItem = null;
    private MenuItem vizBtn1MenuItem = null;
    private MenuItem vizBtn2MenuItem = null;
    private MenuItem vizBtnAspectRatio = null;
    private int currentVizSelection = -1;
    private List<Object> listenerReferenceHolder = new LinkedList();
    private PlaylistSong.Data assignedSongData = PlaylistSong.emptyData;
    private boolean isVisualizerImmersive = false;

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    public MainActivity() {
        EventsGlobalTextNotifier.onTextMsg.subscribeWeak(new WeakEvent1.Handler<String>() { // from class: com.daaw.avee.MainActivity.1
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(final String str) {
                if (UtilsUI.IsUiThread()) {
                    if (!MainActivity.this.isFinishing()) {
                        Boast.makeText(MainActivity.this, str, 1).show();
                        return;
                    }
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() { // from class: com.daaw.avee.MainActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!MainActivity.this.isFinishing()) {
                            Boast.makeText(MainActivity.this, str, 1).show();
                        }
                    }
                });
            }
        }, this.listenerReferenceHolder);
        this.handler = new Handler(new Handler.Callback() { // from class: com.daaw.avee.MainActivity.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i != 2) {
                    if (i == 3) {
                        EventsGlobalApp.onUITick10.invoke();
                        MainActivity.this.handler.removeMessages(3);
                        MainActivity.this.handler.sendMessageDelayed(MainActivity.this.handler.obtainMessage(3), 10000L);
                    } else if (i == 4) {
                        EventsGlobalApp.onUITick1.invoke();
                        MainActivity.this.handler.removeMessages(4);
                        MainActivity.this.handler.sendMessageDelayed(MainActivity.this.handler.obtainMessage(4), 3000L);
                    }
                } else if (MainActivity.this.currentFragmentPage == 2 && AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_pref_visControlsTimeout)) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.showControls(false, mainActivity.currentFragmentPage);
                }
                return false;
            }
        });
    }

    public static MainActivity getInstance() {
        return instanceWeak.get();
    }

    public static Fragment0 getFragment0Instance() {
        MainActivity mainActivity = instanceWeak.get();
        if (mainActivity == null) {
            return null;
        }
        return (Fragment0) mainActivity.findFragmentByPosition(0);
    }

    public static Fragment1 getFragment1Instance() {
        MainActivity mainActivity = instanceWeak.get();
        if (mainActivity == null) {
            return null;
        }
        return (Fragment1) mainActivity.findFragmentByPosition(1);
    }

    public static Fragment2 getFragment2Instance() {
        MainActivity mainActivity = instanceWeak.get();
        if (mainActivity == null) {
            return null;
        }
        return (Fragment2) mainActivity.findFragmentByPosition(2);
    }

    private Toolbar getToolBar() {
        return this.toolbar;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        if (onLaunchIntent.invoke(new ContextData(this), intent, false).booleanValue()) {
            this.viewPager.setCurrentItem(2);
            onViewPagerPageSelected(2);
        }
        super.onNewIntent(intent);
    }

    @Override // com.daaw.avee.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        instanceWeak = new WeakReference<>(this);
        AppPermissions.isPermissionsGranted(getApplicationContext(), this);
        this.playerCore = PlayerCore.s();
        this.sectionsPagerAdapter = null;
        this.viewPager = null;
        try {
            PreferenceManager.setDefaultValues(getApplicationContext(), re.ex.avee.R.xml.preferences, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreateEarly.invoke(getApplicationContext());
        onCreate.invoke(this);
        setContentView(re.ex.avee.R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(re.ex.avee.R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        UtilsUI.setStatusBarDimensions(findViewById(re.ex.avee.R.id.viewStatusBarBg));
        this.drawerLayout = (DrawerLayout) findViewById(re.ex.avee.R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(re.ex.avee.R.id.nav_view);
        navigationView.setBackgroundColor(UtilsUI.getAttrColorWithAlphaRes(getTheme(), this, re.ex.avee.R.attr.containerBackground, re.ex.avee.R.color.white_alpha_1));
        ((TextView) navigationView.getHeaderView(0).findViewById(re.ex.avee.R.id.nav_version_name)).setText(Utils.getAppVersionName());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { // from class: com.daaw.avee.MainActivity.3
            @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (MainActivity.this.onMenuItemSelected(menuItem)) {
                    MainActivity.this.drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
        this.switchSleepTimer = (SwitchCompat) navigationView.getMenu().findItem(re.ex.avee.R.id.menu_sleep_timer).getActionView().findViewById(re.ex.avee.R.id.drawer_switch);
        this.switchLockOrient = (SwitchCompat) navigationView.getMenu().findItem(re.ex.avee.R.id.menu_lock_orient).getActionView().findViewById(re.ex.avee.R.id.drawer_switch);
        this.switchSleepTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.MainActivity.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.onMainUIAction.invoke(1, new ContextData(MainActivity.this));
                } else {
                    MainActivity.onMainUIAction.invoke(10, new ContextData(MainActivity.this));
                }
            }
        });
        this.switchLockOrient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.MainActivity.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MainActivity.onMainUIAction.invoke(2, new ContextData(MainActivity.this));
            }
        });
        onCreateView.invoke(findViewById(re.ex.avee.R.id.layoutMediaControls), findViewById(re.ex.avee.R.id.layoutMediaControlsNarrow), findViewById(re.ex.avee.R.id.layoutMediaControlsContainer));
        this.sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        CustomViewPager customViewPager = (CustomViewPager) findViewById(re.ex.avee.R.id.viewPager);
        this.viewPager = customViewPager;
        customViewPager.setAdapter(this.sectionsPagerAdapter);
        this.viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() { // from class: com.daaw.avee.MainActivity.6
            @Override // com.daaw.avee.CustomViewPager.OnSwipeOutListener
            public void onSwipeOutAtEnd() {
            }

            @Override // com.daaw.avee.CustomViewPager.OnSwipeOutListener
            public void onSwipeOutAtStart() {
                MainActivity.onViewPagerSwipeOutAtStart.invoke(MainActivity.this.getApplicationContext());
            }

            @Override // com.daaw.avee.CustomViewPager.OnSwipeOutListener
            public void onSwipeProgressUpdate(float f) {
                MainActivity.onViewPagerSwipeProgressUpdate.invoke(Float.valueOf(f), MainActivity.this.getApplicationContext());
            }
        });
        this.systemUiHider = null;
        SystemUiHiderHoneycomb systemUiHiderHoneycomb = new SystemUiHiderHoneycomb(this.viewPager, 2);
        this.systemUiHider = systemUiHiderHoneycomb;
        systemUiHiderHoneycomb.setup();
        this.systemUiHider.show();
        this.systemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() { // from class: com.daaw.avee.MainActivity.7
            @Override // com.daaw.avee.Common.SystemUiHider.OnVisibilityChangeListener
            public void onVisibilityChange(boolean z) {
                if (!z) {
                    return;
                }
                MainActivity.this.showControls(MainActivity.onRequestShouldShowControls.invoke(true).booleanValue(), MainActivity.this.currentFragmentPage, true);
            }
        });
        if (onLaunchIntent.invoke(new ContextData(this), getIntent(), false).booleanValue()) {
            this.viewPager.setCurrentItem(2);
            onViewPagerPageSelected(2);
        } else {
            int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_mainPageIndex);
            this.viewPager.setCurrentItem(i);
            onViewPagerPageSelected(i);
        }
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.daaw.avee.MainActivity.8
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                MainActivity.this.onViewPagerPageSelected(i2);
            }
        });
        Message messageObtainMessage = this.handler.obtainMessage(3);
        this.handler.removeMessages(3);
        this.handler.sendMessageDelayed(messageObtainMessage, 10000L);
        Message messageObtainMessage2 = this.handler.obtainMessage(4);
        this.handler.removeMessages(4);
        this.handler.sendMessageDelayed(messageObtainMessage2, 3000L);
    }

    private void detectColdStartAndShowWelcome() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREF_KEY_COLD_START_SHOWN, true)) {
            new Handler().postDelayed(new Runnable() { // from class: com.daaw.avee.MainActivity.9
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity.this.showCustomHtmlDialog();
                }
            }, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCustomHtmlDialog() {
        String str;
        try {
            InputStream inputStreamOpenRawResource = getResources().openRawResource(re.ex.avee.R.raw.whatznew);
            byte[] bArr = new byte[inputStreamOpenRawResource.available()];
            inputStreamOpenRawResource.read(bArr);
            inputStreamOpenRawResource.close();
            str = new String(bArr, "UTF-8");
        } catch (Exception e) {
            str = "<html><body><p style='text-align:center;padding:20px;'>加载失败，请稍后重试</p></body></html>";
        }
        final WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(webView);
        builder.setPositiveButton(getString(re.ex.avee.R.string.dialog_ok), new DialogInterface.OnClickListener() { // from class: com.daaw.avee.MainActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getString(re.ex.avee.R.string.do_not_show_again), (DialogInterface.OnClickListener) null);
        builder.setCancelable(true);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.daaw.avee.MainActivity.11
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialogCreate.getButton(-1);
                final Button button2 = alertDialogCreate.getButton(-2);
                if (button2 != null) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button2.getLayoutParams();
                    layoutParams.gravity = GravityCompat.START;
                    layoutParams.leftMargin = 20;
                    button2.setLayoutParams(layoutParams);
                    button2.setEnabled(false);
                    button2.setAlpha(0.4f);
                }
                if (button != null) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) button.getLayoutParams();
                    layoutParams2.gravity = GravityCompat.END;
                    layoutParams2.rightMargin = 20;
                    button.setLayoutParams(layoutParams2);
                }
                final boolean[] zArr = new boolean[1];
                final boolean[] zArr2 = new boolean[1];
                final long[] jArr = {System.currentTimeMillis()};
                WebView webView2 = webView;
                final WebView webView3 = webView;
                webView2.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.daaw.avee.MainActivity.11.1
                    @Override // android.view.View.OnScrollChangeListener
                    public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                        int contentHeight = webView3.getContentHeight();
                        int height = webView3.getHeight();
                        int scrollY = webView3.getScrollY();
                        if (contentHeight > 0 && height > 0 && scrollY >= (contentHeight - height) - 5) {
                            zArr[0] = true;
                        } else {
                            zArr[0] = false;
                        }
                        updateNegativeButtonState(button2, zArr[0], zArr2[0]);
                    }
                });
                final Handler handler = new Handler();
                final AlertDialog alertDialog = alertDialogCreate;
                handler.postDelayed(new Runnable() { // from class: com.daaw.avee.MainActivity.11.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (alertDialog.isShowing()) {
                            if (System.currentTimeMillis() - jArr[0] >= ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS) {
                                zArr2[0] = true;
                                updateNegativeButtonState(button2, zArr[0], true);
                            } else {
                                handler.postDelayed(this, 500L);
                            }
                        }
                    }
                }, 500L);
                WebView webView4 = webView;
                final WebView webView5 = webView;
                webView4.postDelayed(new Runnable() { // from class: com.daaw.avee.MainActivity.11.3
                    @Override // java.lang.Runnable
                    public void run() {
                        int contentHeight = webView5.getContentHeight();
                        int height = webView5.getHeight();
                        int scrollY = webView5.getScrollY();
                        if (contentHeight > 0 && height > 0 && scrollY >= (contentHeight - height) - 5) {
                            zArr[0] = true;
                            updateNegativeButtonState(button2, true, zArr2[0]);
                        }
                    }
                }, 1000L);
                final AlertDialog alertDialog2 = alertDialogCreate;
                button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.MainActivity.11.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(MainActivity.PREF_KEY_COLD_START_SHOWN, false).apply();
                        alertDialog2.dismiss();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void updateNegativeButtonState(Button button, boolean z, boolean z2) {
                boolean z3 = z && z2;
                button.setEnabled(z3);
                button.setAlpha(z3 ? 1.0f : 0.4f);
            }
        });
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.daaw.avee.MainActivity.12
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4 || keyEvent.getAction() != 1) {
                    return false;
                }
                dialogInterface.dismiss();
                return true;
            }
        });
        alertDialogCreate.show();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        onStart.invoke(this);
        super.onStart();
        Message messageObtainMessage = this.handler.obtainMessage(3);
        this.handler.removeMessages(3);
        this.handler.sendMessageDelayed(messageObtainMessage, 10000L);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        tlog.d("######### onStop");
        this.handler.removeMessages(3);
        this.handler.removeMessages(4);
        super.onStop();
        onStop.invoke(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        tlog.d("######### onPause");
        this.handler.removeMessages(3);
        this.handler.removeMessages(4);
        onPause.invoke(this);
        super.onPause();
    }

    @Override // com.daaw.avee.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.handler.removeMessages(3);
        this.handler.removeMessages(4);
        this.handler.sendMessageDelayed(this.handler.obtainMessage(3), 10000L);
        this.handler.sendMessageDelayed(this.handler.obtainMessage(4), 3000L);
        onResume.invoke(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        tlog.d("######### onDestroy");
        onDestroy.invoke(new ContextData(this));
        setKeepScreenOnLock(false, false);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        int integer = AppPreferences.createOrGetInstance().preferencesGetBoolSafe(getApplicationContext(), "pref_hold_exit", true) ? getResources().getInteger(re.ex.avee.R.integer.hold_exit_ms) : 0;
        if (i == 4 && integer > 0) {
            this.slowClosingInProgress = true;
            Toast toastMakeText = Toast.makeText(getApplicationContext(), getString(re.ex.avee.R.string.hold_exit), 0);
            this.slowClosingToast = toastMakeText;
            toastMakeText.show();
            Timer timer = new Timer();
            this.slowClosingTimer = timer;
            timer.schedule(new TimerTask() { // from class: com.daaw.avee.MainActivity.13
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    MainActivity.this.slowClosingInProgress = false;
                    MainActivity.this.slowClosingToast.cancel();
                    MainActivity.this.slowClosingTimer.cancel();
                    MainActivity.this.doExit();
                }
            }, integer);
            return true;
        }
        return super.onKeyLongPress(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4 || !this.slowClosingInProgress) {
            return super.onKeyUp(i, keyEvent);
        }
        this.slowClosingInProgress = false;
        this.slowClosingTimer.cancel();
        this.slowClosingTimer = null;
        this.slowClosingToast.setText(getString(re.ex.avee.R.string.hold_exit_canceled));
        this.slowClosingToast.setDuration(0);
        this.slowClosingToast.show();
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && closeSearchView()) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!onBackPressed.invoke(this, false).booleanValue()) {
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(re.ex.avee.R.menu.main_menu, menu);
        SleepTimerConfig sleepTimerConfigInvoke = onMainUIRequestSleepTimerConfig.invoke(null);
        if (sleepTimerConfigInvoke == null) {
            sleepTimerConfigInvoke = new SleepTimerConfig();
        }
        updateSleepTimerIndicatorTh(sleepTimerConfigInvoke.enabled, true);
        updateLockOrientIndicator(onRequestLockOrientState.invoke(false).booleanValue(), true);
        MenuItem menuItemFindItem = menu.findItem(re.ex.avee.R.id.action_bar_search);
        this.searchMenuItem = menuItemFindItem;
        final SearchView searchView = (SearchView) menuItemFindItem.getActionView();
        if (searchView != null) {
            searchView.setIconifiedByDefault(true);
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.daaw.avee.MainActivity.14
                @Override // android.widget.SearchView.OnQueryTextListener
                public boolean onQueryTextSubmit(String str) {
                    searchView.clearFocus();
                    if (searchView.getTag() != null) {
                        MainActivity.onUISearchQueryTextChange.invoke(Integer.valueOf(((Integer) searchView.getTag()).intValue()), str);
                    }
                    return true;
                }

                @Override // android.widget.SearchView.OnQueryTextListener
                public boolean onQueryTextChange(String str) {
                    if (searchView.getTag() != null) {
                        MainActivity.onUISearchQueryTextChange.invoke(Integer.valueOf(((Integer) searchView.getTag()).intValue()), str);
                    }
                    return true;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() { // from class: com.daaw.avee.MainActivity.15
                @Override // android.widget.SearchView.OnCloseListener
                public boolean onClose() {
                    MainActivity.onUISearchQueryStateChange.invoke(false);
                    return false;
                }
            });
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.daaw.avee.MainActivity.16
                @Override // android.view.View.OnFocusChangeListener
                public void onFocusChange(View view, boolean z) {
                }
            });
            setSearchViewStyle(searchView);
            updateSearchView(true);
        }
        this.vizBtn0MenuItem = menu.findItem(re.ex.avee.R.id.action_bar_viz_btn0);
        this.vizBtn1MenuItem = menu.findItem(re.ex.avee.R.id.action_bar_viz_btn1);
        this.vizBtn2MenuItem = menu.findItem(re.ex.avee.R.id.action_bar_viz_btn5);
        this.vizBtnAspectRatio = menu.findItem(re.ex.avee.R.id.id_7f090400);
        if (this.vizBtnAspectRatio != null) {
            this.vizBtnAspectRatio.setVisible(false);
        }
        updateVizButtonVisibility(this.currentFragmentPage == 2);
        return super.onCreateOptionsMenu(menu);
    }


    boolean onMenuItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == re.ex.avee.R.id.showTitle) {
            showCreditsDialog();
            return true;
        }
        switch (itemId) {
            case re.ex.avee.R.id.menu_equalizer /* 2131296581 */:
                onMainUIAction.invoke(3, new ContextData(this));
                return true;
            case re.ex.avee.R.id.menu_lib /* 2131296582 */:
                this.viewPager.setCurrentItem(0);
                return true;
            case re.ex.avee.R.id.menu_lock_orient /* 2131296583 */:
                this.switchLockOrient.toggle();
                return false;
            case re.ex.avee.R.id.menu_queue /* 2131296585 */:
                this.viewPager.setCurrentItem(1);
                return true;
            case re.ex.avee.R.id.menu_settings /* 2131296586 */:
                startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
                return true;
            case re.ex.avee.R.id.menu_sleep_timer /* 2131296587 */:
                this.switchSleepTimer.toggle();
                return false;
            case re.ex.avee.R.id.menu_top /* 2131296588 */:
            default:
                return false;
            case re.ex.avee.R.id.menu_visual /* 2131296589 */:
                this.viewPager.setCurrentItem(2);
                return true;
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            this.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            switch (itemId) {
                case re.ex.avee.R.id.action_bar_viz_btn0 /* 2131296310 */:
                    if (onToolbarAction.invoke(new ContextData(this), 1, false).booleanValue()) {
                        updateVizButtonSelection(0);
                    }
                    break;
                case re.ex.avee.R.id.action_bar_viz_btn1 /* 2131296311 */:
                    if (onToolbarAction.invoke(new ContextData(this), 2, false).booleanValue()) {
                        updateVizButtonSelection(1);
                    }
                    break;
                case re.ex.avee.R.id.action_bar_viz_btn5 /* 2131296312 */:
                    if (onToolbarAction.invoke(new ContextData(this), 3, false).booleanValue()) {
                        updateVizButtonSelection(2);
                    }
                    break;
                case re.ex.avee.R.id.id_7f090400 /* 2131297280 */:
                    showAspectRatioDialog();
                    break;
                default:
                    if (!onMenuItemSelected(menuItem)) {
                    }
                    break;
            }
        }
        return true;
    }

    private void updateVizButtonSelection(int i) {
        this.currentVizSelection = i;
        if (this.vizBtn0MenuItem != null) {
            this.vizBtn0MenuItem.setChecked(i == 0);
        }
        if (this.vizBtn1MenuItem != null) {
            this.vizBtn1MenuItem.setChecked(i == 1);
        }
        if (this.vizBtn2MenuItem != null) {
            this.vizBtn2MenuItem.setChecked(i == 2);
        }
    }

    @Override // android.app.Activity
    public void onUserInteraction() {
        resetVideoMaximizeTimeout(true);
        super.onUserInteraction();
    }

    private void showCreditsDialog() {
        String str;
        try {
            InputStream inputStreamOpenRawResource = getResources().openRawResource(re.ex.avee.R.raw.acknowledgments);
            byte[] bArr = new byte[inputStreamOpenRawResource.available()];
            inputStreamOpenRawResource.read(bArr);
            inputStreamOpenRawResource.close();
            str = new String(bArr, "UTF-8");
        } catch (Exception e) {
            str = "<html><body><p>加载失败</p></body></html>";
        }
        WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(webView).setPositiveButton(getString(re.ex.avee.R.string.abc_capital_off), (DialogInterface.OnClickListener) null).setNegativeButton(getString(re.ex.avee.R.string.abc_capital_off), (DialogInterface.OnClickListener) null).create();
        alertDialogCreate.setCancelable(true);
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.daaw.avee.MainActivity.17
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                final Button button = alertDialogCreate.getButton(-1);
                final Button button2 = alertDialogCreate.getButton(-2);
                if (button != null) {
                    final AlertDialog alertDialog = alertDialogCreate;
                    button.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.MainActivity.17.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }
                if (button2 != null) {
                    button2.setPaintFlags(button2.getPaintFlags() | 16);
                    final AlertDialog alertDialog2 = alertDialogCreate;
                    button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.MainActivity.17.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            alertDialog2.setCancelable(false);
                            alertDialog2.setCanceledOnTouchOutside(false);
                            if (button != null) {
                                button.setEnabled(false);
                                button.setAlpha(0.4f);
                            }
                            if (button2 != null) {
                                button2.setEnabled(false);
                                button2.setAlpha(0.4f);
                            }
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(re.ex.avee.R.string.why_tap_it), 0).show();
                        }
                    });
                }
            }
        });
        alertDialogCreate.show();
    }

    public void updateSearchView(boolean z) {
        updateSearchView(onRequestCurrentSearchEntry.invoke(null), z);
    }

    public void updateSearchView(ISearchEntry iSearchEntry, boolean z) {
        SearchView searchView;
        MenuItem menuItem = this.searchMenuItem;
        if (menuItem != null && (searchView = (SearchView) menuItem.getActionView()) != null) {
            if (iSearchEntry != null && iSearchEntry.isEnabled()) {
                String query = iSearchEntry.getQuery();
                this.searchMenuItem.setVisible(true);
                if (query != null && !query.isEmpty()) {
                    searchView.setTag(Integer.valueOf(iSearchEntry.getIndex()));
                    searchView.setQuery(query, false);
                    if (searchView.isIconified()) {
                        searchView.setIconified(false);
                    }
                } else {
                    searchView.setTag(Integer.valueOf(iSearchEntry.getIndex()));
                    searchView.setQuery("", false);
                }
                searchView.setQueryHint(iSearchEntry.getHint());
                return;
            }
            this.searchMenuItem.setVisible(false);
            searchView.setQueryHint("");
        }
    }

    private void setSearchViewStyle(SearchView searchView) {
        UtilsUI.setViewStyle(searchView, ContextCompat.getColor(this, re.ex.avee.R.color.white_alpha_1), ContextCompat.getColor(this, re.ex.avee.R.color.text_color_m2));
    }

    private boolean closeSearchView() {
        SearchView searchView;
        MenuItem menuItem = this.searchMenuItem;
        if (menuItem == null || (searchView = (SearchView) menuItem.getActionView()) == null || searchView.isIconified()) {
            return false;
        }
        searchView.setTag(Integer.valueOf(this.currentFragmentPage));
        searchView.setQuery("", false);
        searchView.setIconified(true);
        return true;
    }

    public void onViewPagerPageSelected(int i) {
        this.currentFragmentPage = i;
        showControls(onRequestShouldShowControls.invoke(true).booleanValue(), this.currentFragmentPage);
        onViewPagerPageSelected.invoke(Integer.valueOf(this.currentFragmentPage), this);
        onSetCurrentSearchIndex.invoke(Integer.valueOf(this.currentFragmentPage));
        if (i != 2) {
            this.currentVizSelection = -1;
        }
        updateVizButtonVisibility(i == 2);
    }

    private Fragment findFragmentByPosition(int i) {
        SectionsPagerAdapter sectionsPagerAdapter;
        CustomViewPager customViewPager = this.viewPager;
        if (customViewPager == null || (sectionsPagerAdapter = this.sectionsPagerAdapter) == null) {
            return null;
        }
        return getFragmentManager().findFragmentByTag("android:switcher:" + customViewPager.getId() + ":" + sectionsPagerAdapter.getItemId(i));
    }

    public void doExit() {
        onExit.invoke();
        finish();
    }

    public void doExitFromService() {
        finish();
    }

    public void setKeepScreenOnLock(boolean z, boolean z2) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = z2 ? 1.0f : 0.0f;
        window.setAttributes(attributes);
        if (z) {
            window.addFlags(128);
        } else {
            window.clearFlags(128);
        }
    }

    public void toggleShowControls(int i) {
        ActionBar supportActionBar = getSupportActionBar();
        showControls((supportActionBar == null || supportActionBar.isShowing()) ? false : true, i);
    }

    public void showControls(boolean z, int i) {
        showControls(z, i, false);
    }

    private void updateVizButtonVisibility(boolean z) {
        if (z) {
            MenuItem menuItem = this.vizBtn0MenuItem;
            if (menuItem != null && !menuItem.isVisible()) {
                if (Build.VERSION.SDK_INT >= 19) {
                    int integer = getResources().getInteger(android.R.integer.config_shortAnimTime);
                    this.vizBtn0MenuItem.setVisible(true);
                    this.vizBtn1MenuItem.setVisible(true);
                    this.vizBtn2MenuItem.setVisible(true);
                    this.vizMenuButtonAnimator = UtilsUI.animShowCustom255(this.vizMenuButtonAnimator, new Action1<Integer>() { // from class: com.daaw.avee.MainActivity.18
                        @Override // com.daaw.avee.Common.Action1
                        public void onInvoke(Integer num) {
                            MainActivity.this.vizBtn0MenuItem.getIcon().setAlpha(num.intValue());
                            MainActivity.this.vizBtn1MenuItem.getIcon().setAlpha(num.intValue());
                            MainActivity.this.vizBtn2MenuItem.getIcon().setAlpha(num.intValue());
                        }
                    }, this.vizBtn0MenuItem.getIcon().getAlpha(), integer);
                    if (this.vizBtnAspectRatio != null) {
                        this.vizBtnAspectRatio.setVisible(true);
                        return;
                    }
                    return;
                }
                this.vizBtn0MenuItem.setVisible(true);
                this.vizBtn1MenuItem.setVisible(true);
                this.vizBtn2MenuItem.setVisible(true);
                if (this.vizBtnAspectRatio != null) {
                    this.vizBtnAspectRatio.setVisible(true);
                    return;
                }
                return;
            }
            return;
        }
        MenuItem menuItem2 = this.vizBtn0MenuItem;
        if (menuItem2 != null && menuItem2.isVisible()) {
            if (Build.VERSION.SDK_INT >= 19) {
                this.vizMenuButtonAnimator = UtilsUI.animHideCustom255(this.vizMenuButtonAnimator, new Action1<Integer>() { // from class: com.daaw.avee.MainActivity.19
                    @Override // com.daaw.avee.Common.Action1
                    public void onInvoke(Integer num) {
                        MainActivity.this.vizBtn0MenuItem.getIcon().setAlpha(num.intValue());
                        MainActivity.this.vizBtn1MenuItem.getIcon().setAlpha(num.intValue());
                        MainActivity.this.vizBtn2MenuItem.getIcon().setAlpha(num.intValue());
                    }
                }, this.vizBtn0MenuItem.getIcon().getAlpha(), getResources().getInteger(android.R.integer.config_shortAnimTime), new Action() { // from class: com.daaw.avee.MainActivity.20
                    @Override // com.daaw.avee.Common.Action
                    public void onInvoke() {
                        MainActivity.this.vizBtn0MenuItem.setVisible(false);
                        MainActivity.this.vizBtn1MenuItem.setVisible(false);
                        MainActivity.this.vizBtn2MenuItem.setVisible(false);
                        if (MainActivity.this.vizBtnAspectRatio != null) {
                            MainActivity.this.vizBtnAspectRatio.setVisible(false);
                        }
                    }
                });
                return;
            }
            this.vizBtn0MenuItem.setVisible(false);
            this.vizBtn1MenuItem.setVisible(false);
            this.vizBtn2MenuItem.setVisible(false);
            if (this.vizBtnAspectRatio != null) {
                this.vizBtnAspectRatio.setVisible(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showControls(boolean z, int i, boolean z2) {
        SystemUiHiderHoneycomb systemUiHiderHoneycomb;
        SystemUiHiderHoneycomb systemUiHiderHoneycomb2;
        if (i == 2 && z) {
            try {
                Fragment fragmentFindFragmentByTag = getFragmentManager().findFragmentByTag("CustomizeVisDialog");
                if (fragmentFindFragmentByTag != null && fragmentFindFragmentByTag.isVisible()) {
                    return;
                }
            } catch (Exception e) {
            }
        }
        resetVideoMaximizeTimeout(z);
        ActionBar supportActionBar = getSupportActionBar();
        updateVizButtonVisibility(i == 2);
        if (i == 2) {
            this.isVisualizerImmersive = !z;
        }
        if (z) {
            if (supportActionBar != null) {
                if (i == 2) {
                    supportActionBar.setDisplayShowTitleEnabled(false);
                    supportActionBar.setDisplayShowHomeEnabled(false);
                    UtilsUI.showControl(getToolBar());
                } else {
                    supportActionBar.setDisplayShowTitleEnabled(true);
                    supportActionBar.setDisplayShowHomeEnabled(true);
                    UtilsUI.showControl(getToolBar());
                }
            }
            updateActionBar(onRequestTrackInfo.invoke(PlaylistSong.emptyData));
            if (!z2 && (systemUiHiderHoneycomb2 = this.systemUiHider) != null) {
                systemUiHiderHoneycomb2.show();
            }
        } else {
            if (supportActionBar != null) {
                UtilsUI.hideControl(getToolBar());
            }
            if (!z2 && (systemUiHiderHoneycomb = this.systemUiHider) != null) {
                systemUiHiderHoneycomb.hide();
            }
        }
        onFullscreenChanged.invoke(Boolean.valueOf(z ? false : true));
    }

    public void updateActionBar(PlaylistSong.Data data) {
        if (!data.equals(this.assignedSongData)) {
            this.assignedSongData = data;
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(data.trackName);
                if (data.isArtistKnownOrSecondName()) {
                    supportActionBar.setSubtitle(data.artistName);
                } else {
                    supportActionBar.setSubtitle("");
                }
            }
            onRequestAlbumArtLarge.invoke(new AlbumArtRequest(data.dataSource, data.getAlbumArtPath0Str(), data.getAlbumArtPath1Str(), data.getAlbumArtGenerateStr()), new ImageLoadedListener() { // from class: com.daaw.avee.MainActivity.21
                Object object1;

                @Override // com.daaw.avee.comp.AlbumArt.ImageLoadedListener
                public void onBitmapLoaded(ImageResult imageResult, String str, String str2, String str3) {
                    UtilsUI.copyBitmapSafe(ImageResult.getNonPersistentBitmapSafe(imageResult));
                    ImageResult.closeSafe(imageResult);
                }

                @Override // com.daaw.avee.comp.AlbumArt.ImageLoadedListener
                public void setUserObject1(Object obj) {
                    this.object1 = obj;
                }
            }, Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
        }
    }

    public void updateSleepTimerIndicatorTh(final boolean z, boolean z2) {
        this.handler.post(new Runnable() { // from class: com.daaw.avee.MainActivity.22
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.sleepTimerIndicatorMenuItem != null) {
                    MainActivity.this.sleepTimerIndicatorMenuItem.setVisible(z);
                }
                if (MainActivity.this.switchSleepTimer != null) {
                    MainActivity.this.switchSleepTimer.setChecked(z);
                }
            }
        });
    }

    public void updateLockOrientIndicator(boolean z, boolean z2) {
        MenuItem menuItem = this.lockOrientIndicatorMenuItem;
        if (menuItem != null) {
            menuItem.setVisible(z);
        }
        SwitchCompat switchCompat = this.switchLockOrient;
        if (switchCompat != null) {
            switchCompat.setChecked(z);
        }
    }

    public void resetVideoMaximizeTimeout(boolean z) {
        if ((this.currentFragmentPage != 2 || !this.isVisualizerImmersive) && AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_pref_visControlsTimeout)) {
            this.handler.removeMessages(2);
            if (z) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(2), getResources().getInteger(re.ex.avee.R.integer.video_maximize_timeout));
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        onRequestPermissionsResult.invoke(this, Integer.valueOf(i));
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!onActivityResult0.invoke(Integer.valueOf(i), Integer.valueOf(i2), intent, false).booleanValue() && i2 == -1) {
            onActivityResult.invoke(Integer.valueOf(i2), intent);
        }
    }

    private void showAspectRatioDialog() {
        float[] fArrLoadAspectRatio = AspectRatioHelper.loadAspectRatio(this);
        float f = fArrLoadAspectRatio[0];
        int i = (int) fArrLoadAspectRatio[1];
        int i2 = (int) fArrLoadAspectRatio[2];
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(60, 40, 60, 20);
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(1);
        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(getString(re.ex.avee.R.string.main_activity_aspect_ratio_default));
        final RadioButton radioButton2 = new RadioButton(this);
        radioButton2.setText(getString(re.ex.avee.R.string.main_activity_aspect_ratio_custom));
        radioGroup.addView(radioButton);
        radioGroup.addView(radioButton2);
        final LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        linearLayout2.setPadding(0, 20, 0, 0);
        linearLayout2.setVisibility(8);
        final EditText editText = new EditText(this);
        editText.setHint("16");
        editText.setInputType(2);
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        editText.setGravity(17);
        TextView textView = new TextView(this);
        textView.setText(" : ");
        textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        final EditText editText2 = new EditText(this);
        editText2.setHint("9");
        editText2.setInputType(2);
        editText2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        editText2.setGravity(17);
        linearLayout2.addView(editText);
        linearLayout2.addView(textView);
        linearLayout2.addView(editText2);
        linearLayout.addView(radioGroup);
        linearLayout.addView(linearLayout2);
        if (f > 0.001f) {
            radioButton2.setChecked(true);
            linearLayout2.setVisibility(0);
            editText.setText(String.valueOf(i));
            editText2.setText(String.valueOf(i2));
        } else {
            radioButton.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.daaw.avee.MainActivity.23
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i3) {
                if (i3 == radioButton2.getId()) {
                    linearLayout2.setVisibility(0);
                } else {
                    linearLayout2.setVisibility(8);
                }
            }
        });
        new AlertDialog.Builder(this).setTitle(getString(re.ex.avee.R.string.main_activity_aspect_ratio_title)).setView(linearLayout).setPositiveButton(getString(re.ex.avee.R.string.main_activity_aspect_ratio_confirm), new DialogInterface.OnClickListener() { // from class: com.daaw.avee.MainActivity.24
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                if (!radioButton.isChecked()) {
                    String strTrim = editText.getText().toString().trim();
                    String strTrim2 = editText2.getText().toString().trim();
                    if (strTrim.isEmpty() || strTrim2.isEmpty()) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(re.ex.avee.R.string.main_activity_aspect_ratio_input_empty), 0).show();
                        return;
                    }
                    try {
                        int i4 = Integer.parseInt(strTrim);
                        int i5 = Integer.parseInt(strTrim2);
                        if (i4 <= 0 || i5 <= 0) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(re.ex.avee.R.string.main_activity_aspect_ratio_input_invalid), 0).show();
                        } else {
                            MainActivity.this.applyAspectRatio((float) i4 / (float) i5, i4, i5); // 修正：int 除法导致 16/9 恒为 1
                        }
                        return;
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(re.ex.avee.R.string.main_activity_aspect_ratio_input_number), 0).show();
                        return;
                    }
                }
                MainActivity.this.applyAspectRatio(0.0f, 0, 0);
            }
        }).setNegativeButton(getString(re.ex.avee.R.string.main_activity_aspect_ratio_cancel), (DialogInterface.OnClickListener) null).create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyAspectRatio(float f, int i, int i2) {
        AspectRatioHelper.saveAspectRatio(this, f, i, i2);
        Fragment2 fragment2Instance = getFragment2Instance();
        if (fragment2Instance != null) {
            fragment2Instance.setAspectRatio(f);
        }
        Toast.makeText(this, f == 0.0f ? getString(re.ex.avee.R.string.main_activity_aspect_ratio_restored) : String.format(getString(re.ex.avee.R.string.main_activity_aspect_ratio_applied), Integer.valueOf(i), Integer.valueOf(i2)), 0).show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return 3;
        }

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override // com.daaw.avee.vendor.legacy.FragmentPagerAdapter
        public Fragment getItem(int i) {
            if (i == 0) {
                return Fragment0.newInstance();
            }
            if (i == 1) {
                return Fragment1.newInstance();
            }
            if (i != 2) {
                return null;
            }
            return Fragment2.newInstance();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public CharSequence getPageTitle(int i) {
            Drawable drawable;
            SpannableStringBuilder spannableStringBuilder;
            if (i == 0) {
                drawable = ContextCompat.getDrawable(MainActivity.this, re.ex.avee.R.drawable.ic_library_2_s);
                spannableStringBuilder = new SpannableStringBuilder("   ");
            } else if (i == 1) {
                drawable = ContextCompat.getDrawable(MainActivity.this, re.ex.avee.R.drawable.ic_playlist4);
                spannableStringBuilder = new SpannableStringBuilder("   ");
            } else {
                if (i != 2) {
                    return " ";
                }
                drawable = ContextCompat.getDrawable(MainActivity.this, re.ex.avee.R.drawable.ic_visual2);
                spannableStringBuilder = new SpannableStringBuilder("   ");
            }
            if (drawable == null) {
                return "";
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            spannableStringBuilder.setSpan(new ImageSpan(drawable, 1), 1, 2, 33);
            return spannableStringBuilder;
        }
    }
}
