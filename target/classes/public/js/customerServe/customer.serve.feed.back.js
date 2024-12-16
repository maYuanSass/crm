layui.use(['table', 'layer', "form"], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    table.render({
        elem: '#customerServeList',
        url: ctx + '/customer_serve/list?state=fw_003',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerServeListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'customer', title: '客户名', minWidth: 50, align: "center"},
            {field: 'dicValue', title: '服务类型', minWidth: 100, align: 'center'},
            {field: 'overview', title: '概要信息', align: 'center'},
            {field: 'createPeople', title: '创建人', minWidth: 100, align: 'center'},
            {field: 'assignTime', title: '分配时间', minWidth: 50, align: "center"},
            {field: 'assigner', title: '分配人', minWidth: 100, align: 'center'},
            {field: 'serviceProcePeople', title: '处理人', minWidth: 50, align: "center"},
            {field: 'serviceProceTime', title: '处理时间', minWidth: 100, align: 'center'},
            {title: '操作', minWidth: 150, templet: '#customerServeListBar', fixed: "right", align: "center"}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click", function () {
        table.reload("customerServeListTable", {
            page: {
                curr: 1
            },
            where: {
                customerName: $("input[name='customer']").val(),
                serveType: $("#type").val()
            }
        })
    });

    /**
     * 监听行工具栏
     */
    table.on('tool(customerServes)', function (data) {
        if (data.event == "back") { //服务反馈操作
            //打开服务反馈对话框
            openCustomerServeFeedBackDialog(data.data.id);
        }
    });

    /**
     * 打开服务反馈对话框
     */
    function openCustomerServeFeedBackDialog(id) {
        var title = "服务反馈";
        var url = ctx + "/customer_serve/toCustomerServeFeedBackPage?id=" + id;

        layui.layer.open({
            type: 2,
            title: title,
            area: ['700px', '500px'],
            maxmin: true,
            content: url
        });
    }

});
