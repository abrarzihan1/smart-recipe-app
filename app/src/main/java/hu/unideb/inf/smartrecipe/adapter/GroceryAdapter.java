package hu.unideb.inf.smartrecipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.model.GroceryItem;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {

    private final List<GroceryItem> itemList;
    private final OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDelete(GroceryItem item);
    }

    public GroceryAdapter(List<GroceryItem> itemList, OnDeleteClickListener deleteListener) {
        this.itemList = itemList;
        this.deleteListener = deleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewName);
            deleteButton = view.findViewById(R.id.buttonDelete);
        }
    }

    @NonNull
    @Override
    public GroceryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryAdapter.ViewHolder holder, int position) {
        GroceryItem item = itemList.get(position);
        holder.name.setText(item.getName());

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
