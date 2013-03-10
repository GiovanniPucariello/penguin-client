package uk.co.blackpepper.penguin.client;

import java.util.List;

public interface StoryService
{
	List<Story> getAll(String queueId) throws ServiceException;
}
