package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Source;

import java.util.ArrayList;
import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private List<Source> sources;
    private AdapterListener listener;

    public SourceAdapter() {
        sources = new ArrayList<>();
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_source, parent, false);

        return new ViewHolder(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View itemView = holder.itemView;
        TextView txtSourceName = itemView.findViewById(R.id.txt_item_source_name);
        txtSourceName.setText(sources.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.itemView.setOnClickListener(
                    view -> listener.onClick(sources.get(getAdapterPosition())));
        }
    }

    public interface AdapterListener {

        void onClick(Source source);
    }
}
