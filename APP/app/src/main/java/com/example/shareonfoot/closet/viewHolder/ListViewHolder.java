package com.example.shareonfoot.closet.viewHolder;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shareonfoot.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;

    public ListViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler_view);
    }
}