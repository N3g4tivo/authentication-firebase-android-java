package it.comania.testautenticazionefirebase;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import it.comania.testautenticazionefirebase.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = "TON_" + ForgotPasswordFragment.class.getName();

    private FragmentForgotPasswordBinding binding;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    public ForgotPasswordFragment() {
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

        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        // Navigate up
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        // reset password
        binding.resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.emailEdtText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    resetPassword(email);
                }
            }
        });

        View view = binding.getRoot();
        return view;
    }

    public void resetPassword(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "resetEmail:success");
                    Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Log.w(TAG, "resetEmail:failure", task.getException());
                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}