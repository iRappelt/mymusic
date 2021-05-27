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
import org.springframework.web.bind.annotation.*;

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
     * 分页获取排行
     */
    @RequestMapping(value = "/getAllCharts", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getAllCharts(Integer pageNo, Integer pageSize) {

        Map<Object, Object> map = new HashMap<>(16);

        List<Charts> charts = chartsServiceImpl.getAllCharts(pageNo, pageSize);
        long total = chartsServiceImpl.getChartsCount();

        map.put("list", charts);
        map.put("total", total);
        return webResponse.getWebResponse(200, "分页获取排行成功", map);
    }

    /**
     * 修改排行
     */
    @RequestMapping(value = "/updateTop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse updateTop(@RequestBody Charts charts) {
        Charts newCharts = chartsServiceImpl.updateCharts(charts);

        if (newCharts != null) {
            return webResponse.getWebResponse(200, "修改成功", newCharts);
        }
        return webResponse.getWebResponse(201, "修改失败", null);
    }

    /**
     * 新增排行
     */
    @RequestMapping(value = "/addTop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addTop(@RequestBody Charts charts) {
        Charts newCharts = chartsServiceImpl.addCharts(charts);
        if (newCharts != null) {
            return webResponse.getWebResponse(200, "添加成功", newCharts);
        }
        return webResponse.getWebResponse(201, "添加失败", null);
    }

    /**
     * 删除排行
     */
    @RequestMapping(value = "/deleteTop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse deleteTop(@RequestBody List<String> chartsIds) {
        chartsServiceImpl.deleteTop(chartsIds);
        return webResponse.getWebResponse(200, "删除成功", null);
    }


}
