package com.appdeveloper.rh.giphyapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TrendingFragment extends Fragment implements DownloadGifs.Communicator {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    EditText searchEditText;
    DownloadGifs downloadGifs;

    TrendingListAdapter adapter;
    ArrayList<GifObject> gifsArrayList = new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;

    int mStartItemCount = 0;
    int mShowItems = 24;
    String mAPIKey = "715e5d5458cf4ad8a63ffd4249559d27";
    String mItemSearch = "";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trending, container, false);

        prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        mItemSearch = prefs.getString("search", "");

        gifsArrayList.clear();
        URL url = null;
        String gifSearchUrl = null;
        try {
            gifSearchUrl = setURL(mItemSearch, mStartItemCount, mShowItems);
            url = new URL(gifSearchUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        searchEditText = (EditText) v.findViewById(R.id.searchText);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        searchEditText.setText(mItemSearch);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity().getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        recyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                downloadGifs.cancel(true);
                gifsArrayList.clear();
                adapter.notifyDataSetChanged();
                scrollListener.resetState();
                mItemSearch = s.toString();
                editor.putString("search", mItemSearch);
                editor.commit();
                String gifSearchUrl = setURL(mItemSearch, mStartItemCount, mShowItems);
                try {
                    updateListView(new URL(gifSearchUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateListView(url);

        progressBar.setVisibility(View.VISIBLE);

        return v;
    }

    private void loadNextData(int offset) {
        Toast.makeText(getActivity().getBaseContext(), "Loading", Toast.LENGTH_SHORT).show();
        String gifSearchUrl = setURL(mItemSearch, offset, mShowItems);
        try {
            updateListView(new URL(gifSearchUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void updateListView(URL url) {
        downloadGifs = new DownloadGifs(this);
        downloadGifs.execute(url);
    }

    public String setURL(String mSearch, int mStart, int mCount) {
        String trending = "search";
        if (mSearch.equals("")) {
            trending = "trending";
        }
        String mURL = "http://api.giphy.com/v1/gifs/" + trending + "?q=" + mSearch + "&api_key=" +
                mAPIKey + "&limit=" + mCount + "&offset=" + mStart;
        return mURL;
    }

    @Override
    public void updateProgressTo(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void updateUI(ArrayList<GifObject> photosArrayList) {
        gifsArrayList.addAll(photosArrayList);
        progressBar.setVisibility(View.GONE);
        adapter = new TrendingListAdapter(getActivity().getBaseContext(), gifsArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyItemInserted(gifsArrayList.size() - photosArrayList.size() - 1);
        recyclerView.scrollToPosition(gifsArrayList.size() - photosArrayList.size() - 11);

        adapter.setOnItemClickListener(new TrendingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                GifObject objectToPass = gifsArrayList.get(position);
                Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                intent.putExtra("theObject", objectToPass);
                startActivity(intent);
            }
        });
    }
}
