package com.irappelt.mymusic.controller;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.Carousel;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.service.CarouselService;
import com.irappelt.mymusic.service.MusicLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huaiyu
 * @project: mymusic
 * @description TODO
 * @date 2021/4/28 17:18
 **/
@Controller
@RequestMapping("/carousel")
@ExceptionCapture
public class CarouselController {

    @Autowired
    private WebResponse webResponse;

    @Autowired
    private CarouselService carouselServiceImpl;

    @Autowired
    private MusicLinkService musicLinkServiceImpl;

    /**
     * 获取轮播图数据
     */
    @RequestMapping(value = "/getCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getCarousel() {

        Map<Object, Object> map = new HashMap<>(16);

        Carousel carousel = carouselServiceImpl.getCarousel();

        String songIds = carousel.getSongIds();
        List<String> songIdList = Arrays.asList(songIds.split("\\|"));

        String picLinks = carousel.getPicLinks();
        List<String> picLinkList = Arrays.asList(picLinks.split("\\|"));


        List<MusicLink> list = musicLinkServiceImpl.getMusicListByIdList(songIdList);

        map.put("list", list);
        map.put("picLinkList", picLinkList);
        return webResponse.getWebResponse(200, "获取轮播数据成功", map);
    }

    /**
     * 分页获取轮播图数据
     */
    @RequestMapping(value = "/getAllCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getAllCarousel(Integer pageNo, Integer pageSize) {

        Map<Object, Object> map = new HashMap<>(16);

        List<Carousel> carousel = carouselServiceImpl.getAllCarousel(pageNo, pageSize);
        long total = carouselServiceImpl.getAllCarouselCount();

        map.put("list", carousel);
        map.put("total", total);
        return webResponse.getWebResponse(200, "获取轮播数据成功", map);
    }

    /**
     * 新增轮播图数据
     */
    @RequestMapping(value = "/addCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addCarousel(@RequestBody Carousel carousel) {

        Carousel newCarousel = carouselServiceImpl.addCarousel(carousel);

        return webResponse.getWebResponse(200, "获取轮播数据成功", newCarousel);
    }

    /**
     * 修改轮播图数据
     */
    @RequestMapping(value = "/updateCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse updateCarousel(@RequestBody Carousel carousel) {

        Carousel newCarousel = carouselServiceImpl.updateCarousel(carousel);

        return webResponse.getWebResponse(200, "修改轮播数据成功", newCarousel);
    }

    /**
     * 删除轮播图数据
     */
    @RequestMapping(value = "/deleteCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse deleteCarousel(@RequestBody List<String> carouselIds) {

        carouselServiceImpl.deleteCarousel(carouselIds);

        return webResponse.getWebResponse(200, "删除轮播数据成功", null);
    }

}
