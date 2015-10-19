package com.rowbot.ui.fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alorma.github.basesdk.client.BaseClient;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.MetaDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Issue;
import com.alorma.github.sdk.bean.dto.response.Label;
import com.alorma.github.sdk.services.issues.GetIssuesClient;
import com.rowbot.R;
import com.rowbot.ui.adapters.GitHubIssueListAdapter;
import com.rowbot.utils.Cipher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class BugListFragment extends BaseFragment implements View.OnClickListener,
        GitHubIssueListAdapter.GitHubTokenListener {

    private static final HashMap<String, String> GITHUB_ISSUE_FILTER = new HashMap<>();

    static {
        GITHUB_ISSUE_FILTER.put("filter", "all");
        GITHUB_ISSUE_FILTER.put("state", "all");
        GITHUB_ISSUE_FILTER.put("sort", "updated");
    }

    private StoreCredentials mCredentials;
    private GetIssuesClient mGetIssuesClient;
    private GitHubIssueListAdapter mIssueAdapter;

    private View mProgressBar;
    private RecyclerView mBugListRecyclerView;
    private View mFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This fragment requires API level 15, trampoline if lower.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            // TODO (#20) Leave this fragment and launch TesterFeedbackFragment instead.
        }

        mIssueAdapter = new GitHubIssueListAdapter(mParent, this);
        mCredentials = new StoreCredentials(mParent);
    }

    @Override
    public void onStart() {
        super.onStart();

        GithubDeveloperCredentials.init(new MetaDeveloperCredentialsProvider(mParent));
        mGetIssuesClient = new GetIssuesClient(mParent, GITHUB_ISSUE_FILTER);
        mGetIssuesClient.setStoreCredentials(mCredentials);
        mGetIssuesClient.setOnResultCallback(new BaseClient.OnResultCallback<List<Issue>>() {
            @Override
            public void onResponseOk(List<Issue> issues, Response response) {
                Iterator<Issue> iterator = issues.iterator();
                while (iterator.hasNext()) {
                    Issue issue = iterator.next();
                    if (!"yperess/RowBot".equals(issue.repository.full_name)) {
                        iterator.remove();
                        continue;
                    }
                    StringBuilder builder = new StringBuilder(issue.id)
                            .append(": ").append(issue.title).append(" @ ")
                            .append(issue.repository.full_name).append(" / ")
                            .append(issue.repository.id).append(" is ")
                            .append(issue.state.name()).append(" [");
                    for (Label label : issue.labels) {
                        builder.append(" ").append(label.name).append("/").append(label.color);
                    }
                    Log.d("GitHub", builder.append("]").toString());
                }
                Toast.makeText(mParent, "Got " + issues.size() + " issues", Toast.LENGTH_LONG)
                        .show();
                mProgressBar.setVisibility(View.GONE);
                mIssueAdapter.setIssueList(issues);
                mBugListRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(RetrofitError retrofitError) {
                Log.e("GitHub", retrofitError.getLocalizedMessage());
                Log.e("GitHub", retrofitError.getBody().toString());
                mProgressBar.setVisibility(View.GONE);
                mIssueAdapter.setLoadingError(R.drawable.github_logo, -1 /* iconTintColor */,
                        retrofitError.getLocalizedMessage());
                mBugListRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        refreshIssues();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.buglist_fragment, container, false);
        mProgressBar = root.findViewById(R.id.progress);
        mBugListRecyclerView = (RecyclerView) root.findViewById(R.id.bug_list);
        mFab = root.findViewById(R.id.fab);

        mBugListRecyclerView.setLayoutManager(new LinearLayoutManager(mParent));
        mBugListRecyclerView.setAdapter(mIssueAdapter);
        mFab.setOnClickListener(this);
        return root;
    }

    private void refreshIssues() {
        Log.d("GitHub", "Calling get issues...");
        if (TextUtils.isEmpty(mCredentials.token())) {
            // Need the token.
            mIssueAdapter.setLoadingError(R.drawable.github_logo, -1 /* iconTintColor */,
                    mParent.getString(R.string.buglist_error_connecting_to_github));
            mBugListRecyclerView.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mGetIssuesClient.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                mParent.showFragment(new TesterFeedbackFragment(), false /* showNav */);
                break;
        }
    }

    @Override
    public void onNewGithubToken(String token) {
        mCredentials.storeToken(token);
        refreshIssues();
    }
}
