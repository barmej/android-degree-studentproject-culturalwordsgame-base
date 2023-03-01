package com.barmej.culturalwords;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /**
     * Constants
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BUNDLE_CURRENT_INDEX = "BUNDLE_CURRENT_INDEX";
    public static final String BUNDLE_TITLE = "BUNDLE_TITLE";
    public static final String BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION";
    public static final String BUNDLE_DRAWABLE_ID = "BUNDLE_IMAGE_DRAWABLE";
    public static final String APP_PREF = "app_pref";
    public static final String APP_LANG = "app_lang";

    /**
     * ImageView to display the cultural image
     */
    private ImageView mImageView;

    /**
     * Data arrays to store cultural words, images and descriptions
     */
    private final int[] mPictures = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4,
            R.drawable.icon_5,
            R.drawable.icon_6,
            R.drawable.icon_7,
            R.drawable.icon_8,
            R.drawable.icon_9,
            R.drawable.icon_10,
            R.drawable.icon_11,
            R.drawable.icon_12,
            R.drawable.icon_13
    };

    private String[] mAnswers;
    private String[] mAnswersDescriptions;

    /**
     * Instance of Random class to generate random position
     */
    private Random mRandom;

    /**
     * Variable used as index to move through images array
     */
    private int mCurrentIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read language preference from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE);
        String appLanguage = sharedPreferences.getString(MainActivity.APP_LANG, "");

        // Apply saved language to app locale
        if (!appLanguage.isEmpty()) {
            LocaleHelper.setLocale(this, appLanguage);
            // Workaround to fix a bug in Android +31
            // https://issuetracker.google.com/issues/228215857#comment1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                final int layoutDirection = TextUtils.getLayoutDirectionFromLocale(new Locale(appLanguage));
                getWindow().getDecorView().setLayoutDirection(layoutDirection);
            }
        }

        setContentView(R.layout.activity_main);
        // Get ImageView from view hierarchy
        mImageView = findViewById(R.id.image_view_question);
        // Get string arrays from app resources
        mAnswers = getResources().getStringArray(R.array.answers);
        mAnswersDescriptions = getResources().getStringArray(R.array.answer_description);
        // Create new Random object
        mRandom = new Random();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_INDEX, mCurrentIndex);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentIndex = savedInstanceState.getInt(BUNDLE_CURRENT_INDEX);
        showImage();
        Log.i(TAG, "onRestoreInstanceState");
    }

    /**
     * Open AnswerActivity
     * @param view
     */
    public void showAnswer(View view) {
        Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
        intent.putExtra(BUNDLE_TITLE, mAnswers[mCurrentIndex]);
        intent.putExtra(BUNDLE_DESCRIPTION, mAnswersDescriptions[mCurrentIndex]);
        startActivity(intent);
    }

    /**
     * Change current cultural image
     * @param view
     */
    public void changeQuestion(View view) {
        mCurrentIndex = mRandom.nextInt(mPictures.length);
        showImage();
    }

    /**
     * Share current cultural image
     * @param view
     */
    public void share(View view) {
        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
        intent.putExtra(BUNDLE_DRAWABLE_ID, mPictures[mCurrentIndex]);
        startActivity(intent);
    }

    /**
     * Show language chooser dialog
     * @param view
     */
    public void showLanguageDialog(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_lang_text)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String language = "ar";
                        switch (which) {
                            case 0:
                                language = "ar";
                                break;
                            case 1:
                                language = "en";
                                break;
                        }
                        saveLanguage(language);
                        LocaleHelper.setLocale(MainActivity.this, language);
                        recreate();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * Show cultural image at current index
     */
    public void showImage() {
        Drawable drawable = ContextCompat.getDrawable(this, mPictures[mCurrentIndex]);
        mImageView.setImageDrawable(drawable);
    }

    /**
     * Save chosen language to SharedPreferences
     * @param lang chosen language
     */
    private void saveLanguage(String lang) {
        SharedPreferences sharedPreferencesSaveLanguage = getSharedPreferences(APP_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesSaveLanguage.edit();
        editor.putString(APP_LANG, lang);
        editor.apply();
    }

}
