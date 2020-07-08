package com.example.sevenspices.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sevenspices.R;
import com.example.sevenspices.Ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmationCode extends AppCompatActivity {
    private static final String TAG = "ConfirmationCode";

    private FirebaseAuth mAuth;
    private FirebaseUser currrentUser;
    private String credential;

    EditText editText1, editText2, editText3, editText4 , editText5,editText6;
    private ProgressBar progressBarConfirmation;
    private TextView textCOnfirm;
    private CardView cardConfirmationcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_code);

        progressBarConfirmation = findViewById(R.id.progressBarConfirmation);
        editText1 = findViewById(R.id.emailEditText1);
        editText2 = findViewById(R.id.emailEditText2);
        editText3 = findViewById(R.id.emailEditText3);
        editText4 = findViewById(R.id.emailEditText4);
        editText5 = findViewById(R.id.emailEditText5);
        editText6 = findViewById(R.id.emailEditText6);
        textCOnfirm = findViewById(R.id.textCOnfirm);
        cardConfirmationcode = findViewById(R.id.cardConfirmationcode);


        mAuth = FirebaseAuth.getInstance();
        currrentUser = mAuth.getCurrentUser();

        credential = getIntent().getStringExtra("otp");

        editText1.addTextChangedListener(new GenericTextWatcher(editText1));
        editText2.addTextChangedListener(new GenericTextWatcher(editText2));
        editText3.addTextChangedListener(new GenericTextWatcher(editText3));
        editText4.addTextChangedListener(new GenericTextWatcher(editText4));
        editText5.addTextChangedListener(new GenericTextWatcher(editText5));
        editText6.addTextChangedListener(new GenericTextWatcher(editText6));


    }


    public void Continueconfirm(View view) {
        String otp = editText1.getText().toString() +
                editText2.getText().toString() +
                editText3.getText().toString() +
                editText4.getText().toString()+
                editText5.getText().toString()+
                editText6.getText().toString();



        if (!otp.isEmpty()) {
            progressBarConfirmation.setVisibility(View.VISIBLE);
            textCOnfirm.setVisibility(View.GONE);
            cardConfirmationcode.setEnabled(false);

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(credential, otp);

            signInWithPhoneAuthCredential(phoneAuthCredential);


        } else {
            Toast.makeText(this, "Enter Verification Code", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currrentUser != null) {
            Intent intent = new Intent(ConfirmationCode.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.emailEditText1:
                    if (text.length() == 1)
                        editText2.requestFocus();
                    break;
                case R.id.emailEditText2:
                    if (text.length() == 1)
                        editText3.requestFocus();
                    else if (text.length() == 0)
                        editText1.requestFocus();
                    break;
                case R.id.emailEditText3:
                    if (text.length() == 1)
                        editText4.requestFocus();
                    else if (text.length() == 0)
                        editText2.requestFocus();
                    break;
                case R.id.emailEditText4:
                    if (text.length() == 1)
                        editText5.requestFocus();
                    else if (text.length()==0)
                        editText3.requestFocus();
                    break;
                    case R.id.emailEditText5:
                    if (text.length() == 1)
                        editText6.requestFocus();
                    else if (text.length()==0)
                        editText4.requestFocus();
                    break;
                    case R.id.emailEditText6:
                    if (text.length() == 0)
                        editText5.requestFocus();
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
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


                            Toast.makeText(ConfirmationCode.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ConfirmationCode.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            progressBarConfirmation.setVisibility(View.GONE);
                            textCOnfirm.setVisibility(View.GONE);
                            cardConfirmationcode.setEnabled(true);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ConfirmationCode.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBarConfirmation.setVisibility(View.GONE);
                            textCOnfirm.setVisibility(View.VISIBLE);
                            cardConfirmationcode.setEnabled(true);
                        }
                    }
                });
    }

}