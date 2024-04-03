package dev.distributed.contract.rmi;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IEditorRmi extends Remote {
    void post(NewBlog blog) throws RemoteException;

    void update(UpdateBlog blog) throws RemoteException;

    void delete(RemoveBlog blog) throws RemoteException;
}
