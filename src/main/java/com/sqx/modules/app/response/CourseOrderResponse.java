package com.sqx.modules.app.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseOrderResponse  implements Serializable {
    /**
     * 短剧名称
     */
    private  String  coursename;
    /**
     * 售卖笔数
     */
    private  int  coursenum;
    /**
     * 售卖点券
     */
    private  Double coursemoney;
}
