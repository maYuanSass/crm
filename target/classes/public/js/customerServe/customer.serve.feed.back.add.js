layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 加载指派人的下拉框 （客户经理）
     */
    $.ajax({
        type: "get",
        url: ctx + "/user/queryAllCustomerManagers",
        data: {},
        success: function (data) {
            if (data != null) {
                //获取隐藏域中设置的分配人
                var assigner = $("[name='man']").val();
                //遍历返回的数据
                for (var i = 0; i < data.length; i++) {
                    var opt = "";
                    //判断是否需要被选中
                    if (assigner == data[i].id) {
                        //设置下拉选项
                        opt = "<option value='" + data[i].id + "' selected>" + data[i].uname + "</option>";
                    } else {
                        //设置下拉选项
                        opt = "<option value='" + data[i].id + "'>" + data[i].uname + "</option>";
                    }
                    //将下拉项设置到下拉框中
                    $("#assigner").append(opt);
                }
            }
            //重新渲染下拉框的内容
            layui.form.render("select");
        }
    });

    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateCustomerServe)', function (data) {
        //提交数据时的加载层 （https://layer.layui.com/）
        var index = top.layer.msg("数据提交中,请稍后...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        //请求的地址
        var url = ctx + "/customer_serve/update"; // 服务反馈操作

        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                top.layer.msg("操作成功！", {icon: 6});
                //关闭加载层
                top.layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口，重新加载数据
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });

        return false;
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


});