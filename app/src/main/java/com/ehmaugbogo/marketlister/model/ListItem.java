package com.ehmaugbogo.marketlister.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class ListItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;
    private int amount;
    private int quantity;
    private String extraNote;

    @ForeignKey(entity = ListItemHolder.class, parentColumns = "id", childColumns = "holder_id" )
    private long holder_id;
    private long created_at;
    private long updated_at;
    private boolean isAccomplised;

    public ListItem(String name, int amount, int quantity, String extraNote, long holder_id) {
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
        this.extraNote = extraNote;
        this.holder_id = holder_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExtraNote() {
        return extraNote;
    }

    public void setExtraNote(String extraNote) {
        this.extraNote = extraNote;
    }

    public long getHolder_id() {
        return holder_id;
    }

    public void setHolder_id(long holder_id) {
        this.holder_id = holder_id;
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

    public boolean isAccomplised() {
        return isAccomplised;
    }

    public void setAccomplised(boolean accomplised) {
        isAccomplised = accomplised;
    }


    protected ListItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        amount = in.readInt();
        quantity = in.readInt();
        extraNote = in.readString();
        holder_id = in.readLong();
        created_at = in.readLong();
        updated_at = in.readLong();
        isAccomplised = in.readByte() != 0;
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeInt(amount);
        parcel.writeInt(quantity);
        parcel.writeString(extraNote);
        parcel.writeLong(holder_id);
        parcel.writeLong(created_at);
        parcel.writeLong(updated_at);
        parcel.writeByte((byte) (isAccomplised ? 1 : 0));
    }
}
