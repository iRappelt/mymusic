package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.CarouselRespository;
import com.irappelt.mymusic.dao.ChartsRespository;
import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.model.po.Charts;
import com.irappelt.mymusic.service.ChartsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Override
    public Charts getCharts() {
        List<Charts> newCharts = chartsRespository.getNewCharts();
        return newCharts.get(0);
    }

    @Override
    public List<Charts> getAllCharts(Integer pageNo, Integer pageSize) {
        pageNo = (pageNo-1)*pageSize;
        return chartsRespository.getAllCharts(pageNo, pageSize);
    }

    @Override
    public Charts updateCharts(Charts charts) {
        Charts oldCharts = chartsRespository.findById(charts.getChartsId()).orElse(null);
        if (oldCharts != null) {
            BeanUtils.copyProperties(charts, oldCharts);
            oldCharts.setUpdateTime(new Date());
            return chartsRespository.save(oldCharts);
        }
        return null;
    }

    @Override
    public Charts addCharts(Charts charts) {
        charts.setCreateTime(new Date());
        charts.setUpdateTime(new Date());
        charts.setChartsId(UUID.randomUUID().toString().replaceAll("-", ""));
        return chartsRespository.save(charts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTop(List<String> chartsIds) {
        List<Charts> chartsList = new ArrayList<>();
        for (String chartsId: chartsIds) {
            Charts charts = new Charts();
            charts.setChartsId(chartsId);
            chartsList.add(charts);
        }
        chartsRespository.deleteAll(chartsList);
    }

    @Override
    public long getChartsCount() {
        return chartsRespository.count();
    }
}
