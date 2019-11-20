package com.ehmaugbogo.marketlister.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EditHistory {
	@PrimaryKey
	private long id;
	private String body;
	private long itemId;
	private long holderId;
	private String created_at;
	private String updated_at;

	public EditHistory(String body, long itemId, long holderId) {
		this.body = body;
		this.itemId = itemId;
		this.holderId = holderId;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getBody()
	{
		return body;
	}

	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}

	public long getItemId()
	{
		return itemId;
	}

	public void setHolderId(long holderId)
	{
		this.holderId = holderId;
	}

	public long getHolderId()
	{
		return holderId;
	}

	
	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}