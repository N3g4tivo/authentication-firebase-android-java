package it.comania.testautenticazionefirebase;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import it.comania.testautenticazionefirebase.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private static final String TAG = "TON_" + LoginFragment.class.getName();

    private FragmentLoginBinding binding;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // this is for Google Authentication
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
        @Override
        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
            onSignInResult(result);
        }
    });

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Go to signup fragment
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });

        // Go to forgotPassword fragment
        binding.resetPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });

        // Log in email
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEdtText.getText().toString();
                String password = binding.passEdtText.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    login(email, password);
                }
            }
        });

        // Log in Google
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent();
            }
        });

        View view = binding.getRoot();
        return view;
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    ////////////////////////////////////
    /// START Google Authentication ////
    ////////////////////////////////////
    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
            Navigation.findNavController(requireView()).navigateUp();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", Objects.requireNonNull(result.getIdpResponse()).getError());
            updateUI(null);
        }
    }
    // [END auth_fui_result]

    ////////////////////////////////////
    ///  END Google Authentication  ////
    ////////////////////////////////////

    private void updateUI(FirebaseUser user) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}