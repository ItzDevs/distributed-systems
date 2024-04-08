package dev.distributed.contract.kafka;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;

public interface IEditorKafka {
    boolean post(NewBlog blog);

    boolean update(UpdateBlog blog);

    boolean delete(RemoveBlog blog);
}
