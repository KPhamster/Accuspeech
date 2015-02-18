package edu.uci.accuspeech.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.service.AudioService;

public class PastRecordingsListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_recordings_list, container, false);
        return rootView;
    }

    public void onResume() {
        super.onResume();
        ViewGroup recordingList = (ViewGroup) getView().findViewById(R.id.recordings_list);
        View noFilesFound = getView().findViewById(R.id.empty_list);
        File directory = new File(AudioService.PAST_RECORDINGS_FILE_PATH);
        String[] listOfRecords = null;
        if (directory.exists()){
            listOfRecords = directory.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".wav");
                }
            });
        }

        // If there are no files, hide list and say there are no files
        if (listOfRecords == null || listOfRecords.length == 0) {
            noFilesFound.setVisibility(View.VISIBLE);
            recordingList.setVisibility(View.GONE);
        } else {
            // List out all the files that end in .wav
            noFilesFound.setVisibility(View.GONE);
            recordingList.setVisibility(View.VISIBLE);
            recordingList.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            for (String file : listOfRecords) {
                View pastRecording = inflater.inflate(R.layout.past_recording, recordingList, false);
                TextView name = (TextView) pastRecording.findViewById(R.id.name);
                name.setText(file);
                recordingList.addView(pastRecording);
            }
        }
    }
}
