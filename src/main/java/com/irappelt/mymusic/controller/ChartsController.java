package com.irappelt.mymusic.controller;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.model.po.Charts;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.service.ChartsService;
import com.irappelt.mymusic.service.MusicLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/28 17:13
 * @version: v1.0
 */
@Controller
@RequestMapping("/charts")
@ExceptionCapture
public class ChartsController {

    @Autowired
    private WebResponse webResponse;

    @Autowired
    private ChartsService chartsServiceImpl;

    @Autowired
    private MusicLinkService musicLinkServiceImpl;


    /**
     * 获取首页排行数据
     */
    @RequestMapping(value = "/getCharts", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getCharts() {

        Map<Object, Object> map = new HashMap<>(16);

        Charts charts = chartsServiceImpl.getCharts();
        List<String> idList = new ArrayList<>();
        String[] splitA = charts.getBlockA().split("\\|");
        String[] splitB = charts.getBlockB().split("\\|");
        String[] splitC = charts.getBlockC().split("\\|");
        String[] splitD = charts.getBlockD().split("\\|");
        String[] splitE = charts.getBlockE().split("\\|");
        idList.addAll(Arrays.asList(splitA));
        idList.addAll(Arrays.asList(splitB));
        idList.addAll(Arrays.asList(splitC));
        idList.addAll(Arrays.asList(splitD));
        idList.addAll(Arrays.asList(splitE));

        List<MusicLink> list = musicLinkServiceImpl.getMusicListByIdList(idList);

        map.put("list", list);
        return webResponse.getWebResponse(200, "获取排行成功", map);

    }

    /**
     * 获取轮播图数据
     */
    @RequestMapping(value = "/getCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getCarousel() {

        Map<Object, Object> map = new HashMap<>(16);

        Carousel carousel = chartsServiceImpl.getCarousel();

        String songIds = carousel.getSongIds();
        List<String> songIdList = Arrays.asList(songIds.split("\\|"));

        String picLinks = carousel.getPicLinks();
        List<String> picLinkList = Arrays.asList(picLinks.split("\\|"));


        List<MusicLink> list = musicLinkServiceImpl.getMusicListByIdList(songIdList);

        map.put("list", list);
        map.put("picLinkList", picLinkList);
        return webResponse.getWebResponse(200, "获取轮播数据成功", map);

    }



}
