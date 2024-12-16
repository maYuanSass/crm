layui.use(['table', 'layer', "form"], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    table.render({
        elem: '#customerServeList',
        url: ctx + '/customer_serve/list?state=fw_001',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
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
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
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
     * 头部工具栏监听
     */
    table.on('toolbar(customerServes)', function (data) {
        if(data.event == "add") {
            //打开添加服务对话框
            layui.layer.open({
                type: 2,
                title: "服务添加",
                area: ['600px', '450px'],
                content: ctx+"/customer_serve/toAddCustomerServePage",
                maxmin: true
            });
        }
    })
});
