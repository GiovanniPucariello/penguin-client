package uk.co.blackpepper.penguin.client;

import java.util.Arrays;

public class Story
{
	private final String _id;

	private final String reference;

	private final String title;

	private final String author;
	
	private final boolean merged;

	public Story(String _id, String reference, String title, String author, boolean merged)
	{
		if (_id == null)
		{
			throw new NullPointerException("_id");
		}

		if (title == null)
		{
			throw new NullPointerException("title");
		}

		if (reference == null)
		{
			throw new NullPointerException("reference");
		}

		if (author == null)
		{
			throw new NullPointerException("author");
		}

		this._id = _id;
		this.reference = reference;
		this.title = title;
		this.author = author;
		this.merged = merged;
	}
	
	/**
	 * Default constructor for GSON.
	 */
	private Story()
	{
		_id = null;
		reference = null;
		title = null;
		author = null;
		merged = false;
	}

	public String getId()
	{
		return _id;
	}

	public String getReference()
	{
		return reference;
	}

	public String getTitle()
	{
		return title;
	}

	public String getAuthor()
	{
		return author;
	}

	public boolean isMerged()
	{
		return merged;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[] {_id, reference, title, author, merged});
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Story))
		{
			return false;
		}

		Story story = (Story) object;

		return _id.equals(story.getId())
			&& reference.equals(story.getReference())
			&& title.equals(story.getTitle())
			&& author.equals(story.getAuthor())
			&& merged == story.isMerged();
	}

	@Override
	public String toString()
	{
		return String.format("%s[_id=%s, reference=%s, title=%s, author=%s, merged=%s]", 
			getClass().getName(), _id, reference, title, author, merged);
	}
}
