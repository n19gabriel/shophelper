package com.example.gabriel.shophelper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {
    private final static int RC_SING_IN =1;
    private final static String TAG = "AUTHENTICATION_ACTIVITY";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient googleApiClient;
    private DatabaseReference myRef;

    private EditText ETemail;
    private EditText ETpassword;
    private Button BlogIn;
    private Button BsingUp;
    private SignInButton BsingIngoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        ETemail = findViewById(R.id.et_email);
        ETpassword = findViewById(R.id.et_password);
        BlogIn = findViewById(R.id.log_in);
        BsingUp = findViewById(R.id.sing_up);
        BsingIngoogle = findViewById(R.id.sing_in_google);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(AuthenticationActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    Intent intent = new Intent(AuthenticationActivity.this, ShopListActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                }

            }
        };
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            //Intent intent = new Intent(AuthenticationActivity.this, Menu.class);
            //startActivity(intent);
        }
        BlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin(ETemail.getText().toString(),ETpassword.getText().toString());
            }
        });

        BsingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration(ETemail.getText().toString(),ETpassword.getText().toString());
            }
        });

        BsingIngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    Toast.makeText(AuthenticationActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AuthenticationActivity.this, Menu.class);
                    startActivity(intent);
                }else
                    Toast.makeText(AuthenticationActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AuthenticationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    FirebaseUser userF = mAuth.getCurrentUser();
                    String userId = userF.getUid();
                    User user = new User(userId, "user");
                    myRef.child("Users").child(userId).setValue(user);
                }
                else
                    Toast.makeText(AuthenticationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SING_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SING_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           if(result.isSuccess()){
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else{
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser userF = mAuth.getCurrentUser();
                            String userId = userF.getUid();
                            User user = new User(userId, "user");
                            myRef.child("Users").child(userId).setValue(user);
                        } else {
                            // If sign in fails, display a message to the User.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                        // ...
                    }
                });
    }
}
