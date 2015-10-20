package org.uvdev.rowbot.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.uvdev.rowbot.concept2api.Concept2;
import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.concept2api.ResultCallback;
import org.uvdev.rowbot.concept2api.rowbot.RowBot.LoadProfilesResult;
import org.uvdev.rowbot.concept2api.rowbot.profile.ProfileCreator;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.adapters.NavDrawerAdapter;
import org.uvdev.rowbot.ui.widgets.ProfileEditDetailView;
import org.uvdev.rowbot.ui.widgets.ProfileEditMainView;
import org.uvdev.rowbot.utils.StockImageUtils;

import java.util.Random;

public class ProfileEditDialogFragment extends BaseDialogFragment
        implements View.OnClickListener, DialogInterface.OnShowListener  {

    public static final String TAG = "ProfileEditDialog";

    private static final String ARG_IS_EDIT = "isEdit";
    private static final String ARG_CANCELABLE = "cancelable";

    public static ProfileEditDialogFragment createInstance(boolean isEdit, boolean cancelable) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_EDIT, isEdit);
        args.putBoolean(ARG_CANCELABLE, cancelable);
        ProfileEditDialogFragment dialogFragment = new ProfileEditDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(cancelable);
        return dialogFragment;
    }

    private static final String STATE_PAGE_ID = "pageId";
    private static final String STATE_CREATOR = "profileCreator";

    private static final int PAGE_BASIC_INFO_ID = 0;
    private static final int PAGE_DETAILS_ID = 1;

    private final UpdateProfileListener mUpdateProfileListener =
            new UpdateProfileListener(false /* isCreate */);
    private final UpdateProfileListener mCreateProfileListener =
            new UpdateProfileListener(true /* isCreate */);

    private int mPageId;

    private Button mNegativeButton;
    private Button mPositiveButton;
    private ProfileEditMainView mBasicInfoPage;
    private ProfileEditDetailView mDetailPage;

    private View mProgressBar;

    private ProfileCreator mProfileCreator;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean cancelable = getArguments().getBoolean(ARG_CANCELABLE, true);
        int titleResId = isEdit() ? R.string.profile_dialog_edit_title
                : R.string.profile_dialog_create_title;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(titleResId)
                .setView(R.layout.dialog_edit_profile)
                .setPositiveButton(R.string.next, null)
                .setNegativeButton(R.string.back, null)
                .setCancelable(cancelable);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mPageId = PAGE_BASIC_INFO_ID;
            if (isEdit()) {
                // Loading a profile to edit.
                mProfileCreator = new ProfileCreator(RowBotActivity.CURRENT_PROFILE.getValue());
            } else {
                // Creating a new profile.
                int selectedImage = new Random(System.currentTimeMillis())
                        .nextInt(StockImageUtils.STOCK_AVATAR_IMAGE_RES_IDS.length);
                mProfileCreator = new ProfileCreator()
                        .setImageId(selectedImage);
            }
        } else {
            mPageId = savedInstanceState.getInt(STATE_PAGE_ID);
            mProfileCreator = savedInstanceState.getParcelable(STATE_CREATOR);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        FragmentManager fragmentManager = getChildFragmentManager();
        AlertDialog alertDialog = (AlertDialog) dialog;
        mNegativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        mPositiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        mProgressBar = alertDialog.findViewById(R.id.progress);
        mBasicInfoPage = (ProfileEditMainView) alertDialog.findViewById(R.id.basic_info_page);
        mDetailPage = (ProfileEditDetailView) alertDialog.findViewById(R.id.detail_page);

        mNegativeButton.setOnClickListener(this);
        mPositiveButton.setOnClickListener(this);

        mBasicInfoPage.setProfile(mProfileCreator);
        mDetailPage.setProfile(mProfileCreator);
        setPageId(mPageId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PAGE_ID, mPageId);
        outState.putParcelable(STATE_CREATOR, mProfileCreator);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mNegativeButton.getId()) {
            setPageId(mPageId - 1);
            return;
        } else if (view.getId() == mPositiveButton.getId()) {
            if (mPageId == PAGE_DETAILS_ID) {
                // Done creating profile.
                mBasicInfoPage.updateProfileCreator(mProfileCreator);
                mDetailPage.updateProfileCreator(mProfileCreator);
                if (isEdit()) {
                    Concept2.RowBot.updateProfile(getActivity(), mProfileCreator)
                            .setResultCallback(mUpdateProfileListener);
                } else {
                    Concept2.RowBot.createNewProfile(getActivity(), mProfileCreator)
                            .setResultCallback(mCreateProfileListener);
                }
                // Show loading.
            } else {
                // Go to next page
                setPageId(mPageId + 1);
            }
            return;
        }
    }

    private void setPageId(int pageId) {
        switch (pageId) {
            case PAGE_BASIC_INFO_ID:
                mNegativeButton.setEnabled(false);
                mPositiveButton.setText(R.string.next);
                break;
            case PAGE_DETAILS_ID:
                mNegativeButton.setEnabled(true);
                mPositiveButton.setText(isEdit() ? R.string.save : R.string.create);
                break;
        }
        mBasicInfoPage.setVisibility(pageId == PAGE_BASIC_INFO_ID ? View.VISIBLE : View.GONE);
        mDetailPage.setVisibility(pageId == PAGE_DETAILS_ID ? View.VISIBLE : View.GONE);
        mPageId = pageId;
    }

    private boolean isEdit() {
        return getArguments().getBoolean(ARG_IS_EDIT, false);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private final class UpdateProfileListener implements ResultCallback<LoadProfilesResult> {

        private final boolean mIsCreate;
        public UpdateProfileListener(boolean isCreate) {
            mIsCreate = isCreate;
        }

        @Override
        public void onResult(LoadProfilesResult result) {
            if (result.getStatus() == Concept2StatusCodes.OK) {
                // Update the current profile.
                if (mIsCreate) {
                    NavDrawerAdapter.addProfile(result.getProfiles().get(0));
                } else {
                    NavDrawerAdapter.updateProfile(result.getProfiles().get(0));
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Failed to create profile", Toast.LENGTH_LONG).show();
            }
        }
    }
}
