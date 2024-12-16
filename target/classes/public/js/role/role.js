layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var tableIns = table.render({
        id: 'roleTable',
        elem: '#roleList',
        height: 'full-125',
        cellMinWidth: 95,
        url: ctx + '/role/list',
        page: true,
        limit: 10,
        toolbar: '#toolbarDemo',
        cols: [[
            {type: 'checkbox', fixed: 'center'},
            {field: 'id', title: '编号', sort: true, fixed: 'left'},
            {field: 'roleName', title: '角色名称', align: 'center'},
            {field: 'roleRemark', title: '角色备注', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'updateDate', title: '修改时间', align: 'center'},
            {title: '操作', templet: '#roleListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        tableIns.reload({
            where: {
                roleName: $("[name='roleName']").val()
            },
            page: {
                curr: 1
            }
        });
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(roles)',function (data) {
        if(data.event == "add") {
            //打开窗口
            openAddOrUpdateRoleDialog();
        } else if(data.event == "grant") {
            //获取数据表格选中的记录数据
            var checkStatus = table.checkStatus(data.config.id);
            //打开授权的对话框
            openAddGrantDialog(checkStatus.data);
        }
    });

    /**
     * 打开添加或者更新窗口
     */
    function openAddOrUpdateRoleDialog(roleId) {
        var title = "添加数据";
        var url = ctx + "/role/toAddOrUpdatePage";

        if(roleId != null) {
            title = "更新数据";
            url += "?roleId=" + roleId;
        }

        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['450px', '270px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(roles)',function (data) {
        if(data.event == "edit") {
            openAddOrUpdateRoleDialog(data.data.id);
        } else if(data.event == "del") {
            deleteRole(data.data.id);
        }
    })

    /**
     * 删除角色
     * @param roleId
     */
    function deleteRole(roleId) {
        layer.confirm('确定要删除该记录吗？',{icon:3}, function (index) {
            layer.close(index);

            //发送ajax请求
            $.ajax({
                type: "post",
                url: ctx + "/role/delete",
                data: {
                    roleId: roleId
                },
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg("删除成功！",{icon:6});
                        //重载表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon:5});
                    }
                }
            });
        });
    }

    /**
     * 打开授权窗口
     */
    function openAddGrantDialog(data) {
        //判断用户是否选择了要授权的记录
        if(data.length == 0) {
            layer.msg("请选择要授权的记录",{icon:5});
            return;
        }
        //只支持单个角色授权
        if(data.length > 1) {
            layer.msg("暂不支持批量角色授权",{icon:5});
            return;
        }

        var title = "角色授权";
        var url = ctx+ "/module/toAddGrantPage?roleId="+data[0].id;
        //打开窗口
        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['500px', '600px'],
            maxmin: true,
            content: url
        });
    }
});
