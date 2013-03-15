package uk.co.blackpepper.penguin.client;

import java.util.List;

public interface StoryService
{
	List<Story> getAll(String queueId) throws ServiceException;

	List<Story> getMerged(String queueId) throws ServiceException;

	List<Story> getUnmerged(String queueId) throws ServiceException;
}
