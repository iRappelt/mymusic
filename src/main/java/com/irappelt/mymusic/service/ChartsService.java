package com.irappelt.mymusic.service;

import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.model.po.Charts;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/28 17:21
 * @version: v1.0
 */
public interface ChartsService {
    Charts getCharts();

    Carousel getCarousel();
}
