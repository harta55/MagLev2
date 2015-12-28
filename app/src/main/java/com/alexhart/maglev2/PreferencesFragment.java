package com.alexhart.maglev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Display preferences fragment on an activity rather than having a separate fragment
 * holder. Contains bluetooth settings, user settings...
 */


public class PreferencesFragment extends Activity {

    private static String mCameraID;
    private static final String TAG = "PrefFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCameraID = getIntent().getStringExtra("cameraID");
//        Toast.makeText(this, "Cameraid" + mCameraID, Toast.LENGTH_SHORT).show();
        //display preferences fragment as only fragment in activity
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFrag()).commit();


    }

    public static class PrefsFrag extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        private ListPreference mPictureQuality;
        private ListPreference mVideoQuality;
        private ListPreference mVideoFormat;
        private CheckBoxPreference mCameraStartup;
        private CameraManager mCameraManager;
        private CameraCharacteristics mCameraCharacteristics;




        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_frag);
            initUI();


        }

        private void initUI() {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String msg = sharedPrefs.getString(getString(R.string.pref_picture_quality_key), "noval");
            makeToast(msg);



            mPictureQuality = (ListPreference) findPreference("picture_quality");
            mVideoQuality = (ListPreference) findPreference("video_quality");
            mVideoFormat = (ListPreference) findPreference("video_format");
            mCameraStartup = (CheckBoxPreference) findPreference("camera_startup");

            mPictureQuality.setOnPreferenceChangeListener(this);
            mVideoQuality.setOnPreferenceChangeListener(this);


            mCameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraID);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            StreamConfigurationMap map = mCameraCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            if (map != null) {
                Size[] vidSizes = map.getOutputSizes(MediaRecorder.class);
                Size[] picSizes = map.getOutputSizes(ImageFormat.JPEG);

                String[] picSizeArray = new String[picSizes.length];
                String[] picSizeEntryVals = new String[picSizes.length];

                List<String> vidSizeList = new ArrayList<>();
                List<String> vidSizeEntryList = new ArrayList<>();

                int i = 0;
                for (Size picSize : picSizes){
                    picSizeArray[i] = picSize.toString();
                    picSizeEntryVals[i] = Integer.toString(i);
                    i++;
                }
                mPictureQuality.setEntries(picSizeArray);
                mPictureQuality.setEntryValues(picSizeEntryVals);

                i = 0;
                for (Size vidSize : vidSizes){
                    //4k resolution is max despite what is outputted
                    if (vidSize.getHeight() * vidSize.getWidth() > 8294400){
                        continue;
                    } else if (vidSize.getHeight() * vidSize.getWidth() == 8294400){
                        String entry = vidSize.toString() + " (4k resolution)";
                        vidSizeList.add(entry);
                        vidSizeEntryList.add(Integer.toString(i));
                    } else if (vidSize.getHeight() * vidSize.getWidth() == 2073600){
                        String entry = vidSize.toString() + " (1080p!)";
                        vidSizeList.add(entry);
                        vidSizeEntryList.add(Integer.toString(i));
                    } else {
                        vidSizeList.add(vidSize.toString());
                        vidSizeEntryList.add(Integer.toString(i));
                    }

                    i++;
                }

                mVideoQuality.setEntries(vidSizeList.toArray(new String[vidSizeList.size()]));
                mVideoQuality.setEntryValues(vidSizeEntryList.toArray(new String[vidSizeEntryList.size()]));


            }




            //TODO fix pref size/format selection




        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object val) {



            if (preference.getKey().equals(getString(R.string.pref_picture_quality_key))) {

                int index= mPictureQuality.findIndexOfValue(val.toString());
                if (index!= -1)
                {
                    makeToast("Value: " + mPictureQuality.getEntries()[index]);
                }


            }


            return true;
        }




        private void makeToast(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStop() {
            super.onStop();
        }



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
