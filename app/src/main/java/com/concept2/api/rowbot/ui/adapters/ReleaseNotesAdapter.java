package com.concept2.api.rowbot.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.concept2.api.common.data.Version;
import com.concept2.api.rowbot.R;
import com.concept2.api.rowbot.help.ReleaseNotes;

public class ReleaseNotesAdapter extends
        RecyclerView.Adapter<ReleaseNotesAdapter.ReleaseNoteViewHolder> {

    @Override
    public ReleaseNoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.release_notes_card, viewGroup, false);
        ReleaseNoteViewHolder viewHolder = new ReleaseNoteViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return ReleaseNotes.sReleaseVersions.size();
    }

    @Override
    public void onBindViewHolder(ReleaseNoteViewHolder releaseNoteViewHolder, int position) {
        Version version = ReleaseNotes.sReleaseVersions.get(position);
        releaseNoteViewHolder.setTitle(version);
        releaseNoteViewHolder.setContent(ReleaseNotes.getReleaseNotes(version));
    }

    public static class ReleaseNoteViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mContent;

        public ReleaseNoteViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mContent = (TextView) itemView.findViewById(R.id.content);
        }

        public void setTitle(Version version) {
            mTitle.setText("V" + version.toString() + (version.isBeta() ? " (BETA)" : ""));
        }

        public void setContent(String content) {
            mContent.setText(Html.fromHtml(content));
        }
    }
}
