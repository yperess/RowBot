package org.uvdev.rowbot.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.uvdev.rowbot.R;
import org.uvdev.rowbot.ui.activities.ReleaseNotesActivity;
import org.uvdev.rowbot.ui.adapters.HelpAndFeedbackAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpAndFeedbackFragment extends BaseFragment implements View.OnClickListener {

    TextView mFeedback;
    ListView mDocumentList;

    ArrayList<String> mActionList;
    HashMap<String, Runnable> mActionMap;

    @Override
    protected Boolean hasNavDrawer() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionList = new ArrayList<>();
        mActionMap = new HashMap<>();
        initActions();
    }

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_help);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.help_and_feedback_fragment, container, false);
        mFeedback = (TextView) rootView.findViewById(R.id.feedback);
        mDocumentList = (ListView) rootView.findViewById(R.id.document_list);
        mDocumentList.setAdapter(new HelpAndFeedbackAdapter(getActivity(), mActionList));
        mDocumentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActionMap.get(mActionList.get(position)).run();
            }
        });

        mFeedback.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.feedback) {
            // Alpha or Beta versions get to post to their respective Google groups whereas prod
            // versions can only leave feedback in the play store.
            if (mParent.getRowBotApplication().getCurrentVersion().isProd()) {
                Toast.makeText(getActivity(), "Leaving feedback...", Toast.LENGTH_SHORT).show();
            } else if (mParent.getRowBotApplication().getCurrentVersion().isBeta()){
                mParent.showFragment(new TesterFeedbackFragment());
            } else if (mParent.getRowBotApplication().getCurrentVersion().isAlpha()) {
                mParent.showFragment(new BugListFragment());
            }
        }
    }

    private void initActions() {
        addAction("Release Notes", new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), ReleaseNotesActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void addAction(String actionName, Runnable runnable) {
        mActionList.add(actionName);
        mActionMap.put(actionName, runnable);
    }
}
