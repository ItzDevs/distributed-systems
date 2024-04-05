package dev.distributed.contract.rmi;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IEditorRmi extends Remote {
    boolean post(NewBlog blog) throws RemoteException;

    boolean update(UpdateBlog blog) throws RemoteException;

    boolean delete(RemoveBlog blog) throws RemoteException;
}
