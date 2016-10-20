package com.example.suhaas.project1.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.suhaas.project1.R;
import com.example.suhaas.project1.model.Trailer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrailerAdapter extends ArrayAdapter<Trailer>{
    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_trailer, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Trailer trailer = getItem(position);
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.trailername.setText(trailer.getName());
        return view;

    }

    static class ViewHolder {
        @Bind(R.id.trailer_text_view)
        TextView trailername;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
