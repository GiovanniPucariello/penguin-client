package uk.co.blackpepper.penguin.client;

import java.util.List;

public interface QueueService
{
    List<Queue> getAll() throws ServiceException;
}
