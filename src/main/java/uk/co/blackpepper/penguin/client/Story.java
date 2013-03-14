package uk.co.blackpepper.penguin.client;

public class Story
{
	private final String _id;

	private final String reference;

	private final String title;

	private final String author;
	
	private final Boolean merged;

	public Story(String _id, String reference, String title, String author, Boolean merged)
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

		if (merged == null)
		{
			throw new NullPointerException("merged");
		}

		this._id = _id;
		this.reference = reference;
		this.title = title;
		this.author = author;
		this.merged = merged;
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

	public Boolean isMerged()
	{
		return merged;
	}

	@Override
	public int hashCode()
	{
		return (_id.hashCode() * 31) 
			+ (reference.hashCode() * 32) 
			+ (title.hashCode() * 33) 
			+ (author.hashCode() * 34)
			+ merged.hashCode();
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
			&& merged.equals(story.isMerged());
	}

	@Override
	public String toString()
	{
		return String.format("%s[_id=%s, reference=%s title=%s author=%s merged=%s]", 
			getClass().getName(), _id, reference, title, author, merged);
	}
}
