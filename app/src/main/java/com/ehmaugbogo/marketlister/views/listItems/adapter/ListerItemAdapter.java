package com.ehmaugbogo.marketlister.views.listItems.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.views.edit.EditActivity;
import com.ehmaugbogo.marketlister.views.listItems.ListerItemActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListerItemAdapter extends RecyclerView.Adapter<ListerItemAdapter.ViewHolder> {
    private static final String TAG = "HolderAdapter";
    private List<ListItem> itemList=new ArrayList<>();
    private Context context;

    public ListerItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem currentItem=itemList.get(position);

        holder.nameTextView.setText(currentItem.getName());
        holder.amountTextView.setText(String.valueOf(currentItem.getAmount()));
        holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));

        if(currentItem.isAccomplised()){
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setTag(currentItem);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<ListItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView quantityTextView;
        TextView amountTextView;
        TextView timeStamp;
        CheckBox checkBox;
        LinearLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.item_NameTextView);
            quantityTextView=itemView.findViewById(R.id.item_quantityTextView);
            amountTextView=itemView.findViewById(R.id.item_amountTextView);
            checkBox=itemView.findViewById(R.id.item_CheckBox);
            timeStamp=itemView.findViewById(R.id.item_timestampTextView);
            rootLayout=itemView.findViewById(R.id.item_RootLayout);

            itemViewOnClick(itemView);
            contextMenu(itemView);
        }

        private void itemViewOnClick(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra(EditActivity.ITEM_KEY,itemList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

        private void contextMenu(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    ListItem currentItem=itemList.get(getAdapterPosition());

                    menu.setHeaderTitle("Choose an option");
                    menu.add(getAdapterPosition(),113,1,"Edit");
                    menu.add(getAdapterPosition(),112,2,"Delete Inner Lists");
                    menu.add(getAdapterPosition(),111,3,"Delete");
                    menu.add(getAdapterPosition(),114,4,"View");
                    menu.add(getAdapterPosition(),115,5,"Share List");
                    menu.add(getAdapterPosition(),117,7,"View List Photos");
                }
            });
        }
    }
}
