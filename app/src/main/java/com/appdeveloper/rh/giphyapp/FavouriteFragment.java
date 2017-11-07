package com.appdeveloper.rh.giphyapp;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    RecyclerView recyclerView;

    DBFavGif db;

    TrendingListAdapter adapter;
    ArrayList<GifObject> gifsArrayList = new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);
        db = new DBFavGif(getActivity().getBaseContext());

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity().getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView = (RecyclerView) v.findViewById(R.id.favRecListView);
        recyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        /*scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        */
        updateListView();

        return v;
    }

    private void updateListView() {
        gifsArrayList.clear();
        String title;
        String urlGif;

        db.open();
        Cursor c = db.getAllItems();
        if (c.moveToFirst()) {
            do {
                title = c.getString(1);
                urlGif = c.getString(2);

                GifObject obj = new GifObject(title, urlGif);
                gifsArrayList.add(obj);
            } while (c.moveToNext());
        }
        updateUI(gifsArrayList);
    }

//    private void loadNextData(int offset) {
//        Toast.makeText(getActivity().getBaseContext(), "Loading", Toast.LENGTH_SHORT).show();
//         String gifSearchUrl = setURL(mItemSearch, offset, mShowItems);
//        try {
//            updateListView(new URL(gifSearchUrl));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateUI(final ArrayList<GifObject> photosArrayList) {
        //gifsArrayList.addAll(photosArrayList);
        adapter = new TrendingListAdapter(getActivity().getBaseContext(), photosArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       // recyclerView.scrollToPosition(gifsArrayList.size() - photosArrayList.size() - 11);

        adapter.setOnItemClickListener(new TrendingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                GifObject objectToPass = photosArrayList.get(position);
                Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                intent.putExtra("theObject", objectToPass);
                startActivity(intent);
            }
        });
    }



}
