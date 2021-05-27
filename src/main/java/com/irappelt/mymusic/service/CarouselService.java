package com.irappelt.mymusic.service;

import com.irappelt.mymusic.model.po.Carousel;

import java.util.List;

/**
 * @author huaiyu
 * @project: mymusic
 * @description TODO
 * @date 2021/4/28 17:27
 **/
public interface CarouselService {

    Carousel getCarousel();

    List<Carousel> getAllCarousel(Integer pageNo, Integer pageSize);

    long getAllCarouselCount();

    Carousel addCarousel(Carousel carousel);

    Carousel updateCarousel(Carousel carousel);

    void deleteCarousel(List<String> carouselIds);
}
