package com.example.dilse.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dilse.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    NavigationView drawer_menu;
    ActionBarDrawerToggle toggle;
    DrawerLayout nav_drawer;
    View navHeader;
    TextView userName, userEmail, deals_first_title, deals_first_desc, deals_second_title, deals_second_desc;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String UID, shareMsg;
    int selected;
    androidx.appcompat.widget.Toolbar homeToolbar;
    Dialog signOutDialog;
    LinearLayout searchContainer;
    ImageSlider imageSlider, deals_first, deals_second;
    ImageView upper_banner;
    CardView flowersCard, cardsCard, softToysCard, earphonesCard, stationariesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_home);
        SharedPreferences getSharedPreferences = getSharedPreferences("selected_value", MODE_PRIVATE);
        selected = Integer.parseInt(getSharedPreferences.getString("value", "0"));

        homeToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolbar);

        drawer_menu = (NavigationView) findViewById(R.id.drawer_menu);
        NavigationMenuView  navigationMenuView = (NavigationMenuView) drawer_menu.getChildAt(0);
        navigationMenuView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, DividerItemDecoration.VERTICAL));
        nav_drawer = (DrawerLayout) findViewById(R.id.nav_drawer);

        drawer_menu.bringToFront();
        toggle = new ActionBarDrawerToggle(this, nav_drawer, homeToolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        nav_drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Displaying logged in user name.
        navHeader = drawer_menu.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.userName);
        userEmail = (TextView) navHeader.findViewById(R.id.userEmail);
        UID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(UID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("name"));
                userEmail.setText(value.getString("email"));
            }
        });


        signOutDialog = new Dialog(this);
        shareMsg = "I recommend you to download and use Dil Se app to shop some amazing gift items for " +
                "your loved ones in various occasions. They provide contact-less and on time delivery " +
                "at your door step.";

        drawer_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.orders:
                        startActivity(new Intent(HomeActivity.this, MyOrdersActivity.class));
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile: openProfileSheet();
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.languages:changeLanguage();
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.info: openInfoDialog();
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.share: shareApp();
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.signOut: signOutFromApp();
                        nav_drawer.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        searchContainer = (LinearLayout) findViewById(R.id.searchContainer);
        searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        imageSlider = (ImageSlider) findViewById(R.id.image_slider);
        List<SlideModel> slider_images = new ArrayList<>();
        firebaseFirestore.collection("slider_images").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(HomeActivity.this, "Error Loading Images", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot index : queryDocumentSnapshots.getDocuments()){
                                slider_images.add(new SlideModel(index.get("url").toString(), ScaleTypes.FIT));
                            }
                            imageSlider.setImageList(slider_images, ScaleTypes.FIT);
                        }
                    }
                });

        //Banner Image
        upper_banner = (ImageView) findViewById(R.id.upper_banner);
        DocumentReference upperBannerReference = firebaseFirestore.collection("banner").document("banner_upper");
        upperBannerReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String image_path = value.getString("url").toString();
                Glide.with(HomeActivity.this).load(image_path).into(upper_banner);
            }
        });

        //Top Deals
        deals_first = (ImageSlider) findViewById(R.id.deals_first);
        List<SlideModel> deals_st_img = new ArrayList<>();
        firebaseFirestore.collection("deals_first_img").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(HomeActivity.this, "Error Loading Images", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot index : queryDocumentSnapshots.getDocuments()){
                                deals_st_img.add(new SlideModel(index.get("url").toString(), ScaleTypes.FIT));
                            }
                            deals_first.setImageList(deals_st_img, ScaleTypes.FIT);
                        }
                    }
                });

        deals_first_title = (TextView) findViewById(R.id.deals_first_title);
        deals_first_desc = (TextView) findViewById(R.id.deals_first_desc);
        DocumentReference dealOne = firebaseFirestore.collection("deals_text").document("first_deal");
        dealOne.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                deals_first_title.setText(value.getString("title"));
                deals_first_desc.setText(value.getString("desc"));
            }
        });

        deals_second = (ImageSlider) findViewById(R.id.deals_second);
        List<SlideModel> deals_second_img = new ArrayList<>();
        firebaseFirestore.collection("deals_second_img").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(HomeActivity.this, "Error Loading Images", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot index : queryDocumentSnapshots.getDocuments()){
                                deals_second_img.add(new SlideModel(index.get("url").toString(), ScaleTypes.FIT));
                            }
                            deals_second.setImageList(deals_second_img, ScaleTypes.FIT);
                        }
                    }
                });

        deals_second_title = (TextView) findViewById(R.id.deals_second_title);
        deals_second_desc = (TextView) findViewById(R.id.deals_second_desc);
        DocumentReference dealtwo = firebaseFirestore.collection("deals_text").document("second_deal");
        dealtwo.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                deals_second_title.setText(value.getString("title"));
                deals_second_desc.setText(value.getString("desc"));
            }
        });
        flowersCard = (CardView) findViewById(R.id.flowersCard);
        cardsCard = (CardView) findViewById(R.id.cardsCard);
        softToysCard = (CardView) findViewById(R.id.softToysCard);
        earphonesCard = (CardView) findViewById(R.id.earphonesCard);
        stationariesCard = (CardView) findViewById(R.id.stationariesCard);

        flowersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FlowersActivity.class));
            }
        });

        cardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CardsActivity.class));
            }
        });

        softToysCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SoftToysActivity.class));
            }
        });

        earphonesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EarphonesActivity.class));
            }
        });



        stationariesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StationariesActivity.class));
            }
        });

    }
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    public void signOutFromApp(){
        signOutDialog.setContentView(R.layout.signout_dialog);
        signOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signOutDialog.setCancelable(false);
        ImageView signOutCloseIcon = signOutDialog.findViewById(R.id.signOutCloseIcon);
        Button yesSignOut = signOutDialog.findViewById(R.id.yesSignOut);
        signOutCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutDialog.dismiss();
            }
        });
        yesSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
        });
        signOutDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.basket: startActivity(new Intent(HomeActivity.this, MyOrdersActivity.class));
            break;
            case R.id.profile: openProfileSheet();
            break;
        }
        return true;
    }

    public void openProfileSheet() {
        BottomSheetDialog profileBottomSheet = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(HomeActivity.this).inflate(
                R.layout.profile, (LinearLayout) findViewById(R.id.profileBottomSheetContainer)
        );
        TextView bottomUserName = bottomSheetView.findViewById(R.id.bottomUserName);
        TextView bottomUserEmail = bottomSheetView.findViewById(R.id.bottomUserEmail);
        TextView bottomUserPhone = bottomSheetView.findViewById(R.id.bottomUserPhone);
        DocumentReference documentReference = firebaseFirestore.collection("users").document(UID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                bottomUserName.setText(value.getString("name"));
                bottomUserEmail.setText(value.getString("email"));
                bottomUserPhone.setText(value.getString("phone"));
            }
        });
        profileBottomSheet.setContentView(bottomSheetView);
        profileBottomSheet.show();
    }

    public void openInfoDialog(){
        BottomSheetDialog infoBottomSheet = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(HomeActivity.this).inflate(
                R.layout.info, (LinearLayout) findViewById(R.id.infoContainer)
        );
        infoBottomSheet.setContentView(bottomSheetView);
        infoBottomSheet.show();
    }

    public void changeLanguage(){
        final String[] allLangs = {"English", "हिंदी", "मराठी", "ગુજરાતી", "বাংলা"};

        SharedPreferences preferences = getSharedPreferences("selected_value", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.LanguageDialogTheme);
        builder.setTitle("Please choose your language");
        builder.setSingleChoiceItems(allLangs, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocale("en");
                    editor.putString("value", Integer.toString(0));
                    editor.apply();
                } else if (which == 1){
                    setLocale("hi");
                    editor.putString("value", Integer.toString(1));
                    editor.apply();
                } else if (which == 2){
                    setLocale("mr");
                    editor.putString("value", Integer.toString(2));
                    editor.apply();
                } else if (which == 3){
                    setLocale("gu");
                    editor.putString("value", Integer.toString(3));
                    editor.apply();
                } else if (which == 4){
                    setLocale("bn");
                    editor.putString("value", Integer.toString(4));
                    editor.apply();
                }
            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Language", lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("Language", "");
        setLocale(language);
    }
}