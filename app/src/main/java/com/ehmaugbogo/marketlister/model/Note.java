package com.ehmaugbogo.marketlister.model;

public class Note {
	private long id;
	private String title;
	private String note;
	private String created_at;
	private String lastEdited_at;
	private int priority;
	private String tag;

	public Note(String title, String note) {
		this.title = title;
		this.note = note;
	}


	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getNote()
	{
		return note;
	}

	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public void setLastEdited_at(String lastEdited_at)
	{
		this.lastEdited_at = lastEdited_at;
	}

	public String getLastEdited_at()
	{
		return lastEdited_at;
	}
	
	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public String getTag()
	{
		return tag;
	}
	
}