package com.irappelt.mymusic.dao;

import com.irappelt.mymusic.model.po.Charts;
import com.irappelt.mymusic.model.po.MusicLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/28 17:24
 * @version: v1.0
 */
public interface ChartsRespository extends JpaRepository<Charts, String> {

    @Query(value = "select * from Charts order by CreateTime desc limit 1", nativeQuery = true)
    List<Charts> getNewCharts();
}
