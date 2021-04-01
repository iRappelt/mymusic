package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.CarouselRespository;
import com.irappelt.mymusic.dao.ChartsRespository;
import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.model.po.Charts;
import com.irappelt.mymusic.service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/28 17:21
 * @version: v1.0
 */
@Service
public class ChartsServiceImpl implements ChartsService {

    @Autowired
    private ChartsRespository chartsRespository;

    @Autowired
    private CarouselRespository carouselRespository;

    @Override
    public Charts getCharts() {
        List<Charts> newCharts = chartsRespository.getNewCharts();
        return newCharts.get(0);
    }

    @Override
    public Carousel getCarousel() {
        List<Carousel> newCarousel = carouselRespository.getNewCarousel();
        return newCarousel.get(0);
    }
}
