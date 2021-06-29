package com.eastrobot.doc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/29 20:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment implements Serializable {

    private String path;
    private String name;
    private Long size;

}
