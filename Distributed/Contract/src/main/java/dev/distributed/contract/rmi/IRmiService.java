package dev.distributed.contract.rmi;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Due to the way RMI works, this interface is required to be used in both projects (Service and Publisher),
// this is for the Java Registry to be able to find the instance.

public interface IRmiService extends Remote {
    boolean post(NewBlog blog) throws RemoteException;

    boolean update(UpdateBlog blog) throws RemoteException;

    boolean delete(RemoveBlog blog) throws RemoteException;
}
