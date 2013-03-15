package uk.co.blackpepper.penguin.client;

import java.util.Arrays;

public class Queue
{
	private final String _id;

	private final String name;

	public Queue(String _id, String name)
	{
		if (_id == null)
		{
			throw new NullPointerException("_id");
		}

		if (name == null)
		{
			throw new NullPointerException("name");
		}

		this._id = _id;
		this.name = name;
	}

	public String getId()
	{
		return _id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[] {_id, name});
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
			&& name.equals(queue.getName());
	}

	@Override
	public String toString()
	{
		return String.format("%s[_id=%s, name=%s]", getClass().getName(), _id, name);
	}
}
