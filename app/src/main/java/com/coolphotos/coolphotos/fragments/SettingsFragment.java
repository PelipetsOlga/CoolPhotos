package com.coolphotos.coolphotos.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.coolphotos.coolphotos.R;
import com.coolphotos.coolphotos.utils.Constants;


public class SettingsFragment extends Fragment {

    private SharedPreferences sPref;
    private SeekBar seekBarDelay;
    private SeekBar seekBarDuration;
    private TextView tvDelay, tvMode;
    private SwitchCompat switchShuffle;
    private SwitchCompat switchMode;
    private View layoutDuration;
    private int period;
    private int duration;
    private int labelDuration;
    private boolean shuffle;
    private boolean modeConscious;
    private AppCompatActivity activity;
    private TextView tvDuration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        //  setupActionBar();
        initViews(view);
        loadSettings();
        init();
        return view;
    }

    /*  private void setupActionBar(){
          ActionBar actionBar = getActivity().getSupportActionBar();
         // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
          //actionBar.setDisplayShowTitleEnabled(true);
          actionBar.setTitle(getResources().getString(R.string.action_settings));
          actionBar.setIcon(R.drawable.ic_arrow_back_white_24dp);
          actionBar.setDisplayHomeAsUpEnabled(true);
          actionBar.setHomeButtonEnabled(true);

      }
  */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
        activity.setTitle(getString(R.string.title_settings));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity.setTitle(getString(R.string.app_name));
    }

    private void loadSettings() {
        sPref = getActivity().getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        period = sPref.getInt(Constants.PERIOD_UPDATING, Constants.DEFAULT_PERIOD_UPDATING);
        shuffle = sPref.getBoolean(Constants.SHUFFLE, Constants.DEFAULT_SHUFFLE);
        modeConscious = sPref.getBoolean(Constants.MODE_CONSCIOUS, Constants.DEFAULT_MODE_CONSCIOUS);
        duration = sPref.getInt(Constants.DURATION, Constants.DEFAULT_DURATION);
        labelDuration = duration / Constants.SCALE_DURATION;
    }

    private void initViews(View view) {
        tvDelay = (TextView) view.findViewById(R.id.tv_delay);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        tvMode = (TextView) view.findViewById(R.id.concious_mode);
        seekBarDelay = (SeekBar) view.findViewById(R.id.seekbar_delay);
        seekBarDuration = (SeekBar) view.findViewById(R.id.seekbar_duration);
        switchShuffle = (SwitchCompat) view.findViewById(R.id.switch_shuffle);
        switchMode = (SwitchCompat) view.findViewById(R.id.switch_mode);
        layoutDuration = view.findViewById(R.id.layout_time_settings);
    }


    private void init() {
        seekBarDelay.setProgress(period - 1);
        tvDelay.setText(period + " " + (period > 1 ? getString(R.string.minutes) : getString(R.string.minute)));
        seekBarDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                period = progress + 1;
                tvDelay.setText(period + " " + (period > 1 ? getString(R.string.minutes) : getString(R.string.minute)));
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt(Constants.PERIOD_UPDATING, period);
                ed.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switchShuffle.setChecked(shuffle);
        switchShuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean(Constants.SHUFFLE, isChecked);
                shuffle = isChecked;
                ed.commit();
            }
        });

        switchMode.setChecked(modeConscious);
        tvMode.setText(modeConscious ? getString(R.string.tv_consious) : getString(R.string.tv_subconsious));
        setDurationLayoutVisibility();
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modeConscious = isChecked;
                saveModeContious();
                setDurationLayoutVisibility();
            }
        });


        seekBarDuration.setProgress(labelDuration - 1);
        tvDuration.setText(duration + " " + (getString(R.string.milliseconds)));
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                labelDuration = progress + 1;
                duration = labelDuration * Constants.SCALE_DURATION;
                tvDuration.setText(duration + " " + (getString(R.string.milliseconds)));
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt(Constants.DURATION, duration);
                ed.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void saveModeContious() {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.MODE_CONSCIOUS, modeConscious);
        ed.commit();
    }

    private void setDurationLayoutVisibility() {
        tvMode.setText(modeConscious ? getString(R.string.tv_consious) : getString(R.string.tv_subconsious));
        if (!modeConscious) {
            layoutDuration.setVisibility(View.GONE);
        } else {
            layoutDuration.setVisibility(View.VISIBLE);
        }
    }


  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // TODO: add notify service
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
