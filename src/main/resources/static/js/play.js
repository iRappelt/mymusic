$(function () {// 注意：此代码块页面刷新即加载

    /************注意Dom对象和jQuery对象的区别，有些方法只能Dom对象使用，所以只能将jQuery对象转换为Dom对象，比如$("audio")是jQuery对象，转换为Dom对象即$("aduio")[0]或者$("audio").get(0)***********/

    /*********************************************文件下载******************************************/
    $('#musicDownload').click(function () {
        $.ajax({
            type: 'get',
            url: "/musicLink/download",
            data: {"songUrl": $.cookie("song_link")},
            success: function (data) {
                var blob = base64toBlob(data.file);
                const href = URL.createObjectURL(blob); //根据二进制对象创造新的链接
                const a = document.createElement('a');
                a.setAttribute('href', href);
                a.setAttribute('download', $.cookie("song_name") + ".mp3");
                a.click();
                URL.revokeObjectURL(href)
            },
            error: function (data) {

            }
        });
    });

    // base64字符串转Blob
    function base64toBlob(base64Str) {
        var bstr = atob(base64Str),
            n = bstr.length,
            u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        // 下载的是mp3格式的文件
        return new Blob([u8arr], {type: "audio/mp3"});
    }

    /*********************************************获取收藏歌曲列表******************************************/
    getList2();

    function getList2() {
        $.ajax({
            url: "myMusic/getMyMusicList",
            type: "POST",
            async: false,// 注意此处要避免异步，虽然可能导致页面停顿，但是避免了元素未填充问题，防止后面调用元素的方法无效果
            data: {
                "user_name": $.cookie("user_name"),
                "user_password": $.cookie("user_password"),
                "song_id": $.cookie("song_id")
            },
            success: function (data) {
                if (data.statusCode == "200") {

                    var musicList = [];
                    var str = '';
                    for (var i = 0; i < data.data.list.length; i++) { // 填充播放列表
                        var innerList = [];
                        innerList.push(data.data.list[i].songLink);
                        innerList.push(data.data.list[i].songName);
                        innerList.push(data.data.list[i].singer);
                        innerList.push(data.data.list[i].imageLink);
                        innerList.push(data.data.list[i].lyricLink);
                        innerList.push(data.data.list[i].songId);
                        musicList.push(innerList);
                    }

                    if ($.cookie("song_index") != undefined && $.cookie("song_index") != "" && $.cookie("song_index") != null) {
                        loopPalyMusic(musicList, $.cookie("song_index"));
                        $.removeCookie("song_index", {path: "/"});
                    }

                    if ($.cookie("new_song") != undefined && $.cookie("new_song") != "" && $.cookie("new_song") != null) {
                        playMusic();
                        $.removeCookie("new_song", {path: "/"});
                    }

                    for (var i = 0; i < data.data.list.length; i++) {
                        var a = i + 1;
                        str += '<li class="list_music" id="musicList' + i + '">\n' +
                            '                        <div class="list_number" id="music_num_'+data.data.list[i].songId+'">' + a + '</div>\n' +
                            '                        <div id= sName' + i + ' class="list_name" style="cursor: pointer;">' + data.data.list[i].songName +
                            '                        </div>\n' +
                            '<div><span class="glyphicon glyphicon-trash music_trash" id=sFav' + i + '></span></div>' +
                            '                        <div class="list_singer">' + data.data.list[i].singer + '</div>\n' +
                            // '                        <div class="list_time"><span class="time1">' + allTime + '</span>\n' +
                            '                    </li>';


                        // 播放
                        function play(i) {
                            $("#sNamee").on('click', '#sName' + i, function () {
                                // $.cookie("last_song_id", $.cookie("song_id"));// 保存上一个歌id
                                // $.cookie("song_id", data.data.list[i].songId, {expires: 7, path: "/"});
                                // $.cookie("song_link", data.data.list[i].songLink, {expires: 7, path: "/"});
                                // $.cookie("song_name", data.data.list[i].songName, {expires: 7, path: "/"});
                                // $.cookie("song_singer", data.data.list[i].singer, {expires: 7, path: "/"});
                                // $.cookie("song_photo", data.data.list[i].imageLink, {expires: 7, path: "/"});
                                // $.cookie("song_lyric", data.data.list[i].lyricLink, {expires: 7, path: "/"});
                                // window.location.href = "/play.html";
                                // playMusic();
                                loopPalyMusic(musicList, i);
                            });
                        }

                        play(i);

                        // 删除收藏
                        function play1(i) {
                            $("#sNamee").on('click', '#sFav' + i, function () {
                                $.cookie("song_id", data.data.list[i].songId, {expires: 7, path: "/"});
                                dle(i);
                            });
                        }

                        play1(i);
                    }

                    $("#sNamee").append(str);
                } else if (data.statusCode == "202") {
                    $("#content_list").html("<div class='no_music_list'>你还未收藏歌曲哦！赶快去收藏吧...</div>");
                } else {
                    $("#content_list").html("<div class='no_music_list'>访问收藏列表失败，请稍后重试...</div>");
                }
            },
            error: function () {
                alert("系统错误，请联系管理员");
                $("#content_list").html("<div class='no_music_list'>访问收藏列表失败，请稍后重试...</div>");
            }
        })
    }


    /*********************************************删除收藏的歌曲******************************************/
    function dle() {
        $.ajax({
            async: false,
            url: "/myMusic/deleteMyMusic",
            type: "post",
            data: {
                "song_id": $.cookie("song_id"),
                "user_id": $.cookie("user_id")
            },
            success: function (data) {
                if (data.statusCode == "200") {
                    let str = "#musicList" + i;
                    $(str).remove();
                    alert("已成功从收藏列表移除");
                } else {
                    alert("add error!" + data.statusMsg);
                }

            },
            error: function (data) {
                alert("系统错误，请联系管理员");
            }

        })
    }

    /*********************************************收藏正在播放的歌曲******************************************/
    $('#musicCollect').click(function () {
        $.ajax({
            async: false,
            url: "/musicLink/addMusicCollect",
            type: "post",
            data: {
                "songName": $.cookie("song_name"),
                "song_id": $.cookie("song_id"),
                "user_name": $.cookie("user_name"),
                "user_password": $.cookie("user_password")
            },
            success: function (data) {
                if (data.statusCode == "200") {
                    alert("歌曲收藏成功，请去我的音乐查看！")
                    //location.reload();
                } else {
                    alert("亲！您已经收藏这首歌了哦，快去我的音乐中查看吧");
                }
            },
            error: function () {
                alert("系统错误，请联系管理员");
            }
        })
    });

    /***************************************************歌曲列表滚动条效果-调用插件******************************/
    $("#content_list").mCustomScrollbar();

    // 鼠标拖动进度条有bug，暂时不用
    // progress.progressMove(function (value) {
    //     let duration = $audio[0].duration;
    //     let currentTime = duration * value;
    //     $audio[0].currentTime = Math.round(currentTime);
    // });



    /******************************************************点击切换播放暂停图标*************************/
    $(".list_check").click(function () {
        $(this).toggleClass("list_checked");
    });
    $(".music_play").bind("click", function () {
        $(this).toggleClass("music_play2");
    });

    /********************************************************声音控制************************************/

    // 音量初始值
    $("audio")[0].volume = 0.5;

    // 音量进度条点击事件
    let $progressBar = $(".music_voice_bar");
    let $progressLine = $(".music_voice_line");
    let $progressDot = $(".music_voice_dot");
    let progress = Progress($progressBar, $progressLine, $progressDot);
    progress.progressMove(function (value) {
        // 歌曲时间同步
        let num = value;
        num = num.toFixed(2) * 10;
        $("audio")[0].volume = num;

    });

    let voice_stop = false;
    // 音量禁音开关
    $("#voice_stop").bind("click", function () {
        if (!voice_stop) {
            $("audio")[0].volume = 0;
            voice_stop = true;
            $progressLine.css("width", 0);
            $progressDot.css("left", 0);
            $(".music_voice_icon").toggleClass("music_voice_stop");
        } else {
            $("audio")[0].volume = 0.5;
            voice_stop = false;
            $progressLine.css("width", 35);
            $progressDot.css("left", 35);
            $(".music_voice_icon").toggleClass("music_voice_stop");
        }
    });

    /**************************************歌曲单曲循环开关*******************************/
    let loop = false;
    $("#music_mode").click(function () {
        if (!loop) {
            $("audio")[0].loop = true;
            $(".music_mode").toggleClass("music_mode4");
            loop = true;
        } else {
            $("audio")[0].loop = false;
            $(".music_mode").toggleClass("music_mode4");
            loop = false;
        }
    });

    /*************************************localStorage设置：播放不开启新窗口*****************/

    // 播放时设置localStorage的值
    window.localStorage.setItem("new_page", String((new Date()).getMilliseconds()));


    // 页面关闭事件：删除localStorage中的new_page值，使得音乐播放不开启新窗口，但要注意此时页面刷新也会清除掉此值
    window.addEventListener('beforeunload', function () {
        window.localStorage.removeItem("new_page");
    }, false);

    // 监听localStorage值变化事件
    window.addEventListener("storage", function (e) {
        //console.log("change");
        playMusic();
    });

    /*************************************页面刷新仍然播放********************************/
    if ($.cookie("new_song") == null) {// 另外页面点击进来时不播放，因为已经调用了播放方法
        playMusic();
    }


});

