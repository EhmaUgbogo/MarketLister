package com.ehmaugbogo.marketlister.views.main.ui.list.Adapters;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.views.listItems.ListerItemActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class HolderAdapter extends RecyclerView.Adapter<HolderAdapter.ViewHolder> {
    private static final String TAG = "HolderAdapter";
    private List<ListItemHolder> holderList=new ArrayList<>();
    private FragmentActivity activity;

    private ListItemViewModel viewModel;

    public HolderAdapter(FragmentActivity activity, ListItemViewModel viewModel) {
        this.activity = activity;
        this.viewModel=viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_holder,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItemHolder currentHolder=holderList.get(position);

        holder.title_Tv.setText(currentHolder.getTitle());
        holder.desc_Tv.setText(currentHolder.getDescription());

        String dateTime = getDateTime(currentHolder.getCreated_at());
        holder.timeStamp_Tv.setText(dateTime);

        String level=setPriorityLevel(currentHolder.getPriority(),holder.priority_Tv);
        holder.priority_Tv.setText(level);

        observeHolderSize(holder, currentHolder);


        if(currentHolder.getReminderTime()!=0){
            holder.alarmImageView.setVisibility(View.VISIBLE);
            holder.listCount_Tv.setBackgroundColor(activity.getResources().getColor(R.color.colorGreen));
        }

    }

    private void observeHolderSize(@NonNull ViewHolder holder, ListItemHolder currentHolder) {
        viewModel.getItemsWithHolderId(currentHolder.getId()).observe(activity, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> listItems) {
                holder.listCount_Tv.setText(String.valueOf(listItems.size()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return holderList.size();
    }

    public void setHolderList(List<ListItemHolder> holderList) {
        this.holderList = holderList;
        notifyDataSetChanged();
    }

    public String getDateTime(long created_at) {
        Date date = new Date();
        date.setTime(created_at);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy | hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public String setPriorityLevel(int level,TextView textView){
        String text="";
        switch(level){
            case 0: text="  None  ";
                //textView.setBackgroundResource(context.getResources().getDrawable(R.drawable.priority_low_back));
                break;
            case 1: text="  Urgent  ";
                textView.setBackgroundResource(R.drawable.priority_urgent);
                break;
            case 2: text="  Important  ";
                textView.setBackgroundResource(R.drawable.priority_important);
                break;
        }
        return text;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title_Tv;
        TextView desc_Tv;
        TextView priority_Tv;
        TextView timeStamp_Tv;
        TextView listCount_Tv;
        ImageView alarmImageView;
        CardView parentCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_Tv=itemView.findViewById(R.id.list_holder_titleTextView);
            desc_Tv=itemView.findViewById(R.id.list_holder_descriptTextView);
            priority_Tv=itemView.findViewById(R.id.list_holder_priorityTextView);
            timeStamp_Tv=itemView.findViewById(R.id.list_holder_timestampTextView);
            listCount_Tv=itemView.findViewById(R.id.list_holder_counterTextView);
            alarmImageView =itemView.findViewById(R.id.list_holder_alarm);
            parentCard=itemView.findViewById(R.id.list_holder_RootLayout);

            itemViewOnClick(itemView);
            contextMenu(itemView);
        }

        private void itemViewOnClick(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ListerItemActivity.class);
                    intent.putExtra(ListerItemActivity.HOLDER_KEY,holderList.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });
        }

        private void contextMenu(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    ListItemHolder currentHolder=holderList.get(getAdapterPosition());

                    //GroupId - Id - Order - Naming

                    menu.setHeaderTitle("Choose an option");
                    menu.add(getAdapterPosition(),111,1,"Mark");
                    menu.add(getAdapterPosition(),112,2,"Edit");
                    menu.add(getAdapterPosition(),113,3,"Delete");
                    menu.add(getAdapterPosition(),114,4,"Delete Inner Lists");
                    menu.add(getAdapterPosition(),115,5,"Share List");
                    if(currentHolder.getReminderTime()==0){
                        menu.add(getAdapterPosition(),116,6,"Add Reminder");
                    } else{
                        menu.add(getAdapterPosition(),116,6,"Cancel Reminder");
                    }

                    menu.add(getAdapterPosition(),117,7,"View List Photos");
                }
            });
        }
    }
}
