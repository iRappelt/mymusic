
/************************分页获取榜单*************************/
function pageTools(curtpage, tpage, topType) {
    var options = {
        bootstrapMajorVersion: 3, //版本
        currentPage: curtpage, //当前页数
        numberOfPages: 5, //设置显示的页码数
        totalPages: tpage, //总页数
        alignment: "center", // 居中显示
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "末页";
                case "page":
                    return page;
            }
        },
        onPageClicked: function (event, originalEvent, type, page) {
            getList(page, topType); //  在页面中的方法名称。
        }
    };
    $("#pagintor").bootstrapPaginator(options); // $("#pagintor") Bootstrap 是2.X 使用div元素，3.X使用ul元素
}



/***************************************修改密码方法***********************/

function setPassWord(url, data) {                   //url和data为参数，在调用该方法是传入
    $.ajax({
        url: url,
        type: "post",
        data: data,
        success: function (data) {
            if (data.statusCode == "200") {
                window.location.href = "/index.html";// 跳转到指定页面
                alert("修改密码成功，请重新登录！"); // 弹窗
            } else {
                alert("add error!" + data.statusMsg);
            }
            ;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        },
    });
}

/*************************登录*************************************/

function denglu(url, data) {                                                 //url和data为参数，在调用该方法是传入
    $.ajax({
        url: url,
        type: "post",
        data: data,                                                         //      下方为登录成功后实现的功能
        success: function (data) {
            if (data.statusCode == "200") {
                $("#display1").attr("style", "display:none;");              //      隐藏按钮
                $("#myModal").attr("style", "display:none;");               //      隐藏模态框
                $(".modal-backdrop.fade").attr("style", "display:none;");   //      隐藏模态框背景颜色
                document.body.style.overflow = "auto";                      //      修复隐藏模态框滚动条消失的bug
                document.body.style.width = "101.2%";                       //      修复隐藏模态框滚动条消失的bug
                $.cookie("user_name", data.data.userName, {expires: 7, path: "/"});       //      将获取到的用户名保存的cookie中
                $.cookie("user_id", data.userId, {expires: 7, path: "/"});  //      将登录成功后后台返回的用户id保存到cookie中
                $.cookie("user_avatar", data.data.userAvatar, {expires: 7, path: "/"});
                window.location.reload();                                               //刷新页面

            } else {
                alert(" " + data.statusMsg);
            }
        },
        error: function (data) {//连接错误的执行方法
            alert(JSON.stringify(data));
        },
    });
}

/**************************************注销的部分方法*****************************/

function zhuXiao() {
    $.cookie("user_name", "", {expires: -1, path: "/"});        // 调用该方法时将用户名的cookie值删除
    $.cookie("user_id", "", {expires: -1, path: "/"});          // 调用该方法时将用户id的cookie值删除
}
