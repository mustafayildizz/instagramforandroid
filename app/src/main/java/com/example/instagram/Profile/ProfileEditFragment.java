package com.example.instagram.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;


public class ProfileEditFragment extends Fragment {

    private View view;
    private ImageView imgClose, imgProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        initVariables();
        closeListener();
        setupProfilePicture();

        return view;
    }

    private void initVariables() {
        imgProfile = view.findViewById(R.id.profileSettingCircleImageView);
        imgClose = view.findViewById(R.id.imgClose);
    }

    private void setupProfilePicture() {
        String URL = "wallpaperaccess.com/full/25640.jpg";
        UniversalImageLoader.setImage(URL, imgProfile, null, "https://");
    }


    private void closeListener() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

    }
}
