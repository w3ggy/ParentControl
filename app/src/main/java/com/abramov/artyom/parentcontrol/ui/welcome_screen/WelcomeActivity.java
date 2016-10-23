package com.abramov.artyom.parentcontrol.ui.welcome_screen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.ui.main_screen.MainActivity;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class WelcomeActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title(getString(R.string.welcome_map_title))
                .description(getString(R.string.welcome_map_description))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.READ_PHONE_STATE})
                .neededPermissions(new String[]{Manifest.permission.READ_PHONE_STATE})
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title(getString(R.string.welcome_phone_title))
                .description(getString(R.string.welcome_phone_description))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.READ_SMS})
                .neededPermissions(new String[]{Manifest.permission.READ_SMS})
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title(getString(R.string.welcome_sms_title))
                .description(getString(R.string.welcome_sms_description))
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void changeScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onFinish() {
        changeScreen();
    }
}
