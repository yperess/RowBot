package org.uvdev.rowbot.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alorma.github.sdk.bean.dto.response.Issue;
import com.alorma.github.sdk.bean.dto.response.IssueState;
import com.alorma.github.sdk.bean.dto.response.Label;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.utils.Cipher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GitHubIssueListAdapter extends StateRecyclerAdapter<RecyclerView.ViewHolder> implements
        View.OnClickListener, Comparator<Issue> {

    private static final String TAG = "GithubIssueAdapter";

    private static final int STATE_EMPTY = 1000;
    public static final int STATE_LOGIN_ERROR = 1001;
    public static final int STATE_LOADING_ERROR = 1002;

    public interface GitHubTokenListener {
        void onNewGithubToken(String token);
    }

    private final Context mContext;
    private final GitHubTokenListener mGitHubTokenListener;
    private String mErrorStatusMessage = null;

    private final List<Issue> mIssueList = new ArrayList<>();

    public GitHubIssueListAdapter(View container, Context context, GitHubTokenListener listener) {
        super(container);
        mContext = context;
        mGitHubTokenListener = listener;
    }

    public void setIssueList(List<Issue> issueList) {
        Collections.sort(issueList, this);
        mIssueList.clear();
        mIssueList.addAll(issueList);
        if (mIssueList.isEmpty()) {
            setState(STATE_EMPTY);
            return;
        }
        setState(STATE_OK);
        notifyDataSetChanged();
    }

    public void setState(int statusCode, String errorStatusMessage) {
        mErrorStatusMessage = errorStatusMessage;
        setState(statusCode);
    }

    @Override
    public int compare(Issue lhs, Issue rhs) {
        // -1 if lhs < rhs, 1 if lhs > rhs, 0 is lhs == rhs.

        // Sort open before closed
        if (lhs.state != rhs.state) {
            if (lhs.state == IssueState.open && rhs.state == IssueState.closed) {
                return -1;
            } else if (lhs.state == IssueState.closed && rhs.state == IssueState.open) {
                return 1;
            }
        }
        // Sort by milestone number. (No milestone first)
        if (lhs.milestone == null && rhs.milestone != null) {
            return -1;
        } else if (lhs.milestone != null && rhs.milestone == null) {
            return 1;
        } else if (lhs.milestone != null && rhs.milestone != null
                && lhs.milestone.number != rhs.milestone.number) {
            if (lhs.milestone.number < rhs.milestone.number) {
                return -1;
            } else if (lhs.milestone.number > rhs.milestone.number) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.github_error: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.github_no_issues, parent,
                    false);
                return new ErrorViewHolder(view);
            }
            case R.id.github_issue: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.github_issue, parent,
                        false);
                return new IssueViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int state, int position) {
        if (holder instanceof IssueViewHolder) {
            ((IssueViewHolder) holder).onBind(mIssueList.get(position));
        } else if (holder instanceof ErrorViewHolder) {
            ((ErrorViewHolder) holder).onBind(getState());
        }
    }

    @Override
    public int getItemCount(int state) {
        return state == STATE_OK ? mIssueList.size() : 1;
    }

    @Override
    public int getItemViewType(int state, int position) {
        return state == STATE_OK ? R.id.github_issue : R.id.github_error;
    }

    @Override
    public void onClick(View v) {
        if (getState() == STATE_LOGIN_ERROR) {
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.buglist_get_github_password_dialog_title)
                    .setView(R.layout.github_password_dialog)
                    .setNegativeButton(android.R.string.cancel, null /* listener */)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText password = (EditText) ((AlertDialog) dialog)
                                    .findViewById(R.id.password);
                            String token = Cipher.getGithubAccessToken(
                                    password.getText().toString());
                            mGitHubTokenListener.onNewGithubToken(token);
                        }
                    })
                    .show();
        }
    }

    public final class ErrorViewHolder extends RecyclerView.ViewHolder implements StateViewHolder {

        private final ImageView mIcon;
        private final TextView mErrorText;

        public ErrorViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mErrorText = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(GitHubIssueListAdapter.this);
        }

        public void onBind(int state) {
            switch (state) {
                case STATE_EMPTY:
                    mErrorText.setText("No issues found.");
                    mIcon.setImageResource(R.drawable.ic_check_mark);
                    break;
                case STATE_LOGIN_ERROR:
                    // Should tell the user that the password is needed.
                    mErrorText.setText(R.string.buglist_error_connecting_to_github);
                    mIcon.setImageResource(R.drawable.github_logo);
                    break;
                case STATE_LOADING_ERROR:
                    // Connection to github failed (network issue?).
                    mErrorText.setText(mErrorStatusMessage);
                    mIcon.setImageResource(R.drawable.github_logo);
                    break;
            }
        }

        @Override
        public boolean fillViewPort() {
            return true;
        }
    }

    public final class IssueViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private final ImageView mStatusIcon;
        private final TextView mTitleView;
        private final TextView mMilestoneView;
        private final TextView mAssignedStatusView;
        private final LinearLayout mLabelList;
        private final ListPopupWindow mLabelPopupWindow;
        private final GithubLabelListAdapter mLabelListAdapter = new GithubLabelListAdapter();

        public IssueViewHolder(View itemView) {
            super(itemView);
            mStatusIcon = (ImageView) itemView.findViewById(R.id.status);
            mTitleView = (TextView) itemView.findViewById(R.id.title);
            mMilestoneView = (TextView) itemView.findViewById(R.id.milestone);
            mAssignedStatusView = (TextView) itemView.findViewById(R.id.assigned_status);
            mLabelList = (LinearLayout) itemView.findViewById(R.id.label_list);

            mLabelPopupWindow = new ListPopupWindow(mContext);
            mLabelPopupWindow.setAdapter(mLabelListAdapter);
            mLabelPopupWindow.setAnchorView(mLabelList);

            mLabelList.setOnClickListener(this);
        }

        public void onBind(Issue issue) {
            mStatusIcon.setImageResource(issue.state == IssueState.open
                    ? R.drawable.ic_error_outline : R.drawable.ic_check_mark);
            mTitleView.setText(issue.title);
            mMilestoneView.setText(issue.milestone == null ? null : issue.milestone.title);
            mAssignedStatusView.setText(issue.assignee == null ? "Not Assigned"
                    : !TextUtils.isEmpty(issue.assignee.name) ? issue.assignee.name
                    : issue.assignee.login);
            mLabelList.removeAllViews();
            for (int i = 0, size = issue.labels.size(); i < size; ++i) {
                Label label = issue.labels.get(i);
                ImageView image = new ImageView(mContext);
                image.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                image.setImageResource(R.drawable.circle);
                image.setColorFilter(Color.parseColor("#" + label.color));
                mLabelList.addView(image);
            }
            mLabelListAdapter.setLabels(issue.labels);
        }

        @Override
        public void onClick(View v) {
            if (!mLabelListAdapter.isEmpty()) {
                mLabelPopupWindow.show();
            }
        }
    }

}
