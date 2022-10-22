/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc.dao;

import com.ibothub.doc.entity.Attachment;
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
