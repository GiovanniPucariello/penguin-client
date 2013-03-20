package uk.co.blackpepper.penguin.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static uk.co.blackpepper.penguin.client.Preconditions.checkNotNull;

public class Queue
{
	private final String _id;

	private final String name;

	private final List<Story> stories;
	
	public Queue(String _id, String name)
	{
		this(_id, name, null);
	}

	public Queue(String _id, String name, Collection<? extends Story> stories)
	{
		if (stories == null)
		{
			stories = Collections.emptyList();
		}

		this._id = checkNotNull(_id, "_id");
		this.name = checkNotNull(name, "name");
		this.stories = Collections.unmodifiableList(new ArrayList<Story>(stories));
	}
	
	/**
	 * Default constructor for GSON.
	 */
	Queue()
	{
		_id = "";
		name = "";
		stories = Collections.emptyList();
	}

	public String getId()
	{
		return _id;
	}

	public String getName()
	{
		return name;
	}
	
	public List<Story> getStories()
	{
		return stories;
	}

	public int getPendingCount()
	{
	    int pending = 0;
	    for (Story s : stories)
	    {
		if (!s.isMerged()) 
		{
		    pending++;
		}
	    }
	    return pending;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[] {_id, name, stories});
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Queue))
		{
			return false;
		}

		Queue queue = (Queue) object;

		return _id.equals(queue.getId())
			&& name.equals(queue.getName())
			&& stories.equals(queue.getStories());
	}

	@Override
	public String toString()
	{
		return String.format("%s[_id=%s, name=%s, stories=%s]", getClass().getName(), _id, name, stories);
	}
}
