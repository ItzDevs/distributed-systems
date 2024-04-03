package dev.distributed.service.rmi;

import dev.distributed.contract.TimestampHelper;
import dev.distributed.service.database.BlogDb;
import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.service.database.UserDb;
import dev.distributed.service.entities.Blog;
import dev.distributed.service.entities.User;
import dev.distributed.contract.rmi.IEditorRmi;
import dev.distributed.service.workers.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j(topic = "rmiEdtiorService")
public class RmiEditorService extends UnicastRemoteObject implements IEditorRmi {

    private UserDb userDb;

    private BlogDb blogDb;

    @Value("${rmi.enabled}")
    private boolean rmiEnabled;

    @Value("${rmi.host}")
    private String host;

    @Value("${rmi.port}")
    private int port;

    @Value("${rmi.hostname}")
    private String hostname;

    public RmiEditorService() throws RemoteException { }

    @Autowired
    public RmiEditorService(BlogDb blogDb,
                            UserDb userDb) throws RemoteException {
        super();

        this.userDb = userDb;
        this.blogDb = blogDb;
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
    public void post(NewBlog blog) {
        User resolvedUser = userDb.findById(blog.getAuthorId()).orElse(null);

        Blog newBlog = Blog.toBlog(blog, resolvedUser);
        blogDb.save(newBlog);

    }

    @Override
    public void update(UpdateBlog blog) {
        Optional<Blog> identifiedBlogContainer = blogDb.findById(blog.getId());
        if(identifiedBlogContainer.isEmpty())
            return;

        Blog identifiedBlog = identifiedBlogContainer.get();

        // Nothing to update
//        if(blog.isSame(identifiedBlog))
//            return;

        if(blog.getTitle() != null){
            identifiedBlog.setTitle(blog.getTitle());
        }
        if(blog.getContent() != null){
            identifiedBlog.setContent(blog.getContent());
        }
        if(blog.getMediaUrls()!= null){
            identifiedBlog.setMediaUrls(blog.getMediaUrls());
        }
        if(blog.getTags()!= null){
            identifiedBlog.setTags(blog.getTags());
        }
        if(blog.getAuthorId()!= null){
            identifiedBlog.setAuthor(userDb.findById(blog.getAuthorId()).orElse(null));
        }

        Timestamp timestamp = TimestampHelper.getCurrentTimestamp();
        identifiedBlog.setUpdatedAt(timestamp);
        blogDb.save(identifiedBlog);
    }

    @Override
    public void delete(UUID blog) {
        blogDb.deleteById(blog);
    }
}

