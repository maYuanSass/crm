layui.use(['layer', 'echarts'], function () {
    var $ = layui.jquery,
        echarts = layui.echarts;

    /**
     * 发送ajax请求，获取柱状图所需要的数据
     */
    $.ajax({
        type: "get",
        url: ctx + "/customer/countCustomerServe",
        dataType: "json",
        success: function (data) {
            //基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('serve'));

            //指定图表的配置项和数据
            var option = {
                title: {
                    text: '客户服务分析'
                },
                xAxis: {
                    type: 'category',
                    data: data.xData
                },
                yAxis: {
                    type: 'value'
                },
                tooltip: {},
                series: [{
                    data: data.yData,
                    type: 'bar',
                    showBackground: true,
                    backgroundStyle: {
                        color: 'rgba(220, 220, 220, 0.8)'
                    }
                }]
            };

            //使用刚指定的配置项和数据显示图表
            myChart.setOption(option);
        }
    });

});