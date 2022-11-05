package it.comania.testautenticazionefirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.comania.testautenticazionefirebase.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {

    private static final String TAG = "TON_" + StartFragment.class.getName();

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private FragmentStartBinding binding;

    public StartFragment() {
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
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentStartBinding.inflate(inflater, container, false);

        // Go to login fragment
        binding.loginStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_loginFragment);
            }
        });

        // Log out and unpdate UI
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG, "Logout effettuato");
                checkSigned();
            }
        });

        // Go to forgotPassword fragment
        binding.updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_forgotPasswordFragment);
            }
        });

        binding.logoutBtn.setVisibility(View.GONE);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check login state and update UI
        checkSigned();
    }

    public void checkSigned() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // not logged
            binding.logoutBtn.setVisibility(View.GONE);
            binding.loginStartBtn.setVisibility(View.VISIBLE);
            binding.dataUserTv.setText("");
            Log.d(TAG, "Not Logged");
        } else {
            // logged
            binding.logoutBtn.setVisibility(View.VISIBLE);
            binding.loginStartBtn.setVisibility(View.GONE);

            String userMail = String.format("Your email is %s", currentUser.getEmail());
            binding.dataUserTv.setText(userMail);

            Log.d(TAG, "Logged");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}