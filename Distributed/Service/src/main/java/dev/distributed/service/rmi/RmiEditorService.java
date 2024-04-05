package dev.distributed.service.rmi;

import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.contract.rmi.IEditorRmi;
import dev.distributed.service.workers.BlogService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Service
@Slf4j(topic = "rmiEdtiorService")
public class RmiEditorService extends UnicastRemoteObject implements IEditorRmi {

    private BlogService blogService;

    @Value("${rmi.host}")
    private String host;

    @Value("${rmi.port}")
    private int port;

    @Value("${rmi.hostname}")
    private String hostname;

    public RmiEditorService() throws RemoteException { }

    @Autowired
    public RmiEditorService(BlogService blogService) throws RemoteException {
        super();

        this.blogService = blogService;
    }


    @PostConstruct // Using PostConstruct here so that this registry can automatically be updated after the RmiEditorService bean is created
    private void postConstruct(){
        try{
            // Configures the registry and binds the RmiEditorService bean to the registry on the specified port
            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry(port);

            String rmiUrl = String.format("rmi://%s:%d/%s", host, port, hostname);
            registry.rebind(rmiUrl, this);

            log.info("RMI Editor Service started; Generated URL {}", rmiUrl);
        }
        catch (RemoteException e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean post(NewBlog blog) {
        boolean result = blogService.postBlog(blog);
        if(result){
            log.info("Posted blog: {}", blog.getTitle());
        } else{
            log.warn("Failed to post blog: {}", blog.getTitle());
        }
        return result;
    }

    @Override
    public boolean update(UpdateBlog blog) {
        boolean result = blogService.updateBlog(blog);
        if(result){
            log.info("Updated blog: {}", blog.getId());
        } else{
            log.warn("Failed to update blog: {}", blog.getId());
        }
        return result;
    }

    @Override
    public boolean delete(RemoveBlog blog) {
        boolean result = blogService.deleteBlog(blog);
        if(result){
            log.info("Deleted blog: {}", blog.getBlogId());
        }
        else{
            log.warn("Failed to delete blog: {}", blog.getBlogId());
        }
        return result;
    }
}

