/*
 * powered by http://ibothub.com
 */
package com.eastrobot.doc.dao;

import com.eastrobot.doc.model.entity.Attachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/18 21:00
 */
@Repository
public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

}
