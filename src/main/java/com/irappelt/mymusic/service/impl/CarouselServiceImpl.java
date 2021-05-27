package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.CarouselRespository;
import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.service.CarouselService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author huaiyu
 * @project: mymusic
 * @description TODO
 * @date 2021/4/28 17:28
 **/
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselRespository carouselRespository;

    @Override
    public Carousel getCarousel() {
        List<Carousel> newCarousel = carouselRespository.getNewCarousel();
        return newCarousel.get(0);
    }

    @Override
    public List<Carousel> getAllCarousel(Integer pageNo, Integer pageSize) {
        pageNo = (pageNo - 1) * pageSize;
        return carouselRespository.getAllCarousel(pageNo, pageSize);
    }

    @Override
    public long getAllCarouselCount() {
        return carouselRespository.count();
    }

    @Override
    public Carousel addCarousel(Carousel carousel) {
        carousel.setCarouselId(UUID.randomUUID().toString().replaceAll("-", ""));
        carousel.setCreateTime(new Date());
        carousel.setUpdateTime(new Date());
        return carouselRespository.save(carousel);
    }

    @Override
    public Carousel updateCarousel(Carousel carousel) {
        Carousel oldCarousel = carouselRespository.findById(carousel.getCarouselId()).orElse(null);
        if (oldCarousel != null) {
            BeanUtils.copyProperties(carousel, oldCarousel);
            oldCarousel.setUpdateTime(new Date());
            return carouselRespository.save(oldCarousel);
        }
        return null;
    }

    @Override
    public void deleteCarousel(List<String> carouselIds) {
        List<Carousel> carouselList = new ArrayList<>();
        for (String carouselId : carouselIds) {
            Carousel carousel = new Carousel();
            carousel.setCarouselId(carouselId);
            carouselList.add(carousel);
        }
        carouselRespository.deleteAll(carouselList);
    }
}
