package com.rowbot.ui.fragments;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.concept2.api.common.Constants;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth
        .GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.groupsmigration.GroupsMigration;
import com.google.api.services.groupsmigration.GroupsMigrationScopes;
import com.google.api.services.groupsmigration.model.Groups;
import com.rowbot.R;
import com.rowbot.ui.dialogs.ProgressDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class TesterFeedbackFragment extends BaseFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final int RC_ACCOUNT_PICKER = 1000;
    private static final int RC_AUTHORIZATION = 1001;
    private static final int RC_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GroupsMigrationScopes.APPS_GROUPS_MIGRATION,
            GmailScopes.GMAIL_COMPOSE };

    private static final String ALPHA_GROUP = "rowbot-alpha@googlegroups.com";
    private static final String BETA_GROUP = "rowbot-beta@googlegroups.com";

    private interface FeedbackType {
        int BUG_REPORT = 0;
        int FEATURE_REQUEST = 1;
    }

    private GoogleAccountCredential mCredential;
    private GroupsMigration mService;

    private Gmail mGmailService;

    private int mFeedbackType = FeedbackType.BUG_REPORT;

    private TextView mAccountName;
    private View mBugReportContainer;
    private EditText mBugReportWhatWentWrong;
    private EditText mBugReportReproSteps;
    private EditText mFeatureRequest;

    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();
    private final JsonFactory mJsonFactory = GsonFactory.getDefaultInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO get the account name from the profile.
        String accountName = getActivity()
                .getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE)
                .getString(PREF_ACCOUNT_NAME, null);
        mCredential = GoogleAccountCredential.usingOAuth2(getActivity().getApplicationContext(),
                Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accountName);
        mService = new GroupsMigration.Builder(mTransport, mJsonFactory, mCredential)
                .setApplicationName("RowBot")
                .build();
        mGmailService = new Gmail.Builder(mTransport, mJsonFactory, mCredential)
                .setApplicationName("RowBot")
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tester_feedback_fragment, container, false);
        root.findViewById(R.id.account_picker).setOnClickListener(this);
        mAccountName = (TextView) root.findViewById(R.id.account_name);
        ((Spinner) root.findViewById(R.id.feedback_type)).setOnItemSelectedListener(this);
        mBugReportContainer = root.findViewById(R.id.bug_report);
        mBugReportWhatWentWrong = (EditText) root.findViewById(R.id.bug_report_description);
        mBugReportReproSteps = (EditText) root.findViewById(R.id.bug_report_repro);
        mFeatureRequest = (EditText) root.findViewById(R.id.feature_request);
        root.findViewById(R.id.submit).setOnClickListener(this);

        mAccountName.setText(mCredential.getSelectedAccountName() == null ? "Choose an account..."
                : mCredential.getSelectedAccountName());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (isDeviceOnline()) {
            // Do stuff.
        } else {
            Toast.makeText(mParent, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        getActivity().getSharedPreferences(Constants.SHARED_PREF_FILE,
                                Context.MODE_PRIVATE).edit()
                                .putString(PREF_ACCOUNT_NAME, accountName).commit();
                        mAccountName.setText(accountName);
                    }
                } else {
                    Toast.makeText(getActivity(), "Account not specified...", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case RC_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case RC_AUTHORIZATION:
                chooseAccount();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_picker:
                chooseAccount();
                break;
            case R.id.submit:
                new SendFeedbackAsyncTask().execute();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mFeedbackType = position;
        mBugReportContainer.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        mFeatureRequest.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void chooseAccount() {
        Intent intent = AccountPicker.zza(mCredential.getSelectedAccount(), null,
                new String[] {GoogleAccountManager.ACCOUNT_TYPE}, true, null, null, null, null,
                false, 1, 0);
        startActivityForResult(intent, RC_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mParent.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mParent);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    private void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, mParent,
                        RC_GOOGLE_PLAY_SERVICES).show();
            }
        });
    }

    private final class SendFeedbackAsyncTask extends AsyncTask<Void, Void, Integer> {

        private static final String DIALOG_TAG = "ProgressDialog";

        private String mMessage = null;
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.createInstance(0,
                    R.string.send_feedback_progress_dialog_message);
            mDialog.show(getChildFragmentManager(), DIALOG_TAG);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                mMessage = mFeedbackType == FeedbackType.BUG_REPORT ? sendBugReport()
                        : sendFeatureRequest();
            } catch (GooglePlayServicesAvailabilityIOException availabilityException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        availabilityException.getConnectionStatusCode());
                return Activity.RESULT_CANCELED;
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                startActivityForResult(userRecoverableException.getIntent(),
                        RC_AUTHORIZATION);
                return Activity.RESULT_CANCELED;
            } catch (Exception e) {
                mMessage = e.getMessage();
                return Activity.RESULT_CANCELED;
            }
            return Activity.RESULT_OK;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mDialog.dismiss();
            Toast.makeText(mParent, mMessage, Toast.LENGTH_LONG).show();
        }

        private String sendBugReport() throws IOException {
            return sendMessage("Bug Report", new StringBuilder("What went wrong?\n")
                    .append(mBugReportWhatWentWrong.getText().toString())
                    .append("\n\nHow can we reproduce this bug?\n")
                    .append(mBugReportReproSteps.getText().toString()).toString());
        }

        private String sendFeatureRequest() throws IOException {
            return sendMessage("Feature Request",
                    new StringBuilder("What would you like to see us do better?\n")
                            .append(mFeatureRequest.getText().toString()).toString());
        }

        private String sendMessage(String subject, String message) throws IOException {
            String groupId = getCurrentVersion().isAlpha() ? ALPHA_GROUP : BETA_GROUP;
            String groupAddress = "<" + groupId + ">";
            String date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                    .format(new Date());
            String messageId = String.format("Message-ID: <%s@%s>",
                    String.valueOf(Math.random()).substring(2), groupAddress);
            StringBuilder builder = new StringBuilder(messageId)
                    .append("\nDate: ").append(date)
                    .append("\nTo: ").append(groupAddress)
                    .append("\nFrom: \"Trusted Tester\" <")
                            .append(mCredential.getSelectedAccountName()).append(">")
                    .append("\nSubject: ").append(subject)
                    .append("\n\n").append(message).append("\n");
            Log.d("Email", builder.toString());
            ByteArrayContent content = new ByteArrayContent("message/rfc822",
                    builder.toString().getBytes("UTF-8"));

            // Create the Gmail message.
            Message email = new Message();
            email.setRaw(Base64.encodeToString(builder.toString().getBytes(), Base64.URL_SAFE));
            // Send the email.
            Message execute = mGmailService.users().messages().send("me", email).execute();
            Log.d("Email", execute.toPrettyString());
            Groups result = mService.archive().insert(groupAddress, content).execute();
            return result.getResponseCode();
        }
    }
}
