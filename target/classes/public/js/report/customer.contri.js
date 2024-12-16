layui.use(['table', 'layer', "form", "laydate"], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate = layui.laydate;

    /**
     * 设置日期（渲染日期选择框）
     */
    laydate.render({
        elem: '#time'
    });

    /**
     * 数据表格渲染
     */
    table.render({
        elem: '#contriList',
        url: ctx + '/customer/queryCustomerContributionByParams',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limit: 10,
        id: "customerContriListTable",
        cols: [[
            {field: 'name', title: '客户名', minWidth: 50, align: "center"},
            {field: 'total', title: '总金额(￥)', minWidth: 50, align: "center"}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click", function () {
        table.reload("customerContriListTable", {
            page: {
                curr: 1
            },
            where: {
                customerName: $("input[name='customerName']").val(),
                type: $("#type").val(),
                time: $("input[name='time']").val()
            }
        })
    });


});
