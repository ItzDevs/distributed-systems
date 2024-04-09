package dev.distributed.publisher.rmi;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.contract.rmi.IRmiService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Service
@Slf4j(topic = "rmiPublisher")
public class RmiPublisher {

    @Value("${rmi.host}")
    private String host;

    @Value("${rmi.port}")
    private int port;

    @Value("${rmi.hostname}")
    private String hostname;

    IRmiService rmiComponent;

    public RmiPublisher() {

    }

    @PostConstruct
    public void postConstruct() {
        try{
            String rmiUrl = String.format("rmi://%s:%d/%s", host, port, hostname);
            log.debug(rmiUrl);
            Registry registry = LocateRegistry.getRegistry(port);
            rmiComponent = (IRmiService) registry.lookup(rmiUrl);
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public boolean post(NewBlog blog) throws RemoteException {
        return rmiComponent.post(blog);
    }

    public boolean update(UpdateBlog blog) throws RemoteException {
        return rmiComponent.update(blog);
    }

    public boolean delete(RemoveBlog blog) throws RemoteException {
        return rmiComponent.delete(blog);
    }
}