/*********************************************单曲音乐播放方法，也是其它页面点击播放时调用的方法，本页面刷新也是调用这个方法******************************************/

function playMusic() {

    /*****************文本替换*********************/

    $("#audio").attr({'src': $.cookie("song_link")});//将cookie存入的播放路径调用出来传给播放标签
    $(".music_progress_name").text($.cookie("song_name") + " / " + $.cookie("song_singer"));//将cookie存入的歌名歌手调用出来替换原有文本
    $(".songName").text($.cookie("song_name"));//将cookie存入的歌名调用出来替换原有文本
    $(".songSinger").text($.cookie("song_singer"));//将cookie存入的歌手调用出来替换原有文本
    $("title").html($.cookie("song_name") + " / " + $.cookie("song_singer") + "...正在播放 ");//将cookie存入的歌名歌手调用出来替换网页标题
    $(".song_img").attr({'src': $.cookie("song_photo")});//替换歌曲图片和播放界面的背景图片

    let $audio = $("audio");

    /****************title滚动方法************/

    let keyWords = ($.cookie("song_name") + " / " + $.cookie("song_singer") + "...正在播放 ");
    function titleChange() {
        let keyList = keyWords.split("");
        let firstChar = keyList.shift();
        keyList.push(firstChar);
        keyWords = keyList.join("");
        document.title = keyWords;
    }

    // 清除上一个播放事件中的title滚动定时事件
    if ($.cookie("timer") != null) {
        //console.log("clear");
        clearInterval($.cookie("timer"));
    }
    // 开始这次的title滚动定时事件
    let timer = setInterval(titleChange, 500);
    $.cookie("timer", timer);

    /**************设置正在播放行样式***********/

    // 先移除上一首的播放样式
    if ($.cookie("last_song_id") != null && $.cookie("last_song_id") != "") {
        let last_playing = "#music_num_"+$.cookie("last_song_id");
        $(last_playing).removeClass("playing");
        $(last_playing).parents(".list_music").removeClass("playing_parent");
    }
    // 再设置正在播放的行样式
    let playing = "#music_num_"+$.cookie("song_id");
    $(playing).addClass("playing");
    $(playing).parents(".list_music").addClass("playing_parent");




    /****************歌曲播放、暂停事件**************/

    $(".music_play").toggleClass("music_play2", true);// 添加class,切换播放暂停图标，true代表添加，false代表移除
    let onOff = false;
    first(".music_play")[0].onclick = function () {
        let playing = "#music_num_"+$.cookie("song_id");
        if (onOff) {
            first("#audio").play();
            $(playing).addClass("playing");// 添加播放特效wave.gif
            onOff = false;
        }
        else {
            first("#audio").pause();
            $(playing).removeClass("playing");// 移除播放特效wave.gif
            onOff = true;
        }
    };

    // document对象转换方法
    function first(selecoter) {
        return selecoter.substring(0, 1) == "." ?
            document.getElementsByClassName(selecoter.substring(1)) :
            document.getElementById(selecoter.substring(1));
    }


    /**************************************************************************歌词滚动相关*********************************************/
    let oLRC = {
        ti: "", //歌曲名
        ar: "", //演唱者
        al: "", //专辑名
        by: "", //歌词制作人
        offset: 0, //时间补偿值，单位毫秒，用于调整歌词整体位置
        ms: [] //歌词数组{t:时间,c:歌词}
    };

    /***********解析lrc歌词文件，填充oLRC对象************/
    function createLrcObj(lrc) {
        if (lrc.length == 0) return;
        let lrcs = lrc.split('\n');//用回车拆分成数组
        for (let i in lrcs) {//遍历歌词数组
            lrcs[i] = lrcs[i].replace(/(^\s*)|(\s*$)/g, ""); //去除前后空格
            if (lrcs[i].substring(0, 1) != "[") continue;
            let t = lrcs[i].substring(lrcs[i].indexOf("[") + 1, lrcs[i].indexOf("]"));//取[]间的内容
            let s = t.split(":");//分离:前后文字
            if (isNaN(parseInt(s[0]))) { //不是数值
                for (let i in oLRC) {
                    if (i != "ms" && i == s[0].toLowerCase()) {
                        oLRC[i] = s[1];
                    }
                }
            } else { //是数值
                let arr = lrcs[i].match(/\[(\d+:.+?)\]/g);//提取时间字段，可能有多个
                let start = 0;
                for (let k in arr) {
                    start += arr[k].length; //计算歌词位置
                }
                let content = lrcs[i].substring(start);//获取歌词内容
                for (let k in arr) {
                    let t = arr[k].substring(1, arr[k].length - 1);//取[]间的内容
                    let s = t.split(":");//分离:前后文字
                    oLRC.ms.push({//对象{t:时间,c:歌词}加入ms数组
                        t: (parseFloat(s[0]) * 60 + parseFloat(s[1])).toFixed(3),
                        c: content
                    });
                }
            }
        }
        oLRC.ms.sort(function (a, b) {//按时间顺序排序
            return a.t - b.t;
        });
    }


    /****必须要多此一举，重新填充song_lyric_span对象，否则translateY()不正常*****/
    $(".song_lyric_span")[0].innerHTML = "<ul id=\"song_lyric\" class=\"aplayer-lrc-contents\" style=\"transform: translateY(0px);\">" +
        "</ul><ul id=\"song_lyric_2\" class=\"aplayer-lrc-contents\" style=\"transform: translateY(0px);\">";

    let ul = $("#song_lyric")[0];//获取ul

    /***********调用服务端方法解析歌词链接，根据歌词链接获得歌词文本***********************/
    let lrc = "";
    if ($.cookie("song_lyric") != undefined && $.cookie("song_lyric") != "" && $.cookie("song_lyric") != null) {
        $.ajax({
            async: false,
            url: "/musicLink/lyric?lyricLink=" + $.cookie("song_lyric"),
            type: "get",
            success: function (data) {
                lrc = data;
                createLrcObj(lrc);// 调用createLrcObj方法
            }
        });
    } else {
        document.getElementById("song_lyric_2").innerHTML = "<li class='on'><p>" + "暂无歌词" + "</p></li>";
    }

    /******************得到歌词对应时间的数组，并且填充所有歌词列表*********************/
    let lrcTime = [];//歌词对应的时间数组

    if ($.cookie("song_lyric") != "") {
        // ul里填充歌词，不知道为什么这里的innerHtml不会覆盖，而是追加
        for (var n in oLRC.ms) {//遍历ms数组，把歌词加入列表
            ul.innerHTML += "<li><p>" + oLRC.ms[n].c + "</p></li>";
        }
        // 填充时间数组
        for (var x = 0; x < oLRC.ms.length; x++) {
            lrcTime[x] = oLRC.ms[x].t;
        }
        // 如不另加一个结束时间，到最后歌词滚动不到最后一句
        lrcTime.push((parseFloat(lrcTime[oLRC.ms.length - 1]) + parseFloat("30.000")).toFixed(3));
    }

    /*******************歌词时间同步展示*********************/
    let $li = $("#song_lyric>li");//获取所有li
    let currentLine = 0;//当前播放到哪一句了
    let currentTime = 0;//当前播放的时间
    let ppxx = 0;//保存ul的translateY值

    // console.log(lrcTime.length+"="+lrcTime);

    // 歌曲时间同步监听事件
    $audio[0].addEventListener('timeupdate', function () {

        // 进度条同步
        if (!isNaN($audio[0].duration)) {
            let duration = $audio[0].duration;
            currentTime = $audio[0].currentTime;
            let timeStr = formatDate(currentTime, duration);
            $(".music_progress_time").text(timeStr);
            let value = currentTime / duration * 100;
            $('.music_progress_line').css({
                "width": value + '%'
            });
            $('.music_progress_dot').css("left", value + '%');


            // 歌词同步，必须要放在进度条同步if块里面，否则有bug
            if ($.cookie("song_lyric") != "") {
                for (j = 0, len = lrcTime.length - 1; j < len; j++) {
                    // console.log(j+"="+currentTime+"="+lrcTime[j]+"="+lrcTime[j+1]);
                    if (currentTime < lrcTime[j + 1] && currentTime > lrcTime[j]) {
                        currentLine = j;
                        ppxx = -currentLine * 32;
                        ul.style.transform = "translateY(" + ppxx + "px)";
                        $li.get(currentLine - 1).className = "";
                        $li.get(currentLine).className = "on";
                        break;
                    }
                }
            }

        }


    }, false);

    // 时间同步方法
    function formatDate(currentTime, duration) {
        let endMin = parseInt(duration / 60);
        let endSec = parseInt(duration % 60);
        if (endMin < 10) {
            endMin = '0' + endMin;
        }
        if (endSec < 10) {
            endSec = '0' + endSec;
        }
        let startMin = parseInt(currentTime / 60);
        let startSec = parseInt(currentTime % 60);
        if (startMin < 10) {
            startMin = '0' + startMin;
        }
        if (startSec < 10) {
            startSec = '0' + startSec;
        }
        return startMin + ":" + startSec + " / " + endMin + ":" + endSec;
    }

    /*****************进度条点击事件，点击进度条歌曲进度改变，歌词也同步变化************************/
    let $progressBar = $(".music_progress_bar");
    let $progressLine = $(".music_progress_line");
    let $progressDot = $(".music_progress_dot");
    let progress = Progress($progressBar, $progressLine, $progressDot);
    progress.progressClick(function (value) {
        // 歌曲时间同步
        let duration = $audio[0].duration;
        let currentTime = duration * value;
        $audio[0].currentTime = Math.round(currentTime);

        // 歌词同步：照理说点击了进度条就会触发上面的歌曲时间同步监听事件，但点击当前播放时间之后的进度条歌词能够同步，点击之前的进度条歌词却不能同步，所以这里在点击事件中也要监听歌词同步
        let currentLine = 0;//当前播放到哪一句了
        if ($.cookie("song_lyric") != "") {
            for (j = currentLine, len = lrcTime.length; j < len; j++) { // len=50
                if (currentTime < lrcTime[j + 1] && currentTime > lrcTime[j]) {
                    currentLine = j;
                    let ppxx = -currentLine * 32;
                    ul.style.transform = "translateY(" + ppxx + "px)";
                    $li.get(currentLine - 1).className = "";
                    $li.get(currentLine).className = "on";
                    break;
                }
            }
        }
    });

    /*************************歌曲正式播放********************/
    $audio[0].play();


}

