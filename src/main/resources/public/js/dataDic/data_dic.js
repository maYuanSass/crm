layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        id:'dataDicTable',
        elem: '#dataDicList',
        height: 'full-125',
        cellMinWidth:95,
        url: ctx + '/data_dic/list',
        page: true,
        limit: 10,
        toolbar: '#toolbarDemo',
        cols: [[
            {type:'checkbox', fixed:'center'},
            {field: 'id', title: '编号',  sort: true, fixed: 'left'},
            {field: 'dataDicName', title: '字典名称', align:'center'},
            {field: 'dataDicValue', title: '字典数据', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {title:'操作',templet:'#dataDicListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        tableIns.reload({
            where: {
                dataDicName: $("[name='dataDicName']").val()
            },
            page: {
                curr: 1
            }
        });
    });


});