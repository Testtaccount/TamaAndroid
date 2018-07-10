package com.tama.chat.utils.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.tama.chat.App;
import com.tama.chat.R;
import java.util.Collections;

public class FirebaseAuthHelper {

    private static final String TAG = FirebaseAuthHelper.class.getSimpleName();

    public static final int RC_SIGN_IN = 456;
    public static final String EXTRA_FIREBASE_ACCESS_TOKEN = "extra_firebase_access_token";

    public void loginByPhone(Activity activity) {
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                        .setTheme(R.style.RedTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    public static FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public static void getIdTokenForCurrentUser(final RequestFirebaseIdTokenCallback callback) {
        if (App.getInstance().isSimulator) {
            String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk1OGE0NGZhNThmZGVkZDE1YTE1YmMwMzk1ODM5NGVjMDA0OTdjYzAifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGFtYS03YWVhYiIsImF1ZCI6InRhbWEtN2FlYWIiLCJhdXRoX3RpbWUiOjE1MzEyMzQwNzAsInVzZXJfaWQiOiI2OFVXdDBVOWdrWm4wUkNQZmtVQnVIdU04TVcyIiwic3ViIjoiNjhVV3QwVTlna1puMFJDUGZrVUJ1SHVNOE1XMiIsImlhdCI6MTUzMTIzNDA3MiwiZXhwIjoxNTMxMjM3NjcyLCJwaG9uZV9udW1iZXIiOiIrMzc0Nzc5Mzk3MzMiLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIiszNzQ3NzkzOTczMyJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.YC3nGqgxpzJxLAGjkrLxEps-yalwMspji-rZhR0a_sUbmW_zMdxvrlb3yf562g0n-XSXM8xyrDJ7VfLrijq1wSQPefPNVqERJeduT2ljGTBei_1mUBkege9vElN01onN5n8qcnRzBRHvhnrX_TeAUKPFChtornkjP1bqn8meORqv7oWbDj5vyBODnq0SBl5RG0vxQzGpUfeSYpiL4jucDZ9Y-MPP6tV7_udPoKbppVHh0vGiTRJAPT07iewXgvxcEO5smukxNzaCnrF53B9l30IxmvJVJhTsZThh1gRFjHfNAHi9Y5yHig9nbOaXsUN8Kl3pjCxgTubkxIqNpR9iCA";
            Log.v(TAG, "Token got successfully. TOKEN = " + accessToken);
            App.getInstance().getAppSharedHelper().saveFirebaseToken(accessToken);
            callback.onSuccess(accessToken);
        } else {
            if (getCurrentFirebaseUser() == null) {
                Log.v(TAG, "Getting Token error. ERROR = Current Firebse User is null");
                SharedHelper.getInstance().saveFirebaseToken(null);
                callback.onError(new NullPointerException("Current Firebse User is null"));
                return;
            }

            getCurrentFirebaseUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String accessToken = task.getResult().getToken();
                        Log.v(TAG, "Token got successfully. TOKEN = " + accessToken);
                        SharedHelper.getInstance().saveFirebaseToken(accessToken);
                        callback.onSuccess(accessToken);
                    } else {//eyJhbGciOiJSUzI1NiIsImtpZCI6IjAwOTZhZDZmZjdjMTIwMzc5MzFiMGM0Yzk4YWE4M2U2ZmFkOTNlMGEifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGFtYS03YWVhYiIsImF1ZCI6InRhbWEtN2FlYWIiLCJhdXRoX3RpbWUiOjE1MzA2MjkwNDYsInVzZXJfaWQiOiI4WDhRZm1xWHZCZkVIckJ4MmhHZ1h5dHNENWwyIiwic3ViIjoiOFg4UWZtcVh2QmZFSHJCeDJoR2dYeXRzRDVsMiIsImlhdCI6MTUzMDYyOTA0OSwiZXhwIjoxNTMwNjMyNjQ5LCJwaG9uZV9udW1iZXIiOiIrMzc0NzczMDA1ODciLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIiszNzQ3NzMwMDU4NyJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.NjZY8VtPINJB6tcnpvsAsG_WGeMxSYVtIkBAX-aPNnwsQgXWWe-kTccn3ld2EmRJ5emmPBErQ-byxZrmi76zCi0uzEhqcMizQ_fI_KlyleZJ-MJ5AgvC1FvF9UZEP9AQn09cUJsPwYMwKfbU8xEe0PYfyx-l6cDE21Wj5pKCFhfJn8TLK6_cS_78mbgrXZoT1MOP97QHgupiAAzgZQDXlj-NApcof5D1ygzpTfPDCpDOAZTgT9ojCU7sg0r7STIv9qg6AA7RgNQLHvMWTuxWYglHEjV1HOMXY5La-H784nebVwAKAZDpjKVaDPDuV1CxtW6CBCB0uYitP281DxPLuw
                        Log.v(TAG, "Getting Token error. ERROR = " + task.getException().getMessage());
                        callback.onError(task.getException());
                    }
                }
            });
        }

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedHelper.getInstance().saveFirebaseToken(null);
    }

    public interface RequestFirebaseIdTokenCallback {

        void onSuccess(String accessToken);

        void onError(Exception e);
    }
}