/***************************************************循环播放方法,在本页面点击播放时调用的是这个方法***************************************************/

function loopPalyMusic(musicList, i) {
    let behindList = musicList.slice(i); // 切片
    let frontList = musicList.slice(0, i);
    behindList.push.apply(behindList, frontList); // 合并
    behindList.reverse(); // 反序

    let innerList = behindList.pop(); // 获取数组最后一个元素并删除

    $.cookie("last_song_id", $.cookie("song_id"));// 保存上一个歌id
    $.cookie("song_link", innerList[0], {expires: 7, path: "/"});
    $.cookie("song_name", innerList[1], {expires: 7, path: "/"});
    $.cookie("song_singer", innerList[2], {expires: 7, path: "/"});
    $.cookie("song_photo", innerList[3], {expires: 7, path: "/"});
    $.cookie("song_lyric", innerList[4], {expires: 7, path: "/"});
    $.cookie("song_id", innerList[5], {expires: 7, path: "/"});

    behindList.unshift(innerList);// 将最后一个元素添加到数组开头，实现循环

    /***************歌曲播放完毕监听事件：继续下一首********************/
    $("#audio")[0].addEventListener("ended", playEndedHandler, false);

    playMusic(); // 播放

    function playEndedHandler() {
        innerList = behindList.pop();
        $.cookie("last_song_id", $.cookie("song_id"));// 保存上一个歌id
        $.cookie("song_link", innerList[0], {expires: 7, path: "/"});
        $.cookie("song_name", innerList[1], {expires: 7, path: "/"});
        $.cookie("song_singer", innerList[2], {expires: 7, path: "/"});
        $.cookie("song_photo", innerList[3], {expires: 7, path: "/"});
        $.cookie("song_lyric", innerList[4], {expires: 7, path: "/"});
        $.cookie("song_id", innerList[5], {expires: 7, path: "/"});
        behindList.unshift(innerList);
        playMusic();
    }

    /******************播放上一首************************/
    $(".music_pre").click(function () {
        behindList.reverse();
        let temp = behindList.pop();
        behindList.unshift(temp);
        let innerList = behindList[behindList.length - 1]; // 获取数组最后一个元素

        $.cookie("last_song_id", $.cookie("song_id"));// 保存上一个歌id
        $.cookie("song_link", innerList[0], {expires: 7, path: "/"});
        $.cookie("song_name", innerList[1], {expires: 7, path: "/"});
        $.cookie("song_singer", innerList[2], {expires: 7, path: "/"});
        $.cookie("song_photo", innerList[3], {expires: 7, path: "/"});
        $.cookie("song_lyric", innerList[4], {expires: 7, path: "/"});
        $.cookie("song_id", innerList[5], {expires: 7, path: "/"});

        $("#audio")[0].addEventListener("ended", playEndedHandler, false);

        playMusic(); // 播放
        behindList.reverse();
    });

    /*****************播放下一首**********************/
    $(".music_next").click(function () {
        let innerList = behindList.pop(); // 获取数组最后一个元素并删除

        $.cookie("last_song_id", $.cookie("song_id"));// 保存上一个歌id
        $.cookie("song_link", innerList[0], {expires: 7, path: "/"});
        $.cookie("song_name", innerList[1], {expires: 7, path: "/"});
        $.cookie("song_singer", innerList[2], {expires: 7, path: "/"});
        $.cookie("song_photo", innerList[3], {expires: 7, path: "/"});
        $.cookie("song_lyric", innerList[4], {expires: 7, path: "/"});
        $.cookie("song_id", innerList[5], {expires: 7, path: "/"});

        behindList.unshift(innerList);// 将最后一个元素添加到数组开头，实现循环

        $("#audio")[0].addEventListener("ended", playEndedHandler, false);

        playMusic(); // 播放
    });
}

