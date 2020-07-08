package com.example.sevenspices.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sevenspices.R;
import com.example.sevenspices.Ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UsernameAndPassword extends AppCompatActivity {

    private static final String TAG = "UsernameAndPassword";

    EditText emailEditText, passwordEditText, countrycodeEditText, mobileNumberEditText;
    private ProgressBar progressBarUsernameandPassword;
    private TextView textViewUandP;
    private RelativeLayout relativeUandP;
    private CardView cardUserNameAndPassword;

    private FirebaseAuth mAuth;
    private FirebaseUser currrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_and_password);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        countrycodeEditText = findViewById(R.id.countrycodeEditText);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        progressBarUsernameandPassword = findViewById(R.id.progressBarUsernameandPassword);
        textViewUandP = findViewById(R.id.textViewUandP);
        relativeUandP = findViewById(R.id.relativeUandP);
        cardUserNameAndPassword = findViewById(R.id.cardUserNameAndPassword);


        mAuth = FirebaseAuth.getInstance();
        currrentUser = mAuth.getCurrentUser();

        cardUserNameAndPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mobileNumberEditText.getText().toString();
                String countryCode = countrycodeEditText.getText().toString();

                String properPhoneNumber = countryCode + " " + phoneNumber;

                if (!countryCode.isEmpty() && !phoneNumber.isEmpty()) {
                    if (phoneNumber.length() == 10) {
                        progressBarUsernameandPassword.setVisibility(View.VISIBLE);
                        cardUserNameAndPassword.setEnabled(false);
                        textViewUandP.setVisibility(View.GONE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                properPhoneNumber,
                                60,
                                TimeUnit.SECONDS,
                                UsernameAndPassword.this,
                                mcallback
                        );


                    } else {
                        Toast.makeText(UsernameAndPassword.this, "", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Snackbar snackbar= new   Snackbar.make(,"Fields cant be Empty",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(UsernameAndPassword.this, "Field Can't be Empty", Toast.LENGTH_SHORT).show();
                }


            }
        });



        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressBarUsernameandPassword.setVisibility(View.GONE);
                cardUserNameAndPassword.setEnabled(true);
                textViewUandP.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBarUsernameandPassword.setVisibility(View.GONE);
                cardUserNameAndPassword.setEnabled(true);
                textViewUandP.setVisibility(View.VISIBLE);


                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(UsernameAndPassword.this, ConfirmationCode.class);
                                intent.putExtra("otp", s);
                                startActivity(intent);
                            }
                        },5000
                );



            }
        };

    }





    @Override
    protected void onStart() {
        super.onStart();
        if (currrentUser != null) {
            Intent intent = new Intent(UsernameAndPassword.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");


                            Toast.makeText(UsernameAndPassword.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(UsernameAndPassword.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            progressBarUsernameandPassword.setVisibility(View.GONE);
                            textViewUandP.setVisibility(View.GONE);
                            cardUserNameAndPassword.setEnabled(true);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(UsernameAndPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBarUsernameandPassword.setVisibility(View.GONE);
                            textViewUandP.setVisibility(View.VISIBLE);
                            cardUserNameAndPassword.setEnabled(true);
                        }
                    }
                });
    }    }