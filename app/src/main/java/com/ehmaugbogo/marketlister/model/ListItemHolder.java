package com.ehmaugbogo.marketlister.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListItemHolder implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private int priority;
    private long created_at;
    private long updated_at;
    private long reminderTime;


    public ListItemHolder(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }



    protected ListItemHolder(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        priority = in.readInt();
        created_at = in.readLong();
        reminderTime = in.readLong();
    }

    public static final Creator<ListItemHolder> CREATOR = new Creator<ListItemHolder>() {
        @Override
        public ListItemHolder createFromParcel(Parcel in) {
            return new ListItemHolder(in);
        }

        @Override
        public ListItemHolder[] newArray(int size) {
            return new ListItemHolder[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListItemHolder)) return false;
        ListItemHolder that = (ListItemHolder) o;
        return getId() == that.getId() &&
                getPriority() == that.getPriority() &&
                getCreated_at() == that.getCreated_at() &&
                getReminderTime() == that.getReminderTime() &&
                getTitle().equals(that.getTitle()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getPriority(), getCreated_at(), getReminderTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(priority);
        parcel.writeLong(created_at);
        parcel.writeLong(reminderTime);
    }
}
