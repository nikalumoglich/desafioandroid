package co.tiozao.desafioandroid.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.tiozao.desafioandroid.R;
import co.tiozao.desafioandroid.model.ShotModel;
import co.tiozao.desafioandroid.view.OnItemClickListener;
import co.tiozao.desafioandroid.view.holder.ShotHolder;

public class ShotAdapter extends RecyclerView.Adapter<ShotHolder> {

    Context context;
    List<ShotModel> data;
    private OnItemClickListener onItemClickListener;

    public ShotAdapter(@NonNull Context context, @NonNull List<ShotModel> objects) {
        this.context = context;
        this.data = objects;
    }

    @Override
    public ShotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_shot, null);
        ShotHolder viewHolder = new ShotHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShotHolder holder, int position) {
        final ShotModel item = data.get(position);

        holder.setShot(item);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(item);
            }
        };

        holder.getImage().setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void clear() {
        if(data != null) {
            data.clear();
        }
    }

    public void setItems(List<ShotModel> objects) {
        data = objects;
    }

    public void appendItem(ShotModel object) {
        data.add(object);
    }

    public void appendItems(List<ShotModel> objects) {
        data.addAll(objects);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void sortBy(Comparator<ShotModel> comparator) {
        Collections.sort(data, comparator);
    }


}
