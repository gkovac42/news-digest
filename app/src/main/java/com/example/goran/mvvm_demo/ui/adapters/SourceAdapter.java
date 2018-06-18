package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Source;

public class SourceAdapter extends ListAdapter<Source, SourceAdapter.ViewHolder> {

    private AdapterListener listener;

    public SourceAdapter() {
        super(Source.DIFF_CALLBACK);
    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_source, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView txtSourceName = holder.itemView.findViewById(R.id.txt_item_source_name);
        txtSourceName.setText(getItem(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.itemView.setOnClickListener(
                    view -> listener.onClick(getItem(getAdapterPosition())));
        }
    }

    public interface AdapterListener {

        void onClick(Source source);
    }
}
