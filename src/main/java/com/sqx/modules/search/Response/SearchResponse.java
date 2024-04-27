package com.sqx.modules.search.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse implements Serializable {

    //用户的搜索记录
    List<String> userSearchName = new ArrayList<>();
    //所有搜索记录前几名
    List<String> AllSerchName = new ArrayList<>();


}
