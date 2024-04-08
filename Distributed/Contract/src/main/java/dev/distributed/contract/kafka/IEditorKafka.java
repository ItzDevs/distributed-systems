package dev.distributed.contract.kafka;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;

public interface IEditorKafka {
    void postBlog(String message);

    void updateBlog(String message);

    void deleteBlog(String message);
}
