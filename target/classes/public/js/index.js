layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 表单submit提交
     * form.on('submit(表单lay-filter的属性值)', function(data) {
     *
     * });
     */
    form.on('submit(login)', function(data) {
        // console.log(data.field);
        //发送ajax请求
        $.ajax({
            type: "post",
            url: ctx+"/user/login",
            data: {
                userName: data.field.username,
                userPwd: data.field.password
            },
            success:function (result) {
                // console.log(result);
                if(result.code == 200) {
                    layer.msg("登录成功",function () {
                        //判断用户是否选择记住密码。如果选中，则设置cookie对象7天有效
                        if($("#rememberMe").prop("checked")) {
                            //选中。
                            //将用户信息存储到cookie中
                            $.cookie("userIdStr",result.result.userIdStr, {expires:7});
                            $.cookie("userName",result.result.userName, {expires:7});
                            $.cookie("trueName",result.result.trueName, {expires:7});
                        } else {
                            //将用户信息存储到cookie中
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }
                        //跳转到主页
                        window.location.href = ctx + "/main";
                    })
                } else {
                    layer.msg(result.msg,{icon:5})
                }
            }
        })
        return false; //阻止表单提交
    });
});