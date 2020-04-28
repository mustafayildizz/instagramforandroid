package com.example.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.utils.EventbusDataEvents;
import com.example.instagram.utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ProfileEditFragment extends Fragment {

    private final static int PICK_IMAGE = 1001;
    DialogFragment fragment;
    private Uri profilPicURI;
    private boolean isAvailableUsarname = true;
    private Boolean isUserUpdated, isChangeProfilPic2;
    private DatabaseReference mDatabaseRef;
    private Users user;
    private View view;
    private TextView tvChangePhoto;
    private EditText etUsername, etRealname, etWebsite, etBio;
    private ImageView imgClose, imgProfile, imgSave;
    private String URL, username, realname, bio, website;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        initVariables();
        closeListener();
        setupUserInfo();
        //  updateUser();
        updateUserDeneme();
        setupProfilePicture();

        tvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        return view;
    }

    private void updateUserDeneme() {
        imgSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profilPicURI != null) {
                    fragment = new ImageLoadingFragment();
                    fragment.show(getFragmentManager(), "LoadingFragment");
                    fragment.setCancelable(false);
                    mStorageRef.child("users").child(user.getUser_id()).child(profilPicURI.getLastPathSegment()).putFile(profilPicURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference newReference = FirebaseStorage.getInstance().getReference("users/" + user.getUser_id() + "/" + profilPicURI.getLastPathSegment());
                            newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("profile_pic")
                                            .setValue(uri.toString());
                                    fragment.dismissAllowingStateLoss();
                                    updateUserName(true);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    updateUserName(false);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                    Intent intent = new Intent(getContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                } else {
                    updateUserName(null);
                }
            }
        });
    }

    private void updateUserName(Boolean isChangeProfilPic) {
        isChangeProfilPic2 = isChangeProfilPic;
        if (!user.getUser_name().equals(etUsername.getText().toString())) {
            mDatabaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    isAvailableUsarname = true;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String userName = ds.getValue(Users.class).getUser_name();
                        if (userName != null && userName.equals(etUsername.getText().toString())) {
                            isAvailableUsarname = false;
                            updateProfileInfo(isChangeProfilPic2, false);
                            break;
                        }
                    }
                    if (isAvailableUsarname) {
                        mDatabaseRef.child("users").child(user.getUser_id()).child("user_name").setValue(etUsername.getText().toString());
                        isUserUpdated = true;
                        updateProfileInfo(isChangeProfilPic2, true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            updateProfileInfo(isChangeProfilPic, null);
        }

    }

    private void updateProfileInfo(Boolean isChangeProfilPic, Boolean isUserNameChanged){
            isUserUpdated = null;
            if (!user.getName_surname().equals(etRealname.getText().toString())) {
                mDatabaseRef.child("users").child(user.getUser_id()).child("name_surname").setValue(etRealname.getText().toString());
                isUserUpdated = true;
            }
            if (!user.getUserDetails().getBiography().equals(etBio.getText().toString())) {
                mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("biography").setValue(etBio.getText().toString());
                isUserUpdated = true;
            }
            if (!user.getUserDetails().getWebSite().equals(etWebsite.getText().toString())) {
                mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("webSite").setValue(etWebsite.getText().toString());
                isUserUpdated = true;
            }


            if (isUserUpdated == null && isChangeProfilPic == null && isUserNameChanged == null) {
                Toast.makeText(getActivity(), "Herhangi bir değişiklik yapılmadı", Toast.LENGTH_LONG).show();

            }
            if (isUserNameChanged == null || isChangeProfilPic == null || isUserUpdated == null) {
                Toast.makeText(getActivity(), "Kullanıcı güncellendi", Toast.LENGTH_LONG).show();

            }


        }

    private void updateUser() {

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserUpdated = false;
                if (!user.getName_surname().equals(etRealname.getText().toString())) {
                    mDatabaseRef.child("users").child(user.getUser_id()).child("name_surname").setValue(etRealname.getText().toString());
                    isUserUpdated = true;
                }
                if (!user.getUserDetails().getBiography().equals(etBio.getText().toString())) {
                    mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("biography").setValue(etBio.getText().toString());
                    isUserUpdated = true;
                }
                if (!user.getUserDetails().getWebSite().equals(etWebsite.getText().toString())) {
                    mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("website").setValue(etWebsite.getText().toString());
                    isUserUpdated = true;
                }
                if (!user.getUser_name().equals(etUsername.getText().toString())) {
                    mDatabaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            isAvailableUsarname = true;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String userName = ds.getValue(Users.class).getUser_name();
                                if (userName != null && userName.equals(etUsername.getText().toString())) {
                                    Toast.makeText(getActivity(), "Bu kullanıcı adı daha önce alınmış", Toast.LENGTH_LONG).show();
                                    isAvailableUsarname = false;
                                    break;
                                }
                            }
                            if (isAvailableUsarname) {
                                mDatabaseRef.child("users").child(user.getUser_id()).child("user_name").setValue(etUsername.getText().toString());
                                isUserUpdated = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if (isUserUpdated) {
                    Toast.makeText(getActivity(), "Kullanıcı bilgileri güncellendi", Toast.LENGTH_LONG).show();
                }
                if (profilPicURI != null) {
                    fragment = new ImageLoadingFragment();
                    fragment.show(getFragmentManager(), "LoadingFragment");
                    mStorageRef.child("users").child(user.getUser_id()).child(profilPicURI.getLastPathSegment()).putFile(profilPicURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference newReference = FirebaseStorage.getInstance().getReference("users/" + user.getUser_id() + "/" + profilPicURI.getLastPathSegment());
                            newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mDatabaseRef.child("users").child(user.getUser_id()).child("userDetails").child("profile_pic")
                                            .setValue(uri.toString());
                                    fragment.dismissAllowingStateLoss();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                    Intent intent = new Intent(getContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            profilPicURI = data.getData();
            imgProfile.setImageURI(profilPicURI);
        }

    }

    private void setupUserInfo() {
        etUsername.setText(user.getUser_name());
        etRealname.setText(user.getName_surname());
        etWebsite.setText(user.getUserDetails().getWebSite());
        etBio.setText(user.getUserDetails().getBiography());
    }

    private void initVariables() {
        imgSave = view.findViewById(R.id.imgSaveProfileEditFragment);
        tvChangePhoto = view.findViewById(R.id.tvProfileSettingChangePhoto);
        imgProfile = view.findViewById(R.id.profileSettingCircleImageView);
        imgClose = view.findViewById(R.id.imgClose);
        etUsername = view.findViewById(R.id.etUsernameProfileEditFragment);
        etRealname = view.findViewById(R.id.etRealNameProfileEditFragment);
        etWebsite = view.findViewById(R.id.etWebsiteProfileEditFragment);
        etBio = view.findViewById(R.id.etBiographiProfileEditFragment);
    }

    private void setupProfilePicture() {
        UniversalImageLoader.setImage(user.getUserDetails().getProfile_pic(), imgProfile, null, "");
    }


    private void closeListener() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }

    @Subscribe(sticky = true)
    public void onUserInfoEvent(EventbusDataEvents.SendUserInfo userInfo) {
        user = userInfo.getUser();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
