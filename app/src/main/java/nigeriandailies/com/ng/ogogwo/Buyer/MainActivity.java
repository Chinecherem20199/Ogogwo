package nigeriandailies.com.ng.ogogwo.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import model.Users;
import nigeriandailies.com.ng.ogogwo.Prevalent;
import nigeriandailies.com.ng.ogogwo.R;
import nigeriandailies.com.ng.ogogwo.Sellers.SellerHomeActivity;
import nigeriandailies.com.ng.ogogwo.Sellers.SellerRegistrationActivity;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginbutton;
    private TextView sellerBegin;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginbutton = findViewById(R.id.main_login_btn);
        sellerBegin = findViewById(R.id.seller_begin);


        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });


        String UserPhoneNumberKey = Paper.book().read(Prevalent.UserPhonenumberKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneNumberKey != ""  && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneNumberKey)  && !TextUtils.isEmpty(UserPasswordKey)){

                AloowAccess(UserPhoneNumberKey, UserPasswordKey);

                loadingBar.setTitle("Already logged in ");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser !=null)
        {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity
                    .class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AloowAccess(final String phonenumber, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            //            Check if the user is having the same phone number and password, then login else dismiss
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phonenumber).exists()){
                    Users userData = dataSnapshot.child("Users").child(phonenumber).getValue(Users.class);
                    if (userData.getPhonenumber().equals(phonenumber)){
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

//                            this prevalent class dot currentOnlineUser is preventing the app from clashing while remembering the user his
//                            name and password while checked the remember me checkbox
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"The password is incorrect" , Toast.LENGTH_SHORT).show();

                        }

                    }

                }else {
                    Toast.makeText(MainActivity.this, "Account with this " +phonenumber + " does not exist", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
