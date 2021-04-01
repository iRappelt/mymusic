package com.irappelt.mymusic.dao;

import com.irappelt.mymusic.model.po.Carousel;
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
 * @date: 2021/04/01 23:25
 * @version: v1.0
 */
public interface CarouselRespository extends JpaRepository<Carousel, String> {
    @Query(value = "select * from Carousel order by CreateTime desc limit 1", nativeQuery = true)
    List<Carousel> getNewCarousel();
}
